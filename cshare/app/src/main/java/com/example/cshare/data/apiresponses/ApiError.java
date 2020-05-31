package com.example.cshare.data.apiresponses;

import java.util.List;

/**
 * The ApiError class allows to access the content of errors returned by the server.
 *
 * @since 2.0
 * @author Clara Gros
 * @author Babacar Toure
 */

public class ApiError {

    private String detail;
    private int code;

    /**
     * Getter method
     * @return (String) Error message returned by the server
     */
    public String getDetail() { return detail; }

    /**
     * Static class that inherits from the api error class and contains errors specific to the
     * login request
     */
    public static class LoginError extends ApiError {
        private List<String> non_field_errors;
        public String getFieldErrors() {
            return non_field_errors.get(0);
        }
    }

    /**
     * Static class that inherits from the api error class and contains errors specific to the
     * sign up request
     */
    public static class RegistrationError extends ApiError{
        private List<String> email;
        public String getEmail() {
            return email.get(0);
        }
    }

    /**
     * Static class that inherits from the api error class and contains errors specific to the
     * change password request
     */
    public static class ChangePasswordError extends ApiError {
        private List<String> old_password;
        public String getOld_password() {
            return old_password.get(0);
        }
    }
}
