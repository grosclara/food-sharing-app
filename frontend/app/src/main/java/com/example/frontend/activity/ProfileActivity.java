package com.example.frontend.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.frontend.R;

/**
 * Display the information of the current User
 *
 * Possibility to edit each field and change profile picture
 * Button Edit and Button Delete profile
 * Edit password / Change email
 * @author Clara Gros, Babacar Toure
 * @version 1.0
 */

public class ProfileActivity extends AppCompatActivity {

    private TextView textViewFirstName;
    private TextView textViewLastName;
    private TextView textViewEmail;
    private TextView textViewCampus;
    private TextView textViewRoomNumber;
    private ImageView imageViewProfilePicture;

    private String firstName;
    private String lastName;
    private String email;
    private String campus;
    private String roomNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        textViewFirstName = findViewById(R.id.textViewFirstName);
        textViewLastName = findViewById(R.id.textViewLastName);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewCampus = findViewById(R.id.textViewCampus);
        textViewRoomNumber = findViewById(R.id.textViewRoomNumber);
        imageViewProfilePicture = findViewById(R.id.imageViewProfilePicture);

        firstName = LauncherActivity.userCredits.getString("first_name", null);
        lastName = LauncherActivity.userCredits.getString("last_name", null);
        campus = LauncherActivity.userCredits.getString("campus", null);
        email = LauncherActivity.userCredits.getString("email", null);
        roomNumber = LauncherActivity.userCredits.getString("room_number", null);


        textViewFirstName.setText(firstName);
        textViewLastName.setText(lastName);
        textViewCampus.setText(campus);
        textViewEmail.setText(email);
        textViewRoomNumber.setText(roomNumber);
    }
}
