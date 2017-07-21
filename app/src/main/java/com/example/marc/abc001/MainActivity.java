package com.example.marc.abc001;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.R.attr.id;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

@SuppressWarnings("deprecation")

public class MainActivity extends AppCompatActivity {
    private Camera mCamera;
    private CameraPreview mPreview;
    String TAG = "marc123";
    Button captureButton;
    int MY_PERMISSIONS_REQUEST_CAMERA = 99;
    int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 99;
    Boolean permission = false;
    static File imgFile = null;
    static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boolean aaa = checkCameraHardware(this);
        // Create an instance of Camera
        mCamera = getCameraInstance();
        captureButton = (Button) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Thread(new Runnable() {
                            public void run() {
                                Log.d(TAG, "0000 thread   button clickListener 1");
                                mCamera.takePicture(null, null, mPicture);
                                Log.d(TAG, "0000 thread  button clickListener 2");
                            }
                        }).start();
                    }
                }
        );
        // Create our Preview view and set it as the content of our activity.
        Log.d(TAG, "0000   main 001_____!!!");
        CameraPreview mPreview = new CameraPreview(this, mCamera);
        Log.d(TAG, "0000   main 002");
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        Log.d(TAG, "0000   main 003");
        preview.addView(mPreview);
        Log.d(TAG, "0000   main 004");
        captureButton.post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "0000___   button post 1");
                captureButton.performClick();
                Log.d(TAG, "0000___   button post 2");
                captureButton.postDelayed(this, 60000);
            }
        });
        Log.d(TAG,"0000   main 007 done");
    }

    private Camera.PictureCallback mPicture = new android.hardware.Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Log.d(TAG, "picture taken");
            captureButton.setText("def");
            mCamera.stopPreview();
            mCamera.startPreview();
            //mCamera = getCameraInstance();

            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null) {
                Log.d(TAG, "Error creating media file, check storage permissions: "); // + e.getMessage());
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {
                Log.d(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, "Error accessing file: " + e.getMessage());
            }
        }
    };

    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            Log.d(TAG, "__________ device has a camera");
            return true;
        } else {
            Log.d(TAG, "__________ device does not have a camera");
            // no camera on this device
            return false;
        }
    }

    public Camera getCameraInstance() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            permission = checkCameraPermission();
        Camera c = null;
        try {
            c.release();
            Log.d(TAG, "__________ camera release okay (from getCameraInstance)");
        } catch (Exception e) {
            Log.d(TAG, "__________ camera release exception");
        }
        try {
            if (c != null) {
                Log.d(TAG, "__________ camera not null");
                c.release();
                Log.d(TAG, "__________ camera released");
            }
            c = Camera.open(); // attempt to get a Camera instance
            Log.d(TAG, "__________ camera open okay");
        } catch (Exception e) {
            Log.d(TAG, "__________ camera open exception");
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    public boolean checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
            }
            return false;
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
            return false;
        }
        return true;
    }

    public File getOutputMediaFile(int type) {

        Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        if (!isSDPresent) {
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, "card not mounted", duration);
            toast.show();

            Log.d("ERROR", "Card not mounted");
        }
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getPath() + "/cats006/");
        Log.d(TAG, "path directory is: " + Environment.getExternalStorageDirectory().getPath());

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {

                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
            imgFile = mediaFile;
        } else {
            return null;
        }

        return mediaFile;
    }
}
