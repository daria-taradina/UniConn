package com.uniconn.backend.services;

import com.uniconn.backend.composite_keys.CommunityMemberId;
import com.uniconn.backend.dtos.*;
import com.uniconn.backend.entities.*;
import com.uniconn.backend.exception.*;
import com.uniconn.backend.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

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
            // community post
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
        // else: profile post - no community, no title required

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
            // not the author - check if they're admin of the post's community
            if (post.getCommunity() == null) {
                // profile post - only author can delete
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
    // HELPER
    // ---------------------------------------------------------------
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