package com.example.cshare.data.apiresponses;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cshare.data.models.User;

import java.util.List;

import static com.example.cshare.data.apiresponses.Status.COMPLETE;
import static com.example.cshare.data.apiresponses.Status.ERROR;
import static com.example.cshare.data.apiresponses.Status.SUCCESS;

/**
 * Class that contains the response to a registration request returning the logged in user and
 * its token.
 * <p>
 * The class consists of a status that indicates the status of the response returned by the server.
 * If successful, the response contains the returned user and its token.
 * In case of failure, the response contains an instance of the Login error class to access
 * the error message returned by the server.
 * <p>
 * The defined methods are on the one hand the getters and on the other hand methods that allow to
 * create instances of the class by associating them a certain status
 *
 * @see Status
 * @see com.example.cshare.data.apiresponses.ApiError.RegistrationError
 * @see User
 * @since 2.0
 * @author Clara Gros
 * @author Babacar Toure
 */
public class RegistrationResponse {

    /**
     * Response status
     */
    public final Status status;
    /**
     * Response body (user) of the request in case of success
     */
    @Nullable
    public final User user;
    /**
     * Response body (token) of the request in case of success
     */
    @Nullable
    public final String key;
    /**
     * Response error object in case of failure
     */
    @Nullable
    public final ApiError.RegistrationError error;

    /**
     * Class constructor
     *
     * @param status
     * @param token
     * @param data
     * @param error
     */
    private RegistrationResponse(Status status, @Nullable String token, @Nullable User data, @Nullable ApiError.RegistrationError error) {
        this.status = status;
        this.user = data;
        this.error = error;
        this.key = token;
    }

    public User getUser() { return user; }
    public Status getStatus() { return status; }
    public String getToken() { return key; }
    public ApiError.RegistrationError getError(){ return error; }

    /**
     * Following a successful sign up response, this method returns a LoginResponse object
     * containing the corresponding status SUCCESS and the response body which is a user and its
     * token.
     *
     * @param user (User)
     * @param token (String)
     * @return RegistrationResponse
     * @see Status#SUCCESS
     * @see User
     */
    public static RegistrationResponse success(@NonNull String token, @NonNull User user) {
        return new RegistrationResponse(SUCCESS, token, user, null);
    }

    /**
     * Following a failure when querying the server to sign up, this method returns a
     * RegistrationResponse object containing the corresponding status ERROR and a RegistrationError
     * object that contains the error message from the server.
     *
     * @param error (RegistrationError) Error message from the server
     * @return RegistrationResponse
     * @see Status#ERROR
     * @see com.example.cshare.data.apiresponses.ApiError.RegistrationError
     */
    public static RegistrationResponse error(@NonNull ApiError.RegistrationError error) {
        return new RegistrationResponse(ERROR, null, null, error);
    }

    /**
     * Special method to reset the sign up response once the request has taken place and the result
     * has been processed.
     * @return RegistrationResponse
     * @see Status#COMPLETE
     */
    public static RegistrationResponse complete(){
        return new RegistrationResponse(COMPLETE, null, null, null);
    }

}