package com.example.cshare.Utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Camera {

    // TODO : Create a File class in Utils

    // Camera activity request codes
    public static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int CAMERA_CHOOSE_IMAGE_REQUEST_CODE = 100;

    public static Uri captureImage(Context context, Fragment fragment) throws IOException {
        // Launching camera app to capture image
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Create a file to store the picture taken
        File pictureFile = Camera.createImageFile(context);
        // Retrieve its Uri
        Uri pictureFileUri = getOutputMediaFileUri(context, pictureFile);
        // Specifying EXTRA_OUTPUT allows to go get the photo from the uri that you provided in EXTRA_OUTPUT
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, pictureFileUri);

        // Checking whether device has camera hardware or not
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            // start the image capture Intent
            fragment.startActivityForResult(takePictureIntent, Camera.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        }
       return pictureFileUri;
    }

    public static void choosePictureFromGallery(Activity activity) {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        // Create an Intent with action as ACTION_PICK
        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Sets the type as image/*. This ensures only components of type image are selected
        pickIntent.setType("image/*");

        // We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        pickIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

        // Create a chooser in case there are third parties app and launch the Intent
        activity.startActivityForResult(Intent.createChooser(pickIntent, "Select Picture"), Camera.CAMERA_CHOOSE_IMAGE_REQUEST_CODE);
    }

    // Creating file uri to store image
    public static Uri getOutputMediaFileUri(Context context, File file) throws IOException {
        return FileProvider.getUriForFile(context, context.getPackageName() + ".provider",
                file);
    }

    public static File processPicture(Context context, Uri uri) throws IOException {

        File fileToUpload = null;

        if (uri != null) {
            // Rotate if necessary and reduce size
            Bitmap bitmap = Camera.handleSamplingAndRotationBitmap(context.getContentResolver(), uri);
            // Save new picture to fileToUpload
            fileToUpload = Camera.saveBitmap(context, bitmap);

        } else {
            Toast.makeText(context,
                    "Sorry, file uri is missing!", Toast.LENGTH_LONG).show();
        }

        return fileToUpload;
    }

    public static File createImageFile(Context context) throws IOException {
        /**
         * Method that creates a file for the photo with a unique name
         * @return
         * @throws IOException
         */

        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return image;
    }

    /**
     * This method is responsible for solving the rotation issue if exist. Also scale the images to
     * 1024x1024 resolution
     *
     * @param contentResolver The current context.getContentResolver()
     * @param selectedImage The Image URI
     * @return Bitmap image results
     * @throws IOException
     */
    public static Bitmap handleSamplingAndRotationBitmap(ContentResolver contentResolver, Uri selectedImage)
            throws IOException {
        int MAX_HEIGHT = 1024;
        int MAX_WIDTH = 1024;

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        InputStream imageStream = contentResolver.openInputStream(selectedImage);
        BitmapFactory.decodeStream(imageStream, null, options);
        imageStream.close();

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, MAX_WIDTH, MAX_HEIGHT);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        imageStream = contentResolver.openInputStream(selectedImage);
        Bitmap bitmap = BitmapFactory.decodeStream(imageStream, null, options);
        bitmap = rotateImageIfRequired(contentResolver, bitmap, selectedImage);
        return bitmap;
    }

    public static File saveBitmap(Context context, Bitmap bmp) throws IOException {
        File file = createImageFile(context);
        try (FileOutputStream out = new FileOutputStream(file.getAbsolutePath())) {
            bmp.compress(Bitmap.CompressFormat.JPEG, 40, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
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
    private static Bitmap rotateImageIfRequired(ContentResolver contentResolver, Bitmap img, Uri selectedImage) throws IOException {

        InputStream input = contentResolver.openInputStream(selectedImage);
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
}
