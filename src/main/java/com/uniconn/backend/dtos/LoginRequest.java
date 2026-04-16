//Lillian Foster - Login Request

package com.uniconn.backend.dtos;

/**
 * This is a DTO that carries login data from the frontend to the backend.
 * It only needs email and password to authenticate an existing user
 */
public class LoginRequest {

    private String csunEmail; // must be a valid @my.csun.edu address
    private String password;  // checked against the stored password hash

    public String getCsunEmail() { return csunEmail; }
    public void setCsunEmail(String csunEmail) { this.csunEmail = csunEmail; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}