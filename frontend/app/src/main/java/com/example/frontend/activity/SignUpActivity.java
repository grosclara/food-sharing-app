package com.example.frontend.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

import com.example.frontend.R;
import com.example.frontend.api.DjangoRestApi;
import com.example.frontend.api.NetworkClient;
import com.example.frontend.model.User;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
    private Spinner spinnerCampus;
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

    // Path to the location of the picture taken by the phone
    private boolean withPicture = false;
    private String imageFilePath;
    private Uri uriImage;
    public static final int PICK_IMAGE = 1;

    // Permissions request code
    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;
    // Permissions that need to be explicitly requested from end user
    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[] {
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermissions();
        setContentView(R.layout.activity_sign_up);

        // Views
        editTextPasswordConfirm = findViewById(R.id.editTextPasswordConfirm);
        editTextEmailSignUp = findViewById(R.id.editTextEmailSignUp);
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextPasswordSignUp = findViewById(R.id.editTextPasswordSignUp);
        editTextRoomNumber = findViewById(R.id.editTextRoomNumber);
        imageViewGallery = findViewById(R.id.imageViewGallery);
        Picasso.get().load(R.drawable.test).into(imageViewGallery);

        // Buttons
        buttonSignUp = findViewById(R.id.buttonSignUp);
        buttonAlreadyHaveAnAccount = findViewById(R.id.buttonAlreadyHaveAnAccount);
        buttonGallery = findViewById(R.id.buttonGallery);
        buttonSignUp.setOnClickListener(this);
        buttonAlreadyHaveAnAccount.setOnClickListener(this);
        buttonGallery.setOnClickListener(this);

        // Spinner
        campusArray = getResources().getStringArray(R.array.campus_array);
        spinnerCampus = findViewById(R.id.spinnerCampus);
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

    @Override
    public void onClick(View v) {
        if (v == buttonAlreadyHaveAnAccount) {

            Intent toSignInActivityIntent = new Intent();
            toSignInActivityIntent.setClass(getApplicationContext(), SignInActivity.class);
            startActivity(toSignInActivityIntent);

        } else if(v == buttonSignUp) {

            email = editTextEmailSignUp.getText().toString().trim();
            lastName = editTextLastName.getText().toString().trim();
            firstName = editTextFirstName.getText().toString().trim();
            password1 = editTextPasswordSignUp.getText().toString().trim();
            password2 = editTextPasswordConfirm.getText().toString().trim();
            room_number = editTextRoomNumber.getText().toString().trim();

            if(withPicture){
                createAccountWithPicture();
            }
            else{
                createAccountWithoutPicture();
            }
        } else if(v == buttonGallery) {
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
        startActivityForResult(Intent.createChooser(pickIntent, "Select Picture"), PICK_IMAGE);
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

    private void createAccountWithoutPicture(){

        // Define the URL endpoint for the HTTP operation.
        Retrofit retrofit = NetworkClient.getRetrofitClient(this);
        DjangoRestApi djangoRestApi = retrofit.create(DjangoRestApi.class);

        User user = new User(email, lastName, firstName, password1, password2, campus,room_number);

        // Creation of a call object that will contain the response
        Call<User> callNewUser = djangoRestApi.createUserWithoutPicture(user);

        // Asynchronous request
        callNewUser.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.d("serverRequest", response.message());
                if (response.isSuccessful()) {

                    Toast.makeText(getApplicationContext(), "You are now redirected to the Sign In page", Toast.LENGTH_SHORT).show();

                    Intent toSignInActivityIntent = new Intent();
                    toSignInActivityIntent.setClass(getApplicationContext(), SignInActivity.class);
                    startActivity(toSignInActivityIntent);

                } else {
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("serverRequest", t.getLocalizedMessage());
            }
        });


    }

    private void createAccountWithPicture() {

        // Create a file object using file path
        File file = new File(imageFilePath);

        // Create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(uriImage)), file);
        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body = MultipartBody.Part.createFormData("profile_picture", file.getName(), requestFile);


        // Define the URL endpoint for the HTTP operation.
        Retrofit retrofit = NetworkClient.getRetrofitClient(this);
        DjangoRestApi djangoRestApi = retrofit.create(DjangoRestApi.class);

        // Creation of a call object that will contain the response
        Call<User> callNewUser = djangoRestApi.createUserWithPicture(body, firstName, lastName, room_number, campus, email, password1, password2);

        // Asynchronous request
        callNewUser.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.d("serverRequest", response.message());
                if (response.isSuccessful()) {

                    Toast.makeText(getApplicationContext(), "You are now redirected to the Sign In page", Toast.LENGTH_SHORT).show();

                    Intent toSignInActivityIntent = new Intent();
                    toSignInActivityIntent.setClass(getApplicationContext(), SignInActivity.class);
                    startActivity(toSignInActivityIntent);

                } else {
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("serverRequest", t.getLocalizedMessage());
            }
        });
    }

    protected void checkPermissions() {
        /**
         * Checks the dynamically-controlled permissions and requests missing permissions from end user.
         */
        final List<String> missingPermissions = new ArrayList<String>();
        // check all required dynamic permissions
        for (final String permission : REQUIRED_SDK_PERMISSIONS) {
            final int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }
        if (!missingPermissions.isEmpty()) {
            // request all missing permissions
            final String[] permissions = missingPermissions
                    .toArray(new String[missingPermissions.size()]);
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_PERMISSIONS);
        } else {
            final int[] grantResults = new int[REQUIRED_SDK_PERMISSIONS.length];
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
            onRequestPermissionsResult(REQUEST_CODE_ASK_PERMISSIONS, REQUIRED_SDK_PERMISSIONS,
                    grantResults);
        }
    }
}
