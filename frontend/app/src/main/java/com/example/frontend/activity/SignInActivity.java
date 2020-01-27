package com.example.frontend.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.textclassifier.TextClassifierEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.frontend.R;
import com.example.frontend.api.DjangoRestApi;
import com.example.frontend.api.NetworkClient;
import com.example.frontend.model.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.GeneralSecurityException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonCreateAccount;
    private Button buttonSignIn;

    private EditText editTextEmailSignIn;
    private EditText editTextPasswordSignIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Buttons
        buttonCreateAccount = findViewById(R.id.buttonCreateAccount);
        buttonSignIn = findViewById(R.id.buttonSignIn);

        //Views
        editTextEmailSignIn = findViewById(R.id.editTextEmailSignIn);
        editTextPasswordSignIn = findViewById(R.id.editTextPasswordSignIn);

        buttonSignIn.setOnClickListener(this);
        buttonCreateAccount.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == buttonCreateAccount) {

            Intent toSignUpActivityIntent = new Intent();
            toSignUpActivityIntent.setClass(getApplicationContext(), SignUpActivity.class);
            startActivity(toSignUpActivityIntent);

        } else if (v == buttonSignIn) {
            //Validate the signing up
            signIn();
        }
    }

    private void signIn() {
        String email = editTextEmailSignIn.getText().toString().trim();
        String password = editTextPasswordSignIn.getText().toString().trim();

        final User user = new User(email,password);

        Retrofit retrofit = NetworkClient.getRetrofitClient(this);
        DjangoRestApi djangoRestApi = retrofit.create(DjangoRestApi.class);

        Call<Object> call = djangoRestApi.login(user);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.d("serverRequest", response.message());
                if (response.isSuccessful()){

                    try {
                        JSONObject JsonResponse = new JSONObject(new Gson().toJson(response.body()));
                        JSONObject userCredits = JsonResponse.getJSONObject("user");

                        String token = JsonResponse.getString("key");
                        int userId = userCredits.getInt("id");
                        String email = userCredits.getString("email");
                        String first_name = userCredits.getString("first_name");
                        String last_name = userCredits.getString("last_name");
                        String room_number = userCredits.getString("room_number");
                        String campus = userCredits.getString("campus");

                        LauncherActivity.userCreditsEditor.putBoolean("logStatus",true);
                        LauncherActivity.userCreditsEditor.putString("token", token);
                        LauncherActivity.userCreditsEditor.putInt("id",userId);
                        LauncherActivity.userCreditsEditor.putString("email",userCredits.getString("email"));
                        LauncherActivity.userCreditsEditor.putString("first_name",userCredits.getString("first_name"));
                        LauncherActivity.userCreditsEditor.putString("last_name",userCredits.getString("last_name"));
                        LauncherActivity.userCreditsEditor.putString("room_number",userCredits.getString("room_number"));
                        LauncherActivity.userCreditsEditor.putString("campus",userCredits.getString("campus"));
                        LauncherActivity.userCreditsEditor.apply();



                        Intent toCollectActivityIntent = new Intent();
                        toCollectActivityIntent.setClass(getApplicationContext(), CollectActivity.class);
                        startActivity(toCollectActivityIntent);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("serverRequest", t.getLocalizedMessage());

            }
        });
    }
}
