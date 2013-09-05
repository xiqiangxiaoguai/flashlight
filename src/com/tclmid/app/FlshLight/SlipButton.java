package com.tclmid.app.FlshLight;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class SlipButton extends View implements OnTouchListener {

	private boolean NowChoose = false;
	private boolean OnSlip = false;
	private float DownY, NowY;
	private Rect Btn_On, Btn_Off;

	private boolean isChgLsnOn = false;
	private OnChangedListener ChgLsn;

	private Bitmap bg_on, bg_off, slip_btn, slip_btn_pressed;

	public SlipButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	public SlipButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	private void init() {

		BitmapFactory.Options a = new BitmapFactory.Options();
		BitmapFactory.Options b = new BitmapFactory.Options();

		a.inDensity = 260;
		b.inTargetDensity = 370;

		bg_on = BitmapFactory.decodeResource(getResources(),
				R.drawable.slip_bg_on, b);

		slip_btn = BitmapFactory.decodeResource(getResources(),
				R.drawable.slip_btn, a);
		slip_btn_pressed = BitmapFactory.decodeResource(getResources(),
				R.drawable.slip_btn_pressed, a);

		Btn_On = new Rect(0, 12, slip_btn.getWidth(), slip_btn.getHeight() + 12);
		Btn_Off = new Rect(0, bg_on.getHeight() - slip_btn.getHeight() - 12,
				slip_btn.getWidth(), bg_on.getHeight() - 12);
		setOnTouchListener(this);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		Matrix matrix = new Matrix();
		Paint paint = new Paint();
		float y;

		{

			canvas.drawBitmap(bg_on, matrix, paint);

			if (OnSlip) {
				if (NowY >= Btn_Off.top + slip_btn.getHeight() / 2) {

					y = Btn_Off.top;
				} else if (NowY <= Btn_On.top + slip_btn.getHeight() / 2)
					y = Btn_On.top;
				else
					y = NowY - slip_btn.getHeight() / 2;
			} else {
				if (NowChoose)
					y = Btn_On.top;
				else
					y = Btn_Off.top;
			}

			if (OnSlip) {
				canvas.drawBitmap(slip_btn_pressed, 9, y, paint);
			} else
				canvas.drawBitmap(slip_btn, 9, y, paint);
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			NowY = event.getY();
			break;
		case MotionEvent.ACTION_DOWN:
			if (event.getY() > bg_on.getHeight()
					|| event.getX() > bg_on.getWidth())
				return false;
			OnSlip = true;
			DownY = event.getY();
			NowY = DownY;
			break;
		case MotionEvent.ACTION_UP:
			OnSlip = false;
			boolean LastChoose = NowChoose;
			if (event.getY() >= (bg_on.getHeight() / 2))
				NowChoose = false;
			else
				NowChoose = true;
			if (isChgLsnOn && (LastChoose != NowChoose))
				ChgLsn.OnChanged(NowChoose);
			break;
		default:

		}
		invalidate();
		return true;
	}

	public void SetOnChangedListener(OnChangedListener l) {
		isChgLsnOn = true;
		ChgLsn = l;
	}

	public void setButtonState(boolean IsOn) {
		if (IsOn) {
			NowChoose = true;
		}

		else
			NowChoose = false;

		invalidate();
	}

}