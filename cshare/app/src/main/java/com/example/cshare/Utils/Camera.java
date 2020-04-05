package com.example.cshare.Utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.Fragment;

import com.example.cshare.Controllers.Fragments.AddFragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Camera {

    private Fragment fragment;

    // Path to the location of the picture taken by the phone
    public String imageFilePath;
    public String imageFileName;
    public Uri fileUri;

    private static final String TEMP_IMAGE_NAME = "temp";

    // Ensures the intent to open the camera can be performed
    public static final int REQUEST_CAPTURE_IMAGE = 1;
    // Check whether a picture has been selected
    public boolean pictureSelected = false;

    public Camera(Fragment fragment, Boolean pictureSelected) {
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
         * The picture taken is then loaded in a temporary file from which we save its absolute
         * path in the picturePath variable
         * We create a URI (Uniform Resource Identifier) for this file.
         * Eventually the intent call for the onActivityResult method.
         * @see #onActivityResult(int, int, Intent)
         * @see #createImageFile()
         */

        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        pictureIntent.putExtra("return-data", true);
        Uri uri = FileProvider.getUriForFile(fragment.getContext(),fragment.getContext().getPackageName() + ".provider",
                getTempFile(fragment.getContext()));
        pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        if (pictureIntent.resolveActivity(fragment.getActivity().getPackageManager()) != null) {
            /*//Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
                Log.d("file", "File was successfully created");
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.d("file", "An error occurred while creating the file");
            }
            if (photoFile != null) {
                fragment.startActivityForResult(pictureIntent, REQUEST_CAPTURE_IMAGE);
                fileUri = FileProvider.getUriForFile(
                        fragment.getContext(),
                        fragment.getActivity().getPackageName() + ".provider",
                        photoFile
                );
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        fileUri); // Set the image file name
                Log.d(Constants.TAG, pictureIntent.getExtras().toString());
                // Rotate image blablabla*/
            fragment.startActivityForResult(pictureIntent,
                    REQUEST_CAPTURE_IMAGE);
        }
    }

    private static File getTempFile(Context context) {
        File imageFile = new File(context.getExternalCacheDir(), TEMP_IMAGE_NAME);
        imageFile.getParentFile().mkdirs();
        return imageFile;
    }

    /**
     * This method is responsible for solving the rotation issue if exist. Also scale the images to
     * 1024x1024 resolution
     *
     * @param context       The current context
     * @param selectedImage The Image URI
     * @return Bitmap image results
     * @throws IOException
     */
    public static Bitmap handleSamplingAndRotationBitmap(Context context, Uri selectedImage)
            throws IOException {
        int MAX_HEIGHT = 1024;
        int MAX_WIDTH = 1024;

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        InputStream imageStream = context.getContentResolver().openInputStream(selectedImage);
        BitmapFactory.decodeStream(imageStream, null, options);
        imageStream.close();

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, MAX_WIDTH, MAX_HEIGHT);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        imageStream = context.getContentResolver().openInputStream(selectedImage);
        Bitmap img = BitmapFactory.decodeStream(imageStream, null, options);

        img = rotateImageIfRequired(context, img, selectedImage);
        return img;
    }

    /**
     * Calculate an inSampleSize for use in a {@link BitmapFactory.Options} object when decoding
     * bitmaps using the decode* methods from {@link BitmapFactory}. This implementation calculates
     * the closest inSampleSize that will result in the final decoded bitmap having a width and
     * height equal to or larger than the requested width and height. This implementation does not
     * ensure a power of 2 is returned for inSampleSize which can be faster when decoding but
     * results in a larger bitmap which isn't as useful for caching purposes.
     *
     * @param options   An options object with out* params already populated (run through a decode*
     *                  method with inJustDecodeBounds==true
     * @param reqWidth  The requested width of the resulting bitmap
     * @param reqHeight The requested height of the resulting bitmap
     * @return The value to be used for inSampleSize
     */
    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee a final image
            // with both dimensions larger than or equal to the requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger inSampleSize).

            final float totalPixels = width * height;

            // Anything more than 2x the requested pixels we'll sample down further
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }

    /**
     * Rotate an image if required.
     *
     * @param img           The image bitmap
     * @param selectedImage Image URI
     * @return The resulted Bitmap after manipulation
     */
    private static Bitmap rotateImageIfRequired(Context context, Bitmap img, Uri selectedImage) throws IOException {

        InputStream input = context.getContentResolver().openInputStream(selectedImage);
        ExifInterface ei;
        if (Build.VERSION.SDK_INT > 23)
            ei = new ExifInterface(input);
        else
            ei = new ExifInterface(selectedImage.getPath());

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img,
                0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    public String BitMapToString(Bitmap userImage1) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        userImage1.compress(Bitmap.CompressFormat.PNG, 60, baos);
        byte[] b = baos.toByteArray();
        String Document_img1 = Base64.encodeToString(b, Base64.DEFAULT);
        return Document_img1;
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public static Bitmap getImageFromResult(Context context, int resultCode,
                                            Intent imageReturnedIntent) throws IOException {
        Log.d(Constants.TAG, "getImageFromResult, resultCode: " + resultCode);
        Bitmap bm = null;
        File imageFile = getTempFile(context);
        Uri selectedImage = null;
        boolean isCamera = imageReturnedIntent == null;
        if (isCamera) {
            selectedImage = FileProvider.getUriForFile(context, context.getPackageName() + ".provider",
                    imageFile);
        } else {
            if (imageReturnedIntent.getData() == null) {
                selectedImage = Uri.fromFile(imageFile);
            } else {
                selectedImage = imageReturnedIntent.getData();
            }
        }
        Log.d(Constants.TAG, "selectedImage: " + selectedImage);

        bm = handleSamplingAndRotationBitmap(context, selectedImage);

        return bm;
    }

}
