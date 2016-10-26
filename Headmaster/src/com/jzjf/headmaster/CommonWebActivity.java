package com.jzjf.headmaster;

import com.jzjf.headmaster.R;
import com.jzjf.headmaster.api.ApiHttpClient;
import com.jzjf.headmaster.global.HeadmasterApplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CommonWebActivity extends BaseActivity implements OnClickListener {

	private WebView webview;
	private ProgressBar progress;
	private Button reloadBtn;
	private TextView errorTv;
	private LinearLayout errorLayout;
	
	private View view;
	
	private String url = ApiHttpClient.WEB_URL + ApiHttpClient.WEB_COMPLAINT_LIST 
			+ HeadmasterApplication.app.userInfo.userid;
	
	@Override
	protected void initView() {
		view = View.inflate(getBaseContext(), R.layout.activity_common_web, null);
		content.addView(view);
		setSonsTitle(getString(R.string.complain));
		baseRight.setVisibility(View.GONE);
		
		webview = (WebView) view.findViewById(R.id.common_webview);
		progress = (ProgressBar) view.findViewById(R.id.common_progress);
		reloadBtn = (Button) view.findViewById(R.id.common_reload_btn);
		reloadBtn.setOnClickListener(this);
		errorTv = (TextView) view.findViewById(R.id.common_error_tv);
		errorLayout = (LinearLayout) view.findViewById(R.id.common_error_ll);
	}
	
	@SuppressLint("SetJavaScriptEnabled") 
	@Override
	protected void initData() {
		
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
			ConnectivityManager connectivityManager = (ConnectivityManager) this
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivityManager != null) {
				NetworkInfo ni = connectivityManager.getActiveNetworkInfo();
				if (ni == null || ni.getState() != NetworkInfo.State.CONNECTED) {
					return false;
				}
			}
			return true;
		} catch(Exception e) {
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
		case R.id.ib_base_arrow: 
			finish();
			break;
		case R.id.common_reload_btn:
			errorLayout.setVisibility(View.GONE);
			progress.setVisibility(View.VISIBLE);
			webview.reload();
			break;
		}
	}

	@Override
	public void processSuccess(String data) {
		
	}

	@Override
	public void processFailure() {
		
	}
	
	
}
