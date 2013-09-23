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
		// ��ʼ��mogoȫ/������棬isRotate���Ƿ��ֻ�����Ϊfalse���չʾ��ʧ��һ�κ��������
				// �档��Ϊtrue���չʾ��ʧ��һ�κ�����������
		adsmogoFull = new AdsMogoInterstitial(this,
				"752d8033330a4bec97710a9c3ba8ebaa", true);
		// ����ȫ/����������
		adsmogoFull.setAdsMogoInterstitialListener(adsmogoFullListener);
		mHandler.postDelayed(mogoRun, 30000);
		//****************************************************
	}
	//********************************************************
	AdsMogoInterstitialListener adsmogoFullListener = new AdsMogoInterstitialListener() {
		// ��ʼԤ����ȫ/�������ʱ����(adNameΪ��ǰ���ƽ ̨����)
				@Override
				public void onInterstitialStartReady(String adName) {
				}
				// Ԥ����ȫ��������ʱ����(adNameΪ��ǰ���ƽ̨�� ��)
				@Override
				public void onInterstitialReadyed(String adName) {
				}
				// �����չʾ�ɹ�ʱ����(�˻ص���Ҫ����һ��intֵ���÷��� ֵ����Ϊ�´ι������ļ��ʱ�䡣adNameΪ��ǰ���ƽ̨����)
				@Override
				public int onInterstitialSucceed(String adName) {
					mHandler.removeCallbacks(mogoRun);
					return 15;
				}
				// ��������չʾʧ��ʱ���ã��˻ص���Ҫ����һ��intֵ���÷���ֵ����Ϊ�� �ι������ļ��ʱ�䣩
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
			.setTitle("��Լ�ֵ�Ͳ")
			.setMessage("�汾:v1.0 \n����:JiangzhouQ \n����:jiangzhouq@gmail.com \n\n���:����Android������������,����Android����,����ҵ��д���Լ�����Ȥ��APP,��ӭ����.")
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
