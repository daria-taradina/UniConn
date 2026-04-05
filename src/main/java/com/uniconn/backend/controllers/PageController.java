package com.uniconn.backend.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/login")
    public String loginPage() {
        return "login/login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "login/register";
    }

    @GetMapping("/profile")
    public String profilePage(Model model) {
        model.addAttribute("avatarUrl", "/Vector Logos/usernameSignIn.svg");
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
