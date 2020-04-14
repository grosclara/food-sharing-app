package com.example.cshare.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Log;

import com.example.cshare.Controllers.Activities.MainActivity;
import com.example.cshare.Models.LoginForm;
import com.example.cshare.Models.LoginResponse;

import com.example.cshare.Models.User;
import com.example.cshare.Utils.Constants;
import com.example.cshare.WebServices.AuthenticationAPI;
import com.example.cshare.WebServices.NetworkClient;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;

public class AuthViewModel extends ViewModel {

    private MutableLiveData<LoginForm> loginFormMutableLiveData;
    private MutableLiveData<LoginResponse> responseMutableLiveData;
    private MutableLiveData<Boolean> loggedOutMutableLiveData;

    private Retrofit retrofit;
    // Insert API interface dependency here
    private AuthenticationAPI authAPI;

    private String token = MainActivity.token;
    private String campus = MainActivity.campus;
    private int userID = MainActivity.userID;

    public MutableLiveData<LoginResponse> getResponseMutableLiveData() {return responseMutableLiveData;}

    public MutableLiveData<Boolean> getLoggedOutMutableLiveData() {return loggedOutMutableLiveData;}

    public AuthViewModel() {
        // Define the URL endpoint for the HTTP request.
        retrofit = NetworkClient.getRetrofitClient();
        authAPI = retrofit.create(AuthenticationAPI.class);
    }

    public void registerWithoutPicture(User user){
        /**
         * Request to the API to register
         */
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
        
    public void logOut(){
        /**
         * Request to the API to logout
         */
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
                        LoginResponse.UserResponse user = loginResponse.getUserResponse();
                        LoginResponse response = new LoginResponse(token,"success",user);
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
        /**
         * Request to the API to create an account with a profile picture
         * @param User
         */

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
    }
}