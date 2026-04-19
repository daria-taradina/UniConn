// Lillian Foster
// PasswordResetService.java - handles all forgot password logic
// security questions are defined here in the service layer (per Daria's instruction)
// covers: getting the question, verifying the answer, resetting the password

package com.uniconn.backend.services;

import com.uniconn.backend.entities.PasswordReset;
import com.uniconn.backend.entities.User;
import com.uniconn.backend.repositories.PasswordResetRepository;
import com.uniconn.backend.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class PasswordResetService {

    private final PasswordResetRepository passwordResetRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // constructor injection - same pattern as rest of the project
    public PasswordResetService(PasswordResetRepository passwordResetRepository,
                                UserRepository userRepository) {
        this.passwordResetRepository = passwordResetRepository;
        this.userRepository = userRepository;
    }

    // security questions defined here in the service layer
    // each question has an id (1-5) and the question text
    public static final Map<Integer, String> SECURITY_QUESTIONS = new LinkedHashMap<>();
    static {
        SECURITY_QUESTIONS.put(1, "What was the name of your first pet?");
        SECURITY_QUESTIONS.put(2, "What city were you born in?");
        SECURITY_QUESTIONS.put(3, "What is your mother's maiden name?");
        SECURITY_QUESTIONS.put(4, "What was the name of your elementary school?");
        SECURITY_QUESTIONS.put(5, "What was the make of your first car?");
    }

    /**
     * Step 1 of forgot password - gets the security question for a user
     * frontend calls this first to show the user their question
     */
    public String getSecurityQuestion(String csunEmail) {

        // find the user by email
        User user = userRepository.findByEmail(csunEmail)
                .orElseThrow(() -> new RuntimeException("No account found with that email"));

        // find their password reset record
        PasswordReset record = passwordResetRepository.findByUserId(user)
                .orElseThrow(() -> new RuntimeException("No security question set up for this account"));

        // get the question text from our map using the stored question id
        String question = SECURITY_QUESTIONS.get(record.getQuestionId());
        if (question == null) {
            throw new RuntimeException("Invalid question ID stored");
        }

        return question;
    }

    /**
     * Step 2 of forgot password - verifies the answer and resets the password
     * answer is compared to the hashed answer stored in the database
     */
    @Transactional
    public void resetPassword(String csunEmail, String answer, String newPassword) {

        // find the user by email
        User user = userRepository.findByEmail(csunEmail)
                .orElseThrow(() -> new RuntimeException("No account found with that email"));

        // find their password reset record
        PasswordReset record = passwordResetRepository.findByUserId(user)
                .orElseThrow(() -> new RuntimeException("No security question set up for this account"));

        // check if the answer matches the stored hashed answer
        if (!passwordEncoder.matches(answer.toLowerCase().trim(), record.getAnswer())) {
            throw new RuntimeException("Incorrect answer to security question");
        }

        // answer is correct - hash and save the new password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}