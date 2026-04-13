package com.uniconn.backend.dtos;

import java.util.List;
import com.uniconn.backend.entities.*;

public class CommunityDTO {
	private String communityName;
    private String description;
    private CommunityCategory category;
    private String communityPicture;
    private List<String> tags;
    
	public String getCommunityName() {
		return communityName;
	}
	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public CommunityCategory getCategory() {
		return category;
	}
	public void setCategory(CommunityCategory category) {
		this.category = category;
	}
	public String getCommunityPicture() {
		return communityPicture;
	}
	public void setCommunityPicture(String communityPicture) {
		this.communityPicture = communityPicture;
	}
	public List<String> getTags() {
		return tags;
	}
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	
}
