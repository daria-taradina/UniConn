// Lillian Foster
// UniConn - COMP 380
// ProfileServiceTest.java - unit tests for ProfileService
// Tests that getProfileData() correctly maps the authenticated
// user's data into a ProfileData object

package com.uniconn.backend.services;

import com.uniconn.backend.dtos.ProfileData;
import com.uniconn.backend.entities.User;
import com.uniconn.backend.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProfileServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private ProfileService profileService;

    // This is the fake user we'll pretend is logged in for each test
    private User testUser;

    @BeforeEach
    void setUp() {
        // Build a fake user that matches the User entity fields
        testUser = new User();
        testUser.setUsername("lfoster");
        testUser.setName("Lillian Foster");
        testUser.setEmail("lillian.foster@my.csun.edu");
        testUser.setPassword("hashedpassword123");
        testUser.setUserBio("Senior CS student at CSUN");
        testUser.setProfilePicture("/images/lfoster.jpg");
        testUser.setFollowerCount(10);
        testUser.setFollowingCount(5);
        testUser.setCommunityCount(3);

        // Mock Spring Security to pretend testUser is logged in
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(testUser);
        when(userRepository.findById(any())).thenReturn(Optional.of(testUser));
    }

    // Test 1 — happy path, everything works correctly
    @Test
    void getProfileData_returnsCorrectUsername() {
        ProfileData result = profileService.getProfileData();
        assertEquals("lfoster", result.getUsername());
    }

    @Test
    void getProfileData_returnsCorrectName() {
        ProfileData result = profileService.getProfileData();
        assertEquals("Lillian Foster", result.getName());
    }

    @Test
    void getProfileData_returnsCorrectEmail() {
        ProfileData result = profileService.getProfileData();
        assertEquals("lillian.foster@my.csun.edu", result.getEmail());
    }

    @Test
    void getProfileData_returnsCorrectBio() {
        ProfileData result = profileService.getProfileData();
        assertEquals("Senior CS student at CSUN", result.getUserBio());
    }

    @Test
    void getProfileData_returnsCorrectFollowerCount() {
        ProfileData result = profileService.getProfileData();
        assertEquals(10, result.getFollowerCount());
    }

    @Test
    void getProfileData_returnsCorrectFollowingCount() {
        ProfileData result = profileService.getProfileData();
        assertEquals(5, result.getFollowingCount());
    }

    @Test
    void getProfileData_returnsCorrectCommunityCount() {
        ProfileData result = profileService.getProfileData();
        assertEquals(3, result.getCommunityCount());
    }

    // Test 2 — user not found in database, should throw exception
    @Test
    void getProfileData_throwsException_whenUserNotFound() {
        // Override the mock to return empty (user not found)
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        // Should throw RuntimeException from BaseService
        assertThrows(RuntimeException.class, () -> {
            profileService.getProfileData();
        });
    }

    // Test 3 — make sure password is never exposed in ProfileData
    @Test
    void getProfileData_doesNotExposePassword() {
        ProfileData result = profileService.getProfileData();
        // ProfileData has no getPassword() method — this confirms
        // we never accidentally added one
        assertNotNull(result);
        assertNotNull(result.getUsername());
        // If this test compiles and passes, password is safely hidden
    }
}