package com.uniconn.backend.entities;

public enum CommunityMemberRole {
	ADMIN("Admin"),
	MODERATOR("Moderator"),
	REGULAR_MEMBER("Member");
	
	private final String displayName;
	
	CommunityMemberRole(String displayName){ this.displayName = displayName; }
	
	public String getDisplayName() {
		return displayName;
	}
}
