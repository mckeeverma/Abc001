package com.example.marc.abc001;

import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

@SuppressWarnings("deprecation")

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
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