package com.example.cshare.Models;

/**
 * Login request response model
 * It makes easy the parsing of the response to a login request
 */
public class LoginResponse {
    private String key;
    private String requestStatus;
    private UserResponse user;

    public class UserResponse{
        private int id;
        private String email;
        private String last_name;
        private String first_name;
        private String room_number;
        private String campus;
        private String profile_picture;
        private boolean is_active;
        private boolean is_staff;
        private String last_login;
        private String date_joined;

        public int getId() {
            return id;
        }

    }

    public LoginResponse(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public UserResponse getUser() {
        return user;
    }

    public UserResponse getUserResponse() {
        return user;
    }

    /*
     * Constructor of the LoginResponse class.
     *
     */
    public LoginResponse(String key, String requestStatus, UserResponse user) {
        this.key = key;
        this.requestStatus = requestStatus;
        this.user = user;
    }

    public String getKey() {
        return key;
    }



}
