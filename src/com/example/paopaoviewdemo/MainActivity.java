package com.example.paopaoviewdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;

public class MainActivity extends Activity {

	private WaterDropView mWaterDropView;
	private float mDy;
	private float mStartY;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//		mWaterDropView = (WaterDropView)findViewById(R.id.pull_to_refresh_waterdrop);
	}

//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		switch (event.getAction()) {
//		case MotionEvent.ACTION_DOWN:
//			mStartY = event.getY();
//			break;
//
//		case MotionEvent.ACTION_MOVE:
//			mDy = event.getY()-mStartY ;
//			mWaterDropView.updateComleteState(mDy/1920);
//			break;
//		default:
//			break;
//		}
//		return true;
//	}

	

}
