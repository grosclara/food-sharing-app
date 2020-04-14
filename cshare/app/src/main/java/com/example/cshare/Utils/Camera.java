package com.example.cshare.Utils;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Camera {

    private Fragment fragment;

    // Path to the location of the picture taken by the phone
    public String imageFilePath;
    public String imageFileName;
    public Uri fileUri;
    // Ensures the intent to open the camera can be performed
    public static final int REQUEST_CAPTURE_IMAGE = 1;

    public static final int CAMERA_CHOOSE_IMAGE_REQUEST_CODE = 100;

    // Check whether a picture has been selected
    public boolean pictureSelected = false;

    public Camera(Fragment fragment, Boolean pictureSelected){
        this.pictureSelected = pictureSelected;
        this.fragment = fragment;
    }

    public File createImageFile() throws IOException {
        /**
         * Method that creates a file for the photo with a unique name
         * @return
         * @throws IOException
         */

        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                fragment.getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        imageFilePath = image.getAbsolutePath();
        return image;
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
        if (pictureIntent.resolveActivity(fragment.getActivity().getPackageManager()) != null) {
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
                        fragment.getContext(),
                        fragment.getActivity().getPackageName() + ".provider",
                        photoFile
                );
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        fileUri);
                fragment.startActivityForResult(pictureIntent,
                        REQUEST_CAPTURE_IMAGE);
            }
        }
    }
}
