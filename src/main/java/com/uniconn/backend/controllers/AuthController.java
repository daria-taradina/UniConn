// Lillian Foster
// AuthController.java - handles login and registration endpoints
// returns JWT token on success so frontend can store and use it

package com.uniconn.backend.controllers;

import com.uniconn.backend.dtos.AuthResponse;
import com.uniconn.backend.dtos.LoginRequest;
import com.uniconn.backend.dtos.RegisterRequest;
import com.uniconn.backend.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    // constructor injection - same pattern as rest of the project
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * POST /api/auth/register
     * Registers a new UniConn user and returns a JWT token
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            AuthResponse response = authService.register(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * POST /api/auth/login
     * Logs in a user and returns a JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * GET /api/auth/logout
     * Logs out - frontend just needs to drop the token
     */
    @GetMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok("Logged out successfully");
    }
}