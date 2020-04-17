package com.example.cshare.RequestManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cshare.Models.User;
import com.example.cshare.WebServices.NetworkError;

import static com.example.cshare.RequestManager.Status.ERROR;
import static com.example.cshare.RequestManager.Status.LOADING;
import static com.example.cshare.RequestManager.Status.SUCCESS;

/**
 * Login request response model
 * It makes easy the parsing of the response to a login request
 */
public class ResponseLogin {

    public final Status status;

    @Nullable
    public final User user;
    @Nullable
    public final String token;

    @Nullable
    public final NetworkError error;

    private ResponseLogin(Status status, @Nullable String token, @Nullable User data, @Nullable NetworkError error) {
        this.status = status;
        this.user = data;
        this.error = error;
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public Status getStatus() {
        return status;
    }

    public String getToken() {
        return token;
    }

    public static ResponseLogin loading() {
        return new ResponseLogin(LOADING, null, null, null);
    }

    public static ResponseLogin success(@NonNull String token, @NonNull User user) {
        return new ResponseLogin(SUCCESS, token, user, null);
    }

    public static ResponseLogin error(@NonNull NetworkError error) {
        return new ResponseLogin(ERROR, null, null, error);
    }

}