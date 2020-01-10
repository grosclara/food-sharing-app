package com.example.frontend.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.frontend.R;
import com.example.frontend.api.DjangoRestApi;
import com.example.frontend.api.NetworkClient;
import com.example.frontend.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.frontend.activity.MainActivity.editor;

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

        User user = new User(email,password);

        Retrofit retrofit = NetworkClient.getRetrofitClient(this);
        DjangoRestApi djangoRestApi = retrofit.create(DjangoRestApi.class);

        Call<User> call = djangoRestApi.authUser(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.d("serverRequest", response.message());
                if (response.isSuccessful()){
                    String token = response.body().getToken();
                    int id = response.body().getId();

                    MainActivity.pref = getApplicationContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                    editor = MainActivity.pref.edit();
                    editor.putBoolean("signed in",true);
                    editor.putInt("id",id);
                    editor.putString("token",token);
                    editor.apply();

                    Intent toMainActivityIntent = new Intent();
                    toMainActivityIntent.setClass(getApplicationContext(), MainActivity.class);
                    startActivity(toMainActivityIntent);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("serverRequest", t.getLocalizedMessage());

            }
        });


    }
}
