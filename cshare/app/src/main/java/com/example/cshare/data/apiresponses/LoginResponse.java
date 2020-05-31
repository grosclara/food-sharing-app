package com.example.cshare.data.apiresponses;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cshare.data.models.User;

import java.util.List;

import static com.example.cshare.data.apiresponses.Status.COMPLETE;
import static com.example.cshare.data.apiresponses.Status.ERROR;
import static com.example.cshare.data.apiresponses.Status.LOADING;
import static com.example.cshare.data.apiresponses.Status.SUCCESS;

public class LoginResponse {

    public static class LoginError {
        private List<String> non_field_errors;

        public String getDetail() {
            return non_field_errors.get(0);
        }
    }

    public final Status status;

    @Nullable
    public final User user;
    @Nullable
    public final String key;

    @Nullable
    public final LoginError error;

    private LoginResponse(Status status, @Nullable String token, @Nullable User data, @Nullable LoginError error) {
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

    public LoginError getError() {
        return error;
    }

    public static LoginResponse success(@NonNull String token, @NonNull User user) {
        return new LoginResponse(SUCCESS, token, user, null);
    }

    public static LoginResponse error(@NonNull LoginError error) {
        return new LoginResponse(ERROR, null, null, error);
    }

    public static LoginResponse complete() {
        return new LoginResponse(COMPLETE, null, null, null);
    }

}
