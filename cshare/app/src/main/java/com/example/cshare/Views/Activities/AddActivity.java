package com.example.cshare.Views.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cshare.Models.ApiResponses.ProductResponse;
import com.example.cshare.Models.Product;
import com.example.cshare.Models.User;
import com.example.cshare.R;
import com.example.cshare.RequestManager.Status;
import com.example.cshare.Utils.Camera;
import com.example.cshare.Utils.Constants;
import com.example.cshare.ViewModels.ProductViewModel;
import com.example.cshare.ViewModels.ProfileViewModel;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Future;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AddActivity extends AppCompatActivity implements View.OnClickListener, Validator.ValidationListener {

    // Form validation
    protected Validator validator;
    private boolean validated;

    private TextView textViewPictureError;
    @NotEmpty
    private EditText editTextProductName;
    private ImageView imageViewPreviewProduct;
    @NotEmpty
    @Future
    private EditText editTextExpirationDate;
    @NotEmpty
    private EditText editTextQuantity;
    private Spinner spinnerProductCategories;
    private Button buttonPhoto;
    private Button buttonSubmit;

    private String productName;
    private String[] productCategoriesArray;
    private String productCategory;
    private String expiration_date;
    private String quantity;
    private User supplier;


    // Path to the location of the picture taken by the phone
    private Uri pictureFileUri;
    private File fileToUpload;
    private Uri fileToUploadUri;
    private String fileToUploadPath;

    // ViewModels
    ProductViewModel productViewModel;
    ProfileViewModel profileViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        getSupportActionBar().setTitle("Share your product");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        configureDesign();

        configureViewModel();
    }

    protected void configureDesign() {
        // Bind views
        editTextProductName = findViewById(R.id.editTextProductName);
        editTextQuantity = findViewById(R.id.editTextQuantity);
        editTextExpirationDate = findViewById(R.id.editTextExpirationDate);
        textViewPictureError = findViewById(R.id.textViewPictureError);
        buttonPhoto = findViewById(R.id.buttonPhoto);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        spinnerProductCategories = findViewById(R.id.spinnerProductCategories);
        imageViewPreviewProduct = findViewById(R.id.imageViewPreviewProduct);

        Picasso.get().load(R.drawable.test).into(imageViewPreviewProduct);

        // Validator
        configureValidator();

        // Click listeners
        buttonPhoto.setOnClickListener(this);
        buttonSubmit.setOnClickListener(this);
        editTextExpirationDate.setOnClickListener(this);
        imageViewPreviewProduct.setOnClickListener(this);

        // Product spinner
        configureProductSpinner();
    }

    protected void configureViewModel() {
        // Retrieve data from view model
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        productViewModel.getAddProductResponse().observe(this, new Observer<ProductResponse>() {
            @Override
            public void onChanged(ProductResponse productResponse) {
                if (productResponse.getStatus().equals(Status.LOADING)) {
                    Toast.makeText(getApplicationContext(), "Loading", Toast.LENGTH_SHORT).show();
                } else if (productResponse.getStatus().equals(Status.SUCCESS)) {

                    Toast.makeText(getApplicationContext(), "Product added successfully", Toast.LENGTH_SHORT).show();
                    // Call the BottomNavigationView.OnNavigationItemSelectedListener in the main activity

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);

                    //((BottomNavigationView) findViewById(R.id.bottom_navigation)).setSelectedItemId(R.id.nav_home);
                } else if (productResponse.getStatus().equals(Status.ERROR)) {
                    Toast.makeText(getApplicationContext(), productResponse.getError().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        supplier = profileViewModel.getUserProfileMutableLiveData().getValue().getUser();
    }

    private void configureValidator() {
        // Instantiate a new Validator
        validator = new Validator((this));
        validator.setValidationListener(this);
    }

    private void configureProductSpinner() {
        productCategoriesArray = getResources().getStringArray(R.array.product_categories_array);
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
            if (view == editTextExpirationDate) {
                editTextExpirationDate.setHintTextColor(Color.parseColor("#FF0000"));
                editTextExpirationDate.setBackgroundColor(Color.parseColor("#33FF0000"));
                editTextExpirationDate.setHint(message);
            } else if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Log.d(Constants.TAG, message);
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void showPictureDialog(Activity activity) {

        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(activity);
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

    @Override
    public void onClick(View v) {

        if (v == buttonPhoto || v == imageViewPreviewProduct) {

            showPictureDialog(this);

        } else if (v == buttonSubmit) {

            // Validate the field
            validator.validate();

            if (validated & fileToUploadUri != null) {

                // Retrieve the name of the product typed in the editText field
                productName = String.valueOf(editTextProductName.getText());
                // Retrieve the quantity of the product typed in the editText field
                quantity = String.valueOf(editTextQuantity.getText());

                fileToUpload = new File(fileToUploadPath);
                // Create RequestBody instance from file
                RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(fileToUploadUri)), fileToUpload);
                // MultipartBody.Part is used to send also the actual file name
                MultipartBody.Part productPictureBody = MultipartBody.Part.createFormData("product_picture", fileToUpload.getAbsolutePath(), requestFile);

                // Format the product to update view models
                String imageFileName = Constants.BASE_URL_API + "media/product/" + fileToUpload.getPath().split("/")[fileToUpload.getPath().split("/").length - 1];

                // HTTP Post request
                Product product = new Product(productPictureBody, productName, productCategory, quantity, expiration_date, supplier.getId(), imageFileName, supplier.getCampus(), supplier.getRoomNumber());

                productViewModel.addProduct(product);

            } else if (fileToUploadUri == null) {
                Toast.makeText(this, "You must choose a product picture", Toast.LENGTH_SHORT).show();
                textViewPictureError.setVisibility(View.VISIBLE);
            }

        } else if (v == editTextExpirationDate) {
            // Get Current Date
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
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
                Picasso.get().load(fileToUploadUri).into(imageViewPreviewProduct);
            } else {
                Picasso.get().load(R.drawable.test).into(imageViewPreviewProduct);
            }
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

            // modify the raw picture taken in a new file and retrieve its Uri
            try {
                fileToUpload = Camera.processPicture(this, pictureFileUri);

                fileToUploadPath = fileToUpload.getAbsolutePath();
                fileToUploadUri = Camera.getOutputMediaFileUri(this, fileToUpload);

                Picasso.get().load(fileToUploadUri).into(imageViewPreviewProduct);

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

                Picasso.get().load(fileToUploadUri).into(imageViewPreviewProduct);

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

}
