package com.example.cshare.Models.Auth.Response;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cshare.Models.User;
import com.example.cshare.RequestManager.Status;

import static com.example.cshare.RequestManager.Status.ERROR;
import static com.example.cshare.RequestManager.Status.LOADING;
import static com.example.cshare.RequestManager.Status.SUCCESS;

/**
 * Login request response model
 * It makes easy the parsing of the response to a login request
 */
public class LoginResponse {

    public final Status status;

    @Nullable
    public final User user;
    @Nullable
    public final String key;

    @Nullable
    public final Throwable error;

    private LoginResponse(Status status, @Nullable String token, @Nullable User data, @Nullable Throwable error) {
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

    public Throwable getError(){ return error; }

    public static LoginResponse loading() {
        return new LoginResponse(LOADING, null, null, null);
    }

    public static LoginResponse success(@NonNull String token, @NonNull User user) {
        return new LoginResponse(SUCCESS, token, user, null);
    }

    public static LoginResponse error(@NonNull Throwable error) {
        return new LoginResponse(ERROR, null, null, error);
    }

}