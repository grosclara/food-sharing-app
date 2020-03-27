package com.example.cshare.Controllers.Fragments;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.cshare.Utils.Camera;
import com.example.cshare.Utils.Constants;
import com.example.cshare.ViewModels.HomeViewModel;
import com.example.cshare.ViewModels.SharedProductsViewModel;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;

import com.example.cshare.R;
import com.mobsandgeeks.saripaar.annotation.Future;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AddFragment extends BaseFragment implements View.OnClickListener, Validator.ValidationListener {

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

    // Path to the location of the picture taken by the phone
    private String imageFilePath;
    private Uri fileUri;
    // Ensures the intent to open the camera can be performed
    private static final int REQUEST_CAPTURE_IMAGE = 1;
    // Check whether a picture has been selected
    private boolean pictureSelected = false;

    // ViewModels
    HomeViewModel homeViewModel;
    SharedProductsViewModel sharedProductsViewModel;

    // Camera
    Camera camera;

    @Override
    protected BaseFragment newInstance() { return new AddFragment(); }

    @Override
    protected int getFragmentLayout() { return R.layout.fragment_add; }

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

    private void configureValidator(){
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
    public void onClick(View v) {

        if (v == buttonPhoto) {
            camera = new Camera(this,pictureSelected);
            camera.openCameraIntent();

        } else if (v == buttonSubmit) {

            // Validate the field
            validator.validate();

            if(validated & pictureSelected) {

                // Retrieve the name of the product typed in the editText field
                productName = String.valueOf(editTextProductName.getText());
                // Retrieve the quantity of the product typed in the editText field
                quantity = String.valueOf(editTextQuantity.getText());

                // Create a file object using file path
                File file = new File(imageFilePath);
                // Create RequestBody instance from file
                RequestBody requestFile = RequestBody.create(MediaType.parse(getActivity().getContentResolver().getType(fileUri)), file);
                // MultipartBody.Part is used to send also the actual file name
                MultipartBody.Part product_picture = MultipartBody.Part.createFormData("product_picture", file.getName(), requestFile);

                // HTTP Post request (CREATE A NEW MODEL PRODUCT TO POST ????)
                homeViewModel.addProduct(product_picture, productName, productCategory, quantity, expiration_date);

                // Format the product to update view models
                String imageFileName = Constants.URL + "media/product/" + camera.imageFilePath.split("/")[camera.imageFilePath.split("/").length -1];
                Product product = new Product(productName, Constants.AVAILABLE, imageFileName, Constants.USERID, productCategory, quantity, expiration_date);
                homeViewModel.insert(product);
                sharedProductsViewModel.insert(product);
            }

            else if(!pictureSelected){
                Toast.makeText(getContext(), "You must choose a product picture",Toast.LENGTH_SHORT).show();
                textViewPictureError.setVisibility(View.VISIBLE);
            }

        } else if (v == editTextExpirationDate){
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

    @Override
    public void onActivityResult(int requestCode, int resultCode,
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

                // Get image file and fileUri
                imageFilePath = camera.imageFilePath;
                fileUri = camera.fileUri;

                // Load with the imageFilePath we obtained before opening the cameraIntent
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
            String message = error.getCollatedErrorMessage(getContext());

            // Display error messages
            if(view == editTextExpirationDate){
                editTextExpirationDate.setHintTextColor(Color.parseColor("#FF0000"));
                editTextExpirationDate.setBackgroundColor(Color.parseColor("#33FF0000"));
                editTextExpirationDate.setHint(message);
            }
            else if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        }
    }

}
