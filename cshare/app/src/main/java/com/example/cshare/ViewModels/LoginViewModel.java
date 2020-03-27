package com.example.cshare.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;
import android.view.View;

import com.example.cshare.Models.LoginForm;
import com.example.cshare.R;

public class LoginViewModel extends ViewModel {

    public MutableLiveData<String> EmailAddress = new MutableLiveData<>();
    public MutableLiveData<String> Password = new MutableLiveData<>();

    private MutableLiveData<LoginForm> userMutableLiveData;

    public MutableLiveData<LoginForm> getUser() {

        if (userMutableLiveData == null) {
            userMutableLiveData = new MutableLiveData<>();
        }
        return userMutableLiveData;

    }

    public void submitValidForm() {

        LoginForm loginUser = new LoginForm(EmailAddress.getValue(), Password.getValue());
        userMutableLiveData.setValue(loginUser);

    }

}