// Lillian Foster
// UniConn - COMP 380
// AuthResponse.java - DTO that wraps the JWT token sent back after login
// When a user logs in successfully, we send back this object
// containing their token so they can use it for future requests

package com.uniconn.backend.dtos;

public class AuthResponse {

    private String token;

    // Constructor - creates an AuthResponse with the given token
    public AuthResponse(String token) {
        this.token = token;
    }

    // Getter
    public String getToken() {
        return token;
    }

    // Setter
    public void setToken(String token) {
        this.token = token;
    }
}