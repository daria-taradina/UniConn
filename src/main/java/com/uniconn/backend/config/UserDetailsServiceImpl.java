// Lillian Foster
// UserDetailsServiceImpl.java - tells Spring Security how to load a user by email
// this is required for JWT auth to work - Spring calls this automatically

package com.uniconn.backend.config;

import com.uniconn.backend.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    // constructor injection - same pattern as rest of the project
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Spring Security calls this when it needs to verify who is making a request
    // we use email as the "username" for UniConn
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // look up user by email in the database
        com.uniconn.backend.entities.User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with email: " + email));

        // wrap in Spring Security's User object so it's compatible
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities("ROLE_USER")
                .build();
    } }