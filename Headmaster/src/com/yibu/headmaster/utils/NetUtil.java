package com.yibu.headmaster.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 需要加上访问网络状态的权限
 * @author Administrator
 *
 */
public class NetUtil {
	
	/**
	 * 是否有网络
	 * 
	 * @param context
	 * @return
	 */
	public static boolean hasNetwork(Context context) {
		ConnectivityManager con = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo workinfo = con.getActiveNetworkInfo();
		if (workinfo == null || !workinfo.isAvailable()) {
			return false;
		}
		return true;
	}
	
	
	/**
	 * 判断是否是WiFi网络
	 * @param context
	 * @return
	 */
	public static boolean isWifiNet(Context context){
		ConnectivityManager con = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo workinfo = con.getActiveNetworkInfo();
		return workinfo!=null && workinfo.getType() == ConnectivityManager.TYPE_WIFI;
	}
}
