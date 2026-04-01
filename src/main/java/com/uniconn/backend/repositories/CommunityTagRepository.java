package com.uniconn.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.uniconn.backend.composite_keys.CommunityTagId;
import com.uniconn.backend.entities.CommunityTag;

@Repository
public interface CommunityTagRepository extends JpaRepository<CommunityTag, CommunityTagId> {
	void deleteByCommunity_CommunityId(Integer communityId);

}
