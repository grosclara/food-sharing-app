package com.example.cshare.RequestManager;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.cshare.Models.Forms.LoginForm;
import com.example.cshare.Models.Forms.PasswordForm;
import com.example.cshare.Models.Forms.RegisterForm;
import com.example.cshare.Models.ApiResponses.ApiEmptyResponse;
import com.example.cshare.Models.ApiResponses.LoginResponse;
import com.example.cshare.Models.ApiResponses.UserReponse;
import com.example.cshare.Models.User;
import com.example.cshare.Utils.Constants;
import com.example.cshare.Utils.PreferenceProvider;
import com.example.cshare.WebServices.AuthenticationAPI;
import com.example.cshare.WebServices.NetworkClient;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class AuthRequestManager {

    private static AuthRequestManager authRequestManager;

    // MutableLiveData object that contains the data
    private MutableLiveData<Boolean> isLoggedInMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<LoginResponse> loginResponseMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<ApiEmptyResponse> logoutResponseMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<UserReponse> deleteResponseMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<ApiEmptyResponse> changePasswordMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<ApiEmptyResponse> resetPasswordMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<LoginResponse> registrationResponseMutableLiveData = new MutableLiveData<>();

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

    public MutableLiveData<LoginResponse> getLoginResponseMutableLiveData() {
        return loginResponseMutableLiveData;
    }

    public MutableLiveData<ApiEmptyResponse> getLogoutResponseMutableLiveData() {
        return logoutResponseMutableLiveData;
    }

    public MutableLiveData<UserReponse> getDeleteResponseMutableLiveData() {
        return deleteResponseMutableLiveData;
    }

    public MutableLiveData<ApiEmptyResponse> getChangePasswordMutableLiveData() {
        return changePasswordMutableLiveData;
    }

    public MutableLiveData<ApiEmptyResponse> getResetPasswordMutableLiveData() {
        return resetPasswordMutableLiveData;
    }

    public MutableLiveData<LoginResponse> getRegistrationResponseMutableLiveData() {
        return registrationResponseMutableLiveData;
    }

    // Requests

    public void isLoggedIn() {
        isLoggedInMutableLiveData.setValue(prefs.isLoggedIn());
    }

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
                        loginResponseMutableLiveData.setValue(LoginResponse.success(response.getToken(), response.getUser()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(Constants.TAG, "error");
                        loginResponseMutableLiveData.postValue(LoginResponse.error(e));
                    }

                    @Override
                    public void onComplete() {
                        Log.d(Constants.TAG, "Log In : All data received");
                        loginResponseMutableLiveData.setValue(LoginResponse.complete());
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

        Observable<ApiEmptyResponse> observable = authApi.logout(prefs.getToken());
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
                .subscribe(new Observer<ApiEmptyResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(Constants.TAG, "Log Out : on start subscription");
                        logoutResponseMutableLiveData.setValue(ApiEmptyResponse.loading());
                    }

                    @Override
                    public void onNext(ApiEmptyResponse response) {
                        prefs.logOut();
                        // Update isLoggedInMutableLiveData
                        //isLoggedIn();
                        logoutResponseMutableLiveData.setValue(ApiEmptyResponse.success());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(Constants.TAG, "Log Out : error");
                        logoutResponseMutableLiveData.setValue(ApiEmptyResponse.error(e));
                    }

                    @Override
                    public void onComplete() {
                        Log.d(Constants.TAG, "Log out : Completed");
                        logoutResponseMutableLiveData.setValue(ApiEmptyResponse.complete());
                    }
                });
    }

    public void registerWithoutPicture(RegisterForm user) {
        /*
        Request to the API to register
         */
        Observable<LoginResponse> registrationObservable;
        registrationObservable = authApi.createUserWithoutPicture(user);
        registrationObservable
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
                        Log.d(Constants.TAG, "Registration : on start subscription");
                        registrationResponseMutableLiveData.setValue(LoginResponse.loading());
                    }

                    @Override
                    public void onNext(LoginResponse userInfo) {
                        Log.d(Constants.TAG, "Registered successfully");
                        registrationResponseMutableLiveData.setValue(LoginResponse.success(userInfo.getToken(), userInfo.getUser()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(Constants.TAG, "Registration : error");
                        registrationResponseMutableLiveData.setValue(LoginResponse.error(e));
                    }

                    @Override
                    public void onComplete() {
                        Log.d(Constants.TAG, "Registration : Completed");
                        registrationResponseMutableLiveData.setValue(LoginResponse.complete());
                    }
                });
    }

    public void registerWithPicture(RegisterForm user) {
        /*
        Request to the API to register
         */
        Observable<LoginResponse> registrationObservable;
        registrationObservable = authApi.createUserWithPicture(user.getProfile_picture(),
                user.getFirst_name(),
                user.getLast_name(),
                user.getRoom_number(),
                user.getCampus(),
                user.getEmail(),
                user.getPassword1(),
                user.getPassword2());
        registrationObservable
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
                        Log.d(Constants.TAG, "Registration : on start subscription");
                        registrationResponseMutableLiveData.setValue(LoginResponse.loading());
                    }

                    @Override
                    public void onNext(LoginResponse userInfo) {
                        Log.d(Constants.TAG, "Registered successfully");
                        registrationResponseMutableLiveData.setValue(LoginResponse.success(userInfo.getToken(), userInfo.getUser()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(Constants.TAG, "Registration : error");
                        registrationResponseMutableLiveData.setValue(LoginResponse.error(e));
                    }

                    @Override
                    public void onComplete() {
                        Log.d(Constants.TAG, "Registration : Completed");
                        registrationResponseMutableLiveData.setValue(LoginResponse.complete());
                    }
                });

    }

    public void changePassword(PasswordForm passwordForm) {
        /**
         * Request to the API to change password
         */
        Observable<ApiEmptyResponse> observable;
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
                .subscribe(new Observer<ApiEmptyResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(Constants.TAG, "Password change : on start subscription");
                        changePasswordMutableLiveData.setValue(ApiEmptyResponse.loading());
                    }

                    @Override
                    public void onNext(ApiEmptyResponse response) {
                        Log.d(Constants.TAG, "Password changed successfully");
                        changePasswordMutableLiveData.setValue(ApiEmptyResponse.success());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(Constants.TAG, "Password change : error");
                        changePasswordMutableLiveData.setValue(ApiEmptyResponse.error(e));
                    }

                    @Override
                    public void onComplete() {
                        Log.d(Constants.TAG, "Password change : Completed");
                        changePasswordMutableLiveData.setValue(ApiEmptyResponse.complete());
                    }
                });
    }

    public void resetPassword(String email) {
        /**
         * Request to the API to change password
         */

        Map<String, String> emailFieldMap = new HashMap<>();
        emailFieldMap.put("email", email);

        Observable<ApiEmptyResponse> observable;
        observable = authApi.resetPassword(emailFieldMap);
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
                .subscribe(new Observer<ApiEmptyResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(Constants.TAG, "Password reset : on start subscription");
                        resetPasswordMutableLiveData.setValue(ApiEmptyResponse.loading());
                    }

                    @Override
                    public void onNext(ApiEmptyResponse response) {
                        Log.d(Constants.TAG, "Password reset successfully");
                        resetPasswordMutableLiveData.setValue(ApiEmptyResponse.success());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(Constants.TAG, "Password reset : error");
                        resetPasswordMutableLiveData.setValue(ApiEmptyResponse.error(e));
                    }

                    @Override
                    public void onComplete() {
                        Log.d(Constants.TAG, "Password reset : Completed");
                        resetPasswordMutableLiveData.setValue(ApiEmptyResponse.complete());
                    }
                });

    }

    public void deleteAccount() {
        /**
         * Request to the API to delete the profile
         */
        Observable<User> observable = authApi.delete(prefs.getToken(), prefs.getUserID());
        Log.d(Constants.TAG, String.valueOf(prefs.getUserID()));
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
                .subscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(Constants.TAG, "Deletion : on start subscription");
                        deleteResponseMutableLiveData.setValue(UserReponse.loading());
                    }

                    @Override
                    public void onNext(User response) {
                        prefs.logOut();
                        deleteResponseMutableLiveData.setValue(UserReponse.success(response));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(Constants.TAG, "Deletion : error");
                        deleteResponseMutableLiveData.setValue(UserReponse.error(e));
                    }

                    @Override
                    public void onComplete() {
                        Log.d(Constants.TAG, "Deletion : Completed");
                        deleteResponseMutableLiveData.setValue(UserReponse.complete());
                    }
                });

    }
}
