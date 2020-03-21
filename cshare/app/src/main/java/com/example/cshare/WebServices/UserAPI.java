package com.example.cshare.WebServices;

import com.example.cshare.Models.User;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface UserAPI {

    @GET("user/{id}/")
    Observable<User> getUserByID(
            @Header("Authorization") String token,
            @Path("id") int userId);

    @Multipart
    @PUT("user/{id}/")
    Observable<User> updateProfileWithPicture(
            @Header("Authorization") String token,
            @Path("id") int id,
            @Part MultipartBody.Part profile_picture,
            @Part("first_name") String firstName,
            @Part("last_name") String lastName,
            @Part("room_number") String roomNumber,
            @Part("campus") String campus,
            @Part("email") String email,
            @Part("is_active") Boolean isActive
    );

    @Multipart
    @PUT("user/{id}/")
    Observable<User> updateProfileWithoutPicture(
            @Header("Authorization") String token,
            @Path("id") int id,
            @Part("first_name") String firstName,
            @Part("last_name") String lastName,
            @Part("room_number") String roomNumber,
            @Part("campus") String campus,
            @Part("email") String email,
            @Part("is_active") Boolean isActive
    );

    @DELETE("user/{id}/")
    Observable<ResponseBody> deleteUserById(
            @Header("Authorization") String token,
            @Path("id") int userId
    );

}
