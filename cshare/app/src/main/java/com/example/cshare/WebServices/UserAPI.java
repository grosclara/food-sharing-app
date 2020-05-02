package com.example.cshare.WebServices;

import com.example.cshare.Models.Forms.EditProfileForm;
import com.example.cshare.Models.User;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface UserAPI {

    @GET("user/{id}/")
    Call<User> getUserByID(
            @Header("Authorization") String token,
            @Path("id") int userId);

    @Multipart
    @PATCH("user/{id}/")
    Call<User> updateProfileWithPicture(
            @Header("Authorization") String token,
            @Path("id") int id,
            @Part MultipartBody.Part profile_picture,
            @Part("first_name") String firstName,
            @Part("last_name") String lastName,
            @Part("room_number") String roomNumber,
            @Part("campus") String campus
    );

    @PATCH("user/{id}/")
    Call<User> updateProfileWithoutPicture(
            @Header("Authorization") String token,
            @Path("id") int id,
            @Body EditProfileForm form
    );

}
