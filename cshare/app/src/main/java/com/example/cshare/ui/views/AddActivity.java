package com.example.cshare.ui.views;

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

import com.example.cshare.data.apiresponses.ProductResponse;
import com.example.cshare.data.models.Product;
import com.example.cshare.data.models.User;
import com.example.cshare.R;
import com.example.cshare.data.apiresponses.Status;
import com.example.cshare.utils.MediaFiles;
import com.example.cshare.utils.Constants;
import com.example.cshare.ui.viewmodels.HomeViewModel;
import com.example.cshare.ui.viewmodels.ProductViewModel;
import com.example.cshare.ui.viewmodels.ProfileViewModel;
import com.example.cshare.ui.viewmodels.SharedViewModel;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
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
    private TextView textViewExpirationDate;
    @NotEmpty
    private EditText editTextQuantity;
    private Spinner spinnerProductCategories;
    private Button buttonPhoto;
    private Button buttonSubmit;
    private Button buttonExpirationDate;

    private String productName;
    private String[] productCategoriesArray;
    private String productCategory;
    private String expiration_date;
    private String quantity;
    private User supplier;

    private Product product;

    // Path to the location of the picture taken by the phone
    private Uri pictureFileUri;
    private File fileToUpload;
    private Uri fileToUploadUri;
    private String fileToUploadPath;

    // ViewModels
    ProductViewModel productViewModel;
    ProfileViewModel profileViewModel;
    HomeViewModel homeViewModel;
    SharedViewModel sharedViewModel;

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
        textViewExpirationDate = findViewById(R.id.textViewExpirationDate);
        textViewPictureError = findViewById(R.id.textViewPictureError);
        buttonPhoto = findViewById(R.id.buttonPhoto);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonExpirationDate = findViewById(R.id.buttonExpirationDate);
        spinnerProductCategories = findViewById(R.id.spinnerProductCategories);
        imageViewPreviewProduct = findViewById(R.id.imageViewPreviewProduct);

        Picasso.get().load(R.drawable.test).into(imageViewPreviewProduct);

        // Validator
        configureValidator();

        // Click listeners
        buttonPhoto.setOnClickListener(this);
        buttonSubmit.setOnClickListener(this);
        buttonExpirationDate.setOnClickListener(this);
        imageViewPreviewProduct.setOnClickListener(this);

        // Product spinner
        configureProductSpinner();
    }

    protected void configureViewModel() {
        // Retrieve data from view model
        productViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(ProductViewModel.class);
        profileViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(ProfileViewModel.class);
        homeViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(HomeViewModel.class);
        sharedViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(SharedViewModel.class);

        productViewModel.getAddProductResponse().observe(this, new Observer<ProductResponse>() {
            @Override
            public void onChanged(ProductResponse productResponse) {

                if (productResponse.getStatus().equals(Status.SUCCESS)) {

                    productViewModel.getAddProductResponse().setValue(ProductResponse.complete());

                    Toast.makeText(getApplicationContext(), "Product added successfully", Toast.LENGTH_SHORT).show();
                    // Call the BottomNavigationView.OnNavigationItemSelectedListener in the main activity
                    homeViewModel.refresh();
                    sharedViewModel.refresh();

                    Intent intent = new Intent(getApplicationContext(), HomeScreenActivity.class);
                    startActivity(intent);

                } else if (productResponse.getStatus().equals(Status.ERROR)) {

                    if (productResponse.getError().getDetail() != null){
                        Toast.makeText(getApplicationContext(), productResponse.getError().getDetail(), Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(getApplicationContext(), "Unexpected error", Toast.LENGTH_SHORT).show();
                    }
                    productViewModel.getAddProductResponse().setValue(ProductResponse.complete());

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

    private void configureDatePicker(){

        // Get Current Date
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        int realMonth = monthOfYear + 1;
                        String strMonth = "";
                        if (realMonth < 10){
                            strMonth = "0"+ realMonth;
                        } else {
                            strMonth = String.valueOf(realMonth) ;
                        }
                        String strDay = "";
                        if (dayOfMonth < 10){
                            strDay = "0"+ dayOfMonth;
                        } else {
                            strDay = String.valueOf(dayOfMonth) ;
                        }
                        expiration_date = year + "-" + strMonth + "-" + strDay;
                        textViewExpirationDate.setText(expiration_date);
                    }
                },
                year, month, day);
        datePickerDialog.show();

        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.show();
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
            if (view == textViewExpirationDate) {
                ((TextView) view).setError(message);
                Toast.makeText(this, "Expiration date is required", Toast.LENGTH_LONG).show();
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
                                MediaFiles.choosePictureFromGallery(activity, null);
                                break;
                            case 1:
                                try {
                                    pictureFileUri = MediaFiles.captureImage(activity, null);
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

            Log.d(Constants.TAG, productCategory);

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
                product = new Product(productPictureBody, productName, productCategory, quantity, expiration_date, supplier.getId(), imageFileName, supplier.getCampus(), supplier.getRoomNumber());

                productViewModel.addProduct(product);

            } else if (fileToUploadUri == null) {
                textViewPictureError.setVisibility(View.VISIBLE);
            }

        } else if (v == buttonExpirationDate) {
            configureDatePicker();
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
        if (requestCode == MediaFiles.CHOOSE_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {

            // data.getData returns the content URI for the selected Image
            pictureFileUri = data.getData();

            // modify the raw picture taken in a new file and retrieve its Uri
            try {
                fileToUpload = MediaFiles.processPicture(this, pictureFileUri);

                fileToUploadPath = fileToUpload.getAbsolutePath();
                fileToUploadUri = MediaFiles.getOutputMediaFileUri(this, fileToUpload);

                Picasso.get().load(fileToUploadUri).into(imageViewPreviewProduct);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == MediaFiles.CAPTURE_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            // successfully captured the image

            try {
                Log.d(Constants.TAG, pictureFileUri.toString());
                fileToUpload = MediaFiles.processPicture(this, pictureFileUri); // modify the raw picture taken
                fileToUploadPath = fileToUpload.getAbsolutePath();
                fileToUploadUri = MediaFiles.getOutputMediaFileUri(this, fileToUpload);

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
