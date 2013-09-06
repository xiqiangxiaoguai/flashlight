package com.tclmid.app.FlshLight;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.Vibrator;
import android.view.View;

public class FlashLightActivity extends Activity implements OnChangedListener {

    
	private Vibrator mVibrator;
	private FlashLightSurface mSurface; 
	private static final String PRIVATE_FLASHLIGHT_NOTIFY_ACTION = "FLASHLIGHT_NOTIFICATION";

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			if (action.equals(PRIVATE_FLASHLIGHT_NOTIFY_ACTION)) {
					if (mSurface != null) {

						boolean isFlashlightOn = mSurface.isFlashLightOn();

						setBackGround(isFlashlightOn);
					}
			}
		}
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		
//		mSurface = (FlashLightSurface)findViewById(R.id.surface_view);
		
	}

	public void onResume() {
		super.onResume();

		IntentFilter filter = new IntentFilter(PRIVATE_FLASHLIGHT_NOTIFY_ACTION);
		registerReceiver(mReceiver, filter);

			if (mSurface != null) {
				boolean isFlashlightOn = mSurface.isFlashLightOn();

			}
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		unregisterReceiver(mReceiver);
	}

	@Override
	public void OnChanged(boolean CheckState) {
		// TODO Auto-generated method stub
		mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

		setLightFlag(CheckState);

		mVibrator.vibrate(20);
		setBackGround(CheckState);
	}

	public void setBackGround(boolean IsFlashLightOn) {

		View V = findViewById(R.id.main_layout);
		if (IsFlashLightOn) {
			V.setBackgroundResource(R.drawable.bg_on_background);
		} else {
			V.setBackgroundResource(R.drawable.bg_off_background);
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		setLightFlag(false);
	}

	/**
	 * set spash light.
	 * 
	 * @param flag
	 *            To turn light on or off. If flag is true turn light on.
	 * 
	 */
	private void setLightFlag(Boolean flag) {
		if (mSurface != null) {
			mSurface.setFlashlightSwitch(flag ? true : false);
		}
	}
}
