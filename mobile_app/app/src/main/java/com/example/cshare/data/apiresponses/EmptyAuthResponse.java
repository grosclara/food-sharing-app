package com.example.cshare.data.apiresponses;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.example.cshare.data.apiresponses.Status.COMPLETE;
import static com.example.cshare.data.apiresponses.Status.ERROR;
import static com.example.cshare.data.apiresponses.Status.SUCCESS;

/**
 * Class that contains the response to an auth request that doesn't return anything.
 * <p>
 * The class consists of a status that indicates the status of the response returned by the server.
 * In case of failure, the response contains an instance of the ChangePasswordError class to access
 * the error message returned by the server.
 * <p>
 * The defined methods are on the one hand the getters and on the other hand methods that allow to
 * create instances of the class by associating them a certain status
 *
 * @see Status
 * @see com.example.cshare.data.apiresponses.ApiError.ChangePasswordError
 * @since 2.0
 * @author Clara Gros
 * @author Babacar Toure
 */
public class EmptyAuthResponse {

    /**
     * Response status
     */
    public final Status status;
    /**
     * Response error object in case of failure
     */
    @Nullable
    public final ApiError.ChangePasswordError error;

    /**
     * Class constructor
     *
     * @param status
     * @param error
     */
    private EmptyAuthResponse(Status status, @Nullable ApiError.ChangePasswordError error) {
        this.status = status;
        this.error = error;
    }

    public Status getStatus() {
        return status;
    }
    public ApiError.ChangePasswordError getError() {
        return error;
    }

    /**
     * Following a successful request, this method returns a EmptyAuthResponse object containing
     * the corresponding status SUCCESS.
     *
     * @return EmptyAuthResponse
     * @see Status#SUCCESS
     */
    public static EmptyAuthResponse success() {
        return new EmptyAuthResponse(SUCCESS, null);
    }

    /**
     * Following a failure when querying the server, this method returns a EmptyAuthResponse
     * object containing the corresponding status ERROR and a ChangePasswordError object that
     * contains the error message from the server.
     *
     * @param error (ChangePasswordError) Error message from the server
     * @return EmptyAuthResponse
     * @see Status#ERROR
     * @see com.example.cshare.data.apiresponses.ApiError.ChangePasswordError
     */
    public static EmptyAuthResponse error(@NonNull ApiError.ChangePasswordError error) {
        return new EmptyAuthResponse(ERROR, error);
    }

    /**
     * Special method to reset the response once the request has taken place and the result
     * has been processed.
     * @return EmptyAuthResponse
     * @see Status#COMPLETE
     */
    public static EmptyAuthResponse complete() {
        return new EmptyAuthResponse(COMPLETE, null);
    }
}
