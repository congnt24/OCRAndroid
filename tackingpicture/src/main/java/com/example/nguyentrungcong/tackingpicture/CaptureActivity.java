package com.example.nguyentrungcong.tackingpicture;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Nguyen Trung Cong on 9/18/2015.
 */
public class CaptureActivity extends AppCompatActivity {

    private Camera mCamera;
    private TakingPicture takingPicture;
    private FrameLayout camera_layout;
    private final int RESULT_CODE=10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        findViewById(R.id.btn_capture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera.takePicture(null, null, mPicture);
            }
        });


        safeCameraOpenInView();
    }
    private boolean safeCameraOpenInView() {
        boolean qOpened = false;
        //releaseCameraAndPreview();
        mCamera = getCameraInstance();
        qOpened = (mCamera != null);

        if(qOpened == true){
            FrameLayout preview = (FrameLayout) findViewById(R.id.camera_layout);
            takingPicture = new TakingPicture(getApplicationContext(), mCamera, preview);
            preview.addView(takingPicture);
            takingPicture.startCameraPreview();
        }
        return qOpened;
    }
    public Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return c; // returns null if camera is unavailable
    }

    /**
     * Picture Callback for handling a picture capture and saving it out to a file.
     */
    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Intent result = new Intent();
            Bundle bundle = new Bundle();
            bundle.putByteArray("data", data);
            result.putExtras(bundle);
            setResult(RESULT_OK, result);
            finish();
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseCameraAndPreview();
    }
    /**
     * Clear any existing preview / camera.
     */
    private void releaseCameraAndPreview() {

        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
        if(takingPicture != null){
            takingPicture.destroyDrawingCache();
            takingPicture.mCamera = null;
        }
    }
}
