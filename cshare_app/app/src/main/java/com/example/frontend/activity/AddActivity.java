package com.example.frontend.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frontend.R;
import com.example.frontend.api.DjangoRestApi;
import com.example.frontend.api.NetworkClient;
import com.example.frontend.model.Product;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Future;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Select;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * AddActivity Class.
 * Give the possibility to the user to add a product in the database.
 * The form to fill the product includes EditTexts.
 * Allows to take a picture of the product by opening the camera clicking on the buttonPicture.
 * After having added the product, the user is redirected to the CollectActivity.
 *
 * @author Clara Gros, Babacar Toure
 * @version 1.0
 */

public class AddActivity extends AppCompatActivity implements View.OnClickListener, Validator.ValidationListener {

    // Form validation
    protected Validator validator;
    private boolean validated;

    private TextView textViewPictureError;
    @NotEmpty
    private EditText editTextProductName;
    private ImageView imageViewPreviewProduct;
    private Spinner spinnerProductCategories;
    @NotEmpty
    @Future
    private EditText editTextExpirationDate;
    @NotEmpty
    private EditText editTextQuantity;

    private Button buttonPhoto;
    private Button buttonSubmit;

    private String productName;
    private String[] productCategoriesArray;
    private String productCategory;
    private String expiration_date;
    private String quantity;

    // Date picker
    private DatePickerDialog.OnDateSetListener dateSetListener;

    // Path to the location of the picture taken by the phone
    private String imageFilePath;
    private Uri fileUri;
    // Ensures the intent to open the camera can be performed
    private static final int REQUEST_CAPTURE_IMAGE = 1;
    // Check whether a picture has been selected
    private boolean pictureSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        // Views
        editTextProductName = findViewById(R.id.editTextProductName);
        editTextQuantity = findViewById(R.id.editTextQuantity);
        editTextExpirationDate = findViewById(R.id.editTextExpirationDate);
        textViewPictureError = findViewById(R.id.textViewPictureError);

        // Buttons
        buttonPhoto = findViewById(R.id.buttonPhoto);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonPhoto.setOnClickListener(this);
        buttonSubmit.setOnClickListener(this);

        productCategoriesArray = getResources().getStringArray(R.array.product_categories_array);
        spinnerProductCategories = findViewById(R.id.spinnerProductCategories);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, productCategoriesArray);
        // Apply the adapter to the spinner
        spinnerProductCategories.setAdapter(adapterSpinner);
        spinnerProductCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // An item was selected. You can retrieve the selected item using
                productCategory = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        editTextExpirationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get Current Date
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                expiration_date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                editTextExpirationDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                editTextExpirationDate.setBackgroundColor(Color.WHITE);

                            }
                        },
                        year, month, day);
                datePickerDialog.show();

                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        // Instantiate a new Validator
        validator = new Validator((this));
        validator.setValidationListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonPhoto) {

            openCameraIntent();

        } else if(v == buttonSubmit) {

            // Validate the field
            validator.validate();

            if(validated & pictureSelected) {

                // Retrieve the name of the product typed in the editText field
                productName = String.valueOf(editTextProductName.getText());

                // Retrieve the quantity of the product typed in the editText field
                quantity = String.valueOf(editTextQuantity.getText());

                // Call for the addProduct(Product) method to transfer data to the server
                addProduct(productName);
            }
            else if(!pictureSelected){

                Toast.makeText(getApplicationContext(), "You must choose a product picture",Toast.LENGTH_SHORT).show();
                textViewPictureError.setVisibility(View.VISIBLE);
            }
        }
    }

    public void addProduct(String productName) {
        /**
         * Send a HTTP request to post the Product product taken in param
         * @param productName
         */

        // Define the URL endpoint for the HTTP operation.
        Retrofit retrofit = NetworkClient.getRetrofitClient(this);
        DjangoRestApi djangoRestApi = retrofit.create(DjangoRestApi.class);

        // Create a file object using file path
        File file = new File(imageFilePath);

        // Create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(fileUri)), file);
        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body = MultipartBody.Part.createFormData("product_picture", file.getName(), requestFile);


        // Asynchronous request
        Call<Product> call = djangoRestApi.addProduct(CollectActivity.token, body, productName, productCategory, quantity, expiration_date, CollectActivity.userId);
        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                Log.d("serverRequest", response.message() + ' ' + String.valueOf(response.code()));
                if (response.isSuccessful()) {
                    // In case of success, toast "Submit!"
                    Toast.makeText(getApplicationContext(), "Submit!", Toast.LENGTH_SHORT).show();
                    // Go back to the CollectActivity
                    Intent toCollectActivityIntent = new Intent();
                    toCollectActivityIntent.setClass(getApplicationContext(), CollectActivity.class);
                    startActivity(toCollectActivityIntent);
                    finish(); // Disable the "going back functionality" from the CollectActivity to the AddActivity
                } else {
                    Toast.makeText(getApplicationContext(), "An error occurred!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Log.d("serverRequest", t.getLocalizedMessage());
            }
        });
    }

    public void openCameraIntent() {
        /**
         * Creation of an Intent of type ACTION_IMAGE_CAPTURE to open the camera.
         * The picture taken is then loaded in a temporary file from which we save its absolute path in the picturePath variable
         * We create a URI (Uniform Resource Identifier) for this file.
         * Eventually the intent call for the onActivityResult method.
         * @see #onActivityResult(int, int, Intent)
         * @see #createImageFile()
         */
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
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
                        AddActivity.this,
                        AddActivity.this.getApplicationContext().getPackageName() + ".provider",
                        photoFile
                );
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        fileUri);
                startActivityForResult(pictureIntent,
                        REQUEST_CAPTURE_IMAGE);
            }
        }
    }

    private File createImageFile() throws IOException {
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
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        /**
         * Return of the camera call (startActivityForResult)
         * Get the picture and load it into the imageView to give the user a preview of the picture he took
         * @param requestCode
         * @param resultCode
         * @param data
         */
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAPTURE_IMAGE) {
            // Handle the case where the user cancelled the camera intent without taking a picture like,
            // though we have the imagePath, but it’s not a valid image because the user has not taken the picture.
            if (resultCode == Activity.RESULT_OK) {

                pictureSelected = true;
                textViewPictureError.setVisibility(View.GONE);

                // Load with the imageFilePath we obtained before opening the cameraIntent
                imageViewPreviewProduct = findViewById(R.id.imageViewPreviewProduct);
                Bitmap imageBitmap = BitmapFactory.decodeFile(imageFilePath);
                imageViewPreviewProduct.setImageBitmap(imageBitmap);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // User Cancelled the action
            }
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
            if(view == editTextExpirationDate){
                editTextExpirationDate.setHintTextColor(Color.parseColor("#FF0000"));
                editTextExpirationDate.setBackgroundColor(Color.parseColor("#33FF0000"));
                editTextExpirationDate.setHint(message);
            }
            else if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
