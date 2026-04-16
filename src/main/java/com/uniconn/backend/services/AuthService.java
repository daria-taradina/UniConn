// Lillian Foster
// AuthService.java - handles login and registration with JWT token generation
// called by AuthController for /auth/login and /auth/register endpoints

package com.uniconn.backend.services;

import com.uniconn.backend.dtos.AuthResponse;
import com.uniconn.backend.dtos.LoginRequest;
import com.uniconn.backend.dtos.RegisterRequest;
import com.uniconn.backend.entities.User;
import com.uniconn.backend.utils.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    // constructor injection - same pattern as rest of the project
    public AuthService(UserService userService, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Registers a new user and returns a JWT token
     * so they are logged in immediately after signing up
     */
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // use existing UserService to register the user
        User user = userService.registerUser(request);

        // generate a JWT token for the new user
        String token = jwtUtil.generateToken(user.getEmail());

        return new AuthResponse(token);
    }

    /**
     * Logs in a user by verifying credentials via Spring Security
     * then generating and returning a JWT token
     */
    public AuthResponse login(LoginRequest request) {
        // Spring Security checks the email and password against the database
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getCsunEmail(),
                        request.getPassword()
                )
        );

        // if we get here, credentials were valid - generate token
        String token = jwtUtil.generateToken(request.getCsunEmail());

        return new AuthResponse(token);
    }
}