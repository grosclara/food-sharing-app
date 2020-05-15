package com.example.cshare.data.apiresponses;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import static com.example.cshare.data.apiresponses.Status.COMPLETE;
import static com.example.cshare.data.apiresponses.Status.ERROR;
import static com.example.cshare.data.apiresponses.Status.LOADING;
import static com.example.cshare.data.apiresponses.Status.SUCCESS;

public class EmptyAuthResponse {

    public static class EmptyAuthError {
        private String detail;
        private List<String> old_password;

        public String getOld_password() {
            return old_password.get(0);
        }
        public String getDetail() {
            return detail;
        }
    }

    public final Status status;

    @Nullable
    public final EmptyAuthError error;

    private EmptyAuthResponse(Status status, @Nullable EmptyAuthError error) {
        this.status = status;
        this.error = error;
    }

    public Status getStatus() {
        return status;
    }

    public EmptyAuthError getError() {
        return error;
    }

    public static EmptyAuthResponse loading() {
        return new EmptyAuthResponse(LOADING, null);
    }

    public static EmptyAuthResponse success() {
        return new EmptyAuthResponse(SUCCESS, null);
    }

    public static EmptyAuthResponse error(@NonNull EmptyAuthError error) {
        return new EmptyAuthResponse(ERROR, error);
    }

    public static EmptyAuthResponse complete() {
        return new EmptyAuthResponse(COMPLETE, null);
    }
}