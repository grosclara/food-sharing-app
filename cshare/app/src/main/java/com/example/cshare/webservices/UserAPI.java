package com.example.cshare.webservices;

import com.example.cshare.data.models.User;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Interface that defines the user-related API endpoints.
 * <p>
 * Provides endpoints to retrieve user information or to update a profile.
 *
 * @since 1.0
 * @author Clara Gros
 * @author Babacar Toure
 */
public interface UserAPI {

    /**
     * Returns a Call object that contains the response to the API's getUserByID request.
     *
     * @param  token  (String) of the form "token eyJ0eXAiOiAiand0IiwgImFsZyI6ICJIUzUxMiJ9" which corresponds
     *                to the token of the authenticated user and it is passed in the request header
     * @param  userID an integer that corresponds to the ID of the authenticated user in the url path
     * @return (Call) A Call object containing the user information in a {@link User} object
     * @see User
     * @see com.example.cshare.data.sources.ProfileRequestManager#getUserByID(int)
     */
    @GET("user/{id}/")
    Call<User> getUserByID(
            @Header("Authorization") String token,
            @Path("id") int userID);

    /**
     * Returns a Call object that contains the response to the API's updateProfile
     * request. This method takes a MultipartBody.Part profile picture as argument which means the
     * user wants to update its profile picture
     *
     * @param token (String) of the form "token eyJ0eXAiOiAianFsZyI6ICJIUzUxMiJ9", it corresponds
     *              to the token of the authenticated user and it is passed in the request header
     * @param userID (int) corresponds to the ID of the authenticated user in the url path
     * @param profile_picture (Image) New profile picture of the authenticated user (request body)
     * @param roomNumber (String) New room number of the authenticated user (request body)
     * @param campus (String) New campus of the authenticated user (request body)
     * @return (Call) A Call object containing the user information in a {@link User} object
     * @see User
     * @see #updateProfileWithoutPicture(String, int, User)
     * @see com.example.cshare.data.sources.ProfileRequestManager#editProfileWithPicture(User)
     */
    @Multipart
    @PATCH("user/{id}/")
    Call<User> updateProfileWithPicture(
            @Header("Authorization") String token,
            @Path("id") int userID,
            @Part MultipartBody.Part profile_picture,
            @Part("room_number") String roomNumber,
            @Part("campus") String campus
    );

    /**
     * Returns a Call object that contains the response to the API's updateProfile request. The
     * request does not contain a profile picture which means the user only wants to update its
     * room number and/or campus.
     *
     * @param token (String) of the form "token eyJ0eXAiOiAianFsZyI6ICJIUzUxMiJ9", it corresponds
     *              to the token of the authenticated user and it is passed in the request header
     * @param userID (int) corresponds to the ID of the authenticated user in the url path
     * @param editProfileForm (User) Contains the campus, and room number attributes to change (request body)
     * @return (Call) A Call object containing the user information in a {@link User} object
     * @see User
     * @see #updateProfileWithPicture(String, int, MultipartBody.Part, String, String)
     * @see com.example.cshare.data.sources.ProfileRequestManager#editProfileWithoutPicture(User)
     */
    @PATCH("user/{id}/")
    Call<User> updateProfileWithoutPicture(
            @Header("Authorization") String token,
            @Path("id") int userID,
            @Body User editProfileForm
    );

}
