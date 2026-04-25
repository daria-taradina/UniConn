package com.uniconn.backend.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String home() {
        return "login/login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login/login";
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "login/forgotPassword";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "login/register";
    }

    @GetMapping("/feed")
    public String feedPage() {
        return "userFeed/userFeed";
    }
    
    @GetMapping("/profile")
    public String profilePage() {
        return "profile/profile";
    }
    
    @GetMapping("/post/createPost")
    public String createPostPage() {
        return "post/createPost";
    }
    
    
    
 // Explore page — all communities (category filter handled by same template)
    @GetMapping("/explore-communities")
    public String exploreCommunitiesPage() {
        return "communities/exploreCommunities";
    }
 
    // Category filter — same template, JS reads the path segment to pre-select filter
    @GetMapping("/explore-communities/{category}")
    public String exploreCommunitiesByCategoryPage() {
        return "community/exploreCommunities";
    }
 
    // My communities page — all/created by me/member of filter handled in same template
    @GetMapping("/my-communities")
    public String myCommunitiesPage() {
        return "community/myCommunities";
    }

    // Community profile page — identified by name, not numeric id
    @GetMapping("/c/{communityName}")
    public String communityDetailPage() {
        return "community/communityDetail";
    }

}
