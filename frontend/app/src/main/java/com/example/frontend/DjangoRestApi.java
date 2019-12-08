package com.example.frontend;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface DjangoRestApi {

    @GET("user")
    Call<List<User>> getAllUsers();

}
