package com.example.paopaoviewdemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
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

public class PaoPaoTextView extends View {

	private Circle mCircleOne;
	private Circle mCircleTwo;
	private float lastX;
	private float lastY;
	private OverScroller mScroller;
	private float mCircleTwoStartX;
	private float mCircleTwoStartY;
	private int curDistance;
	private double mMaxDistance;
	private float mStartRadius;
	private boolean mIsMove = false; // 只有在拖动的时候才画 drawBezierPath

	public PaoPaoTextView(Context context) {
		super(context);
		init(context);
	}

	public PaoPaoTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	@SuppressLint("NewApi")
	private void init(Context context) {
		mScroller = new OverScroller(context, new BounceInterpolator());
		mCircleOne = new Circle();
		mCircleOne.setColor(0xffff0000);
		mCircleOne.setRadius(50);
		mCircleOne.setX(50);
		mCircleOne.setY(50);

		mCircleTwo = new Circle();
		mCircleTwo.setColor(0xffff0000);
		mCircleTwo.setRadius(50);
		mCircleTwo.setX(50);
		mCircleTwo.setY(50);

		mCircleTwoStartX = 50;
		mCircleTwoStartY = 50;
		mStartRadius = 50;
		mMaxDistance = 1000;
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

	public Bitmap createViewBitmap(View v) {
		Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawBezierPath(canvas);

		Paint paint = new Paint();
		paint.setColor(mCircleOne.getColor());
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		paint.setStrokeWidth(2);
		canvas.drawCircle(mCircleOne.getX(), mCircleOne.getY(), mCircleOne.getRadius(), paint);

		canvas.drawCircle(mCircleTwo.getX(), mCircleTwo.getY(), mCircleTwo.getRadius(), paint);

		v.draw(canvas);

		return bitmap;
	}

	@SuppressLint("NewApi")
	private void drawBezierPath(Canvas canvas) {
		if (!mIsMove) {
			return;
		}
		Path path = new Path();

		float offx = mCircleTwo.getX() - mCircleOne.getX();
		float offy = mCircleTwo.getY() - mCircleOne.getY();
		curDistance = (int) Math.sqrt(offx * offx + offy * offy);

		if (curDistance > mMaxDistance) {
			return;
		}
		float cos = -1.0f * offx / curDistance;
		float sin = 1.0f * offy / curDistance;

		BezierPoint point1 = new BezierPoint(mCircleOne.getX(), mCircleOne.getY());
		BezierPoint point2 = new BezierPoint(mCircleOne.getX() + mCircleOne.getRadius() * sin,
				mCircleOne.getY() + mCircleOne.getRadius() * cos);
		BezierPoint point3 = new BezierPoint((mCircleOne.getX() + mCircleTwo.getX()) / 2,
				(mCircleOne.getY() + mCircleTwo.getY()) / 2);

		BezierPoint point4 = new BezierPoint(mCircleTwo.getX() + mCircleTwo.getRadius() * sin,
				mCircleTwo.getY() + mCircleTwo.getRadius() * cos);
		BezierPoint point5 = new BezierPoint(mCircleTwo.getX(), mCircleTwo.getY());
		BezierPoint point6 = new BezierPoint(mCircleTwo.getX() - mCircleTwo.getRadius() * sin,
				mCircleTwo.getY() - mCircleTwo.getRadius() * cos);

		BezierPoint point7 = new BezierPoint((mCircleOne.getX() + mCircleTwo.getX()) / 2,
				(mCircleOne.getY() + mCircleTwo.getY()) / 2);
		BezierPoint point8 = new BezierPoint(mCircleOne.getX() - mCircleOne.getRadius() * sin,
				mCircleOne.getY() - mCircleOne.getRadius() * cos);

		path.moveTo(point1.x, point1.y);
		path.lineTo(point2.x, point2.y);
		path.quadTo(point3.x, point3.y, point4.x, point4.y);
		path.lineTo(point5.x, point5.y);
		path.lineTo(point6.x, point6.y);
		path.quadTo(point7.x, point7.y, point8.x, point8.y);
		path.close();

		Paint paint = new Paint();
		paint.setColor(0xffff0000);
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
			mIsMove=true;
			return true;
		case MotionEvent.ACTION_MOVE:
			float disX = event.getRawX() - lastX;
			float disY = event.getRawY() - lastY;
			float curRadius = (float) (mStartRadius * (1.0 - 1.0 * curDistance / mMaxDistance));
			Log.v(VIEW_LOG_TAG, "disX " + disX + " disY " + disY);
			mCircleOne.setRadius(curRadius);
			mCircleTwo.setX(mCircleTwo.getX() + disX);
			mCircleTwo.setY(mCircleTwo.getY() + disY);
			lastX = event.getRawX();
			lastY = event.getRawY();
			postInvalidate();
			break;
		case MotionEvent.ACTION_UP:
			mIsMove=false;
			mCircleOne.setRadius(0);
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
