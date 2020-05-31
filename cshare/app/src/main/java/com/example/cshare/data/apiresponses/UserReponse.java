package com.example.cshare.data.apiresponses;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cshare.data.models.User;

import static com.example.cshare.data.apiresponses.Status.COMPLETE;
import static com.example.cshare.data.apiresponses.Status.ERROR;
import static com.example.cshare.data.apiresponses.Status.LOADING;
import static com.example.cshare.data.apiresponses.Status.SUCCESS;

public class UserReponse {

    public static class UserError{
        private String detail;

        public String getDetail() {
            return detail;
        }
    }

    public final Status status;

    @Nullable
    public final User user;

    @Nullable
    public final UserError error;

    private UserReponse(Status status, @Nullable User user, @Nullable UserError error) {
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
    public UserError getError() {
        return error;
    }

    public static UserReponse success(@NonNull User user) {
        return new UserReponse(SUCCESS, user, null);
    }

    public static UserReponse error(@NonNull UserError error) {
        return new UserReponse(ERROR, null, error);
    }

    public static UserReponse complete(){
        return new UserReponse(COMPLETE, null, null);
    }
}
