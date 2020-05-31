package com.example.cshare.ui.views.auth;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.cshare.R;
import com.example.cshare.data.apiresponses.RegistrationResponse;
import com.example.cshare.data.apiresponses.Status;
import com.example.cshare.data.models.User;
import com.example.cshare.ui.viewmodels.AuthViewModel;
import com.example.cshare.utils.MediaFiles;
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

import fr.ganfra.materialspinner.MaterialSpinner;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Activity responsible for the registration of a new user.
 * <p>
 * Once the user fills out the form, a validator checks the validity of the fields.
 * If the form is valid, the account is created using the AuthViewModel's register method and
 * then an Intent is used to access the LoginActivity.
 *
 * @author Clara Gros
 * @author Babacar Toure
 * @see AuthViewModel
 * @see LoginActivity
 * @see User
 * @see AppCompatActivity
 * @since 1.0
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener,
        Validator.ValidationListener {

    // Form validation
    /**
     * Asynchronous validations, provided by the Saripaar library.
     */
    protected Validator validator;
    private boolean validated;

    // Views
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
    private MaterialSpinner spinnerCampus;
    @NotEmpty
    private EditText editTextRoomNumber;
    private ImageView imageViewGallery;

    // Buttons
    private Button buttonSignUp;
    private Button buttonAlreadyHaveAnAccount;

    // Form
    private User registerForm;

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

        configureDesign();

        // Validator
        configureValidator();

        // Business VM logic
        configureViewModel();
        observeDataChanges();
    }

    private void configureDesign(){
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
        spinnerCampus = findViewById(R.id.spinnerCampus);
        Picasso.get().load(R.drawable.default_profile_picture).into(imageViewGallery);

        // Activate buttons
        buttonSignUp.setOnClickListener(this);
        buttonAlreadyHaveAnAccount.setOnClickListener(this);
        imageViewGallery.setOnClickListener(this);

        // Campus spinner
        configureCampusSpinner();
        // Action bar
        configureActionBar();
    }

    /**
     * Configures ViewModels with default ViewModelProvider
     *
     * @see androidx.lifecycle.ViewModelProvider
     */
    private void configureViewModel(){
        authViewModel = new ViewModelProvider(this,
                new ViewModelProvider.AndroidViewModelFactory(getApplication())
        ).get(AuthViewModel.class);
    }

    /**
     * Calls the public methods of our ViewModel to observe their results.
     * <p>
     * For the Get methods, we used the observe() method to be automatically alerted if the
     * database result changes.
     */
    private void observeDataChanges(){ this.getRegistrationResponse(); }

    @Override
    public void onClick(View v) {
        if (v == buttonAlreadyHaveAnAccount) {
            Intent toLoginActivityIntent = new Intent();
            toLoginActivityIntent.setClass(getApplicationContext(), LoginActivity.class);
            startActivity(toLoginActivityIntent);
        }
        if (v == buttonSignUp) { signUp(); }
        if (v == imageViewGallery) { showPictureDialog(this); }
    }

    /**
     * Observe the registration response data from the authViewModel.
     * <p>
     * After a request to register, the response status changes to success or failure.
     * In case of a successful response, redirects to the LoginActivity. In case of failure, toasts
     * an error message.
     * After having done so, Set the status of the response to Complete to indicate the event has
     * been handled.
     *
     * @see RegistrationResponse
     * @see LoginActivity
     */
    private void getRegistrationResponse(){
        authViewModel.getRegistrationResponseMutableLiveData().observe(this, new Observer<RegistrationResponse>() {
            @Override
            public void onChanged(RegistrationResponse registrationResponse) {
                if (registrationResponse.getStatus().equals(Status.SUCCESS)) {
                    Toast.makeText(getApplicationContext(), R.string.account_creation_successful, Toast.LENGTH_SHORT).show();
                    authViewModel.getRegistrationResponseMutableLiveData().setValue(RegistrationResponse.complete());
                    // Redirect to the LoginActivity
                    Intent toLoginActivityIntent = new Intent();
                    toLoginActivityIntent.setClass(getApplicationContext(), LoginActivity.class);
                    startActivity(toLoginActivityIntent);
                } else if (registrationResponse.getStatus().equals(Status.ERROR)) {
                    if (registrationResponse.getError().getEmail() != null) {
                        Toast.makeText(getApplicationContext(), registrationResponse.getError().getEmail(), Toast.LENGTH_SHORT).show();
                    }
                    else if (registrationResponse.getError().getDetail() != null) {
                        Toast.makeText(getApplicationContext(), registrationResponse.getError().getDetail(), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), R.string.unexpected_error, Toast.LENGTH_SHORT).show();
                    }
                    authViewModel.getRegistrationResponseMutableLiveData().setValue(RegistrationResponse.complete());
                }
            }
        });
    }

    /**
     * Instantiate a new Validator
     *
     * @see Validator
     */
    private void configureValidator() {
        // Instantiate a new Validator
        validator = new Validator((this));
        validator.setValidationListener(this);
    }

    /**
     * Check if the form and valid and if there is a product photo. If yes, the method creates
     * the registerForm (User) object to create and calls the register method of the authViewModel.
     *
     * @see Validator#validate()
     * @see User
     * @see AuthViewModel#register(User)
     */
    private void signUp(){
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
            registerForm = new User(email, password1, password2, lastName, firstName, roomNumber, campus);
            if (fileToUploadUri != null) {
                fileToUpload = new File(fileToUploadPath);
                // Create RequestBody instance from file
                RequestBody requestFile = RequestBody.create(MediaType.parse(
                        getContentResolver().getType(fileToUploadUri)), fileToUpload);
                // MultipartBody.Part is used to send also the actual file name
                MultipartBody.Part profilePictureBody = MultipartBody.Part.createFormData(
                        "profile_picture", fileToUploadPath, requestFile);
                registerForm.setProfilePictureBody(profilePictureBody);
            }
            authViewModel.register(registerForm);
        }
    }

    /**
     * Build and show an AlertDialog to select the method from the MediaFiles class to apply :
     * either choosePictureFromGallery or captureImage
     *
     * @param activity (Activity) the current activity
     * @see AlertDialog
     * @see MediaFiles#captureImage(Activity, androidx.fragment.app.Fragment)
     * @see MediaFiles#choosePictureFromGallery(Activity, androidx.fragment.app.Fragment)
     */
    private void showPictureDialog(Activity activity) {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle(getString(R.string.select_action));
        String[] pictureDialogItems = {
                getString(R.string.gallery),
                getString(R.string.camera) };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                MediaFiles.choosePictureFromGallery(activity, null);
                                break;
                            case 1:
                                try {
                                    pictureFileUri = MediaFiles.captureImage(activity, null);
                                } catch (IOException e) { e.printStackTrace(); }
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    /**
     * Configure the spinner that contains every campus.
     * <p>
     * Creates an ArrayAdapter using a defined category string array and a default spinner layout
     * and enables to retrieve the item when selected
     *
     * @see Spinner
     * @see ArrayAdapter
     * @see Spinner#setOnItemClickListener(AdapterView.OnItemClickListener)
     */
    private void configureCampusSpinner() {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(this,R.array.campus_array, R.layout.spinner_item);// Apply the adapter to the spinner
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

    private void configureActionBar(){
        getSupportActionBar().setTitle(R.string.sign_up);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Receiving activity result method will be called after closing the gallery or the camera.
     * Modify the raw picture, store it in a new file and retrieve its Uri
     *
     * @see MediaFiles
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result code is RESULT_OK only if the user selects an Image
        if (requestCode == MediaFiles.CHOOSE_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            // data.getData returns the content URI for the selected Image
            pictureFileUri = data.getData();
            // modify the raw picture taken in a new file and retrieve its Uri
            try {
                fileToUpload = MediaFiles.processPicture(this, pictureFileUri);
                fileToUploadPath = fileToUpload.getAbsolutePath();
                fileToUploadUri = MediaFiles.getOutputMediaFileUri(this, fileToUpload);
                Picasso.get().load(fileToUploadUri).into(imageViewGallery);
            } catch (IOException e) { e.printStackTrace(); }
        } else if (requestCode == MediaFiles.CAPTURE_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            // successfully captured the image
            try {
                fileToUpload = MediaFiles.processPicture(this, pictureFileUri); // modify the raw picture taken
                fileToUploadPath = fileToUpload.getAbsolutePath();
                fileToUploadUri = MediaFiles.getOutputMediaFileUri(this, fileToUpload);
                Picasso.get().load(fileToUploadUri).into(imageViewGallery);
            } catch (IOException e) { e.printStackTrace(); }
        } else if (resultCode == RESULT_CANCELED) {
            // user cancelled Image capture
        } else {
            // failed to capture image
            Toast.makeText(this,
                    R.string.image_choice_failed, Toast.LENGTH_SHORT)
                    .show();
        }
    }

    /**
     * Called when the form has passed all the validations and set the boolean validated to true.
     *
     * @see Validator
     */
    @Override
    public void onValidationSucceeded() {
        //Called when all your views pass all validations.
        validated = true;
    }

    /**
     * Called when the form hasn't passed all the validations : set the boolean validated to false
     * and display the errors.
     *
     * @see Validator
     */
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
        outState.putParcelable(MediaFiles.URI, fileToUploadUri);
        outState.putString(MediaFiles.PATH, fileToUploadPath);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            // get the file uri
            fileToUploadUri = savedInstanceState.getParcelable(MediaFiles.URI);
            fileToUploadPath = savedInstanceState.getString(MediaFiles.PATH);
            // Reload the image view picture
            if (fileToUploadUri != null) {
                Picasso.get().load(fileToUploadUri).into(imageViewGallery);
            } else {
                Picasso.get().load(R.drawable.default_profile_picture).into(imageViewGallery);
            }
        }
    }

}
