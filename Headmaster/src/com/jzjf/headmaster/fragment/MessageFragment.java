package com.jzjf.headmaster.fragment;

import com.jzjf.headmaster.R;
import com.jzjf.headmaster.api.ApiHttpClient;
import com.jzjf.headmaster.global.HeadmasterApplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MessageFragment extends Fragment implements OnClickListener {

	private WebView webview;
	private ProgressBar progress;
	private Button reloadBtn;
	private TextView errorTv;
	private LinearLayout errorLayout;
	
	private String url = ApiHttpClient.WEB_URL + ApiHttpClient.WEB_MESSAGE_LIST 
			+ HeadmasterApplication.app.userInfo.userid;
	
	public MessageFragment(Context context) {
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = View.inflate(this.getActivity(), R.layout.fragment_message, null);
		webview = (WebView) view.findViewById(R.id.message_webview);
		progress = (ProgressBar) view.findViewById(R.id.message_progress);
		reloadBtn = (Button) view.findViewById(R.id.message_reload_btn);
		reloadBtn.setOnClickListener(this);
		errorTv = (TextView) view.findViewById(R.id.message_error_tv);
		errorLayout = (LinearLayout) view.findViewById(R.id.message_error_ll);
		
		initData();
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@SuppressLint("SetJavaScriptEnabled") 
	private void initData() {
		
		webview.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				progress.setVisibility(View.GONE);
				super.onPageFinished(view, url);
			}
			
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if(isNetConnect()) {
					if (!TextUtils.isEmpty(url)) {
						if (url.startsWith("tel:")) { 
					        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url)); 
					        startActivity(intent);
					        return true;
					    }
					    view.loadUrl(url);
					}
				} else {
					errorTv.setText("网络未连接，请连接网络后重试！");
					errorLayout.setVisibility(View.VISIBLE);
					webview.stopLoading();
				}
				return true;
			}
			
			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				if(isNetConnect()) {
					errorTv.setText("页面加载错误，请点击刷新按钮重新加载！");
				} else {
					errorTv.setText("网络未连接，请连接网络后重试！");
				}
				errorLayout.setVisibility(View.VISIBLE);
				webview.stopLoading();
			}
		});
		
		WebSettings settings = webview.getSettings();
		settings.setAllowFileAccess(true);
        //如果访问的页面中有Javascript，则webview必须设置支持Javascript
		settings.setJavaScriptEnabled(true);
		settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		settings.setAllowFileAccess(true);
		settings.setAppCacheEnabled(true);
		settings.setDomStorageEnabled(true);
		settings.setDatabaseEnabled(true);
		webview.setWebChromeClient(new WebChromeClient());
		
		setCookie(url);
		webview.loadUrl(url);
		webview.addJavascriptInterface(this, "wst");
	}

	private boolean isNetConnect() {
		try {
			ConnectivityManager connectivityManager = (ConnectivityManager) this.getActivity()
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivityManager != null) {
				NetworkInfo ni = connectivityManager.getActiveNetworkInfo();
				if (ni == null || ni.getState() != NetworkInfo.State.CONNECTED) {
					return false;
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private void setCookie(String url) {
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setAcceptThirdPartyCookies(webview, true);
        cookieManager.setCookie(url, "token=" + HeadmasterApplication.app.userInfo.token);
        cookieManager.flush();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.message_reload_btn:
			errorLayout.setVisibility(View.GONE);
			progress.setVisibility(View.VISIBLE);
			webview.reload();
			break;
		}
	}

}
