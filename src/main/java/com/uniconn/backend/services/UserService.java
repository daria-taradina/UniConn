// Lillian Foster - User Service
package com.uniconn.backend.services;

import com.uniconn.backend.dtos.RegisterRequest;
import com.uniconn.backend.dtos.LoginRequest;
import com.uniconn.backend.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Handles all authentication logic for UniConn.
 * Includes registration, login, email validation,
 * and forgot password via secret question.
 * Note: secretQuestion and secretAnswer fields pending
 * addition to User entity by DB teammate.
 */
@Service
public class UserService extends BaseService {

    // Used to hash passwords before saving to the database
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Registers a new user after validating their CSUN email,
     * checking for duplicate username/email, and hashing their password.
     */
    @Transactional
    public User registerUser(RegisterRequest request) {

        // Validate CSUN email format
        if (!validateCsunEmail(request.getCsunEmail())) {
            throw new RuntimeException("Email must end in @my.csun.edu and contain no spaces");
        }

        // Check if email is already registered using findByEmail
        if (userRepository.findByEmail(request.getCsunEmail()).isPresent()) {
            throw new RuntimeException("Email already in use: " + request.getCsunEmail());
        }

        // Check if username is already taken using findByUsername
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already taken: " + request.getUsername());
        }

        // Build the new user object using exact method names from User entity
        User user = new User();
        user.setUsername(request.getUsername());
        user.setName(request.getFullName());
        user.setEmail(request.getCsunEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setActive(true);

        // TODO: add these lines once DB teammate adds secretQuestion
        // and secretAnswer columns to User entity:
        // user.setSecretQuestion(request.getSecretQuestion());
        // user.setSecretAnswer(request.getSecretAnswer().toLowerCase().trim());

        return userRepository.save(user);
    }

    /**
     * Logs in a user by checking their email exists
     * and password matches the stored hash.
     */
    public User loginUser(LoginRequest request) {

        // Validate CSUN email format
        if (!validateCsunEmail(request.getCsunEmail())) {
            throw new RuntimeException("Invalid email format");
        }

        // Find user by email
        User user = userRepository.findByEmail(request.getCsunEmail())
                .orElseThrow(() -> new RuntimeException("No account found with that email"));

        // Check if password matches the stored hash
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Incorrect password");
        }

        return user;
    }

    /**
     * Validates that the email ends in @my.csun.edu and has no spaces.
     * Required for all UniConn accounts.
     */
    public boolean validateCsunEmail(String email) {
        if (email == null) return false;
        if (email.contains(" ")) return false;
        return email.endsWith("@my.csun.edu");
    }

    /**
     * Resets a user's password after verifying their secret question answer.
     * TODO: uncomment once DB teammate adds secretAnswer to User entity.
     */
    @Transactional
    public void resetPasswordBySecretQuestion(String csunEmail, String answer, String newPassword) {

        // Find user by email
        User user = userRepository.findByEmail(csunEmail)
                .orElseThrow(() -> new RuntimeException("No account found with that email"));

        // TODO: uncomment once secretAnswer is added to User entity
        // if (!user.getSecretAnswer().equalsIgnoreCase(answer.trim())) {
        //     throw new RuntimeException("Incorrect answer to secret question");
        // }

        // Hash and save new password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}