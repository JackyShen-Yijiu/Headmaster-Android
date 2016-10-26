package com.jzjf.headmaster.api;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.jzjf.headmaster.utils.LogUtil;
import com.jzjf.headmaster.utils.TestInternet;

import android.content.Context;
import android.text.TextUtils;

public class ApiHttpClient {

	public static String Base_URL = "http://api.dev.jizhijiafu.cn/";
	
	public static String WEB_URL = "http://web.dev.jizhijiafu.cn";
	
	public static String API_URL = Base_URL + "headmaster/v1/%s";
	
	// 用户登录
	public static String USER_LOGIN = "login";
	
	// 获取主页数据
	public static String MAIN_DATA = "collection";
	
	// 获取公告数量
	public static String BULLETIN_COUNT = "bulletin/count";
	
	// 获取教练反馈
	public static String COACH_FEEDBACK = "coach/feedback";
	
	// 获取资讯列表
	public static String INFO_LIST = "information";
	
	// 统计 本周／本月／本年 报名数据
	public static String COLLECT_APPLY = "collection/apply";
	
	// 统计 科一到科四 考试月份
	public static String COLLECT_EXAM_MONTH = "collection/exam/month";
	
	// 统计 某月的 通过率
	public static String COLLECT_EXAM_PASS = "collection/exam/pass";
	
	// 统计 今天／本周／本月／上周／上月 评价数据
	public static String COLLECT_COMMENT = "collection/comment";
	
	// 获取投诉列表
	public static String COMPLAINT_LIST = "complaint";
	
	// 查询教练列表
	public static String COACH_LIST = "coach";
	
	// 消息提醒设置
	public static String MESSAGE_REMIND = "setting";
	
	// 消息
	public static String WEB_MESSAGE_LIST = "/messages.html?type=2&id=";
	
	// 投诉列表
	public static String WEB_COMPLAINT_LIST = "/complaints.html?id=";
	
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
				String msg = null;
				try {

					JSONObject jsonObject = new JSONObject(new String(responseBody));
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
		LogUtil.print("request url = " + url);
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

	public static void setHeader(String[] params) {
		client.addHeader(params[0], params[1]);

	}
}
