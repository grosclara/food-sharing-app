package com.example.frontend.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.frontend.R;
import com.example.frontend.api.DjangoRestApi;
import com.example.frontend.api.NetworkClient;
import com.example.frontend.model.Product;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.net.URL;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

// PHOTOS PB

/**
 * AddActivity Class.
 * Give the possibility to the user to add a product in the database.
 * The form to fill the product includes EditTexts.
 * Allows to take a picture of the product by opening the camera clicking on the buttonPicture.
 * After having added the product, the user is redirected to the MainActivity.
 * @author Clara Gros, Babacar Toure
 * @version 1.0
 */

public class AddActivity extends AppCompatActivity {

    private EditText editTextProductName;
    private ImageView imageViewPreviewProduct;

    private Product product;
    private String productName;
    private int supplierId;
    private boolean is_available;

    // Path to the location of the picture taken by the phone
    private String imageFilePath;
    private Uri fileUri;

    // Ensures the intent to open the camera can be performed
    private static final int REQUEST_CAPTURE_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
    }

    /**
     * Get the product information from the editTextViews to create a Product object.
     * Call the addProduct(Product) method.
     * Eventually redirect to the MainActivity when clicking the buttonSubmit.
     * @param view buttonSubmit
     * @see #addProduct(Product product)
     */
    public void fromAddToMainActivity(View view) {

        // Retrieve the name of the product typed in the editText field
        editTextProductName = findViewById(R.id.editTextProductName);
        productName = String.valueOf(editTextProductName.getText());
        supplierId = 1; //default value before having set the log in module
        is_available = true; // By default, when creating a product, this attribute must equals true

        // Creation of a new product with its attribute
        // While the login module isn't set, we provide a default supplier id
        product = new Product(productName, MainActivity.userId);

        // Call for the addProduct(Product) method to transfer data to the server
        addProduct(product);

        // Go back to the mainActivity
        Intent toMainActivityIntent = new Intent();
        toMainActivityIntent.setClass(getApplicationContext(), MainActivity.class);
        startActivity(toMainActivityIntent);
        finish(); // Disable the "going back functionality" from the MainActivity to the AddActivity
    }

    public void addProduct(Product product) {
        /**
         * Send a HTTP request to post the Product product taken in param
         * @param product
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
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), productName);


        // Asynchronous request
        Call<Product> call = djangoRestApi.addProduct(body, name, supplierId, is_available);
        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                Log.d("serverRequest",response.message()+' '+String.valueOf(response.code()));
                if (response.isSuccessful()) {
                    // In case of success, toast "Submit!"
                    Toast.makeText(getApplicationContext(), "Submit!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "An error occurred!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Log.d("serverRequest",t.getLocalizedMessage());
            }
        });
    }

    public void openCameraIntent(View view) {
        /**
         * Creation of an Intent of type ACTION_IMAGE_CAPTURE to open the camera.
         * The picture taken is then loaded in a temporary file from which we save its absolute path in the picturePath variable
         * We create a URI (Uniform Resource Identifier) for this file.
         * Eventually the intent call for the onActivityResult method.
         * @param view ButtonGallery to pick a picture
         * @see #onActivityResult(int, int, Intent)
         * @see #createImageFile()
         */
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if(pictureIntent.resolveActivity(getPackageManager()) != null){
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
                        AddActivity.this.getApplicationContext().getPackageName()+".provider",
                        photoFile
                );
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        fileUri);
                startActivityForResult(pictureIntent,
                        REQUEST_CAPTURE_IMAGE);
            }
        }
    }

    /**
     * Method that creates a file for the photo with a unique name
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {
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

    /**
     * Return of the camera call (startActivityForResult)
     * Get the picture and load it into the imageView to give the user a preview of the picture he took
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("file","Request code: "+String.valueOf(requestCode));
        Log.d("file","Result code: "+String.valueOf(resultCode));
        if (requestCode == REQUEST_CAPTURE_IMAGE) {
            // Handle the case where the user cancelled the camera intent without taking a picture like,
            // though we have the imagePath, but it’s not a valid image because the user has not taken the picture.
            if (resultCode == Activity.RESULT_OK) {
                // Load with the imageFilePath we obtained before opening the cameraIntent
                imageViewPreviewProduct = findViewById(R.id.imageViewPreviewProduct);
                Bitmap imageBitmap = BitmapFactory.decodeFile(imageFilePath);
                imageViewPreviewProduct.setImageBitmap(imageBitmap);
            }
            else if(resultCode == Activity.RESULT_CANCELED) {
                // User Cancelled the action
            }
        }
    }
}