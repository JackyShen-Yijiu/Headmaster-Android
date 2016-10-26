package com.jzjf.headmaster.global;

import java.util.Iterator;
import java.util.List;

import com.loopj.android.http.AsyncHttpClient;
import com.jzjf.headmaster.api.ApiHttpClient;
import com.jzjf.headmaster.bean.UserBean;
import com.jzjf.headmaster.utils.LogUtil;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import cn.jpush.android.api.JPushInterface;

public class HeadmasterApplication extends Application {

	// 上下文
	private static Context mContext;
	// 全局handler
	private static Handler mHandler;
	public static HeadmasterApplication app;
	public boolean isLogin;

	// 用户信息
	public UserBean userInfo;
	
	public static float density = 2.0f;

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = this;
		mHandler = new Handler();

		app = new HeadmasterApplication();
		init();

		// 环信初始化
//		DemoHXSDKHelper.getInstance().onInit(this);

		// 极光
		JPushInterface.setDebugMode(true);// 设置开发模式
		JPushInterface.init(this);// 极光初始化
		
		
	}

	public static HeadmasterApplication getInstance() {
		if(app==null){
			app = new HeadmasterApplication();
		}
		LogUtil.print("sssssssssss"+app);
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

	protected String getAppName(int pID) {
		String processName = null;
		ActivityManager am = (ActivityManager) this
				.getSystemService(ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> l = am.getRunningAppProcesses();
		Iterator<RunningAppProcessInfo> i = l.iterator();
		while (i.hasNext()) {
			ActivityManager.RunningAppProcessInfo info = (i.next());
			try {
				if (info.pid == pID) {
					processName = info.processName;
					return processName;
				}
			} catch (Exception e) {
			}
		}
		return processName;
	}
}
