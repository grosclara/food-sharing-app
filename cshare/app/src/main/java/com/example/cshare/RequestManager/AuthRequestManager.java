package com.example.cshare.RequestManager;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.cshare.Models.Auth.LoginForm;
import com.example.cshare.Models.Auth.PasswordForm;
import com.example.cshare.Models.Auth.RegisterForm;
import com.example.cshare.Models.Auth.ResetPasswordForm;
import com.example.cshare.Models.Auth.Response.AuthResponse;
import com.example.cshare.Models.Auth.Response.LoginResponse;
import com.example.cshare.Models.User;
import com.example.cshare.Utils.Constants;
import com.example.cshare.Utils.PreferenceProvider;
import com.example.cshare.WebServices.AuthenticationAPI;
import com.example.cshare.WebServices.NetworkClient;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AuthRequestManager {

    private static AuthRequestManager authRequestManager;

    // MutableLiveData object that contains the data
    private MutableLiveData<Boolean> isLoggedInMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isRegisteredMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isPasswordChangedMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isPasswordResetMutableLiveData = new MutableLiveData<>();

    private MutableLiveData<LoginResponse> loginResponseMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<AuthResponse> logoutResponseMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<AuthResponse> deleteResponseMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<AuthResponse> changePasswordMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<AuthResponse> resetPasswordMutableLiveData = new MutableLiveData<>();
    // Data sources dependencies
    private PreferenceProvider prefs;
    private Retrofit retrofit;
    // Insert API interface dependency here
    private AuthenticationAPI authApi;

    public AuthRequestManager(PreferenceProvider prefs) {

        this.prefs = prefs;
        // Define the URL endpoint for the HTTP request.
        this.retrofit = NetworkClient.getRetrofitClient();
        this.authApi = this.retrofit.create(AuthenticationAPI.class);

        // Initialize the value of the boolean isLoggedIn
        isLoggedIn();
    }

    public synchronized static AuthRequestManager getInstance(Application application) throws GeneralSecurityException, IOException {
        /**
         * Method that return the current repository object if it exists
         * else it creates new repository and returns it
         */
        if (authRequestManager == null) {
            authRequestManager = new AuthRequestManager(new PreferenceProvider(application));
        }
        return authRequestManager;
    }

    // Getter method
    public MutableLiveData<Boolean> getIsLoggedInMutableLiveData() {
        return isLoggedInMutableLiveData;
    }
    public MutableLiveData<Boolean> getIsRegisteredMutableLiveData() {
        return isRegisteredMutableLiveData;
    }
    public MutableLiveData<Boolean> getIsPasswordChangedMutableLiveData(){return isPasswordChangedMutableLiveData;}
    public MutableLiveData<Boolean> getIsPasswordResetMutableLiveData(){
        return isPasswordResetMutableLiveData;
    }

    public MutableLiveData<LoginResponse> getLoginResponseMutableLiveData(){ return loginResponseMutableLiveData; }
    public MutableLiveData<AuthResponse> getLogoutResponseMutableLiveData() { return logoutResponseMutableLiveData; }
    public MutableLiveData<AuthResponse> getDeleteResponseMutableLiveData() {return deleteResponseMutableLiveData; }
    public MutableLiveData<AuthResponse> getChangePasswordMutableLiveData() { return changePasswordMutableLiveData; }
    public MutableLiveData<AuthResponse> getResetPasswordMutableLiveData() {return resetPasswordMutableLiveData; }

    // Requests

    public void isLoggedIn() { isLoggedInMutableLiveData.setValue(prefs.isLoggedIn());}

    public void logIn(LoginForm loginForm) {

        Observable<LoginResponse> loginResponseObservable;
        loginResponseObservable = authApi.login(loginForm);
        loginResponseObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS)
                .subscribe(new Observer<LoginResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(Constants.TAG, "Log In : On start subscription");
                        loginResponseMutableLiveData.setValue(LoginResponse.loading());
                    }

                    @Override
                    public void onNext(LoginResponse response) {
                        Log.d(Constants.TAG, "Log In successful");
                        saveUserCredentials(response);
                        loginResponseMutableLiveData.setValue(LoginResponse.success(response.getToken(), response.getUser()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(Constants.TAG, "error");
                        loginResponseMutableLiveData.setValue(LoginResponse.error(e));
                    }

                    @Override
                    public void onComplete() {
                        Log.d(Constants.TAG, "Log In : All data received");
                        //loginResponseMutableLiveData.setValue(LoginResponse.complete());
                        loginResponseMutableLiveData = new MutableLiveData<>();
                    }
                });
    }

    public void saveUserCredentials(LoginResponse loginResponse) {
        prefs.fillPrefs(loginResponse);
        // Update isLoggedInMutableLiveData
        isLoggedIn();
    }

    public void logOut() {
        /**
         * Request to the API to logout
         */

        Observable<Response<AuthResponse>> observable = authApi.logout(prefs.getToken());
        observable
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
                .subscribe(new Observer<Response<AuthResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(Constants.TAG, "Log Out : on start subscription");
                        logoutResponseMutableLiveData.setValue(AuthResponse.loading());
                    }

                    @Override
                    public void onNext(Response<AuthResponse> response) {
                        prefs.logOut();
                        // Update isLoggedInMutableLiveData
                        //isLoggedIn();
                        Log.d(Constants.TAG, "Logged out successfully");
                        logoutResponseMutableLiveData.setValue(AuthResponse.success());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(Constants.TAG, "Log Out : error");
                        logoutResponseMutableLiveData.setValue(AuthResponse.error(e));
                    }

                    @Override
                    public void onComplete() {
                        Log.d(Constants.TAG, "Log out : Completed");
                        logoutResponseMutableLiveData = new MutableLiveData<>();
                    }
                });
    }

    public void registerWithoutPicture(RegisterForm user) {
        /*
        Request to the API to register
         */
        Observable<RegisterForm> userObservable;
        userObservable = authApi.createUserWithoutPicture(user);
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
                .subscribe(new Observer<RegisterForm>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(Constants.TAG, "Registration : on start subscription");
                    }

                    @Override
                    public void onNext(RegisterForm userInfo) {
                        Log.d(Constants.TAG, "Registered successfully");
                        isRegisteredMutableLiveData.setValue(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(Constants.TAG, "Registration : error");
                        isRegisteredMutableLiveData.setValue(false);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(Constants.TAG, "Registration : Completed");
                    }
                });
    }

    public void registerWithPicture(RegisterForm user) {
        /*
        Request to the API to register
         */
        Observable<RegisterForm> userObservable;
        userObservable = authApi.createUserWithPicture(user.getProfile_picture(),
                user.getFirst_name(),
                user.getLast_name(),
                user.getRoom_number(),
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
                .subscribe(new Observer<RegisterForm>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(Constants.TAG, "Registration : on start subscription");
                    }

                    @Override
                    public void onNext(RegisterForm userInfo) {
                        Log.d(Constants.TAG, "Registered successfully");
                        isRegisteredMutableLiveData.setValue(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(Constants.TAG, "Registration : error");
                        Log.d(Constants.TAG, e.getLocalizedMessage());
                        isRegisteredMutableLiveData.setValue(false);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(Constants.TAG, "Registration : Completed");
                    }
                });

    }

    public void changePassword(PasswordForm passwordForm){
            /**
             * Request to the API to change password
             */
            Observable<Response<AuthResponse>> observable;
            observable = authApi.changePassword(prefs.getToken(), passwordForm);
            observable
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
                    .subscribe(new Observer<Response<AuthResponse>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            Log.d(Constants.TAG, "Password change : on start subscription");
                            changePasswordMutableLiveData.setValue(AuthResponse.loading());
                        }

                        @Override
                        public void onNext(Response<AuthResponse> response) {
                            Log.d(Constants.TAG, "Password changed successfully");
                            changePasswordMutableLiveData.setValue(AuthResponse.success());
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d(Constants.TAG, "Password change : error");
                            changePasswordMutableLiveData.setValue(AuthResponse.error(e));
                        }

                        @Override
                        public void onComplete() {
                            Log.d(Constants.TAG, "Password change : Completed");
                            changePasswordMutableLiveData = new MutableLiveData<>();
                        }
                    });
    }

    public void resetPassword(ResetPasswordForm passwordForm){
        /**
         * Request to the API to change password
         */
        Observable<Response<AuthResponse>> observable;
        observable = authApi.resetPassword(passwordForm);
        observable
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
                .subscribe(new Observer<Response<AuthResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(Constants.TAG, "Password reset : on start subscription");
                        resetPasswordMutableLiveData.setValue(AuthResponse.loading());
                    }

                    @Override
                    public void onNext(Response<AuthResponse> response) {
                        Log.d(Constants.TAG, "Password reset successfully");
                        resetPasswordMutableLiveData.setValue(AuthResponse.success());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(Constants.TAG, "Password reset : error");
                        resetPasswordMutableLiveData.setValue(AuthResponse.error(e));
                    }

                    @Override
                    public void onComplete() {
                        Log.d(Constants.TAG, "Password reset : Completed");
                        resetPasswordMutableLiveData = new MutableLiveData<>();
                    }
                });

    }

    public void deleteAccount(){
        /**
         * Request to the API to delete the profile
         */
        Observable<Response<AuthResponse>> observable = authApi.delete(prefs.getToken(), prefs.getUserID());
        observable
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
                .subscribe(new Observer<Response<AuthResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(Constants.TAG, "Deletion : on start subscription");
                        deleteResponseMutableLiveData.setValue(AuthResponse.loading());
                    }

                    @Override
                    public void onNext(Response<AuthResponse> response) {
                        Log.d(Constants.TAG, "Deletion successful");
                        prefs.logOut();
                        deleteResponseMutableLiveData.setValue(AuthResponse.success());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(Constants.TAG, "Deletion : error");
                        deleteResponseMutableLiveData.setValue(AuthResponse.error(e));
                    }

                    @Override
                    public void onComplete() {
                        Log.d(Constants.TAG, "Deletion : Completed");
                        deleteResponseMutableLiveData = new MutableLiveData<>();
                    }
                });

    }
}
