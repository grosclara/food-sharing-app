package com.example.cshare.ui.views;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.cshare.R;
import com.example.cshare.data.apiresponses.ProductResponse;
import com.example.cshare.data.apiresponses.Status;
import com.example.cshare.data.models.Product;
import com.example.cshare.data.models.User;
import com.example.cshare.ui.viewmodels.ProductViewModel;
import com.example.cshare.utils.Constants;
import com.example.cshare.utils.MediaFiles;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Activity responsible for the addition of a product by the user.
 * <p>
 * Once the user fills out the form, a validator checks the validity of the fields.
 * If the form is valid, the product is added using the ProductViewModel's addProduct method and
 * then an Intent is used to access the HomeScreenActivity again.
 *
 * @author Clara Gros
 * @author Babacar Toure
 * @see ProductViewModel
 * @see Product
 * @see User
 * @see AppCompatActivity
 * @see com.mobsandgeeks.saripaar.Validator.ValidationListener
 * @since 1.0
 */
public class AddActivity extends AppCompatActivity implements View.OnClickListener,
        Validator.ValidationListener {

    // Form validation
    /**
     * Asynchronous validations, provided by the Saripaar library.
     */
    protected Validator validator;
    private boolean validated;

    // Views
    private TextView textViewPictureError;
    @NotEmpty
    private EditText editTextProductName;
    private ImageView imageViewPreviewProduct;
    @NotEmpty
    private TextView textViewExpirationDate;
    @NotEmpty
    private EditText editTextQuantity;
    private MaterialSpinner spinnerProductCategories;
    private Button buttonPhoto;
    private Button buttonSubmit;
    private Button buttonExpirationDate;

    // Form fields
    private String productName;
    private String productCategory;
    private String expiration_date;
    private String quantity;
    private User supplier;

    // Product to add
    private Product product;

    // Path to the location of the picture taken by the phone
    private Uri pictureFileUri;
    private File fileToUpload;
    private Uri fileToUploadUri;
    private String fileToUploadPath;

    // ViewModels
    ProductViewModel productViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        getSupportActionBar().setTitle("Share your product");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        configureDesign();

        // Validator
        configureValidator();

        // VM business logic
        configureViewModel();
        observeDataChanges();
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

        Picasso.get().load(R.drawable.default_product_picture).into(imageViewPreviewProduct);


        // Click listeners
        buttonPhoto.setOnClickListener(this);
        buttonSubmit.setOnClickListener(this);
        buttonExpirationDate.setOnClickListener(this);
        imageViewPreviewProduct.setOnClickListener(this);

        // Product spinner
        configureProductSpinner();
    }

    /**
     * Configures ViewModels with default ViewModelProvider
     *
     * @see androidx.lifecycle.ViewModelProvider
     */
    protected void configureViewModel() {
        productViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(ProductViewModel.class);
    }

    /**
     * Calls the public methods of our ViewModel to observe their results.
     * <p>
     * For the Get methods, we used the observe() method to be automatically alerted if the
     * database result changes.
     */
    private void observeDataChanges() {
        getAddProductResponse();
    }

    /**
     * Observe the add response data from the productViewModel.
     * <p>
     * After a request to add a product, the response status changes to success or failure.
     * In case of a successful response, calls the onProductAdded method in the HomeScreenActivity
     * to update data that has changed following the addition of the product and creates an Intent
     * to go back to the HomeScreenActivity. In case of failure,
     * toasts an error message.
     * After having done so, Set the status of the response to Complete to indicate the event has
     * been handled.
     *
     * @see ProductViewModel#getAddProductResponse() ()
     * @see ProductResponse
     * @see HomeScreenActivity
     * @see HomeScreenActivity#onProductAdded()
     */
    private void getAddProductResponse() {
        productViewModel.getAddProductResponse().observe(this, new Observer<ProductResponse>() {
            @Override
            public void onChanged(ProductResponse productResponse) {
                if (productResponse.getStatus().equals(Status.SUCCESS)) {
                    Toast.makeText(getApplicationContext(), R.string.addition_successful, Toast.LENGTH_SHORT).show();
                    // Call the main activity on product added method to refresh product lists
                    HomeScreenActivity.onProductAdded();
                    productViewModel.getAddProductResponse().setValue(ProductResponse.complete());
                    // Intent
                    Intent intent = new Intent(getApplicationContext(), HomeScreenActivity.class);
                    startActivity(intent);
                    finish();

                } else if (productResponse.getStatus().equals(Status.ERROR)) {
                    if (productResponse.getError().getDetail() != null) {
                        Toast.makeText(getApplicationContext(), productResponse.getError().getDetail(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.unexpected_error, Toast.LENGTH_SHORT).show();
                    }
                    productViewModel.getAddProductResponse().setValue(ProductResponse.complete());
                }
            }
        });
    }

    /**
     * Instantiate a new Validator
     *
     * @see Validator
     */
    private void configureValidator() {
        validator = new Validator((this));
        validator.setValidationListener(this);
    }

    /**
     * Configure the spinner that contains every category.
     * <p>
     * Creates an ArrayAdapter using a defined category string array and a default spinner layout
     * and enables to retrieve the item when selected
     *
     * @see Spinner
     * @see ArrayAdapter
     * @see Spinner#setOnItemClickListener(AdapterView.OnItemClickListener)
     */
    private void configureProductSpinner() {
        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(this, R.array.product_categories_array, R.layout.spinner_item);
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

    /**
     * When called, this method creates and shows a datePickerDialog and enables to retrieve the
     * selected date and set it to the corresponding text view
     *
     * @see DatePicker
     */
    private void configureDatePicker() {

        // Get Current Date
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                android.R.style.Theme_DeviceDefault_Light_Dialog,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        int realMonth = monthOfYear + 1;
                        String strMonth = "";
                        if (realMonth < 10) {
                            strMonth = "0" + realMonth;
                        } else {
                            strMonth = String.valueOf(realMonth);
                        }
                        String strDay = "";
                        if (dayOfMonth < 10) {
                            strDay = "0" + dayOfMonth;
                        } else {
                            strDay = String.valueOf(dayOfMonth);
                        }
                        expiration_date = year + "-" + strMonth + "-" + strDay;
                        textViewExpirationDate.setText(expiration_date);
                    }
                },
                year, month, day);
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.show();
    }

    /**
     * Called when the form has passed all the validations and set the boolean validated to true.
     *
     * @see Validator
     */
    @Override
    public void onValidationSucceeded() {
        validated = true;
    }

    /**
     * Called when the form hasn't passed all the validations : set the boolean validated to false
     * and display the errors.
     *
     * @see Validator
     */
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
                Toast.makeText(this, R.string.expiration_date_required, Toast.LENGTH_LONG).show();
            } else if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Build and show an AlertDialog to select the method from the MediaFiles class to apply :
     * either choosePictureFromGallery or captureImage
     *
     * @param activity (Activity) the current activity
     * @see AlertDialog
     * @see MediaFiles#captureImage(Activity, Fragment)
     * @see MediaFiles#choosePictureFromGallery(Activity, Fragment)
     */
    private void showPictureDialog(Activity activity) {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(activity);
        pictureDialog.setTitle(R.string.select_action);
        String[] pictureDialogItems = {
                getString(R.string.gallery),
                getString(R.string.camera)};
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
            submitProduct();
        } else if (v == buttonExpirationDate) {
            configureDatePicker();
        }
    }

    /**
     * Check if the form and valid and if there is a product photo. If yes, the method creates
     * the product object to share and calls the addProduct method of the productViewModel.
     *
     * @see Validator#validate()
     * @see Product
     * @see ProductViewModel#addProduct(Product)
     */
    private void submitProduct() {
        // Validate the field
        validator.validate();
        if (validated & fileToUploadUri != null) {
            // Retrieve the name of the product typed in the editText field
            productName = String.valueOf(editTextProductName.getText());
            // Retrieve the quantity of the product typed in the editText field
            quantity = String.valueOf(editTextQuantity.getText());
            supplier = HomeScreenActivity.profileViewModel.getUserProfileMutableLiveData().getValue()
                    .getUser();

            fileToUpload = new File(fileToUploadPath);
            // Create RequestBody instance from file
            RequestBody requestFile = RequestBody.create(
                    MediaType.parse(getContentResolver().getType(fileToUploadUri)),
                    fileToUpload);
            // MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part productPictureBody = MultipartBody.Part.createFormData(
                    "product_picture",
                    fileToUpload.getAbsolutePath(),
                    requestFile);
            // Format the product to update view models
            String imageFileName = Constants.SERVER_URL +
                    "media/product/" +
                    fileToUpload.getPath()
                            .split("/")[fileToUpload.getPath().split("/").length - 1];

            // HTTP Post request
            product = new Product(productPictureBody,
                    productName,
                    productCategory,
                    quantity,
                    expiration_date,
                    supplier.getId(),
                    imageFileName,
                    supplier.getCampus(),
                    supplier.getRoomNumber()
            );
            productViewModel.addProduct(product);
        } else if (fileToUploadUri == null) {
            textViewPictureError.setVisibility(View.VISIBLE);
        }
    }

    /*
      Here we store the file uri as it will be null after returning from camera app
    */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save file uri in bundle as it will be null on screen orientation changes
        outState.putParcelable(MediaFiles.URI, fileToUploadUri);
        outState.putString(MediaFiles.PATH, fileToUploadPath);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            // get the file uri
            fileToUploadUri = savedInstanceState.getParcelable(MediaFiles.URI);
            fileToUploadPath = savedInstanceState.getString(MediaFiles.PATH);
            // Reload the image view picture
            if (fileToUploadUri != null) {
                Picasso.get().load(fileToUploadUri).into(imageViewPreviewProduct);
            } else {
                Picasso.get().load(R.drawable.default_product_picture).into(imageViewPreviewProduct);
            }
        }
    }

    /**
     * Receiving activity result method will be called after closing the gallery or the camera.
     * Modify the raw picture, store it in a new file and retrieve its Uri
     *
     * @see MediaFiles
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
                fileToUpload = MediaFiles.processPicture(this, pictureFileUri); // modify the raw picture taken
                fileToUploadPath = fileToUpload.getAbsolutePath();
                fileToUploadUri = MediaFiles.getOutputMediaFileUri(this, fileToUpload);

                Picasso.get().load(fileToUploadUri).into(imageViewPreviewProduct);

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (resultCode == RESULT_CANCELED) {// user cancelled Image capture
        } else {
            // failed to capture image
            Toast.makeText(this,
                    R.string.image_choice_failed, Toast.LENGTH_SHORT)
                    .show();
        }
    }

}
