package com.yibu.headmaster.api;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.yibu.headmaster.utils.LogUtil;
import com.yibu.headmaster.utils.TestInternet;
import com.yibu.headmaster.utils.ToastUtil;

public class ApiHttpClient {

	// public static String Base_URL = "http://101.200.204.240:8181/";
	// public static String Base_URL = "http://api.yibuxueche.com/";
	public static String Base_URL = "http://jzapi.yibuxueche.com/";
	public static String API_URL = Base_URL + "api/headmaster/%s";
	public static final String DELETE = "DELETE";
	public static final String GET = "GET";
	public static final String POST = "POST";
	public static final String PUT = "PUT";
	public static AsyncHttpClient client;

	public ApiHttpClient() {
	}

	public static AsyncHttpClient getHttpClient() {
		return client;
	}

	public static void cancelAll(Context context) {
		client.cancelRequests(context, true);
	}

	public static void clearUserCookies(Context context) {
		// (new HttpClientCookieStore(context)).a();
	}

	public static void delete(String partUrl, AsyncHttpResponseHandler handler) {
		client.delete(getAbsoluteApiUrl(partUrl), handler);
	}

	public static void getTag(final String tag, String url,
			final TestInternet callback) {
		get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				if (arg0 == 200) {
					callback.onSuccess(tag, arg2);
				} else {
					callback.onF(tag, arg2);
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				callback.onF(tag, arg2);

			}
		});
	}

	public static void get(String partUrl, AsyncHttpResponseHandler handler) {
		client.get(getAbsoluteApiUrl(partUrl), handler);
	}

	public static void get(String partUrl, final String type,final ICallBack callback) {
		 AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				if(statusCode !=200){
					callback.processFailure(type);
					return;
				}
				String value = null;
				JSONObject dataObject = null;
				JSONArray dataArray = null;
				String dataString = null;
				String result =null;
				String msg = null;
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
				if (!TextUtils.isEmpty(msg)) {
					// 加载失败，弹出失败对话框
				} else {
					callback.process(value,type);

				}

			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				callback.processFailure(type);
			}
		};
		
		client.get(getAbsoluteApiUrl(partUrl), handler );
	}

	public static void getWithFullPath(String partUrl,
			AsyncHttpResponseHandler handler) {
		LogUtil.print(Base_URL + partUrl);
		client.get(Base_URL + partUrl, handler);
	}

	public static void postWithFullPath(String partUrl, RequestParams params,
			AsyncHttpResponseHandler handler) {
		LogUtil.print(Base_URL + partUrl);
		client.post(Base_URL + partUrl, params, handler);
	}

	public static void get(String partUrl, RequestParams params,
			AsyncHttpResponseHandler handler) {
		client.get(getAbsoluteApiUrl(partUrl), params, handler);
	}

	/**
	 * 获取绝对地址
	 * 
	 * @param partUrl
	 * @param params
	 * @param handler
	 */
	public static void getAbsolute(String partUrl, RequestParams params,
			AsyncHttpResponseHandler handler) {
		client.get(Base_URL + partUrl, params, handler);
	}

	public static String getAbsoluteApiUrl(String partUrl) {
		String url = String.format(API_URL, partUrl);
		LogUtil.print("request--url" + url);
		return url;
	}

	public static String getApiUrl() {
		return API_URL;
	}

	public static void getDirect(String url, AsyncHttpResponseHandler handler) {
		client.get(url, handler);
	}

	public static void post(String partUrl, AsyncHttpResponseHandler handler) {
		client.post(getAbsoluteApiUrl(partUrl), handler);
	}

	public static void post(String partUrl, RequestParams params,
			AsyncHttpResponseHandler handler) {
		client.post(getAbsoluteApiUrl(partUrl), params, handler);
	}

	public static void postDirect(String url, RequestParams params,
			AsyncHttpResponseHandler handler) {
		client.post(url, params, handler);
	}

	public static void put(String partUrl, AsyncHttpResponseHandler handler) {
		client.put(getAbsoluteApiUrl(partUrl), handler);
	}

	public static void put(String partUrl, RequestParams params,
			AsyncHttpResponseHandler handler) {
		client.put(getAbsoluteApiUrl(partUrl), params, handler);
	}

	public static void setApiUrl(String apiUrl) {
		API_URL = apiUrl;
	}

	public static void setHttpClient(AsyncHttpClient c) {
		client = c;
		// client.addHeader("Accept-Language", Locale.getDefault().toString());
		// client.addHeader("Host", HOST);
		// client.addHeader("Connection", "Keep-Alive");
		// client.getHttpClient().getParams()
		// .setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);

		// setUserAgent(ApiClientHelper.getUserAgent(AppContext.getInstance()));
	}

	public static void setUserAgent(String userAgent) {
		client.setUserAgent(userAgent);
	}

	public static void setCookie(String cookie) {
		client.addHeader("Cookie", cookie);
	}

	private static String appCookie;

	public static void cleanCookie() {
		appCookie = "";
	}

	public static void setHeader(String[] params) {
		client.addHeader(params[0], params[1]);

	}
}
