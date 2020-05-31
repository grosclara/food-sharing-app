package com.example.cshare.data.apiresponses;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cshare.data.models.User;

import java.util.List;

import static com.example.cshare.data.apiresponses.Status.COMPLETE;
import static com.example.cshare.data.apiresponses.Status.ERROR;
import static com.example.cshare.data.apiresponses.Status.LOADING;
import static com.example.cshare.data.apiresponses.Status.SUCCESS;

/**
 * Login request response model
 * It makes easy the parsing of the response to a login request
 */
public class RegistrationResponse {

    public static class RegistrationError {
        private List<String> email;
        public String getEmail() {
            return email.get(0);
        }
    }

    public final Status status;

    @Nullable
    public final User user;
    @Nullable
    public final String key;

    @Nullable
    public final RegistrationError error;

    private RegistrationResponse(Status status, @Nullable String token, @Nullable User data, @Nullable RegistrationError error) {
        this.status = status;
        this.user = data;
        this.error = error;
        this.key = token;
    }

    public User getUser() {
        return user;
    }

    public Status getStatus() {
        return status;
    }

    public String getToken() {
        return key;
    }

    public RegistrationError getError(){ return error; }

    public static RegistrationResponse success(@NonNull String token, @NonNull User user) {
        return new RegistrationResponse(SUCCESS, token, user, null);
    }

    public static RegistrationResponse error(@NonNull RegistrationError error) {
        return new RegistrationResponse(ERROR, null, null, error);
    }

    public static RegistrationResponse complete(){
        return new RegistrationResponse(COMPLETE, null, null, null);
    }

}