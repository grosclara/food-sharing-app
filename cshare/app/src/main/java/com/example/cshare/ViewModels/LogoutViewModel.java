package com.example.cshare.ViewModels;

import androidx.lifecycle.ViewModel;

import com.example.cshare.WebServices.AuthenticationAPI;
import com.example.cshare.WebServices.NetworkClient;

import retrofit2.Retrofit;

public class LogoutViewModel extends ViewModel {
    private Retrofit retrofit;
    // Insert API interface dependency here
    private AuthenticationAPI authAPI;


    public LogoutViewModel() {
        // Define the URL endpoint for the HTTP request.
        retrofit = NetworkClient.getRetrofitClient();
        authAPI = retrofit.create(AuthenticationAPI.class);
    }
}
