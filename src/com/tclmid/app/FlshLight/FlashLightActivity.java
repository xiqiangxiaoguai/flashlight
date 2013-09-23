package com.tclmid.app.FlshLight;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.adsmogo.interstitial.AdsMogoInterstitial;
import com.adsmogo.interstitial.AdsMogoInterstitialListener;

public class FlashLightActivity extends Activity implements OnCheckedChangeListener, OnClickListener{

	private FlashLightSurface mSurface; 
	private Switch mSwitch;
	private ImageButton mImageButton;
	private ImageButton mAboutButton;
	private boolean isFlashOn = false;
	Handler mHandler = new  Handler(){
	};
	private AdsMogoInterstitial adsmogoFull;
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
		//****************************************************
		//752d8033330a4bec97710a9c3ba8ebaa
		// 初始化mogo全/插屏广告，isRotate：是否轮换，如为false广告展示或失败一次后不再请求广
				// 告。如为true广告展示或失败一次后会继续请求广告
		adsmogoFull = new AdsMogoInterstitial(this,
				"752d8033330a4bec97710a9c3ba8ebaa", true);
		// 设置全/插屏广告监听
		adsmogoFull.setAdsMogoInterstitialListener(adsmogoFullListener);
		mHandler.postDelayed(mogoRun, 30000);
		//****************************************************
	}
	//********************************************************
	AdsMogoInterstitialListener adsmogoFullListener = new AdsMogoInterstitialListener() {
		// 开始预加载全/插屏广告时调用(adName为当前广告平 台名称)
				@Override
				public void onInterstitialStartReady(String adName) {
				}
				// 预加载全屏广告完成时调用(adName为当前广告平台名 称)
				@Override
				public void onInterstitialReadyed(String adName) {
				}
				// ：广告展示成功时调用(此回调需要返回一个int值，该返回 值将作为下次广告请求的间隔时间。adName为当前广告平台名称)
				@Override
				public int onInterstitialSucceed(String adName) {
					mHandler.removeCallbacks(mogoRun);
					return 15;
				}
				// 广告请求或展示失败时调用（此回调需要返回一个int值，该返回值将作为下 次广告请求的间隔时间）
				@Override
				public int onInterstitialFailed() {
					return 15;
				}
				@Override
				public void onInterstitialClickAd(String adName) {
				}
				@Override
				public void onInterstitialRealClickAd(String adName) {
				}
				@Override
				public boolean onInterstitialCloseAd() {
					mHandler.postDelayed(mogoRun, 30000);
					return false;
				}
				@Override
				public View onInterstitialGetView() {
					return mImageButton;
				}
	};
	
	Runnable mogoRun = new Runnable() {
		
		@Override
		public void run() {
			if (adsmogoFull.getInterstitialAdStart()) {
				adsmogoFull.showInterstitialAD();
			}
			mHandler.postDelayed(mogoRun, 30000);
		}
	};
	//********************************************************
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
			.setTitle("简约手电筒")
			.setMessage("版本:v1.0 \n作者:JiangzhouQ \n邮箱:jiangzhouq@gmail.com \n\n简介:从事Android开发两年有余,白天Android工作,晚上业余写点自己感兴趣的APP,欢迎合作.")
			.create()
			.show();
		}
	}
	
	
	@Override
	protected void onPause() {
		super.onPause();
		stopFlash();
		isFlashOn = false;
		mHandler.removeCallbacks(mogoRun);
	}
}
