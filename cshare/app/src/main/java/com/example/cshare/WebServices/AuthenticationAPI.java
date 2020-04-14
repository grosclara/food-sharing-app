package com.example.cshare.WebServices;

import com.example.cshare.Models.LoginForm;
import com.example.cshare.Models.LoginResponse;
import com.example.cshare.Models.User;

import io.reactivex.Observable;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface AuthenticationAPI {

    // CREATE NEW MODEL CLASS TO AUTHENTICATE ANY USER

    @GET("rest-auth/user/")
    Observable<User> getProfileInfo(
            @Header("Authorization") String token
    );

    @Multipart
    @POST("rest-auth/registration/")
    Observable<User> createUserWithPicture(
            @Part MultipartBody.Part profile_picture,
            @Part("first_name") String firstName,
            @Part("last_name") String lastName,
            @Part("room_number") String roomNumber,
            @Part("campus") String campus,
            @Part("email") String email,
            @Part("password1") String password1,
            @Part("password2") String password2
    );

    @POST("rest-auth/registration/")
    Observable<User> createUserWithoutPicture(
            @Body User user
    );

    @POST("rest-auth/login/")
    Observable<LoginResponse> login(@Body LoginForm loginForm);

    @POST("rest-auth/logout/")
    Observable<ResponseBody> logout(
            @Header("Authorization") String token
    );

    @POST("rest-auth/password/change/")
    Observable<User> changePassword(
            @Header("Authorization") String token,
            @Body User user
    );

    @POST("rest-auth/password/reset/")
    Observable<User> resetPassword(
            @Body User user
    );
}
