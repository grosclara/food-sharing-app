package com.example.cshare.data.apiresponses;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cshare.data.models.User;

import static com.example.cshare.data.apiresponses.Status.COMPLETE;
import static com.example.cshare.data.apiresponses.Status.ERROR;
import static com.example.cshare.data.apiresponses.Status.SUCCESS;

/**
 * Class that contains the response to a query returning a user.
 * <p>
 * The class consists of a status that indicates the status of the response returned by the server.
 * If successful, the response contains the returned user. In case of failure, the response contains
 * an instance of the class Api error to access the error message returned by the server.
 * <p>
 * The defined methods are on the one hand the getters and on the other hand methods that allow to
 * create instances of the class by associating them a certain status
 *
 * @see Status
 * @see ApiError
 * @see User
 * @since 2.0
 * @author Clara Gros
 * @author Babacar Toure
 */
public class UserResponse {

    /**
     * Response status
     */
    public final Status status;
    /**
     * Response body of the request in case of success
     */
    @Nullable
    public final User user;
    /**
     * Response error object in case of failure
     */
    @Nullable
    public final ApiError error;

    /**
     * Class constructor
     *
     * @param status
     * @param user
     * @param error
     */
    private UserResponse(Status status, @Nullable User user, @Nullable ApiError error) {
        this.status = status;
        this.user = user;
        this.error = error;
    }

    public Status getStatus() { return status; }

    @Nullable
    public User getUser() { return user; }

    @Nullable
    public ApiError getError() { return error; }

    /**
     * Following a successful response, this method returns a UserResponse object containing the
     * corresponding status SUCCESS and the response body which is a user
     * @param user (User)
     * @return UserResponse
     * @see Status#SUCCESS
     * @see User
     */
    public static UserResponse success(@NonNull User user) {
        return new UserResponse(SUCCESS, user, null);
    }

    /**
     * Following a failure when querying the server response, this method returns a UserResponse
     * object containing the corresponding status ERROR and a ApiError object that contains the
     * error message from the server.
     * @param error (ApiError) Error message from the server
     * @return UserResponse
     * @see Status#ERROR
     * @see ApiError
     */
    public static UserResponse error(@NonNull ApiError error) {
        return new UserResponse(ERROR, null, error);
    }

    /**
     * Special method to reset the response once the request has taken place and the result has
     * been processed.
     * @return UserResponse
     * @see Status#COMPLETE
     */
    public static UserResponse complete(){
        return new UserResponse(COMPLETE, null, null);
    }
}
