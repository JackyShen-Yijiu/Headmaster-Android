package com.yibu.headmaster.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
	public static Toast mToast;

	/**
	 * 弹吐司
	 * @param mContext
	 * @param msg
	 */
	public static void showToast(final Context mContext, final String msg) {
		CommonUtils.runOnUIThread(new Runnable() {
			@Override
			public void run() {
				if (mToast == null) {
					mToast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
				}
				mToast.setText(msg);
				mToast.show();
				
			}
		});
	}
	
	
}
