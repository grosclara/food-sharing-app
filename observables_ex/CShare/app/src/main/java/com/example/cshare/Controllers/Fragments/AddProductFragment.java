package com.example.cshare.Controllers.Fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cshare.Models.Product;
import com.example.cshare.R;
import com.example.cshare.Utils.ApiStreams;
import com.example.cshare.Utils.Camera;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Future;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

import static android.R.*;

public class AddProductFragment extends BaseFragment implements Validator.ValidationListener {

    // FOR DESIGN
    // Declare form fields
    @BindView(R.id.fragment_add_product_detail)
    TextView textViewFormDetails;

    @BindView(R.id.fragment_add_product_name)
    @NotEmpty
    EditText editTextProductName;

    @BindView(R.id.fragment_add_product_category)
    Spinner spinnerCategory;

    @BindView(R.id.fragment_add_product_expiration_date)
    @NotEmpty
    @Future
    EditText editTextExpirationDate;

    @BindView(R.id.fragment_add_product_quantity)
    @NotEmpty
    EditText editTextQuantity;

    @BindView(R.id.fragment_add_product_picture)
    ImageView imageViewProduct;
    @BindView(R.id.fragment_add_product_picture_error)
    TextView textViewPictureError;

    // FOR DATA

    // Declare Callback
    private OnButtonClickedListener buttonClickedCallback;

    //HTTP requests
    private Disposable disposable;

    // Declare product(Product)
    private Product newProduct;
    private String newName;
    private String newCategory;
    private String newExpirationDate;
    private String newQuantity;
    private String[] productCategoriesArray;

    // Date picker
    private DatePickerDialog.OnDateSetListener dateSetListener;

    // Handle pictures
    Camera camera = new Camera(this, false);
    /*// Path to the location of the picture taken by the phone
    private String imageFilePath;
    private Uri fileUri;
    // Ensures the intent to open the camera can be performed
    private static final int REQUEST_CAPTURE_IMAGE = 1;
    // Check whether a picture has been selected
    private boolean pictureSelected = false;*/

    // Form validation
    private Validator validator;
    private Boolean isValid;

    // --------------
    // BASE METHODS
    // --------------

    @Override
    protected BaseFragment newInstance() {
        return (new AddProductFragment());
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_add_product;
    }

    // We call here the callback creation method from onAttach(), because it is only then that
    // we know for sure that our fragment is well attached to its parent activity
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Call the method that creating callback after being attached to parent activity
        this.createCallbackToParentActivity();
    }


    @Override
    protected void configureDesign() {
        configureButtons();
        configureSpinner();
        configureDateField();
        configureValidator();
    }

    @Override
    protected void updateDesign() {
    }

    // Declare our interface that will be implemented by any container activity
    // implement the Callback that will allow us to communicate with our parent activity
    public interface OnButtonClickedListener {
        public void onButtonClicked(View view);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data) {
        /**
         * Return of the camera call (startActivityForResult)
         * Get the picture and load it into the imageView to give the user a preview of the picture he took
         * @param //requestCode
         * @param //resultCode
         * @param //data
         */
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == camera.REQUEST_CAPTURE_IMAGE) {
            // Handle the case where the user cancelled the camera intent without taking a picture like,
            // though we have the imagePath, but it’s not a valid image because the user has not taken the picture.
            if (resultCode == Activity.RESULT_OK) {

                camera.pictureSelected = true;
                textViewPictureError.setVisibility(View.GONE);

                // Load with the imageFilePath we obtained before opening the cameraIntent
                Bitmap imageBitmap = BitmapFactory.decodeFile(camera.imageFilePath);
                imageViewProduct.setImageBitmap(imageBitmap);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // User Cancelled the action
            }
        }
    }

    // -----------------
    // CONFIGURATION
    // -----------------

    private void configureSpinner() {
        productCategoriesArray = getResources().getStringArray(R.array.product_categories_array);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(getContext(), layout.simple_spinner_dropdown_item, productCategoriesArray);
        // Apply the adapter to the spinner
        spinnerCategory.setAdapter(adapterSpinner);
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // An item was selected. You can retrieve the selected item using
                newCategory = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void configureButtons() {
        //Set onClickListener to buttons
        /*buttonTakePhoto.setOnClickListener(this);
        fabSubmit.setOnClickListener(this);*/
    }

    private void configureDateField() {
        editTextExpirationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                                newExpirationDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
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
    }

    private void configureValidator() {
        // Instantiate a new Validator
        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    // ------------------------------
    //  HTTP REQUEST (RxJAVA)
    // ------------------------------

    private void HttpRequestAddProduct() {

        newProduct = new Product(newName, newCategory, newQuantity, newExpirationDate, 3);
        // this.updateUIWhenStartingHTTPRequest();
        this.disposable = ApiStreams.streamPostProductFollowing(
                getContext(),
                "Token c5ebcab735b5c52ce2d01649fcfe8172c86b32c4",
                camera.imageFilePath,
                camera.fileUri,
                newProduct
        )
                .subscribeWith(new DisposableObserver<Product>() {
                    @Override
                    public void onNext(Product product) {
                        Log.e("TAG", "On Next");
                        Toast.makeText(getContext(), "You just added a product",
                                Toast.LENGTH_SHORT).show();

                        //updateUIWithUserInfo(users);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG", "On Error" + Log.getStackTraceString(e));
                    }

                    @Override
                    public void onComplete() {
                        Log.e("TAG", "On Complete !!");
                    }
                });
    }

    // --------------
    // UPDATE UI
    // --------------

    // --------------
    // ACTIONS
    // --------------

    // Here we will propagate our user's click directly to our parent activity via the
    // onButtonClicked(View) method.
    @OnClick(R.id.fragment_add_product_fab)
    protected void addProduct(View v) {

        // Validate the form
        validator.validate();

        if(isValid) {

            // Spread the click to the parent activity
            buttonClickedCallback.onButtonClicked(v);
        }

    }

    @OnClick(R.id.fragment_add_product_take_photo)
    protected void takeProductPicture(){

        try {
            camera.createImageFile();
        } catch (IOException e) {
            Log.e("TAG", e.getLocalizedMessage());
        }
        camera.openCameraIntent();
    }
    // --------------
    // FRAGMENT SUPPORT
    // --------------

    // Create callback to parent activity
    // Link our Callback with our parent business by subscribing to it from the child fragment.
    // However, our parent activity (which contains this fragment) will have to implement the
    // OnButtonClickedListener interface.
    protected void createCallbackToParentActivity() {
        try {
            //Parent activity will automatically subscribe to callback
            buttonClickedCallback = (OnButtonClickedListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString() + " must implement OnButtonClickedListener");
        }
    }

    // --------------
    // CALLBACKS
    // --------------

    @Override
    public void onValidationSucceeded() {
        //Called when all your views pass all validations

        // Retrieve the name and quantity of the product typed in the editText fields
        newName = String.valueOf(editTextProductName.getText());
        newQuantity = String.valueOf(editTextQuantity.getText());

        if (camera.pictureSelected) {
            // If a picture has been taken by the user, create a new Object Product product and
            // call for the HttpRequestAddProduct method.

            isValid = true;

            // Call the stream
            this.HttpRequestAddProduct();

        } else {
            Toast.makeText(getContext(), "You must choose a product picture", Toast.LENGTH_SHORT).show();
            textViewPictureError.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        //Called when there are validation error(s)
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

        if(!camera.pictureSelected) {
            Toast.makeText(getContext(), "You must choose a product picture", Toast.LENGTH_SHORT).show();
            textViewPictureError.setVisibility(View.VISIBLE);
        }
    }

}