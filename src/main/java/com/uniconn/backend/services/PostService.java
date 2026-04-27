package com.uniconn.backend.services;

import com.uniconn.backend.dtos.*;
import com.uniconn.backend.entities.*;
import com.uniconn.backend.exception.ResourceNotFoundException;
import com.uniconn.backend.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PostService extends BaseService {

    private final PostRepository postRepository;
    private final CommunityRepository communityRepository;
    private final TagService tagService;

    public PostService(PostRepository postRepository,
                       CommunityRepository communityRepository,
                       TagService tagService) {
        this.postRepository = postRepository;
        this.communityRepository = communityRepository;
        this.tagService = tagService;
    }

    // ---------------------------------------------------------------
    // CREATE POST
    // ---------------------------------------------------------------
    @Transactional
    public PostSummaryDTO createPost(CreatePostRequest request) {
        User author = getAuthenticatedUser();

        Post post = new Post();
        post.setAuthor(author);
        post.setContentText(request.getContentText());

        if (request.getCommunityId() != null) {
            Community community = communityRepository.findById(request.getCommunityId())
                    .orElseThrow(() -> new ResourceNotFoundException("Community not found"));
            post.setCommunity(community);
            post.setTitle(request.getTitle());
        }

        Post saved = postRepository.save(post);

        return toDTO(saved);
    }

    // ---------------------------------------------------------------
    // FEED
    // ---------------------------------------------------------------
    public List<PostSummaryDTO> getFeedForUser(Integer userId) {
        return postRepository.findFeedPostsForUser(userId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ---------------------------------------------------------------
    // TRENDING TAGS (last 30 days)
    // ---------------------------------------------------------------
    public List<TrendingTagDTO> getTrendingTags() {
        LocalDateTime since = LocalDateTime.now().minusDays(30);
        return postRepository.findTrendingTagsRaw(since)
                .stream()
                .map(row -> new TrendingTagDTO(
                        (String) row[0],
                        (Long) row[1]
                ))
                .collect(Collectors.toList());
    }

    // ---------------------------------------------------------------
    // POSTS BY EXACT TAG (tag page / trending click)
    // ---------------------------------------------------------------
    public List<PostSummaryDTO> getPostsByTag(String tagName) {
        return postRepository.findPostsByExactTag(tagName)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ---------------------------------------------------------------
    // SEARCH BY TAG (contains match)
    // ---------------------------------------------------------------
    public List<PostSummaryDTO> searchPostsByTag(String query) {
        return postRepository.findPostsByTagContaining(query.trim())
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ---------------------------------------------------------------
    // PROFILE POSTS (no community)
    // ---------------------------------------------------------------
    public List<PostSummaryDTO> getProfilePosts(Integer userId) {
        return postRepository.findProfilePostsByUser(userId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    // Profile posts for any user by username
    public List<PostSummaryDTO> getProfilePostsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return getProfilePosts(user.getUserId());
    }

    // ---------------------------------------------------------------
    // COMMUNITY POSTS BY USER
    // ---------------------------------------------------------------
    public List<PostSummaryDTO> getCommunityPostsByUser(Integer userId) {
        return postRepository.findCommunityPostsByUser(userId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ---------------------------------------------------------------
    // ALL POSTS IN A COMMUNITY
    // ---------------------------------------------------------------
    public List<PostSummaryDTO> getPostsByCommunity(Integer communityId) {
        return postRepository.findPostsByCommunity(communityId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ---------------------------------------------------------------
    // MAPPER: Post entity → PostSummaryDTO
    // ---------------------------------------------------------------
    private PostSummaryDTO toDTO(Post post) {
        List<String> tagNames = post.getTags()
                .stream()
                .map(pt -> pt.getTag().getName())
                .collect(Collectors.toList());

        return new PostSummaryDTO(
                post.getPostId(),
                post.getAuthor().getUsername(),
                post.getAuthor().getUserId(),
                post.getCommunity() != null ? post.getCommunity().getCommunityName() : null,
                post.getCommunity() != null ? post.getCommunity().getCommunityId()   : null,
                post.getTitle(),
                post.getContentText(),
                post.getLikeCount(),
                post.getCommentCount(),
                post.getCreatedAt(),
                tagNames
        );
    }
}