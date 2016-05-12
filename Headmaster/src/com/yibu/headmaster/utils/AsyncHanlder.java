package com.yibu.headmaster.utils;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.ResponseHandlerInterface;
/**
 * 防止多次请求
 * @author pengdonghua
 *
 */
public class AsyncHanlder extends AsyncHttpResponseHandler{
	
	private String tag;
	
	public AsyncHanlder(String tag){
		this.tag = tag;
	}
	
	@Override
	public void onPostProcessResponse(ResponseHandlerInterface instance,
			HttpResponse response) {
		super.onPostProcessResponse(instance, response);
	}



	@Override
	public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
		OnSuccess11(tag, arg0, arg1, arg2);
	}
	
	public void OnSuccess11(String tag,int arg0,Header[] arg1,byte[] arg2){
		
	}
	

}
