package com.example.frontend.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frontend.R;
import com.example.frontend.api.DjangoRestApi;
import com.example.frontend.api.NetworkClient;
import com.example.frontend.model.User;
import com.squareup.picasso.Picasso;

import java.io.Serializable;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Display the information of the current User
 * <p>
 * Possibility to edit each field and change profile picture
 * Button Edit and Button Delete profile
 * Edit password / Change email
 *
 * @author Clara Gros, Babacar Toure
 * @version 1.0
 */

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textViewFirstName;
    private TextView textViewLastName;
    private TextView textViewEmail;
    private TextView textViewCampus;
    private TextView textViewRoomNumber;
    private ImageView imageViewProfilePicture;

    private Button buttonDeleteAccount;
    private Button buttonEditProfile;

    private User profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Views
        textViewFirstName = findViewById(R.id.textViewFirstName);
        textViewLastName = findViewById(R.id.textViewLastName);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewCampus = findViewById(R.id.textViewCampus);
        textViewRoomNumber = findViewById(R.id.textViewRoomNumber);
        imageViewProfilePicture = findViewById(R.id.imageViewProfilePicture);

        //Buttons
        buttonDeleteAccount = findViewById(R.id.buttonDeleteAccount);
        buttonEditProfile = findViewById(R.id.buttonEditProfile);

        buttonEditProfile.setOnClickListener(this);
        buttonDeleteAccount.setOnClickListener(this);

        displayProfileInfo();
    }

    public void displayProfileInfo() {
        /**
         * Send a HTTP request to retrieve all information from the user
         */

        // Define the URL endpoint for the HTTP operation.
        Retrofit retrofit = NetworkClient.getRetrofitClient(this);
        DjangoRestApi djangoRestApi = retrofit.create(DjangoRestApi.class);

        // Creation of a call object that will contain the response
        Call<User> call = djangoRestApi.getProfileInfo(CollectActivity.token);
        // Asynchronous request
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.i("serverRequest", response.message());
                if (response.isSuccessful()) {

                    profile = response.body();

                    textViewFirstName.setText(profile.getFirst_name());
                    textViewLastName.setText(profile.getLast_name());
                    textViewCampus.setText(profile.getCampus());
                    textViewEmail.setText(profile.getEmail());
                    textViewRoomNumber.setText(profile.getRoom_number());
                    Picasso.get().load(profile.getProfile_picture()).into(imageViewProfilePicture);

                } else {
                    Toast.makeText(getApplicationContext(), "An error occurred to retrieve the supplier info!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("serverRequest", t.getMessage());
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == buttonEditProfile) {

            Intent toEditProfileActivityIntent = new Intent();
            toEditProfileActivityIntent.setClass(getApplicationContext(), EditProfileActivity.class);
            toEditProfileActivityIntent.putExtra("profile", (Serializable) profile);
            startActivity(toEditProfileActivityIntent);

        } else if (v == buttonDeleteAccount) {
            deleteAccount();
        }
    }

    private void deleteAccount() {

        // Alert Dialog to confirm the will to sign out

        // Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Chain together various setter methods to set the dialog characteristics
        builder.setMessage("Are you sure you want to delete your account?")
                .setTitle("Delete")
                // Add the buttons
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button -> delete the account the user

                        Retrofit retrofit = NetworkClient.getRetrofitClient(getApplicationContext());
                        DjangoRestApi djangoRestApi = retrofit.create(DjangoRestApi.class);

                        Call<ResponseBody> call = djangoRestApi.deleteUserById(CollectActivity.token, CollectActivity.userId);
                        call.enqueue(new Callback<ResponseBody>() {


                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if(response.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Account successfully deleted", Toast.LENGTH_SHORT).show();

                                    // Redirect to the Sign up activity with no possibility to go back
                                    Intent toSignUpActivityIntent = new Intent();
                                    toSignUpActivityIntent.setClass(getApplicationContext(), SignUpActivity.class);
                                    startActivity(toSignUpActivityIntent);
                                    finish();
                                }else{};
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(getApplicationContext(), "Fail to delete the account", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
