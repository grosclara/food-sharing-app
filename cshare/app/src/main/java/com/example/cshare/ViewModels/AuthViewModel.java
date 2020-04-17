package com.example.cshare.ViewModels;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import android.app.Application;

import com.example.cshare.RequestManager.AuthRequestManager;
import com.example.cshare.Models.LoginForm;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class AuthViewModel extends AndroidViewModel {

    private AuthRequestManager authRequestManager;

    // MutableLiveData object that contains the data
    private MutableLiveData<Boolean> isLoggedInMutableLiveData;

    public AuthViewModel(Application application) throws GeneralSecurityException, IOException {
        super(application);

        // Get request manager instance
        authRequestManager = AuthRequestManager.getInstance(getApplication());

        // Initialize isLoggedIn var
        isLoggedInMutableLiveData = authRequestManager.getIsLoggedInMutableLiveData();
    }

    // Getter method
    public MutableLiveData<Boolean> getIsLoggedInMutableLiveData() {
        return isLoggedInMutableLiveData;
    }

    public void logIn(LoginForm loginForm){
        authRequestManager.logIn(loginForm);
    }

 /*   public void registerWithoutPicture(User user) {
        *//**
         * Request to the API to register
         *//*
        Observable<User> userObservable;
        userObservable = authAPI.createUserWithoutPicture(user);
        userObservable
                // Run the Observable in a dedicated thread (Schedulers.io)
                .subscribeOn(Schedulers.io())
                // Allows to tell all Subscribers to listen to the Observable data stream on the
                // main thread (AndroidSchedulers.mainThread) which will allow us to modify elements
                // of the graphical interface from the  method
                .observeOn(AndroidSchedulers.mainThread())
                // If the Subscriber has not sent data before the defined time (10 seconds),
                // the data transmission will be stopped and a Timeout error will be sent to the
                // Subscribers via their onError() method.
                .timeout(10, TimeUnit.SECONDS)
                .subscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(Constants.TAG, "Registration : on start subscription");
                    }

                    @Override
                    public void onNext(User userInfo) {
                        Log.d(Constants.TAG, "Registered successfully");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(Constants.TAG, "Registration : error");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(Constants.TAG, "Registration : Completed");
                    }
                });

    }

    public boolean isLoggedIn() {
        return authRequestManager.isLoggedIn();
    }

    public void logOut() {
        *//**
         * Request to the API to logout
         *//*
        loggedOutMutableLiveData = new MutableLiveData<>();

        Observable<ResponseBody> key = authAPI.logout(token);
        key
                // Run the Observable in a dedicated thread (Schedulers.io)
                .subscribeOn(Schedulers.io())
                // Allows to tell all Subscribers to listen to the Observable data stream on the
                // main thread (AndroidSchedulers.mainThread) which will allow us to modify elements
                // of the graphical interface from the  method
                .observeOn(AndroidSchedulers.mainThread())
                // If the Subscriber has not sent data before the defined time (10 seconds),
                // the data transmission will be stopped and a Timeout error will be sent to the
                // Subscribers via their onError() method.
                .timeout(10, TimeUnit.SECONDS)
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(Constants.TAG, "Log Out : on start subscription");
                    }

                    @Override
                    public void onNext(ResponseBody empty) {
                        loggedOutMutableLiveData.setValue(true);
                        Log.d(Constants.TAG, "Logged out successfully");
                    }

                    @Override
                    public void onError(Throwable e) {
                        loggedOutMutableLiveData.setValue(false);
                        Log.d(Constants.TAG, "Log Out : error");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(Constants.TAG, "Log out : Completed");
                    }
                });

    }

    public void submitValidForm(LoginForm loginForm) {
        responseMutableLiveData = new MutableLiveData<>();
        loggedOutMutableLiveData = new MutableLiveData<>();
        loginFormMutableLiveData = new MutableLiveData<>();
        loginFormMutableLiveData.setValue(loginForm);

        Observable<LoginResponse> loginResponseObservable;
        loginResponseObservable = authAPI.login(
                loginFormMutableLiveData.getValue());
        loginResponseObservable
                // Run the Observable in a dedicated thread (Schedulers.io)
                .subscribeOn(Schedulers.io())
                // Allows to tell all Subscribers to listen to the Observable data stream on the
                // main thread (AndroidSchedulers.mainThread) which will allow us to modify elements
                // of the graphical interface from the  method
                .observeOn(AndroidSchedulers.mainThread())
                // If the Subscriber has not sent data before the defined time (10 seconds),
                // the data transmission will be stopped and a Timeout error will be sent to the
                // Subscribers via their onError() method.
                .timeout(10, TimeUnit.SECONDS)
                .subscribe(new Observer<LoginResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(Constants.TAG, "Log In : on start subscription");
                    }


                    @Override
                    public void onNext(LoginResponse loginResponse) {

                        String token = loginResponse.getKey();
                        User user = loginResponse.getUser();
                        LoginResponse response = new LoginResponse(token, "success", user);
                        responseMutableLiveData.setValue(response);
                        loggedOutMutableLiveData.setValue(false);
                        Log.d(Constants.TAG, "Log In : live Data filled");
                    }


                    @Override
                    public void onError(Throwable e) {
                        Log.d(Constants.TAG, "error");
                        LoginResponse profileResponse = new LoginResponse("Log In : failed");
                        responseMutableLiveData.setValue(profileResponse);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(Constants.TAG, "Log In : All data received");
                    }
                });

    }

    public void registerWithPicture(User user) {
        *//**
         * Request to the API to create an account with a profile picture
         * @param User
         *//*

        Observable<User> userObservable;
        userObservable = authAPI.createUserWithPicture(
                user.getProfilePictureBody(),
                user.getFirstName(),
                user.getLastName(),
                user.getRoomNumber(),
                user.getCampus(),
                user.getEmail(),
                user.getPassword1(),
                user.getPassword2());
        userObservable
                // Run the Observable in a dedicated thread (Schedulers.io)
                .subscribeOn(Schedulers.io())
                // Allows to tell all Subscribers to listen to the Observable data stream on the
                // main thread (AndroidSchedulers.mainThread) which will allow us to modify elements
                // of the graphical interface from the  method
                .observeOn(AndroidSchedulers.mainThread())
                // If the Subscriber has not sent data before the defined time (10 seconds),
                // the data transmission will be stopped and a Timeout error will be sent to the
                // Subscribers via their onError() method.
                .timeout(10, TimeUnit.SECONDS)
                .subscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(Constants.TAG, "Register : on start subscription");
                    }

                    @Override
                    public void onNext(User newUser) {
                        Log.d(Constants.TAG, "Account created successfully");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(Constants.TAG, "Register : error");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(Constants.TAG, "Register : Completed");
                    }
                });
    }*/
}