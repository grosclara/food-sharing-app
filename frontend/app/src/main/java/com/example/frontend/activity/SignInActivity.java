package com.example.frontend.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frontend.R;
import com.example.frontend.activity.ui.main.ChangePasswordFragment;
import com.example.frontend.activity.ui.main.ResetPasswordFragment;
import com.example.frontend.api.DjangoRestApi;
import com.example.frontend.api.NetworkClient;
import com.example.frontend.model.User;
import com.google.gson.Gson;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonCreateAccount;
    private Button buttonSignIn;
    private Button buttonResetPassword;

    private LinearLayout linearLayoutCredentials;
    private EditText editTextEmailSignIn;
    private EditText editTextPasswordSignIn;
    private TextView textViewWrongCredentials;

    private static final String state = "resetPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Buttons
        buttonCreateAccount = findViewById(R.id.buttonCreateAccount);
        buttonSignIn = findViewById(R.id.buttonSignIn);
        buttonResetPassword = findViewById(R.id.buttonResetPassword);

        //Views
        editTextEmailSignIn = findViewById(R.id.editTextEmailSignIn);
        editTextPasswordSignIn = findViewById(R.id.editTextPasswordSignIn);
        linearLayoutCredentials = findViewById(R.id.linearLayoutCredentials);
        textViewWrongCredentials = findViewById(R.id.textViewWrongCredentials);

        buttonSignIn.setOnClickListener(this);
        buttonCreateAccount.setOnClickListener(this);
        buttonResetPassword.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == buttonCreateAccount) {

            Intent toSignUpActivityIntent = new Intent();
            toSignUpActivityIntent.setClass(getApplicationContext(), SignUpActivity.class);
            startActivity(toSignUpActivityIntent);

        }
        if (v == buttonSignIn) {
            // Sign in
            signIn();
        }
        if (v == buttonResetPassword) {
            // Reset password
            DialogFragment newFragment = new ResetPasswordFragment(getApplicationContext());
            newFragment.show(getSupportFragmentManager(), state);
        }
    }

    private void signIn() {
        String email = editTextEmailSignIn.getText().toString().trim();
        String password = editTextPasswordSignIn.getText().toString().trim();

        final User user = new User(email, password);

        Retrofit retrofit = NetworkClient.getRetrofitClient(this);
        DjangoRestApi djangoRestApi = retrofit.create(DjangoRestApi.class);

        Call<Object> call = djangoRestApi.login(user);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.d("serverRequest", response.message());

                if (response.isSuccessful()) {

                    try {
                        JSONObject JsonResponse = new JSONObject(new Gson().toJson(response.body()));
                        JSONObject userCredits = JsonResponse.getJSONObject("user");

                        String token = JsonResponse.getString("key");
                        int userId = userCredits.getInt("id");

                        LauncherActivity.userCreditsEditor.putBoolean("logStatus", true);
                        LauncherActivity.userCreditsEditor.putString("token", token);
                        LauncherActivity.userCreditsEditor.putInt("id", userId);

                        LauncherActivity.userCreditsEditor.apply();

                        Intent toCollectActivityIntent = new Intent();
                        toCollectActivityIntent.setClass(getApplicationContext(), CollectActivity.class);
                        startActivity(toCollectActivityIntent);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Unable to log in with provided credentials",Toast.LENGTH_SHORT).show();
                    textViewWrongCredentials.setVisibility(View.VISIBLE);
                    linearLayoutCredentials.setBackgroundColor(Color.parseColor("#33FF0000"));

                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("serverRequest", t.getLocalizedMessage());

            }
        });
    }
}
