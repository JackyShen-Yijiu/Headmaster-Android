package com.yibu.headmaster.base;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.yibu.headmaster.utils.ToastUtil;

public abstract class BasePager {

	public View rootView;
	protected Context mContext;
	protected String result = "";
	protected String msg = "";

	protected AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			String value = parseJson(responseBody);
			if (!TextUtils.isEmpty(msg)) {
				// 加载失败，弹出失败对话框
				ToastUtil.showToast(mContext, msg);
			} else {
				process(value);

			}

		}

		private String parseJson(byte[] responseBody) {
			String value = null;
			JSONObject dataObject = null;
			JSONArray dataArray = null;
			String dataString = null;
			try {

				JSONObject jsonObject = new JSONObject(new String(responseBody));
				result = jsonObject.getString("type");
				msg = jsonObject.getString("msg");
				try {
					dataObject = jsonObject.getJSONObject("data");

				} catch (Exception e2) {
					try {
						dataArray = jsonObject.getJSONArray("data");
					} catch (Exception e3) {
						dataString = jsonObject.getString("data");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (dataObject != null) {
				value = dataObject.toString();
			} else if (dataArray != null) {
				value = dataArray.toString();

			} else if (dataString != null) {
				value = dataString;
			}
			return value;
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
		}
	};

	// @Override
	// public void onResume() {
	// super.onResume();
	// // 统计页面
	// MobclickAgent.onPageStart(this.getClass().getSimpleName());
	// }
	//
	// @Override
	// public void onPause() {
	// super.onPause();
	// // 统计页面
	// MobclickAgent.onPageStart(this.getClass().getSimpleName());
	// }

	public BasePager(Context context) {
		mContext = context;
		rootView = initView();
	}

	public View initView() {
		return null;
	}

	public void initData() {

	}

	public abstract void process(String data);

}
