package com.example.frontend.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextLastName;
    private EditText editTextFirstName;
    private EditText editTextEmailSignUp;
    private EditText editTextPasswordSignUp;
    private EditText editTextPasswordConfirm;
    private EditText editTextCampus;
    private EditText editTextRoomNumber;

    private Button buttonSignUp;
    private Button buttonAlreadyHaveAnAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Views
        editTextPasswordConfirm = findViewById(R.id.editTextPasswordConfirm);
        editTextEmailSignUp = findViewById(R.id.editTextEmailSignUp);
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextPasswordSignUp = findViewById(R.id.editTextPasswordSignUp);
        editTextCampus = findViewById(R.id.editTextCampus);
        editTextRoomNumber = findViewById(R.id.editTextRoomNumber);

        // Buttons
        buttonSignUp = findViewById(R.id.buttonSignUp);
        buttonAlreadyHaveAnAccount = findViewById(R.id.buttonAlreadyHaveAnAccount);
        buttonSignUp.setOnClickListener(this);
        buttonAlreadyHaveAnAccount.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == buttonAlreadyHaveAnAccount){

            Intent toSignInActivityIntent = new Intent();
            toSignInActivityIntent.setClass(getApplicationContext(), SignInActivity.class);
            startActivity(toSignInActivityIntent);

        }
        else if (v == buttonSignUp){
            createAccount();
        }
    }

    private void createAccount() {

        String email = editTextEmailSignUp.getText().toString().trim();
        String lastName = editTextLastName.getText().toString().trim();
        String firstName = editTextFirstName.getText().toString().trim();
        String password1 = editTextPasswordSignUp.getText().toString().trim();
        String password2 = editTextPasswordConfirm.getText().toString().trim();
        String campus = editTextCampus.getText().toString().trim();
        String room_number = editTextRoomNumber.getText().toString().trim();

        // Call to a field validation method before registering the user

        User user = new User(email, lastName, firstName, password1, password2, campus, room_number);

        // Define the URL endpoint for the HTTP operation.
        Retrofit retrofit = NetworkClient.getRetrofitClient(this);
        DjangoRestApi djangoRestApi = retrofit.create(DjangoRestApi.class);

        // Creation of a call object that will contain the response
        Call<User> callNewUser = djangoRestApi.createUser(user);

        // Asynchronous request
        callNewUser.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.d("serverRequest", response.message());
                if (response.isSuccessful()){

                    Intent toSignInActivityIntent = new Intent();
                    toSignInActivityIntent.setClass(getApplicationContext(), SignInActivity.class);
                    startActivity(toSignInActivityIntent);

                }
                else{}
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("serverRequest", t.getLocalizedMessage());
            }
        });

    }
}
