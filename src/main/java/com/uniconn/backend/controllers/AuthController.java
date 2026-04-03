// Lillian Foster - Auth Controller

package com.uniconn.backend.controllers;

import com.uniconn.backend.dtos.LoginRequest;
import com.uniconn.backend.dtos.RegisterRequest;
import com.uniconn.backend.entities.User;

import com.uniconn.backend.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * handles all authentication endpoints for UniConn.
 * Covers registration, login, and logout.
 * Teammates can get the current logged in user
 * via Spring Security context after login.
 */

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    // Constructor injection following team pattern
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * POST /api/auth/register
     * Registers a new UniConn user.
     * Email must end in @my.csun.edu with no spaces.
     * Password is hashed before saving.
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            User newUser = userService.registerUser(request);
            return ResponseEntity.ok("User registered successfully: " + newUser.getUsername());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * POST /api/auth/login
     * Logs in an existing user.
     * Returns user info if credentials are valid.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            User user = userService.loginUser(request);
            return ResponseEntity.ok("Login successful: " + user.getUsername());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * GET /api/auth/logout
     * Logs out the current user.
     */
    @GetMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok("Logged out successfully");
    }
}