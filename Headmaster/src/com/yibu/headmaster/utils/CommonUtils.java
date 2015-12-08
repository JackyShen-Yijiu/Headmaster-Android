package com.yibu.headmaster.utils;

import com.yibu.headmaster.global.HeadmasterApplication;

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

	public static String[] getStringArray(int resId) {
		return HeadmasterApplication.getContext().getResources()
				.getStringArray(resId);
	}
}