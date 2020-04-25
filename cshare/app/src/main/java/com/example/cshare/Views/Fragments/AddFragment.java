package com.example.cshare.Views.Fragments;


import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
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
import com.example.cshare.RequestManager.Status;
import com.example.cshare.Models.Product;
import com.example.cshare.Models.Forms.ProductForm;
import com.example.cshare.Utils.Camera;
import com.example.cshare.Utils.Constants;
import com.example.cshare.ViewModels.ProductViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;

import com.example.cshare.R;
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

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

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

    // Check whether a picture has been selected
    private boolean pictureSelected = false;

    // Path to the location of the picture taken by the phone
    private Uri pictureFileUri;
    private File fileToUpload;
    private Uri fileToUploadUri;
    private String fileToUploadPath;

    // ViewModels
    ProductViewModel productViewModel;

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

    @Override
    protected void updateDesign() {
    }

    @Override
    protected void configureViewModel() {
        // Retrieve data from view model
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        productViewModel.getAddProductResponse().observe(getViewLifecycleOwner(), new Observer<ProductResponse>() {
            @Override
            public void onChanged(ProductResponse productResponse) {
                if (productResponse.getStatus().equals(Status.LOADING)) {
                    Toast.makeText(getContext(), "Loading", Toast.LENGTH_SHORT).show();
                } else if (productResponse.getStatus().equals(Status.SUCCESS)){
                    // Add an alert dialog box and go back home
                    // 1. Instantiate an AlertDialog.Builder with its constructor
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    // 2. Chain together various setter methods to set the dialog characteristics
                    builder.setMessage("Your product has been added successfully")
                            .setTitle("Merci !")
                            // Add the button
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User clicked OK button
                                    // Call the BottomNavigationView.OnNavigationItemSelectedListener in the main activity
                                    ((BottomNavigationView)getActivity().findViewById(R.id.bottom_navigation)).setSelectedItemId(R.id.nav_home);
                                }
                            });

                    // 3. Get the AlertDialog from create()
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else if (productResponse.getStatus().equals(Status.ERROR)){
                    Toast.makeText(getContext(), productResponse.getError().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
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
                pictureFileUri = Camera.captureImage(getContext(), this);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        else if (v == buttonSubmit) {

            // Validate the field
            validator.validate();

            if (validated & fileToUploadUri != null) {

                // Retrieve the name of the product typed in the editText field
                productName = String.valueOf(editTextProductName.getText());
                // Retrieve the quantity of the product typed in the editText field
                quantity = String.valueOf(editTextQuantity.getText());

                fileToUpload = new File(fileToUploadPath);
                // Create RequestBody instance from file
                RequestBody requestFile = RequestBody.create(MediaType.parse(getActivity().getContentResolver().getType(fileToUploadUri)), fileToUpload);
                // MultipartBody.Part is used to send also the actual file name
                MultipartBody.Part product_picture = MultipartBody.Part.createFormData("product_picture", fileToUpload.getAbsolutePath(), requestFile);

                // HTTP Post request
                ProductForm productToPost = new ProductForm(product_picture, productName, productCategory, quantity, expiration_date);

                // Format the product to update view models
                String imageFileName = Constants.BASE_URL + "media/product/" + fileToUpload.getPath().split("/")[fileToUpload.getPath().split("/").length - 1];
                Product product = new Product(productName, Constants.AVAILABLE, imageFileName, productCategory, quantity, expiration_date);

                productViewModel.addProduct(productToPost, product);

            } else if ( fileToUploadUri == null) {
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
        // Save file uri in bundle as it will be null on screen orientation changes
        outState.putParcelable("file_uri", fileToUploadUri);
        outState.putString("file_path", fileToUploadPath);

    }

    @Override
    public void onViewStateRestored(@NonNull Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            // get the file uri
            fileToUploadUri = savedInstanceState.getParcelable("file_uri");
            fileToUploadPath = savedInstanceState.getString("file_path");
            // Reload the image view picture
            if (fileToUploadUri != null)
            { Picasso.get().load(fileToUploadUri).into(imageViewPreviewProduct); }
            else { Picasso.get().load(R.drawable.test).into(imageViewPreviewProduct); }
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
                fileToUpload = Camera.processPicture(getContext(), pictureFileUri); // modify the raw picture taken
                fileToUploadPath = fileToUpload.getAbsolutePath();
                fileToUploadUri = Camera.getOutputMediaFileUri(getContext(), fileToUpload);

                Picasso.get().load(fileToUploadUri).into(imageViewPreviewProduct);

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

}
