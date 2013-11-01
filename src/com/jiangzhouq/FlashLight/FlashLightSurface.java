package com.jiangzhouq.FlashLight;

import java.text.AttributedCharacterIterator.Attribute;

import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class FlashLightSurface extends SurfaceView implements SurfaceHolder.Callback{
	private SurfaceHolder mHolder;  
    private Camera mCameraDevices;  
    private Camera.Parameters mParameters;
    private boolean CAMERA_ON = false;
    public FlashLightSurface(Context context) {
    	super(context);
    	init();
	}
	public FlashLightSurface(Context context, AttributeSet attr) {
		super(context);
		init();
	}
	private void init(){
		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		mParameters = mCameraDevices.getParameters();  
		Size preSize = mParameters.getSupportedPreviewSizes().get(0);
		Size picSize = mParameters.getSupportedPictureSizes().get(0);
        if(mParameters != null)  
            mParameters.setPictureFormat(PixelFormat.JPEG);  
        mParameters.setPreviewSize(preSize.width, preSize.height);  
        mParameters.setPictureSize(picSize.width, picSize.height);  
        mCameraDevices.setParameters(mParameters);  
        mCameraDevices.startPreview();  
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {  
            mCameraDevices = Camera.open();  
            mCameraDevices.setPreviewDisplay(mHolder);  
        } catch (Exception e) {  
            if(mCameraDevices != null)  
                mCameraDevices.release();  
            mCameraDevices = null;  
        }  
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if(mCameraDevices == null) return;  
        mCameraDevices.stopPreview();  
        mCameraDevices.release();  
        mCameraDevices = null;
	}
	
	public void releaseCamera(){
		if(mCameraDevices == null) return;  
        mCameraDevices.stopPreview();  
        mCameraDevices.release();  
        mCameraDevices = null;
	}
	
    public void setFlashlightSwitch(boolean on){  
        if(mCameraDevices == null) return;  
        if(mParameters == null){  
            mParameters = mCameraDevices.getParameters();  
        }  
        if(on){  
            mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);  
            CAMERA_ON = true;
        }else{  
            mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);  
            CAMERA_ON = false;
        }  
        mCameraDevices.setParameters(mParameters);  
    }  
    
    public boolean isFlashLightOn(){
    	return CAMERA_ON;
    }
}
