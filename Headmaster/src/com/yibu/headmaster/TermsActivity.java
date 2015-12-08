package com.yibu.headmaster;

import android.annotation.SuppressLint;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class TermsActivity extends BaseActivity implements OnClickListener {

	private WebView webView;
	private View view;

	@Override
	protected void initView() {
		view = View.inflate(getBaseContext(), R.layout.activity_question, null);
		content.addView(view);
		webView = (WebView) findViewById(R.id.question_webview);
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void initListener() {
		setSonsTitle(getString(R.string.allow_protocol));
		String url = "http://www.ifanying.com/userAgreement.html";
		WebSettings webSettings = webView.getSettings();
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return false;
			}
		});

		webSettings.setJavaScriptEnabled(true);
		webView.loadUrl(url);
	}

	@Override
	public void onClick(View v) {
		if (!onClickSingleView()) {
			return;
		}
		switch (v.getId()) {
		case R.id.ib_base_arrow:
			finish();
			break;
		}
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void processSuccess(String data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processFailure() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onPause() {
		webView.onPause();
		super.onPause();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
			webView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
