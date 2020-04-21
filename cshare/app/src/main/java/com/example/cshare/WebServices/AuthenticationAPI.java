package com.example.cshare.WebServices;

import com.example.cshare.Models.Auth.LoginForm;
import com.example.cshare.Models.Auth.LoginResponse;
import com.example.cshare.Models.Auth.PasswordForm;
import com.example.cshare.Models.Auth.RegisterForm;
import com.example.cshare.Models.Auth.ResetPasswordForm;
import com.example.cshare.Models.User;

import io.reactivex.Observable;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface AuthenticationAPI {

    // CREATE NEW MODEL CLASS TO AUTHENTICATE ANY USER

    @GET("rest-auth/user/")
    Observable<User> getProfileInfo(
            @Header("Authorization") String token
    );

    @Multipart
    @POST("rest-auth/registration/")
    Observable<RegisterForm> createUserWithPicture(
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
    Observable<RegisterForm> createUserWithoutPicture(
            @Body RegisterForm user
    );

    @POST("rest-auth/login/")
    Observable<LoginResponse> login(@Body LoginForm loginForm);

    @POST("rest-auth/logout/")
    Observable<Response<User>> logout(
            @Header("Authorization") String token
    );

    @POST("rest-auth/password/change/")
    Observable<PasswordForm> changePassword(
            @Header("Authorization") String token,
            @Body PasswordForm passwordForm
    );

    @POST("rest-auth/password/reset/")
    Observable<Response<ResetPasswordForm>> resetPassword(
            @Body ResetPasswordForm resetPasswordForm
    );

    @DELETE("user/{id}/")
    Observable<Response<User>> delete(
            @Header("Authorization") String token,
            @Path("id") int userId
    );
}
