package com.example.frontend.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.frontend.R;
import com.example.frontend.api.DjangoRestApi;
import com.example.frontend.api.NetworkClient;
import com.example.frontend.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextRoomNumber;
    private EditText editTextCampus;
    private ImageView imageViewProfilePicture;

    private String firstName;
    private String lastName;
    private String roomNumber;
    private String campus;
    private String profilePicture;

    private Button buttonSubmit;
    private Button buttonGallery;

    private User profile;

    public static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Views
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextRoomNumber = findViewById(R.id.editTextRoomNumber);
        editTextCampus = findViewById(R.id.editTextCampus);
        imageViewProfilePicture = findViewById(R.id.imageViewProfilePicture);

        // Buttons
        buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(this);
        buttonGallery = findViewById(R.id.buttonGallery);
        buttonGallery.setOnClickListener(this);

        // Get the user info from the ProfileActivity intent
        Intent fromProfileActivityIntent = getIntent();
        profile = (User) fromProfileActivityIntent.getSerializableExtra("profile");

        firstName = profile.getFirst_name();
        lastName = profile.getLast_name();
        roomNumber = profile.getRoom_number();
        campus = profile.getCampus();

        // Set the previous values as default
        editTextFirstName.setHint(firstName);
        editTextLastName.setHint(lastName);
        editTextRoomNumber.setHint(roomNumber);
        editTextCampus.setHint(campus);

    }

    @Override
    public void onClick(View v) {
        if (buttonSubmit.equals(v)) {
            editProfile();
        }
        if (buttonGallery.equals(v)) {
            choosePictureFromGallery();
        }
    }

    public void editProfile() {

        if (!editTextFirstName.getText().toString().isEmpty()) {
            profile.setFirst_name(editTextFirstName.getText().toString().trim());
        }
        if (!editTextLastName.getText().toString().isEmpty()) {
            profile.setLast_name(editTextLastName.getText().toString().trim());
        }
        if (!editTextRoomNumber.getText().toString().isEmpty()) {
            profile.setRoom_number(editTextRoomNumber.getText().toString().trim());
        }
        if (!editTextCampus.getText().toString().isEmpty()) {
            profile.setCampus(editTextCampus.getText().toString().trim());
        }

        // Define the URL endpoint for the HTTP operation.
        Retrofit retrofit = NetworkClient.getRetrofitClient(this);
        DjangoRestApi djangoRestApi = retrofit.create(DjangoRestApi.class);

        // Creation of a call object that will contain the response
        Call<User> call = djangoRestApi.updateProfile(CollectActivity.token, profile.getId(), profile);
        // Asynchronous request
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.i("serverRequest", response.message());
                Toast.makeText(getApplicationContext(), "profile editted", Toast.LENGTH_SHORT).show();

                Intent toProfileActivityIntent = new Intent();
                toProfileActivityIntent.setClass(getApplicationContext(), ProfileActivity.class);
                startActivity(toProfileActivityIntent);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "profile error", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void choosePictureFromGallery() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            // Result code is RESULT_OK only if the user selects an Image
            if (resultCode == Activity.RESULT_OK)
                switch (requestCode){
                    case PICK_IMAGE:
                        //data.getData returns the content URI for the selected Image
                        Uri selectedImage = data.getData();
                        imageViewProfilePicture.setImageURI(selectedImage);
                        break;
                }
        }
    }
}
