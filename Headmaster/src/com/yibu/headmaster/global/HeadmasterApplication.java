package com.yibu.headmaster.global;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.loopj.android.http.AsyncHttpClient;
import com.yibu.headmaster.api.ApiHttpClient;
import com.yibu.headmaster.bean.UserBean;

public class HeadmasterApplication extends Application {

	// 上下文
	private static Context mContext;
	// 全局handler
	private static Handler mHandler;

	public static HeadmasterApplication app;
	public boolean isLogin;

	// 用户信息
	public UserBean userInfo;

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = this;
		mHandler = new Handler();

		app = new HeadmasterApplication();
		init();
	}

	public static HeadmasterApplication getInstance() {
		app = new HeadmasterApplication();
		return app;
	}

	public static Handler getMainHandler() {
		return mHandler;
	}

	public static Context getContext() {
		return mContext;
	}

	private void init() {
		// 初始化网络请求
		AsyncHttpClient client = new AsyncHttpClient();
		ApiHttpClient.setHttpClient(client);

	}
}
