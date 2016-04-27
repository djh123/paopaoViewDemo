package com.example.paopaoviewdemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.OverScroller;
import android.widget.TextView;

public class PaoPaoTextView2 extends View {

	private Circle mCircleOne;
	private Circle mCircleTwo;
	private float lastX;
	private float lastY;
	private OverScroller mScroller;
	private float mCircleTwoStartX;
	private float mCircleTwoStartY;

	public PaoPaoTextView2(Context context) {
		super(context);
		init(context);
	}

	public PaoPaoTextView2(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	@SuppressLint("NewApi")
	private void init(Context context) {
		mScroller = new OverScroller(context, new BounceInterpolator());
		mCircleOne = new Circle();
		mCircleOne.setColor(0x18ff0000);
		mCircleOne.setRadius(50);
		mCircleOne.setX(50);
		mCircleOne.setY(50);

		mCircleTwo = new Circle();
		mCircleTwo.setColor(0xffff0000);
		mCircleTwo.setRadius(50);
		mCircleTwoStartX = 500;
		mCircleTwo.setX(500);
		mCircleTwoStartY = 50;
		mCircleTwo.setY(50);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		drawBezierPath(canvas);

		Paint paint = new Paint();
		paint.setColor(mCircleOne.getColor());
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		paint.setStrokeWidth(2);
		canvas.drawCircle(mCircleOne.getX(), mCircleOne.getY(), mCircleOne.getRadius(), paint);

		canvas.drawCircle(mCircleTwo.getX(), mCircleTwo.getY(), mCircleTwo.getRadius(), paint);
	}

	@SuppressLint("NewApi")
	private void drawBezierPath(Canvas canvas) {
		Path path = new Path();

		// path.moveTo(getX(),getY()+getHeight()/2);
		// path.quadTo(getX()+110, getY()-200, getX(), getY()-getHeight()/2);
		float dy = 50;
		float dx = 50;
		BezierPoint point1 = new BezierPoint(mCircleOne.getX(), mCircleOne.getY());
		BezierPoint point2 = new BezierPoint(mCircleOne.getX(), mCircleOne.getY() + mCircleOne.getRadius());
		BezierPoint point3 = new BezierPoint(mCircleOne.getX() + 50, mCircleOne.getY() + mCircleOne.getRadius() - 50);

		BezierPoint point4 = new BezierPoint(mCircleTwo.getX(), mCircleTwo.getY() + mCircleTwo.getRadius());
		BezierPoint point5 = new BezierPoint(mCircleTwo.getX(), mCircleTwo.getY());
		BezierPoint point6 = new BezierPoint(mCircleTwo.getX(), mCircleTwo.getY() - mCircleTwo.getRadius());

		BezierPoint point7 = new BezierPoint(mCircleOne.getX() + 50, mCircleOne.getY() - mCircleOne.getRadius() + 50);
		BezierPoint point8 = new BezierPoint(mCircleOne.getX(), mCircleOne.getY() - mCircleOne.getRadius());

		path.moveTo(point1.x, point1.y);
		path.lineTo(point2.x, point2.y);
		path.quadTo(point3.x, point3.y, point4.x, point4.y);
		path.lineTo(point5.x, point5.y);
		path.lineTo(point6.x, point6.y);
		path.quadTo(point7.x, point7.y, point8.x, point8.y);
		path.close();

		Paint paint = new Paint();
		paint.setColor(0xffffff00);
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		paint.setStrokeWidth(2);

		canvas.drawPath(path, paint);
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			Log.v(VIEW_LOG_TAG, "ACTION_DOWN ");
			lastX = event.getRawX();
			lastY = event.getRawY();
			return true;
		case MotionEvent.ACTION_MOVE:
			float disX = event.getRawX() - lastX;
			float disY = event.getRawY() - lastY;
			Log.v(VIEW_LOG_TAG, "disX " + disX + " disY " + disY);
			// offsetLeftAndRight((int) disX);
			// offsetTopAndBottom((int) disY);
			mCircleTwo.setX(mCircleTwo.getX() + disX);
			mCircleTwo.setY(mCircleTwo.getY() + disY);
			lastX = event.getRawX();
			lastY = event.getRawY();
			postInvalidate();
			break;
		case MotionEvent.ACTION_UP:
			mScroller.startScroll((int) mCircleTwo.getX(), (int) mCircleTwo.getY(),
					-(int) (mCircleTwo.getX() - mCircleTwoStartX), -(int) (mCircleTwo.getY() - mCircleTwoStartY));
			invalidate();
			break;
		}

		return super.onTouchEvent(event);
	}

	@SuppressLint("NewApi")
	@Override
	public void computeScroll() {
		Log.v("TAG", "computeScroll");
		if (mScroller.computeScrollOffset()) {
			mCircleTwo.setX(mScroller.getCurrX());
			mCircleTwo.setY(mScroller.getCurrY());
			invalidate();
		}
	}

	class BezierPoint {
		public float x;
		public float y;

		public BezierPoint(float x, float y) {
			this.x = x;
			this.y = y;
		}
	}
}
