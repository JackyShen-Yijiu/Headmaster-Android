package com.jzjf.headmaster.utils;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;

import com.jzjf.headmaster.R;

/**
 * ����������
 * 
 * @ClassName WindowUtils
 * 
 * 
 */
public class WindowUtils {

	private static View mView = null;
	private static WindowManager mWindowManager = null;
	private static Context mContext = null;

	public static Boolean isShown = false;

	/**
	 * ��ʾ������
	 * 
	 * @param context
	 * @param view
	 */
	public static void showPopupWindow(final Context context) {
		if (isShown) {
			// LogUtil.i(LOG_TAG, "return cause already shown");
			return;
		}

		isShown = true;
		// LogUtil.i(LOG_TAG, "showPopupWindow");

		// ��ȡӦ�õ�Context
		mContext = context.getApplicationContext();
		// ��ȡWindowManager
		mWindowManager = (WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE);

		mView = setUpView(context);

		final WindowManager.LayoutParams params = new WindowManager.LayoutParams();

		// ����
		params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;

		// WindowManager.LayoutParams.TYPE_SYSTEM_ALERT

		// ����flag

		int flags = WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
		// | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		// ���������WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE��������View�ղ���Back�����¼�
		params.flags = flags;
		// ����������������͸��������ʾΪ��ɫ
		params.format = PixelFormat.TRANSLUCENT;
		// FLAG_NOT_TOUCH_MODAL�������¼����ݵ�����Ĵ���
		// ���� FLAG_NOT_FOCUSABLE �������ڽ�Сʱ�������Ӧ��ͼ���ɲ��ɳ�����Ϊ�ɳ���
		// ���������flag�Ļ���homeҳ�Ļ�����������

		params.width = LayoutParams.MATCH_PARENT;
		params.height = LayoutParams.MATCH_PARENT;

		params.gravity = Gravity.CENTER;

		mWindowManager.addView(mView, params);

		// LogUtil.i(LOG_TAG, "add view");

	}

	/**
	 * ���ص�����
	 */
	public static void hidePopupWindow() {
		// LogUtil.i(LOG_TAG, "hide " + isShown + ", " + mView);
		if (isShown && null != mView) {
			// LogUtil.i(LOG_TAG, "hidePopupWindow");
			mWindowManager.removeView(mView);
			isShown = false;
		}

	}

	// ���ص���view������ʱ�����
	private static View setUpView(final Context context) {

		// LogUtil.i(LOG_TAG, "setUp view");

		View view = LayoutInflater.from(context).inflate(R.layout.popupwindow,
				null);
		Button positiveBtn = (Button) view.findViewById(R.id.positiveBtn);
		positiveBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// LogUtil.i(LOG_TAG, "ok on click");
				// �򿪰�װ��
				// ���ص���
				WindowUtils.hidePopupWindow();

			}
		});

		Button negativeBtn = (Button) view.findViewById(R.id.negativeBtn);
		negativeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// LogUtil.i(LOG_TAG, "cancel on click");
				WindowUtils.hidePopupWindow();

			}
		});

		// ��������ⲿ���������
		// ����ʵ����Ҫ������������Ϊȫ����С������и�͸���������м�һ������Ϊ��������
		// ���Ե�����������ⲿ��Ϊ����������ⲿ
		final View popupWindowView = view.findViewById(R.id.popup_window);// ��͸������������
		// ע������������������������Χ�Լ����˼��˳�
		/*
		 * view.setOnTouchListener(new OnTouchListener() {
		 * 
		 * @Override public boolean onTouch(View v, MotionEvent event) {
		 * 
		 * // LogUtil.i(LOG_TAG, "onTouch"); int x = (int) event.getX(); int y =
		 * (int) event.getY(); Rect rect = new Rect();
		 * popupWindowView.getGlobalVisibleRect(rect); if (!rect.contains(x, y))
		 * { WindowUtils.hidePopupWindow(); }
		 * 
		 * // LogUtil.i(LOG_TAG, "onTouch : " + x + ", " + y + ", rect: " // +
		 * rect); return false; } });
		 * 
		 * // ���back�������� view.setOnKeyListener(new OnKeyListener() {
		 * 
		 * @Override public boolean onKey(View v, int keyCode, KeyEvent event) {
		 * switch (keyCode) { case KeyEvent.KEYCODE_BACK:
		 * WindowUtils.hidePopupWindow(); return true; default: return false; }
		 * } });
		 */

		return view;

	}
}
