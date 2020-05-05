package com.example.cshare.Views.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.example.cshare.Models.Forms.RegisterForm;
import com.example.cshare.Models.ApiResponses.RegistrationResponse;
import com.example.cshare.R;
import com.example.cshare.RequestManager.Status;
import com.example.cshare.Utils.Camera;
import com.example.cshare.Utils.Constants;
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

    private RegisterForm registerForm;

    private String email;
    private String lastName;
    private String firstName;
    private String password1;
    private String password2;
    private String campus;
    private String roomNumber;

    // Path to the location of the picture taken by the phone
    private Uri pictureFileUri;
    private File fileToUpload;
    private Uri fileToUploadUri;
    private String fileToUploadPath;

    // ViewModel
    AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        getSupportActionBar().setTitle("Sign up");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Bind views
        editTextPasswordConfirm = findViewById(R.id.editTextPasswordConfirm);
        editTextEmailSignUp = findViewById(R.id.editTextEmailSignUp);
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextPasswordSignUp = findViewById(R.id.editTextPasswordSignUp);
        editTextRoomNumber = findViewById(R.id.editTextRoomNumber);
        imageViewGallery = findViewById(R.id.imageViewGallery);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        buttonAlreadyHaveAnAccount = findViewById(R.id.buttonAlreadyHaveAnAccount);
        buttonGallery = findViewById(R.id.buttonGallery);
        spinnerCampus = findViewById(R.id.spinnerCampus);

        Picasso.get().load(R.drawable.test).into(imageViewGallery);

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
        authViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(AuthViewModel.class);
        authViewModel.getRegistrationResponseMutableLiveData().observe(this, new Observer<RegistrationResponse>() {
            @Override
            public void onChanged(RegistrationResponse registrationResponse) {

                if (registrationResponse.getStatus().equals(Status.SUCCESS)) {

                    authViewModel.getRegistrationResponseMutableLiveData().setValue(RegistrationResponse.complete());

                    Toast.makeText(getApplicationContext(), "Account successfully created !", Toast.LENGTH_SHORT).show();

                    // Redirect to the LoginActivity
                    Intent toLoginActivityIntent = new Intent();
                    toLoginActivityIntent.setClass(getApplicationContext(), LoginActivity.class);
                    startActivity(toLoginActivityIntent);

                } else if (registrationResponse.getStatus().equals(Status.ERROR)) {

                    if (registrationResponse.getError().getEmail() != null) {

                        Toast.makeText(getApplicationContext(), registrationResponse.getError().getEmail(), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Unexpected error", Toast.LENGTH_SHORT).show();
                    }

                    authViewModel.getRegistrationResponseMutableLiveData().setValue(RegistrationResponse.complete());
                }
            }
        });
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

                registerForm = new RegisterForm(email, password1, password2, lastName, firstName, roomNumber, campus);

                if (fileToUploadUri != null) {

                    fileToUpload = new File(fileToUploadPath);
                    // Create RequestBody instance from file
                    RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(fileToUploadUri)), fileToUpload);
                    // MultipartBody.Part is used to send also the actual file name
                    MultipartBody.Part profilePictureBody = MultipartBody.Part.createFormData("profile_picture", fileToUploadPath, requestFile);

                    registerForm.setProfile_picture(profilePictureBody);
                }

                authViewModel.register(registerForm);

            }
        }

        if (v == buttonGallery || v == imageViewGallery) {
            showPictureDialog(this);
        }
    }

    private void showPictureDialog(Activity activity) {

        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Camera.choosePictureFromGallery(activity, null);
                                break;
                            case 1:
                                try {
                                    pictureFileUri = Camera.captureImage(activity, null);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                        }
                    }
                });
        pictureDialog.show();
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

            // modify the raw picture taken in a new file and retrieve its Uri
            try {
                fileToUpload = Camera.processPicture(this, pictureFileUri);

                fileToUploadPath = fileToUpload.getAbsolutePath();
                fileToUploadUri = Camera.getOutputMediaFileUri(this, fileToUpload);

                Picasso.get().load(fileToUploadUri).into(imageViewGallery);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == Camera.CAMERA_CAPTURE_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            // successfully captured the image

            try {
                Log.d(Constants.TAG, pictureFileUri.toString());
                fileToUpload = Camera.processPicture(this, pictureFileUri); // modify the raw picture taken
                fileToUploadPath = fileToUpload.getAbsolutePath();
                fileToUploadUri = Camera.getOutputMediaFileUri(this, fileToUpload);

                Picasso.get().load(fileToUploadUri).into(imageViewGallery);

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
        // Save file uri in bundle as it will be null on screen orientation changes
        outState.putParcelable("file_uri", fileToUploadUri);
        outState.putString("file_path", fileToUploadPath);

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            // get the file uri
            fileToUploadUri = savedInstanceState.getParcelable("file_uri");
            fileToUploadPath = savedInstanceState.getString("file_path");
            // Reload the image view picture
            if (fileToUploadUri != null) {
                Picasso.get().load(fileToUploadUri).into(imageViewGallery);
            } else {
                Picasso.get().load(R.drawable.test).into(imageViewGallery);
            }
        }
    }

}
