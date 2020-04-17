package com.example.cshare.Views.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.cshare.Models.User;
import com.example.cshare.R;
import com.example.cshare.Utils.Camera;
import com.example.cshare.ViewModels.AuthViewModel;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, Validator.ValidationListener {

    // Form validation
    protected Validator validator;
    private boolean validated;

    @NotEmpty
    private EditText editTextLastName;
    @NotEmpty
    private EditText editTextFirstName;
    @Email
    private EditText editTextEmailSignUp;
    @Password
    private EditText editTextPasswordSignUp;
    @ConfirmPassword
    private EditText editTextPasswordConfirm;
    private Spinner spinnerCampus;
    @NotEmpty
    private EditText editTextRoomNumber;
    private ImageView imageViewGallery;

    private Button buttonSignUp;
    private Button buttonAlreadyHaveAnAccount;
    private Button buttonGallery;

    private String[] campusArray;
    private String email;
    private String lastName;
    private String firstName;
    private String password1;
    private String password2;
    private String campus;
    private String roomNumber;

    // Check whether a picture has been selected
    private boolean pictureSelected = false;

    // Path to the location of the picture taken by the phone
    private Uri pictureFileUri;
    private File fileToUpload;
    private Uri fileToUploadUri;

    // ViewModel
    AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Bind views
        editTextPasswordConfirm = findViewById(R.id.editTextPasswordConfirm);
        editTextEmailSignUp = findViewById(R.id.editTextEmailSignUp);
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextPasswordSignUp = findViewById(R.id.editTextPasswordSignUp);
        editTextRoomNumber = findViewById(R.id.editTextRoomNumber);
        imageViewGallery = findViewById(R.id.imageViewGallery);
        Picasso.get().load(R.drawable.test).into(imageViewGallery);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        buttonAlreadyHaveAnAccount = findViewById(R.id.buttonAlreadyHaveAnAccount);
        buttonGallery = findViewById(R.id.buttonGallery);
        spinnerCampus = findViewById(R.id.spinnerCampus);

        // Activate buttons
        buttonSignUp.setOnClickListener(this);
        buttonAlreadyHaveAnAccount.setOnClickListener(this);
        buttonGallery.setOnClickListener(this);
        imageViewGallery.setOnClickListener(this);

        // Campus spinner
        configureCampusSpinner();

        // Validator
        configureValidator();

        //ViewModel
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }

    private void configureCampusSpinner() {
        campusArray = getResources().getStringArray(R.array.campus_array);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, campusArray);
        // Apply the adapter to the spinner
        spinnerCampus.setAdapter(adapterSpinner);
        spinnerCampus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // An item was selected. You can retrieve the selected item using
                campus = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void configureValidator() {
        // Instantiate a new Validator
        validator = new Validator((this));
        validator.setValidationListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v == buttonAlreadyHaveAnAccount) {

            Intent toLoginActivityIntent = new Intent();
            toLoginActivityIntent.setClass(getApplicationContext(), LoginActivity.class);
            startActivity(toLoginActivityIntent);

        }

        if (v == buttonSignUp) {

            // Validate the field
            validator.validate();

            if (validated) {

                // Retrieve user details from the edit text
                email = editTextEmailSignUp.getText().toString().trim();
                lastName = editTextLastName.getText().toString().trim();
                firstName = editTextFirstName.getText().toString().trim();
                password1 = editTextPasswordSignUp.getText().toString().trim();
                password2 = editTextPasswordConfirm.getText().toString().trim();
                roomNumber = editTextRoomNumber.getText().toString().trim();

                if (pictureSelected) {

                    try {
                        fileToUploadUri = Camera.getOutputMediaFileUri(this, fileToUpload);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // Create RequestBody instance from file
                    RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(fileToUploadUri)), fileToUpload);
                    // MultipartBody.Part is used to send also the actual file name
                    MultipartBody.Part profilePictureBody = MultipartBody.Part.createFormData("profile_picture", fileToUpload.getAbsolutePath(), requestFile);

                    User user = new User(profilePictureBody, firstName, lastName, roomNumber, campus, email, password1, password2);
                    //authViewModel.registerWithPicture(user);

                } else {
                    User user = new User(email, lastName, firstName, password1, password2, campus, roomNumber);
                    //authViewModel.registerWithoutPicture(user);
                }

                Toast.makeText(this, "Account created successfully, please sign in", Toast.LENGTH_SHORT).show();

                Intent toLoginActivityIntent = new Intent();
                toLoginActivityIntent.setClass(getApplicationContext(), LoginActivity.class);
                startActivity(toLoginActivityIntent);
            }
        }

        if (v == buttonGallery || v == imageViewGallery) {

            Camera.choosePictureFromGallery(getParent());

        }
    }

    /**
     * Receiving activity result method will be called after closing the gallery
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result code is RESULT_OK only if the user selects an Image
        if (requestCode == Camera.CAMERA_CHOOSE_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {

            // data.getData returns the content URI for the selected Image
            pictureFileUri = data.getData();

            try {
                fileToUpload = Camera.processPicture(this, pictureFileUri, imageViewGallery); // modify the raw picture taken
                pictureSelected = true;
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (resultCode == RESULT_CANCELED) {
            // user cancelled Image capture
            Toast.makeText(this,
                    "User cancelled image capture", Toast.LENGTH_SHORT)
                    .show();
        } else {
            // failed to capture image
            Toast.makeText(this,
                    "Sorry! Failed to choose any image", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void onValidationSucceeded() {
        //Called when all your views pass all validations.
        validated = true;
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        //Called when there are validation error(s).
        validated = false;
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    /*
      Here we store the file uri as it will be null after returning from camera app
    */

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", pictureFileUri);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("tag", "onRestoreInstanceState");
        if (savedInstanceState != null) {
            // get the file url
            pictureFileUri = savedInstanceState.getParcelable("file_uri");
        }
    }

}
