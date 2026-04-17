//Lillian Foster - Register Request
package com.uniconn.backend.dtos;

/**
 * This is a DTO that carries registration form data from the frontend to the backend.
 * The fields need to match the registration form inputs submitted by a new user.
 */
public class RegisterRequest {

    private String username;       // chosen display name
    private String fullName;       // user's real name
    private String csunEmail;      // must end in @my.csun.edu
    private String password;       // will be hashed before saving
    private String secretQuestion; // used for forgot password flow
    private String secretAnswer;   // answer to the secret question

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getCsunEmail() { return csunEmail; }
    public void setCsunEmail(String csunEmail) { this.csunEmail = csunEmail; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getSecretQuestion() { return secretQuestion; }
    public void setSecretQuestion(String secretQuestion) { this.secretQuestion = secretQuestion; }

    public String getSecretAnswer() { return secretAnswer; }
    public void setSecretAnswer(String secretAnswer) { this.secretAnswer = secretAnswer; }
}