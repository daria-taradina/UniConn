package com.uniconn.backend.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.uniconn.backend.services.ImageUploadService;

@RestController
@RequestMapping("/api/upload")
public class ImageUploadController {
	
	private final ImageUploadService imageUploadService;
	
	public ImageUploadController(ImageUploadService imageUploadService) {
		this.imageUploadService = imageUploadService;		
	}
	
	// POST /api/upload/community
    // Returns: { "url": "https://res.cloudinary.com/..." }
	@PostMapping("/community")
	public ResponseEntity<Map<String, String>> uploadCommunityImage(
			@RequestParam("file") MultipartFile file) {
		String url = imageUploadService.upload(file, "communities");
		return ResponseEntity.ok(Map.of("url", url));
	}
	
	// POST /api/upload/user
    // Returns: { "url": "https://res.cloudinary.com/..." }
	@PostMapping("/user")
	public ResponseEntity<Map<String, String>> uploadUserImage(
			@RequestParam("file") MultipartFile file) {
		String url = imageUploadService.upload(file, "users");
		return ResponseEntity.ok(Map.of("url", url));
	} 
}
