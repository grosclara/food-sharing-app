package com.example.cshare.WebServices;

import com.example.cshare.Models.Forms.LoginForm;
import com.example.cshare.Models.ApiResponses.ApiEmptyResponse;
import com.example.cshare.Models.ApiResponses.LoginResponse;
import com.example.cshare.Models.Forms.PasswordForm;
import com.example.cshare.Models.Forms.RegisterForm;
import com.example.cshare.Models.Forms.ResetPasswordForm;
import com.example.cshare.Models.User;

import io.reactivex.Observable;

import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface AuthenticationAPI {

    @GET("rest-auth/user/")
    Observable<User> getProfileInfo(
            @Header("Authorization") String token
    );

    @Multipart
    @POST("rest-auth/registration/")
    Observable<LoginResponse> createUserWithPicture(
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
    Observable<LoginResponse> createUserWithoutPicture(
            @Body RegisterForm user
    );

    @POST("rest-auth/login/")
    Observable<LoginResponse> login(@Body LoginForm loginForm);

    @POST("rest-auth/logout/")
    Observable<ApiEmptyResponse> logout(
            @Header("Authorization") String token
    );

    @POST("rest-auth/password/change/")
    Observable<ApiEmptyResponse> changePassword(
            @Header("Authorization") String token,
            @Body PasswordForm passwordForm
    );

    @POST("rest-auth/password/reset/")
    Observable<ApiEmptyResponse> resetPassword(
            @Body ResetPasswordForm resetPasswordForm
    );

    @DELETE("user/{id}/")
    Observable<User> delete(
            @Header("Authorization") String token,
            @Path("id") int userId
    );
}
