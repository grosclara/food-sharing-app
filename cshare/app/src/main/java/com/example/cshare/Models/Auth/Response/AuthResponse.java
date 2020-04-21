package com.example.cshare.Models.Auth.Response;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cshare.RequestManager.Status;

import static com.example.cshare.RequestManager.Status.ERROR;
import static com.example.cshare.RequestManager.Status.LOADING;
import static com.example.cshare.RequestManager.Status.SUCCESS;

public class AuthResponse {

    /**
     * Auth response empty model
     */

    public final Status status;

    @Nullable
    public final Throwable error;

    private AuthResponse(Status status, @Nullable Throwable error) {
        this.status = status;
        this.error = error;
    }

    public Status getStatus() {
        return status;
    }

    public Throwable getError(){ return error; }

    public static AuthResponse loading() {
        return new AuthResponse(LOADING, null);
    }

    public static AuthResponse success() {
        return new AuthResponse(SUCCESS, null);
    }

    public static AuthResponse error(@NonNull Throwable error) {
        return new AuthResponse(ERROR, error);
    }
}
