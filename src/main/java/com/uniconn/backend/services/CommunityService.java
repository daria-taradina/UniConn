package com.uniconn.backend.services;

import com.uniconn.backend.dtos.*;
import com.uniconn.backend.entities.*;
import com.uniconn.backend.repositories.*;
import jakarta.transaction.Transactional;
import com.uniconn.backend.composite_keys.CommunityMemberId;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CommunityService extends BaseService {	
	private final CommunityRepository communityRepository;
	private final CommunityMemberRepository communityMemberRepository;
	private final CommunityTagService communityTagService;
	
	public CommunityService(CommunityRepository communityRepository, 
			CommunityMemberRepository communityMemberRepository, CommunityTagService communityTagService) {
		this.communityRepository = communityRepository;
		this.communityMemberRepository = communityMemberRepository;
		this.communityTagService = communityTagService;
	}
	
	@Transactional
	public CommunityResponseDTO createCommunity(CommunityDTO communityDTO) {
		User currentUser = getAuthenticatedUser();
		
		if (communityRepository.existsByCommunityName(communityDTO.getCommunityName())) {
			throw new RuntimeException("Community name already taken: " + communityDTO.getCommunityName());
		}
		
		if (communityDTO.getCommunityName().contains(" ")) {
			throw new RuntimeException("Community name must not contain spaces");
		}
		
		Community community = new Community();
		// add case sensitivity test/resolution
		community.setCommunityName(communityDTO.getCommunityName());
		community.setDescription(communityDTO.getDescription());
		community.setCreatedBy(currentUser);
		community.setCategory(communityDTO.getCategory());
		community.setCommunityPicture(communityDTO.getCommunityPicture());
		Community saved = communityRepository.save(community);
		communityTagService.saveTags(saved, communityDTO.getTags());
		
		CommunityMember member = new CommunityMember(saved, currentUser, CommunityMemberRole.ADMIN);
		communityMemberRepository.save(member);
		
		currentUser.setCommunityCount(currentUser.getCommunityCount() + 1);
		userRepository.save(currentUser);
		
		saved.setMemberCount(1);
		communityRepository.save(saved);
		
		return mapToResponseDTO(saved);
	}
	
	private CommunityResponseDTO mapToResponseDTO(Community community) {
		Community fresh = communityRepository.findById(community.getCommunityId())
				.orElseThrow(() -> new RuntimeException("Community not found"));
		List<String> tagNames = fresh.getTags()
                .stream()
                .map(ct -> ct.getTag().getName())
                .toList();
		
		return new CommunityResponseDTO(
				community.getCommunityId(),
                community.getCommunityName(),
                community.getDescription(),
                community.getMemberCount(),
                community.getCreatedAt(),
                community.getCreatedBy().getUserId(),
                community.getCreatedBy().getUsername(),
                community.getCategory(),
                community.getCommunityPicture(),
                tagNames
			);
	}
	
	// test
	@Transactional
	public List<CommunityResponseDTO> getAllCommunities() {
	    return communityRepository.findAll()
	        .stream()
	        .map(this::mapToResponseDTO)
	        .collect(Collectors.toList());
	}
}
