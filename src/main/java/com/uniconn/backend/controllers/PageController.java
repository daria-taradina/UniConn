package com.uniconn.backend.controllers;

import com.uniconn.backend.dtos.ProfileData;
import com.uniconn.backend.services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @Autowired
    private ProfileService profileService;

    @GetMapping("/")
    public String home() {
        return "userFeed/userFeed";
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

    @GetMapping("/communities")
    public String communitiesPage() {
        return "communities/communities";
    }

    @GetMapping("/post/createPost")
    public String createPostPage(Model model) {
        model.addAttribute("avatarUrl", "/vector-logos/usernameSignIn.svg");
        return "post/createPost";
    }

    @GetMapping("/profile")
    public String profilePage(Model model) {
        try {
            ProfileData profile = profileService.getProfileData();
            String profilePicture = (profile.getProfilePicture() != null)
                ? profile.getProfilePicture()
                : "/vector-logos/usernameSignIn.svg";
            model.addAttribute("profilePicture", profilePicture);
            model.addAttribute("username", profile.getUsername());
            model.addAttribute("name", profile.getName());
            model.addAttribute("userBio", profile.getUserBio());
            model.addAttribute("followerCount", profile.getFollowerCount());
            model.addAttribute("followingCount", profile.getFollowingCount());
            model.addAttribute("communityCount", profile.getCommunityCount());
        } catch (Exception e) {
            model.addAttribute("profilePicture", "/vector-logos/usernameSignIn.svg");
            model.addAttribute("username", "");
            model.addAttribute("name", null);
            model.addAttribute("userBio", null);
            model.addAttribute("followerCount", 0);
            model.addAttribute("followingCount", 0);
            model.addAttribute("communityCount", 0);
        }
        return "profile/profile";
    }

}
