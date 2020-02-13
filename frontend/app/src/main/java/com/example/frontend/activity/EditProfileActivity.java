package com.example.frontend.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.content.Intent;
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

import com.example.frontend.R;
import com.example.frontend.activity.ui.main.ChangePasswordFragment;
import com.example.frontend.api.DjangoRestApi;
import com.example.frontend.api.NetworkClient;
import com.example.frontend.model.User;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextRoomNumber;
    private ImageView imageViewProfilePicture;
    private Spinner spinnerCampus;

    private String firstName;
    private String lastName;
    private String roomNumber;
    private String campus;
    private String[] campusArray;

    private Button buttonSubmit;
    private Button buttonGallery;
    private Button buttonChangePassword;

    private User profile;

    private static final String state = "changePassword";

    // Path to the location of the picture taken by the phone
    private boolean withPicture = false;
    private String imageFilePath;
    private Uri uriImage;
    public static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Views
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextRoomNumber = findViewById(R.id.editTextRoomNumber);
        spinnerCampus = findViewById(R.id.spinnerCampus);
        imageViewProfilePicture = findViewById(R.id.imageViewProfilePicture);

        // Buttons
        buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(this);
        buttonGallery = findViewById(R.id.buttonGallery);
        buttonGallery.setOnClickListener(this);
        buttonChangePassword = findViewById(R.id.buttonChangePassword);
        buttonChangePassword.setOnClickListener(this);

        // Get the user info from the ProfileActivity intent
        Intent fromProfileActivityIntent = getIntent();
        profile = (User) fromProfileActivityIntent.getSerializableExtra("profile");

        firstName = profile.getFirst_name();
        lastName = profile.getLast_name();
        roomNumber = profile.getRoom_number();
        campus = profile.getCampus();
        Picasso.get().load(profile.getProfile_picture()).into(imageViewProfilePicture);

        // Set the previous values as default
        editTextFirstName.setHint(firstName);
        editTextLastName.setHint(lastName);
        editTextRoomNumber.setHint(roomNumber);

        campusArray = getResources().getStringArray(R.array.campus_array);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, campusArray);
        // Apply the adapter to the spinner
        spinnerCampus.setAdapter(adapterSpinner);
        spinnerCampus.setSelection(adapterSpinner.getPosition(campus));
        spinnerCampus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // An item was selected. You can retrieve the selected item using
                profile.setCampus(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (buttonSubmit.equals(v)) {
            if (!editTextFirstName.getText().toString().isEmpty()) {
                profile.setFirst_name(editTextFirstName.getText().toString().trim());
            }
            if (!editTextLastName.getText().toString().isEmpty()) {
                profile.setLast_name(editTextLastName.getText().toString().trim());
            }
            if (!editTextRoomNumber.getText().toString().isEmpty()) {
                profile.setRoom_number(editTextRoomNumber.getText().toString().trim());
            }
            if(withPicture){editProfileWithPicture();}
            else{editProfileWithoutPicture();}
        }
        if (buttonGallery.equals(v)) {
            choosePictureFromGallery();
        }
        if (buttonChangePassword.equals(v)) {
            DialogFragment changePasswordFragment = new ChangePasswordFragment(getApplicationContext());
            changePasswordFragment.show(getSupportFragmentManager(), state);
        }
    }

    public void editProfileWithPicture() {

        // Define the URL endpoint for the HTTP operation.
        Retrofit retrofit = NetworkClient.getRetrofitClient(this);
        DjangoRestApi djangoRestApi = retrofit.create(DjangoRestApi.class);

        // Create a file object using file path
        File file = new File(imageFilePath);

        // Create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(uriImage)), file);
        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body = MultipartBody.Part.createFormData("profile_picture", file.getName(), requestFile);

        // Creation of a call object that will contain the response
        Call<User> call = djangoRestApi.updateProfileWithPicture(CollectActivity.token,
                profile.getId(),
                body,
                profile.getFirst_name(),
                profile.getLast_name(),
                profile.getRoom_number(),
                profile.getCampus(),
                profile.getEmail(),
                true);
        // Asynchronous request
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.i("serverRequest", response.message());
                Toast.makeText(getApplicationContext(), "profile editted", Toast.LENGTH_SHORT).show();

                Intent toProfileActivityIntent = new Intent();
                toProfileActivityIntent.setClass(getApplicationContext(), ProfileActivity.class);
                startActivity(toProfileActivityIntent);
                finish();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "profile error", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void editProfileWithoutPicture() {

        // Define the URL endpoint for the HTTP operation.
        Retrofit retrofit = NetworkClient.getRetrofitClient(this);
        DjangoRestApi djangoRestApi = retrofit.create(DjangoRestApi.class);

        // Creation of a call object that will contain the response
        Call<User> call = djangoRestApi.updateProfileWithoutPicture(CollectActivity.token,
                profile.getId(),
                profile.getFirst_name(),
                profile.getLast_name(),
                profile.getRoom_number(),
                profile.getCampus(),
                profile.getEmail(),
                true);
        // Asynchronous request
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.i("serverRequest", response.message());
                Toast.makeText(getApplicationContext(), "profile editted", Toast.LENGTH_SHORT).show();

                Intent toProfileActivityIntent = new Intent();
                toProfileActivityIntent.setClass(getApplicationContext(), ProfileActivity.class);
                startActivity(toProfileActivityIntent);
                finish();
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

        // Create an Intent with action as ACTION_PICK
        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Sets the type as image/*. This ensures only components of type image are selected
        pickIntent.setType("image/*");

        // We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        pickIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

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

                        imageViewProfilePicture.setImageBitmap(BitmapFactory.decodeFile(imageFilePath));
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

}
