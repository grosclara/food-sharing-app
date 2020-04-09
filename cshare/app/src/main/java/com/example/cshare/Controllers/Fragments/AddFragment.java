package com.example.cshare.Controllers.Fragments;


import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
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

import com.example.cshare.Models.Product;
import com.example.cshare.Models.ProductToPost;
import com.example.cshare.Utils.Camera;
import com.example.cshare.Utils.Constants;
import com.example.cshare.ViewModels.HomeViewModel;
import com.example.cshare.ViewModels.SharedProductsViewModel;
import com.example.cshare.WebServices.NetworkClient;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;

import com.example.cshare.R;
import com.mobsandgeeks.saripaar.annotation.Future;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class AddFragment extends BaseFragment implements View.OnClickListener, Validator.ValidationListener {

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
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

    // Check whether a picture has been selected
    private boolean pictureSelected = false;

    // Path to the location of the picture taken by the phone
    private String pictureFilePath;
    private Uri pictureFileUri;
    private File fileToUpload;
    private Uri fileToUploadUri;

    // ViewModels
    HomeViewModel homeViewModel;
    SharedProductsViewModel sharedProductsViewModel;

    @Override
    protected BaseFragment newInstance() {
        return new AddFragment();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_add;
    }

    @Override
    protected void configureDesign(View view) {

        // Bind views
        editTextProductName = view.findViewById(R.id.editTextProductName);
        editTextQuantity = view.findViewById(R.id.editTextQuantity);
        editTextExpirationDate = view.findViewById(R.id.editTextExpirationDate);
        textViewPictureError = view.findViewById(R.id.textViewPictureError);
        buttonPhoto = view.findViewById(R.id.buttonPhoto);
        buttonSubmit = view.findViewById(R.id.buttonSubmit);
        spinnerProductCategories = view.findViewById(R.id.spinnerProductCategories);
        imageViewPreviewProduct = view.findViewById(R.id.imageViewPreviewProduct);

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

    @Override
    protected void updateDesign() {
    }

    @Override
    protected void configureViewModel() {
        // Retrieve data from view model
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        sharedProductsViewModel = new ViewModelProvider(this).get(SharedProductsViewModel.class);
    }

    private void configureValidator() {
        // Instantiate a new Validator
        validator = new Validator((this));
        validator.setValidationListener(this);
    }

    private void configureProductSpinner() {
        productCategoriesArray = getResources().getStringArray(R.array.product_categories_array);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, productCategoriesArray);
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
            String message = error.getCollatedErrorMessage(getContext());

            // Display error messages
            if (view == editTextExpirationDate) {
                editTextExpirationDate.setHintTextColor(Color.parseColor("#FF0000"));
                editTextExpirationDate.setBackgroundColor(Color.parseColor("#33FF0000"));
                editTextExpirationDate.setHint(message);
            } else if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onClick(View v) {

        if (v == buttonPhoto || v == imageViewPreviewProduct) {

            // Capture picture
            try {
                captureImage();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (v == buttonSubmit) {

            // Validate the field
            validator.validate();

            if (validated & pictureSelected) {

                // Retrieve the name of the product typed in the editText field
                productName = String.valueOf(editTextProductName.getText());
                // Retrieve the quantity of the product typed in the editText field
                quantity = String.valueOf(editTextQuantity.getText());

                try {
                    fileToUploadUri = Camera.getOutputMediaFileUri(getContext(), fileToUpload);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Create RequestBody instance from file
                RequestBody requestFile = RequestBody.create(MediaType.parse(getActivity().getContentResolver().getType(fileToUploadUri)), fileToUpload);
                // MultipartBody.Part is used to send also the actual file name
                String imageFileName = NetworkClient.BASE_URL + "media/product/" + fileToUpload.getAbsolutePath();
                MultipartBody.Part product_picture = MultipartBody.Part.createFormData("product_picture", imageFileName, requestFile);


                // HTTP Post request (CREATE A NEW MODEL PRODUCT TO POST ????)
                ProductToPost productToPost = new ProductToPost(product_picture, productName, productCategory, quantity, expiration_date, Constants.USERID);
                homeViewModel.addProduct(productToPost);

                // Format the product to update view models
                Product product = new Product(productName, Constants.AVAILABLE, imageFileName, Constants.USERID, productCategory, quantity, expiration_date);
                homeViewModel.insert(product);
                sharedProductsViewModel.insert(product);

            } else if (!pictureSelected) {
                Toast.makeText(getContext(), "You must choose a product picture", Toast.LENGTH_SHORT).show();
                textViewPictureError.setVisibility(View.VISIBLE);
            }

        } else if (v == editTextExpirationDate) {
            // Get Current Date
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
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
        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", pictureFileUri);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d("tag", "onViewStateRestored");
        if (savedInstanceState != null) {
            // get the file url
            pictureFileUri = savedInstanceState.getParcelable("file_uri");
        }
    }

    public void captureImage() throws IOException {
        // Launching camera app to capture image
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Create a file to store the picture taken
        File pictureFile = Camera.createImageFile(getContext());
        // Retrieve its Uri
        pictureFileUri = Camera.getOutputMediaFileUri(getContext(), pictureFile);
        // Specifying EXTRA_OUTPUT allows to go get the photo from the uri that you provided in EXTRA_OUTPUT
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, pictureFileUri);

        // Checking whether device has camera hardware or not
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // start the image capture Intent
            startActivityForResult(takePictureIntent, Camera.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        }
    }

    /**
     * Receiving activity result method will be called after closing the camera
     */
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Handle the case where the user cancelled the camera intent without taking a picture like,
        // though we have the imagePath, but it’s not a valid image because the user has not taken the picture.
        if (requestCode == Camera.CAMERA_CAPTURE_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            // successfully captured the image
            try {
                processPicture(pictureFileUri); // modify the raw picture taken
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (resultCode == RESULT_CANCELED) {
            // user cancelled Image capture
            Toast.makeText(getContext(),
                    "User cancelled image capture", Toast.LENGTH_SHORT)
                    .show();
        } else {
            // failed to capture image
            Toast.makeText(getContext(),
                    "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void processPicture(Uri uri) throws IOException {
        if (uri != null) {

            pictureSelected = true;
            // Rotate if necessary and reduce size
            Bitmap bitmap = Camera.handleSamplingAndRotationBitmap(getActivity().getContentResolver(), uri);
            // Displaying the image or video on the screen
            Camera.previewMedia(bitmap, imageViewPreviewProduct);
            // Save new picture to fileToUpload
            fileToUpload = Camera.saveBitmap(getContext(), bitmap);

        } else {
            Toast.makeText(getContext(),
                    "Sorry, file uri is missing!", Toast.LENGTH_LONG).show();
        }
    }
}
