package com.jzjf.headmaster.api;

import org.apache.http.Header;

import com.jzjf.headmaster.utils.LogUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class HeadmasterApi {

	String data;
	AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
//			getData();
			setData(new String(responseBody));
			LogUtil.print(data + "");
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			data = "error";
//			getData();
		}
	};

	public void setData(String data1){
		this.data = data1;
		
	}
	public String getData(){
		return data;
	}
	public void  getPostDataWithNoParams(String url) {
		ApiHttpClient.post(url, handler);	
	}

	public void getPostDataWithParams(String url, RequestParams params) {

		ApiHttpClient.post(url, params, handler);
		
	}

	public void getGetDataWithNoParams(String url) {

		ApiHttpClient.get(url, handler);	
	}

	public void getGetDataWithParams(String url, RequestParams params) {

		ApiHttpClient.get(url, params, handler);
		
	}
}
