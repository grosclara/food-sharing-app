package com.example.cshare.utils;

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

import com.example.cshare.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Utility public class that allows the management of multimedia files and images.
 * <p>
 * Provides methods for retrieving images from the camera or from the gallery and then proposes a
 * treatment of these images (in terms of rotation and compression).
 * The class also contains methods needed to create and retrieve files to store these images.
 *
 * @see com.example.cshare.ui.views.auth.RegisterActivity
 * @see com.example.cshare.ui.views.ProfileFragment
 * @see com.example.cshare.ui.views.AddActivity
 * @since 1.0
 * @author Clara Gros
 * @author Babacar Toure
 */
public class MediaFiles {

    /**
     * Capture image request code
     */
    public static final int CAPTURE_IMAGE_REQUEST_CODE = 100;
    /**
     * Select image from gallery applications request code
     */
    public static final int CHOOSE_IMAGE_REQUEST_CODE = 200;

    /**
     * Launches the camera application to capture an image, creates a file to store the picture and
     * returns its URI (Unique Resource Identifier). Call the startActivityForResult method in the
     * activity/fragment where the method is called.
     * <p>
     * In case of a failed or interrupted I/O operation, throws a {@link IOException}
     * <p>
     * The method parameters are used to inform the context in which the method is called and to
     * call the startActivityForResult method.
     * If the method is called inside an activity, then the fragment parameter will be null
     * and vice versa.
     *
     * @param activity (Activity) null if the method is called within a fragment
     * @param fragment (Fragment) null if the method is called within an activity
     * @return (Uri) Picture file uri
     * @throws IOException
     * @see #getOutputMediaFileUri(Context, File)
     * @see #createImageFile(Context)
     */
    public static Uri captureImage(Activity activity, Fragment fragment) throws IOException {
        // Launching camera app to capture image
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        
        Uri pictureFileUri = null;

        if (activity != null) {
            // Create a file to store the picture taken
            File pictureFile = createImageFile(activity);
            // Retrieve its Uri
            pictureFileUri = getOutputMediaFileUri(activity, pictureFile);
            // Specifying EXTRA_OUTPUT allows to go get the photo from the uri that you provided
            // in EXTRA_OUTPUT
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, pictureFileUri);
            // Checking whether device has camera hardware or not
            if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
                // Start the image capture intent
                activity.startActivityForResult(takePictureIntent, MediaFiles.CAPTURE_IMAGE_REQUEST_CODE);
            }
        } else if (fragment != null){
            File pictureFile = createImageFile(fragment.getContext());
            pictureFileUri = getOutputMediaFileUri(fragment.getContext(), pictureFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, pictureFileUri);
            if (takePictureIntent.resolveActivity(fragment.getActivity().getPackageManager()) != null) {
                fragment.startActivityForResult(takePictureIntent, MediaFiles.CAPTURE_IMAGE_REQUEST_CODE);
            }
        }
       return pictureFileUri;
    }

    /**
     * Creates an chooser Intent to retrieve an image (.png or .jpeg) from a gallery application,
     * the Intent will store the fileUri in the data attribute and call for the
     * startActivityForResult method of the view within the method is called.
     * <p>
     * The method parameters are used to inform the context in which the method is called and to
     * call the startActivityForResult method.
     * If the method is called inside an activity, then the fragment parameter will be null and
     * vice versa.
     *
     * @param activity (Activity) null if the method is called within a fragment
     * @param fragment (Fragment) null if the method is called within an activity
     */
    public static void choosePictureFromGallery(Activity activity, Fragment fragment) {

        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        // Create an Intent with action as ACTION_PICK
        Intent pickIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Sets the type as image/*. This ensures only components of type image are selected
        pickIntent.setType("image/*");

        // We pass an extra array with the accepted mime types.
        // This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        pickIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

        // Create a chooser in case there are third parties app and launch the Intent
        if (activity != null){
            Log.d(Constants.TAG, "choosePictureFromGallery");
            activity.startActivityForResult(Intent.createChooser(pickIntent,
                    activity.getString(R.string.select_picture)),
                    MediaFiles.CHOOSE_IMAGE_REQUEST_CODE);
        } else if (fragment != null) {
            fragment.startActivityForResult(Intent.createChooser(pickIntent,
                    fragment.getString(R.string.select_picture)),
                    MediaFiles.CHOOSE_IMAGE_REQUEST_CODE);
        }
    }

    /**
     * Takes the uri of the picture to process and then returns the processed picture
     * <p>
     * In case of a failed or interrupted I/O operation, throws a {@link IOException}
     *
     * @param context (Context) Current context
     * @param uri (Uri) File uri to process
     * @return
     * @throws IOException
     * @see #handleSamplingAndRotationBitmap(ContentResolver, Uri)
     * @see #saveBitmap(Context, Bitmap)
     */
    public static File processPicture(Context context, Uri uri) throws IOException {

        File fileToUpload = null;

        if (uri != null) {
            // Rotate if necessary and reduce size
            Bitmap bitmap = MediaFiles.handleSamplingAndRotationBitmap(context.getContentResolver(), uri);
            // Save new picture to fileToUpload
            fileToUpload = MediaFiles.saveBitmap(context, bitmap);

        } else {
            Toast.makeText(context, R.string.file_uri_missing, Toast.LENGTH_LONG).show();
        }

        return fileToUpload;
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
     * @param img The image bitmap
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

    /**
     * Rotates an image from the angle passed in parameter and returns the rotated image
     *
     * @param img (Bitmap) The image to be rotated
     * @param degree (int) Rotation angle
     * @return (Bitmap) The rotated bitmap
     */
    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img,
                0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    /**
     * To securely offer a file from this app to another app, you need to configure your app to
     * offer a secure handle to the file, in the form of a content URI. The Android
     * {@link FileProvider} component generates content URIs for files,
     * based on specifications you provide in XML.
     *
     * @param context
     * @param file
     * @return
     * @see FileProvider
     */
    public static Uri getOutputMediaFileUri(Context context, File file) {
        return FileProvider.getUriForFile(context, context.getPackageName() + ".provider",
                file);
    }

    /**
     * Method that creates a file with a unique filename using a timeStamp.
     * <p>
     * In case of a failed or interrupted I/O operation, throws a {@link IOException}
     *
     * @return (File) the created file
     * @throws IOException
     * @see File#createTempFile(String, String, File)
     */
    public static File createImageFile(Context context) throws IOException {
        // Create a time stamp
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        // Retrieve the directory where to store the file
        File storageDir =
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        // Create temporary file
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",   /* suffix */
                storageDir      /* directory */
        );
        return image;
    }

    /**
     * Creates a file and store the image passed as a parameter in it.
     * <p>
     * In case of a failed or interrupted I/O operation, throws a {@link IOException}
     *
     * @param context (Context) The current context
     * @param bmp (Bitmap) The image to store
     * @return (File) The created file in which is stored the image
     * @throws IOException
     * @see #createImageFile(Context)
     */
    public static File saveBitmap(Context context, Bitmap bmp) throws IOException {
        File file = MediaFiles.createImageFile(context);
        try (FileOutputStream out = new FileOutputStream(file.getAbsolutePath())) {
            // PNG is a lossless format, the compression factor (100) is ignored
            bmp.compress(Bitmap.CompressFormat.JPEG, 40, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

}
