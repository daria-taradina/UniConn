// Lillian Foster
// AuthController.java - handles login, registration, and forgot password endpoints
// returns JWT token on login/register success
// forgot password endpoints are public - no token needed

package com.uniconn.backend.controllers;

import com.uniconn.backend.dtos.AuthResponse;
import com.uniconn.backend.dtos.LoginRequest;
import com.uniconn.backend.dtos.RegisterRequest;
import com.uniconn.backend.dtos.ResetPasswordRequest;
import com.uniconn.backend.services.AuthService;
import com.uniconn.backend.services.PasswordResetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final PasswordResetService passwordResetService;

    // constructor injection - same pattern as rest of the project
    public AuthController(AuthService authService, PasswordResetService passwordResetService) {
        this.authService = authService;
        this.passwordResetService = passwordResetService;
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

    /**
     * GET /api/auth/forgot-password/question
     * Step 1 - takes the user's email and returns their security question
     * public endpoint - no token needed since user isn't logged in yet
     */
    @GetMapping("/forgot-password/question")
    public ResponseEntity<?> getSecurityQuestion(@RequestParam String csunEmail) {
        try {
            String question = passwordResetService.getSecurityQuestion(csunEmail);
            return ResponseEntity.ok(question);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * POST /api/auth/forgot-password/reset
     * Step 2 - takes email, answer, and new password and resets the password
     * public endpoint - no token needed since user isn't logged in yet
     */
    @PostMapping("/forgot-password/reset")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        try {
            passwordResetService.resetPassword(
                    request.getCsunEmail(),
                    request.getAnswer(),
                    request.getNewPassword()
            );
            return ResponseEntity.ok("Password reset successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}