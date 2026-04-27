package com.uniconn.backend.services;

import com.uniconn.backend.composite_keys.CommunityMemberId;
import com.uniconn.backend.dtos.*;
import com.uniconn.backend.entities.*;
import com.uniconn.backend.exception.*;
import com.uniconn.backend.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostManagementService extends BaseService {

    private final PostRepository postRepository;
    private final CommunityRepository communityRepository;
    private final CommunityMemberRepository communityMemberRepository;
    private final PostTagService postTagService;

    public PostManagementService(PostRepository postRepository,
                                 CommunityRepository communityRepository,
                                 CommunityMemberRepository communityMemberRepository,
                                 PostTagService postTagService) {
        this.postRepository = postRepository;
        this.communityRepository = communityRepository;
        this.communityMemberRepository = communityMemberRepository;
        this.postTagService = postTagService;
    }

    // ---------------------------------------------------------------
    // CREATE
    // ---------------------------------------------------------------
    @Transactional
    public PostSummaryDTO createPost(PostCreateDTO dto) {
        User currentUser = getAuthenticatedUser();

        Post post = new Post();
        post.setAuthor(currentUser);
        post.setContentText(dto.getContentText());

        if (dto.getCommunityId() != null) {
            Community community = communityRepository.findById(dto.getCommunityId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Community not found: " + dto.getCommunityId()));

            boolean isMember = communityMemberRepository.existsById(
                new CommunityMemberId(dto.getCommunityId(), currentUser.getUserId()));
            if (!isMember) {
                throw new UnauthorizedException("You must be a member to post in this community");
            }

            if (dto.getTitle() == null || dto.getTitle().isBlank()) {
                throw new InvalidInputException("Title is required for community posts");
            }

            post.setCommunity(community);
            post.setTitle(dto.getTitle());
        }

        Post saved = postRepository.save(post);
        List<String> tagNames = postTagService.saveTags(saved, dto.getTags());

        return mapToSummaryDTO(saved, tagNames);
    }

    // ---------------------------------------------------------------
    // DELETE
    // ---------------------------------------------------------------
    @Transactional
    public void deletePost(Integer postId) {
        User currentUser = getAuthenticatedUser();

        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new ResourceNotFoundException("Post not found: " + postId));

        if (post.isDeleted()) {
            throw new ResourceNotFoundException("Post not found: " + postId);
        }

        boolean isAuthor = post.getAuthor().getUserId().equals(currentUser.getUserId());

        if (!isAuthor) {
            if (post.getCommunity() == null) {
                throw new UnauthorizedException("You are not allowed to delete this post");
            }

            boolean isAdmin = communityMemberRepository
                .existsById_CommunityIdAndId_UserIdAndRole(
                    post.getCommunity().getCommunityId(),
                    currentUser.getUserId(),
                    CommunityMemberRole.ADMIN);

            if (!isAdmin) {
                throw new UnauthorizedException("You are not allowed to delete this post");
            }
        }

        post.setDeleted(true);
        postRepository.save(post);
    }

    // ---------------------------------------------------------------
    // FEED
    // ---------------------------------------------------------------
    @Transactional(readOnly = true)
    public List<PostSummaryDTO> getFeedForUser(Integer userId) {
        return postRepository.findFeedPostsForUser(userId)
                .stream()
                .map(this::mapToSummaryDTO)
                .collect(Collectors.toList());
    }

    // ---------------------------------------------------------------
    // TRENDING TAGS (last 30 days)
    // ---------------------------------------------------------------
    @Transactional(readOnly = true)
    public List<TrendingTagDTO> getTrendingTags() {
        LocalDateTime since = LocalDateTime.now().minusDays(30);
        return postRepository.findTrendingTagsRaw(since)
                .stream()
                .map(row -> new TrendingTagDTO((String) row[0], (Long) row[1]))
                .collect(Collectors.toList());
    }

    // ---------------------------------------------------------------
    // POSTS BY EXACT TAG
    // ---------------------------------------------------------------
    @Transactional(readOnly = true)
    public List<PostSummaryDTO> getPostsByTag(String tagName) {
        return postRepository.findPostsByExactTag(tagName)
                .stream()
                .map(this::mapToSummaryDTO)
                .collect(Collectors.toList());
    }

    // ---------------------------------------------------------------
    // SEARCH BY TAG (contains match)
    // ---------------------------------------------------------------
    @Transactional(readOnly = true)
    public List<PostSummaryDTO> searchPostsByTag(String query) {
        return postRepository.findPostsByTagContaining(query.trim())
                .stream()
                .map(this::mapToSummaryDTO)
                .collect(Collectors.toList());
    }

    // ---------------------------------------------------------------
    // PROFILE POSTS
    // ---------------------------------------------------------------
    @Transactional(readOnly = true)
    public List<PostSummaryDTO> getProfilePosts(Integer userId) {
        return postRepository.findProfilePostsByUser(userId)
                .stream()
                .map(this::mapToSummaryDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<PostSummaryDTO> getProfilePostsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
        return getProfilePosts(user.getUserId());
    }

    // ---------------------------------------------------------------
    // COMMUNITY POSTS BY USER
    // ---------------------------------------------------------------
    @Transactional(readOnly = true)
    public List<PostSummaryDTO> getCommunityPostsByUser(Integer userId) {
        return postRepository.findCommunityPostsByUser(userId)
                .stream()
                .map(this::mapToSummaryDTO)
                .collect(Collectors.toList());
    }

    // ---------------------------------------------------------------
    // ALL POSTS IN A COMMUNITY
    // ---------------------------------------------------------------
    @Transactional(readOnly = true)
    public List<PostSummaryDTO> getPostsByCommunity(Integer communityId) {
        return postRepository.findPostsByCommunity(communityId)
                .stream()
                .map(this::mapToSummaryDTO)
                .collect(Collectors.toList());
    }

    // ---------------------------------------------------------------
    // HELPERS
    // ---------------------------------------------------------------
    private PostSummaryDTO mapToSummaryDTO(Post post) {
        List<String> tagNames = post.getTags()
                .stream()
                .map(pt -> pt.getTag().getName())
                .collect(Collectors.toList());
        return mapToSummaryDTO(post, tagNames);
    }

    private PostSummaryDTO mapToSummaryDTO(Post post, List<String> tagNames) {
        return new PostSummaryDTO(
            post.getPostId(),
            post.getAuthor().getUsername(),
            post.getAuthor().getUserId(),
            post.getCommunity() != null ? post.getCommunity().getCommunityName() : null,
            post.getCommunity() != null ? post.getCommunity().getCommunityId() : null,
            post.getTitle(),
            post.getContentText(),
            post.getLikeCount(),
            post.getCommentCount(),
            post.getCreatedAt(),
            tagNames
        );
    }
}