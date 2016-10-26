package com.jzjf.headmaster.utils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.text.TextUtils;

@SuppressLint("SimpleDateFormat")
public class UTC2LOC {

	public static UTC2LOC instance;

	static {
		if (instance == null) {
			synchronized (UTC2LOC.class) {
				if (instance == null) {
					instance = new UTC2LOC();
				}
			}
		}
	}

	public String getDate(String date, String format) {
		try {
			if (TextUtils.isEmpty(date)) {
				return "";
			}
			date = date.replace("Z", "");
			Date dt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
					.parse(date);
			// 转换为北京时间,相差8个小时
			dt.setTime(dt.getTime() + 28800000);
			SimpleDateFormat sf = new SimpleDateFormat(format);
			return sf.format(dt);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}

	public Date getDates(String date, String format) {
		try {
			if (TextUtils.isEmpty(date)) {
				return null;
			}
			date = date.replace("Z", "");
			Date dt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
					.parse(date);
			// 转换为北京时间,相差8个小时
			dt.setTime(dt.getTime() + 28800000);
			SimpleDateFormat sf = new SimpleDateFormat(format);
			return sf.parse(sf.format(dt));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}