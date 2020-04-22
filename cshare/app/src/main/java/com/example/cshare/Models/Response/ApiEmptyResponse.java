package com.example.cshare.Models.Response;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cshare.RequestManager.Status;

import static com.example.cshare.RequestManager.Status.COMPLETE;
import static com.example.cshare.RequestManager.Status.ERROR;
import static com.example.cshare.RequestManager.Status.LOADING;
import static com.example.cshare.RequestManager.Status.SUCCESS;

public class ApiEmptyResponse {

    /**
     * Auth response empty model
     */

    public final Status status;

    @Nullable
    public final Throwable error;

    private ApiEmptyResponse(Status status, @Nullable Throwable error) {
        this.status = status;
        this.error = error;
    }

    public Status getStatus() {
        return status;
    }

    public Throwable getError(){ return error; }

    public static ApiEmptyResponse loading() {
        return new ApiEmptyResponse(LOADING, null);
    }

    public static ApiEmptyResponse success() {
        return new ApiEmptyResponse(SUCCESS, null);
    }

    public static ApiEmptyResponse error(@NonNull Throwable error) {
        return new ApiEmptyResponse(ERROR, error);
    }

    public static ApiEmptyResponse complete(){
        return new ApiEmptyResponse(COMPLETE, null);
    }
}
