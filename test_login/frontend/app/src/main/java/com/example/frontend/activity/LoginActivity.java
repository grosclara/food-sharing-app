package com.example.frontend.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.frontend.R;
import com.example.frontend.api.DjangoRestApi;
import com.example.frontend.api.NetworkClient;
import com.example.frontend.model.Login;
import com.example.frontend.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText username_edt;
    EditText password_edt;
    Button login;
    Button to_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        login = findViewById(R.id.buttonLogin);
        to_register = findViewById(R.id.buttonToRegister);

        login.setOnClickListener(this);
        to_register.setOnClickListener(this);

    }

    @Override
    /**
     * Listener method that waits for a a click on any button
     * It will call a logButtonClick method if the button login is clicked
     * and else it will redirect to the RegisterActivity
     * @param view
     */
    public void onClick(View v) {
        if (v == login){
            logButtonClick();
        }
        else{
            Intent toRegisterActivityIntent = new Intent();
            toRegisterActivityIntent.setClass(getApplicationContext(), RegisterActivity.class);
            startActivity(toRegisterActivityIntent);
        }
    }
    /**
     * Method that will create a Login object and put it in the login
     * in server method
     * It use edittexts and the constructor of Login
     */
    private void logButtonClick() {
        //identify edittexts
        username_edt = findViewById(R.id.edtUsername);
        password_edt = findViewById(R.id.edtPassword);
        //create variables from values retrieved from the edittext
        String username = username_edt.getText().toString();
        String password = password_edt.getText().toString();
        //create a login object and put it through the login function
        Login login = new Login(username, password);
        login(login);
    }

    /**
     * Send a HTTP request to post the Login login taken in param and get the token
     * @param login
     */
    private void login(Login login) {
        Retrofit retrofit = NetworkClient.getRetrofitClient(this);
        DjangoRestApi djangoRestApi = retrofit.create(DjangoRestApi.class);

        // Creation of a call object that will contain the response
        Call<User> call = djangoRestApi.login(login);
        // Asynchronous request
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.i("serverRequest", response.message());
                if (response.isSuccessful()) {
                    String token = response.body().getToken();
                    Toast.makeText(getApplicationContext(), token , Toast.LENGTH_SHORT).show();
                    //save the token of the user in shared preferencies
                    SharedPreferences preferences = getApplicationContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor prefLoginEdit = preferences.edit();
                    prefLoginEdit.putBoolean("loggedin", true);
                    prefLoginEdit.putString("token", token);
                    prefLoginEdit.commit();

                    // redicrect to the HomeScreenActivity
                    Intent toHomeActivityIntent = new Intent();
                    toHomeActivityIntent.setClass(getApplicationContext(), HomeScreenActivity.class);
                    startActivity(toHomeActivityIntent);
                } else {
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("serverRequest", t.getLocalizedMessage());
            }
        });
    }
}

