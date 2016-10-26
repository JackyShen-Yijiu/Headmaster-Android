package com.jzjf.headmaster.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

public class BulleinEditText extends EditText {

	public BulleinEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public BulleinEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BulleinEditText(Context context) {
		super(context);
	}

	private int downX;
	private int downY;

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downX = (int) event.getX();
			downY = (int) event.getY();
			getParent().requestDisallowInterceptTouchEvent(true);
			break;
		case MotionEvent.ACTION_MOVE:
			int moveX = (int) event.getX();
			int moveY = (int) event.getY();

			int diffX = moveX - downX;
			int diffY = moveY - downY;
			// 左右滑动
			if (Math.abs(diffY) > Math.abs(diffX)) {
				getParent().requestDisallowInterceptTouchEvent(true);
			}
			break;

		default:
			break;
		}
		return super.dispatchTouchEvent(event);
	}

}
