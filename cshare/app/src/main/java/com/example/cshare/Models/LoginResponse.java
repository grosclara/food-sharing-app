package com.example.cshare.Models;

/**
 * Login request response model
 * It makes easy the parsing of the response to a login request
 */
public class LoginResponse {
    private String key;
    private String requestStatus;
    private User user;

    public LoginResponse(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public LoginResponse(String key, String requestStatus, User user) {
        this.key = key;
        this.requestStatus = requestStatus;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getKey() {
        return key;
    }

}
