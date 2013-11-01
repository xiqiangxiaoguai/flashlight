package com.jiangzhouq.FlashLight;

import com.tclmid.app.FlshLight.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Switch;


public class FlashLightActivity extends Activity implements OnCheckedChangeListener, OnClickListener{

	private FlashLightSurface mSurface; 
	private Switch mSwitch;
	private ImageButton mImageButton;
	private ImageButton mAboutButton;
	private boolean isFlashOn = false;
	Handler mHandler = new  Handler(){
	};
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mSurface = new FlashLightSurface(this);
		RelativeLayout relative = (RelativeLayout) findViewById(R.id.main_layout);
		relative.addView(mSurface, 0);
		mImageButton = (ImageButton) findViewById(R.id.switch_button);
		mImageButton.setOnClickListener(this);
		mSwitch = (Switch) findViewById(R.id.mode_switch);
		mSwitch.setOnCheckedChangeListener(this);
		mAboutButton = (ImageButton) findViewById(R.id.about);
		mAboutButton.setOnClickListener(this);
	}
	Runnable run = new Runnable() {
		@Override
		public void run() {
			mSurface.setFlashlightSwitch(!mSurface.isFlashLightOn());
			mHandler.postDelayed(run, 500);
		}
	};
	private void startFlash(boolean flick){
		if(flick){
			mHandler.post(run);
			mImageButton.setImageResource(R.drawable.switch_on);
		}else{
			mHandler.removeCallbacks(run);
			if(!mSurface.isFlashLightOn()){
				mSurface.setFlashlightSwitch(true);
				mImageButton.setImageResource(R.drawable.switch_on);
			}
		}
	}
	
	private void stopFlash(){
		mHandler.removeCallbacks(run);
		if(mSurface.isFlashLightOn()){
			mSurface.setFlashlightSwitch(false);
		}
		mImageButton.setImageResource(R.drawable.switch_off);
	}
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(buttonView.getId() == R.id.mode_switch){
			if(isFlashOn){
				startFlash(!isChecked);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
		case R.id.switch_button:
			if(isFlashOn){
				stopFlash();
				isFlashOn = false;
			}else{
				startFlash(!mSwitch.isChecked());
				isFlashOn = true;
			}
			break;
		case R.id.about:
			new AlertDialog.Builder(this)
			.setTitle("About")
			.setMessage("Version:v1.0 \nAuthor:JiangzhouQ \nEmail:jiangzhouq@gmail.com")
			.create()
			.show();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mSurface.releaseCamera();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		stopFlash();
		isFlashOn = false;
	}
}
