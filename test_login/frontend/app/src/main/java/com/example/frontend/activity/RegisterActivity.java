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

/**
 * RegisterActivity Class.
 * Give the possibility to the user to create an account in the database.
 * The form to fill the user information includes EditTexts.
 * After having created the account, the user is redirected to the Login.
 * @author Clara Gros, Babacar Toure
 * @version 1.0
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    EditText username_edt;
    EditText password_edt;
    EditText email_edt;
    EditText name_edt;
    EditText first_name_edt;
    Button register;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register = findViewById(R.id.buttonRegister);
        login = findViewById(R.id.buttonToLogin);

        register.setOnClickListener(this);

    }

    @Override
    /**
     * Listener method that waits for a a click on any button
     * It will call a registerclick method if the button register is clicked
     * and else it will redirect to the LoginActivity
     * @param view
     */
    public void onClick(View v) {
        if(v==register){
            registerClick();
        }
        else if (v==login){
            Intent toLoginActivityIntent = new Intent();
            toLoginActivityIntent.setClass(getApplicationContext(), LoginActivity.class);
            startActivity(toLoginActivityIntent);
        }
    }

    /**
     * Method that will create a User object and put it in the register
     * in server method
     * It use edittexts and the constructor of User
     */
    public void registerClick() {
        //identify the edittexts
        username_edt = findViewById(R.id.editTextUsername);
        password_edt = findViewById(R.id.editTextPassword);
        email_edt = findViewById(R.id.editTextEmail);
        name_edt = findViewById(R.id.editTextName);
        first_name_edt = findViewById(R.id.editTextFirstName);
        //set values from the forms in variables
        String username = username_edt.getText().toString();
        String password = password_edt.getText().toString();
        String email = email_edt.getText().toString();
        String name = name_edt.getText().toString();
        String first_name = first_name_edt.getText().toString();
        //construct the user object
        User user = new User(username,name, first_name,email,password);
        // call method that will send the request
        registerInServer(user);

    }

    /**
     * Send a HTTP request to post the User user taken in param
     * @param user
     */
    private void registerInServer(User user) {

        Retrofit retrofit = NetworkClient.getRetrofitClient(this);
        DjangoRestApi djangoRestApi = retrofit.create(DjangoRestApi.class);

        // Creation of a call object that will contain the response
        Call<User> call = djangoRestApi.registerInServer(user);
        // Asynchronous request
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.i("serverRequest", response.message());
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "You registered well!", Toast.LENGTH_SHORT).show();
                    Intent toLoginActivityIntent = new Intent();
                    toLoginActivityIntent.setClass(getApplicationContext(), LoginActivity.class);
                    startActivity(toLoginActivityIntent);

                } else {
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("serverRequest", t.getMessage());
            }
        });
    }
}
