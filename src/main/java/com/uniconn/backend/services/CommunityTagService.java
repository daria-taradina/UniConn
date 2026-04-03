package com.uniconn.backend.services;

import java.util.List;
import org.springframework.stereotype.Service;
import com.uniconn.backend.entities.*;
import com.uniconn.backend.repositories.CommunityTagRepository;
import jakarta.transaction.Transactional;

@Service
public class CommunityTagService {
	private final CommunityTagRepository communityTagRepository;
	private final TagService tagService;
	
	public CommunityTagService(CommunityTagRepository communityTagRepository, TagService tagService) {
		this.communityTagRepository = communityTagRepository;
		this.tagService = tagService;
	}
	
	@Transactional
	public void saveTags(Community community, List<String> rawTags) {
		if (rawTags == null || rawTags.isEmpty()) return;
		
		List<String> limited = rawTags.stream().limit(5).toList();
		for (String raw : limited) {
			Tag tag = tagService.findOrCreate(raw);
			communityTagRepository.save(new CommunityTag(community, tag));
		}
	}
	
	@Transactional
	public void updateTags(Community community, List<String> newRawTags) {
		communityTagRepository.deleteByCommunity_CommunityId(community.getCommunityId());
		saveTags(community, newRawTags);
	}

}
