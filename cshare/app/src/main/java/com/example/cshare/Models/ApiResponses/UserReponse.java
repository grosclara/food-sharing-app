package com.example.cshare.Models.ApiResponses;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cshare.Models.User;
import com.example.cshare.RequestManager.Status;

import static com.example.cshare.RequestManager.Status.COMPLETE;
import static com.example.cshare.RequestManager.Status.ERROR;
import static com.example.cshare.RequestManager.Status.LOADING;
import static com.example.cshare.RequestManager.Status.SUCCESS;

public class UserReponse {

    public final Status status;

    @Nullable
    public final User user;

    @Nullable
    public final Throwable error;

    private UserReponse(Status status, @Nullable User user, @Nullable Throwable error) {
        this.status = status;
        this.user = user;
        this.error = error;
    }

    public Status getStatus() {
        return status;
    }

    @Nullable
    public User getUser() {
        return user;
    }

    @Nullable
    public Throwable getError() {
        return error;
    }

    public static UserReponse loading() {
        return new UserReponse(LOADING, null, null);
    }

    public static UserReponse success(@NonNull User user) {
        return new UserReponse(SUCCESS, user, null);
    }

    public static UserReponse error(@NonNull Throwable error) {
        return new UserReponse(ERROR, null, error);
    }

    public static UserReponse complete(){
        return new UserReponse(COMPLETE, null, null);
    }
}
