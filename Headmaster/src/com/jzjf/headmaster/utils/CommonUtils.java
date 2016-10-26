package com.jzjf.headmaster.utils;

import android.content.Context;

import com.jzjf.headmaster.global.HeadmasterApplication;

public class CommonUtils {
	public static void runOnUIThread(Runnable runnable) {
		HeadmasterApplication.getMainHandler().post(runnable);
	}

	public static float getDimens(int resId) {
		return HeadmasterApplication.getContext().getResources()
				.getDimension(resId);
	}

	public static String getString(int resId) {
		return HeadmasterApplication.getContext().getResources()
				.getString(resId);
	}
	public static int getColor(int resId) {
		return HeadmasterApplication.getContext().getResources()
				.getColor(resId);
	}

	public static String[] getStringArray(int resId) {
		return HeadmasterApplication.getContext().getResources()
				.getStringArray(resId);
	}
	public static  int px2dp(Context context, float px) {  
	    final float scale = context.getResources().getDisplayMetrics().density;  
	    return (int) (px / scale + 0.5f);  
	}  
}