package com.uniconn.backend.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

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

    @GetMapping("/profile")
    public String profilePage(Model model) {
        model.addAttribute("avatarUrl", "/vector-logos/usernameSignIn.svg");
        model.addAttribute("followerCount", 0);
        model.addAttribute("friendCount", 0);
        model.addAttribute("biography", "");
        model.addAttribute("major", "");
        model.addAttribute("year", "");
        model.addAttribute("hobbies", "");
        model.addAttribute("clubName", "");
        return "profile/profile";
    }

}
