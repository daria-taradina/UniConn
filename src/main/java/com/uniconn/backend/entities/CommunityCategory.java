package com.uniconn.backend.entities;

public enum CommunityCategory {
	ACADEMICS("Academics"),
	CAREER("Career"),
	Social("Social"),
	CAMPUS_AND_EVENTS("Campus&Events"),
	OTHER("Other");
	
	private final String displayName;
	
	CommunityCategory(String displayName){ this.displayName = displayName; }

	public String getDisplayName() {
		return displayName;
	}
	
}
