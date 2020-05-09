package com.example.cshare.WebServices;

import com.example.cshare.Models.ApiResponses.LoginResponse;
import com.example.cshare.Models.ApiResponses.EmptyAuthResponse;
import com.example.cshare.Models.ApiResponses.RegistrationResponse;
import com.example.cshare.Models.Forms.PasswordForm;
import com.example.cshare.Models.User;

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

public interface AuthenticationAPI {

    @GET("rest-auth/user/")
    Call<User> getProfileInfo(
            @Header("Authorization") String token
    );

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

    @POST("rest-auth/registration/")
    Call<RegistrationResponse> createUserWithoutPicture(
            @Body User registerForm
    );

    @POST("rest-auth/login/")
    Call<LoginResponse> login(@Body User loginForm);

    @POST("rest-auth/logout/")
    Call<EmptyAuthResponse> logout(
            @Header("Authorization") String token
    );

    @POST("rest-auth/password/change/")
    Call<EmptyAuthResponse> changePassword(
            @Header("Authorization") String token,
            @Body PasswordForm passwordForm
    );

    @FormUrlEncoded
    @POST("rest-auth/password/reset/")
    Call<EmptyAuthResponse> resetPassword(
            @Body User resetPasswordForm
    );

    @DELETE("user/{id}/")
    Call<EmptyAuthResponse> delete(
            @Header("Authorization") String token,
            @Path("id") int userID
    );
}
