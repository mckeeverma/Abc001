package com.example.marc.abc001;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
@SuppressWarnings("deprecation")
public class MainActivity extends AppCompatActivity {
    private Camera mCamera;
    private CameraPreview mPreview;
    String TAG = "marclog_MainActivity";
    Button captureButton;
    int MY_PERMISSIONS_REQUEST_CAMERA = 99;
    int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 99;
    Boolean permission = false;
    static File imgFile = null;
    static Context context;
    int iii = 0;
    int jjj = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "at the start of onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        captureButton = (Button) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCamera.takePicture(null, null, mPicture);
                    }
                }
        );
        mCamera = getCameraInstance();
        CameraPreview mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        captureButton.post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "captureButton.post is here_____");
                iii++;
                if (iii == 1) {
                    captureButton.performClick();
                }
                if (jjj == 1) {
                    finish();
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
                captureButton.postDelayed(this, 20);
            }
        });
        Log.d(TAG, "at the end of onCreate");
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "in onResume");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "in onDestroy");
        android.os.Process.killProcess(android.os.Process.myPid());
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "in onStop");
        android.os.Process.killProcess(android.os.Process.myPid());
    }
    private Camera.PictureCallback mPicture = new android.hardware.Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Log.d(TAG, "picture taken");
            mCamera.startPreview();
            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null) {
                Log.d(TAG, "Error creating media file, check storage permissions: ");
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
            jjj = 1;
        }
    };
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Log.d(TAG, "__________ device has a camera");
            return true;
        } else {
            Log.d(TAG, "__________ device does not have a camera");
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
@SuppressWarnings("deprecation")
class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mCamera;
    String TAG = "marclog_CameraPreview";
    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;
        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }
    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        Log.d(TAG, "0000001");
        try {
            Log.d(TAG, "0000002");
            mCamera.setPreviewDisplay(holder);
            Log.d(TAG, "0000003");
            mCamera.startPreview();
            Log.d(TAG, "0000004");
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }
    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
        // TODO Auto-generated method stub
        //if(previewing && camera != null) {
        //    if(camera!=null) {
        Log.d(TAG, "in surfaceDestroyed");
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
        //   }
        //    previewing = false;
    }
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.
        if (mHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }
        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }
        // set preview size and make any resize, rotate or
        // reformatting changes here
        //-----------------------------------------------------------------------------
        Camera.Parameters parameters = mCamera.getParameters();
        Log.d(TAG, "Camera ______________ settings");
        //-----------------------------------------------------------------------------
        try {
            parameters = mCamera.getParameters();
        } catch (Exception e) {
            Log.d(TAG, "Error on camera getParameters");
        }
        Log.d(TAG, "okay: camera getParameters");
        //-----------------------------------------------------------------------------
        try {
            parameters.setRotation(90);
        } catch (Exception e) {
            Log.d(TAG, "Error on parameters.setRotation");
        }
        Log.d(TAG, "okay: parameters.setRotation");
        //-----------------------------------------------------------------------------
        try {
            parameters.set("jpeg-quality", 40);
        } catch (Exception e) {
            Log.d(TAG, "Error on parameters.set(jpeg-quality...)");
        }
        Log.d(TAG, "okay: parameters.set(jpeg-quality...)");
        //-----------------------------------------------------------------------------
        try {
            parameters.setPictureFormat(PixelFormat.JPEG);
        } catch (Exception e) {
            Log.d(TAG, "Error on parameters.setPictureFormat(PixelFormat.JPEG)");
        }
        Log.d(TAG, "okay: parameters.setPictureFormat(PixelFormat.JPEG)");
        //-----------------------------------------------------------------------------
        try {
            parameters.setPictureSize(320, 240);
        } catch (Exception e) {
            Log.d(TAG, "Error on parameters.setPictureSize()");
        }
        Log.d(TAG, "okay: parameters.setPictureSize(...)");
        //-----------------------------------------------------------------------------
        try {
            parameters.setFlashMode(parameters.FLASH_MODE_ON);
            //parameters.setFlashMode(parameters.FLASH_MODE_TORCH);
        } catch (Exception e) {
            Log.d(TAG, "Error on parameters.setFlashMode(parameters.FLASH_MODE_ON)");
        }
        Log.d(TAG, "okay: parameters.setFlashMode(parameters.FLASH_MODE_ON)");
        //-----------------------------------------------------------------------------
        try {
            mCamera.setParameters(parameters);
        } catch (Exception e) {
            Log.d(TAG, "Error on mCamera.setParameters(parameters)");
        }
        Log.d(TAG, "okay: mCamera.setParameters(parameters)");
        //-----------------------------------------------------------------------------
        try {
            mCamera.setDisplayOrientation(90);
        } catch (Exception e) {
            Log.d(TAG, "Error on mCamera.setDisplayOrientation");
        }
        Log.d(TAG, "okay: mCamera.setDisplayOrientation");
        //-----------------------------------------------------------------------------
        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (Exception e) {
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }
}