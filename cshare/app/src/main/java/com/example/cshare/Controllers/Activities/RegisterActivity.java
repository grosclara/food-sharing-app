package com.example.cshare.Controllers.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.example.cshare.Models.UserWithPicture;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, Validator.ValidationListener{

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
    private String room_number;

    // Check whether a picture has been selected
    private boolean pictureSelected = false;

    // Path to the location of the picture taken by the phone
    private boolean withPicture = false;
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

    private void configureCampusSpinner(){
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

    private void configureValidator(){
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

        if(v == buttonSignUp) {

            // Validate the field
            validator.validate();

            if(validated) {

                email = editTextEmailSignUp.getText().toString().trim();
                lastName = editTextLastName.getText().toString().trim();
                firstName = editTextFirstName.getText().toString().trim();
                password1 = editTextPasswordSignUp.getText().toString().trim();
                password2 = editTextPasswordConfirm.getText().toString().trim();
                room_number = editTextRoomNumber.getText().toString().trim();

                if (withPicture) {
                    // Create a file object using file path
                    File file = new File(imageFilePath);

                    // Create RequestBody instance from file
                    RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(uriImage)), file);
                    // MultipartBody.Part is used to send also the actual file name
                    MultipartBody.Part body = MultipartBody.Part.createFormData("profile_picture", file.getName(), requestFile);

                    UserWithPicture newUser = new UserWithPicture(body, firstName, lastName, room_number, campus, email, password1, password2);
                    authViewModel.registerWithPicture(newUser);
                } else {
                    User user = new User(email, lastName, firstName, password1, password2, campus,room_number);
                    authViewModel.registerWithoutPicture(user);

                }
                Intent toLoginActivityIntent = new Intent();
                toLoginActivityIntent.setClass(getApplicationContext(), LoginActivity.class);
                startActivity(toLoginActivityIntent);
            }
        }

        if (v == buttonGallery || v == imageViewGallery) {
            choosePictureFromGallery();
        }
    }


    private void choosePictureFromGallery() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        // Create an Intent with action as ACTION_PICK
        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Sets the type as image/*. This ensures only components of type image are selected
        pickIntent.setType("image/*");

        // We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        pickIntent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);

        // Create a chooser in case there are third parties app and launch the Intent
        startActivityForResult(Intent.createChooser(pickIntent, "Select Picture"), Camera.CAMERA_CHOOSE_IMAGE_REQUEST_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            // Result code is RESULT_OK only if the user selects an Image
            if (resultCode == Activity.RESULT_OK)
                switch (requestCode) {
                    case PICK_IMAGE:
                        withPicture = true;
                        // data.getData returns the content URI for the selected Image
                        uriImage = data.getData();

                        // Content URI is not same as absolute file path. Need to retrieve the absolute file path from the content URI
                        // Get the path from the Uri
                        imageFilePath = getPathFromURI(uriImage);
                        Log.d("TAG",imageFilePath);


                        imageViewGallery.setImageBitmap(BitmapFactory.decodeFile(imageFilePath));
                        break;
                }
        }
    }

    public String getPathFromURI(Uri contentUri) {
        String path = null;
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        // Get the cursor
        Cursor cursor = getContentResolver().query(contentUri, filePathColumn, null, null, null);
        // Move to first row

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            path = cursor.getString(columnIndex);
        }
        cursor.close();
        return path;
    }


    protected boolean validate() {
        if (validator != null)
            validator.validate();
        return validated;           // would be set in one of the callbacks below
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
}
