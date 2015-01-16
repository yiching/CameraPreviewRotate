package com.example.textureviewcamera;

import java.util.List;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.TextureView;

public class MainActivity extends Activity implements TextureView.SurfaceTextureListener {
    private Camera mCamera;
    private TextureView mTextureView;
    private int mRotation = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        mTextureView = new TextureView(this);
        mTextureView.setSurfaceTextureListener(this);

        setContentView(mTextureView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.rotate:
                mRotation += 10;
                mTextureView.setRotation(mRotation);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {

        mCamera = Camera.open(0);
        Log.e("Log", "mCamera = " + mCamera);
        
        try {
            mCamera.setPreviewTexture(surface);
            
            Camera.Parameters params = mCamera.getParameters();
            List<String> focusMode = params.getSupportedFocusModes();

            if(0 != focusMode.size()) {
                for(String mode :focusMode) {
                    if(mode.equals(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                        params.setFocusMode(mode);
                        Log.e("Log","Auto Focus");
                    }
                }
            }
            mCamera.setParameters(params);
            mCamera.setDisplayOrientation(90);
            mCamera.startPreview();
        } catch (Exception e) {
            Log.e("Log", "Camera Error");
        }

    }

    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        // Ignored, Camera does all the work for us
    }

    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {

        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
        }

        return true;
    }

    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        // Invoked every time there's a new Camera preview frame
    }

    @Override
    protected void onDestroy() {

        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
        }

        super.onDestroy();
    }

}
