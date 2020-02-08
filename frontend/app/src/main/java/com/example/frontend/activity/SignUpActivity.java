package com.example.frontend.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.Intent;
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
import java.util.Date;
import java.util.Locale;

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
    private String imageFilePath;
    // Path to the location of the picture taken by the phone
    private Uri uriImage;

    public static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        } else if (v == buttonSignUp) {
            createAccount();
        } else if (v == buttonGallery) {

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

        /*Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        if (chooserIntent.resolveActivity(getPackageManager()) != null) {
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
                Log.d("file", "File was successfully created");
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.d("file", "An error occurred while creating the file");
            }
            if (photoFile != null) {
                fileUri = FileProvider.getUriForFile(
                        SignUpActivity.this,
                        SignUpActivity.this.getApplicationContext().getPackageName() + ".provider",
                        photoFile
                );
                chooserIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        fileUri);
                startActivityForResult(chooserIntent, PICK_IMAGE);
            }
        }*/

        // Create a chooser in case there are third parties app and launch the Intent
        startActivityForResult(Intent.createChooser(pickIntent, "Select Picture"), PICK_IMAGE);
    }

    private File createImageFile () throws IOException {
        /**
         * Method that creates a file for the photo with a unique name
         * @return
         * @throws IOException
         */
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        imageFilePath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            // Result code is RESULT_OK only if the user selects an Image
            if (resultCode == Activity.RESULT_OK)
                switch (requestCode) {
                    case PICK_IMAGE:
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

    private void createAccount() {

        Toast.makeText(getApplicationContext(), String.valueOf(PICK_IMAGE), Toast.LENGTH_SHORT).show();

        email = editTextEmailSignUp.getText().toString().trim();
        lastName = editTextLastName.getText().toString().trim();
        firstName = editTextFirstName.getText().toString().trim();
        password1 = editTextPasswordSignUp.getText().toString().trim();
        password2 = editTextPasswordConfirm.getText().toString().trim();
        room_number = editTextRoomNumber.getText().toString().trim();

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
        Call<User> callNewUser = djangoRestApi.createUser(body, firstName, lastName, room_number, campus, email, password1, password2);

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
}
