package com.example.cshare.webservices;

import com.example.cshare.data.apiresponses.EmptyAuthResponse;
import com.example.cshare.data.apiresponses.LoginResponse;
import com.example.cshare.data.apiresponses.RegistrationResponse;
import com.example.cshare.data.models.User;
import com.example.cshare.data.sources.AuthRequestManager;
import com.example.cshare.data.sources.ProfileRequestManager;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Interface that defines the authentication-related API endpoints.
 * <p>
 * Provides endpoints for registering, logging in or out, changing and resetting password or
 * deleting account information.
 *
 * @since 1.0
 * @author Clara Gros
 * @author Babacar Toure
 */
public interface AuthenticationAPI {

    /**
     * Returns a Call object that contains the response to the API's getProfileInfo request.
     *
     * @param token (String) of the form "token eyJ0eXAiOiAianFsZyI6ICJIUzUxMiJ9", it corresponds
     *              to the token of the authenticated user and it is passed in the request header
     * @return (Call) A Call object containing the authenticated user in a {@link User} object
     * @see User
     * @see ProfileRequestManager#getUserProfile()
     */
    @GET("rest-auth/user/")
    Call<User> getProfileInfo(
            @Header("Authorization") String token
    );

    /**
     * Returns a Call object that contains the response to the API's register request.
     * <p>
     * Multipart annotation is used to upload the profile picture image to the server.
     *
     * @param profilePictureBody (Image) Profile picture
     * @param firstName (String) First name
     * @param lastName (String) Last name
     * @param roomNumber (String) Room number
     * @param campus (String) Campus
     * @param email (String) Email
     * @param password1 (String) Password
     * @param password2 (String) Password confirmation
     * @return (Call) A Call object containing the registered user and its token in a
     * {@link RegistrationResponse} object
     * @see User
     * @see com.example.cshare.data.sources.AuthRequestManager#registerWithPicture(User)
     * @see #createUserWithoutPicture(User)
     * @see RegistrationResponse
     */
    @Multipart
    @POST("rest-auth/registration/")
    Call<RegistrationResponse> createUserWithPicture(
            @Part MultipartBody.Part profilePictureBody,
            @Part("first_name") String firstName,
            @Part("last_name") String lastName,
            @Part("room_number") String roomNumber,
            @Part("campus") String campus,
            @Part("email") String email,
            @Part("password1") String password1,
            @Part("password2") String password2
    );

    /**
     * Returns a Call object that contains the response to the API's register request.
     * <p>
     * No profile picture is uploaded in this method.
     *
     * @param registerForm (User) contains the first_name, last_name, room_number, campus,
     *                     email, password1, password2 attributes
     * @return (Call) A Call object containing the registered user and its token in a
     * {@link RegistrationResponse} object
     * @see User
     * @see RegistrationResponse
     * @see com.example.cshare.data.sources.AuthRequestManager#registerWithPicture(User)
     * @see #createUserWithPicture(MultipartBody.Part, String, String, String, String, String, String, String)
     */
    @POST("rest-auth/registration/")
    Call<RegistrationResponse> createUserWithoutPicture(
            @Body User registerForm
    );

    /**
     * Returns a Call object that contains the response to the API's login request.
     *
     * @param loginForm (User) contains the credentials (eg. email and password) of the user
     * @return (Call) A Call object containing the logged in user and its token in a
     * {@link LoginResponse} object
     * @see User
     * @see LoginResponse
     * @see com.example.cshare.data.sources.AuthRequestManager#logIn(User)
     */
    @POST("rest-auth/login/")
    Call<LoginResponse> login(@Body User loginForm);

    /**
     * Returns a Call object that contains the response to the API's logout request.
     *
     * @param token (String) of the form "token eyJ0eXAiOiAianFsZyI6ICJIUzUxMiJ9", it corresponds
     *              to the token of the authenticated user and it is passed in the request header
     * @return (Call) A Call object containing an {@link EmptyAuthResponse} object
     * @see EmptyAuthResponse
     * @see AuthRequestManager#logOut()
     */
    @POST("rest-auth/logout/")
    Call<EmptyAuthResponse> logout(
            @Header("Authorization") String token
    );

    /**
     * Returns a Call object that contains the response to the API's change password request.
     *
     * @param token (String) of the form "token eyJ0eXAiOiAianFsZyI6ICJIUzUxMiJ9", it corresponds
     *        to the token of the authenticated user and it is passed in the request header
     * @param changePasswordForm (User) contains the new password, its confirmation and the old
     *                          password of the user
     * @return (Call) A Call object containing an {@link EmptyAuthResponse} object
     * @see EmptyAuthResponse
     * @see AuthRequestManager#changePassword(User)
     */
    @POST("rest-auth/password/change/")
    Call<EmptyAuthResponse> changePassword(
            @Header("Authorization") String token,
            @Body User changePasswordForm
    );

    /**
     * Returns a Call object that contains the response to the API's reset password request.
     *
     * @param resetPasswordForm (User) contains the user email address
     * @return (Call) A Call object containing an {@link EmptyAuthResponse} object
     * @see EmptyAuthResponse
     * @see AuthRequestManager#resetPassword(User)
     */
    @FormUrlEncoded
    @POST("rest-auth/password/reset/")
    Call<EmptyAuthResponse> resetPassword(
            @Body User resetPasswordForm
    );

    /**
     * Returns a Call object that contains the response to the API's delete account request.
     *
     * @param token (String) of the form "token eyJ0eXAiOiAianFsZyI6ICJIUzUxMiJ9", it corresponds
     *        to the token of the authenticated user and it is passed in the request header
     * @param userID (int) ID of the user to delete
     * @return (Call) A Call object containing an {@link EmptyAuthResponse} object
     * @see EmptyAuthResponse
     * @see AuthRequestManager#deleteAccount()
     */
    @DELETE("user/{id}/")
    Call<EmptyAuthResponse> delete(
            @Header("Authorization") String token,
            @Path("id") int userID
    );
}
