package com.yibu.headmaster;

import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import com.jzjf.headmaster.R;

public class NewsDetailActivity extends BaseActivity {

	private WebView webview;

	private ProgressBar progress;

	private WebSettings settings;

	@Override
	protected void initView() {
		View view = View.inflate(getBaseContext(),
				R.layout.news_information_detail, null);
		content.addView(view);
		webview = (WebView) view.findViewById(R.id.news_webview);
		progress = (ProgressBar) view.findViewById(R.id.news_progress);
	}

	@Override
	protected void initListener() {

	}

	@Override
	protected void initData() {
		baseTitle.setText("资讯详情");
		baseRight.setVisibility(View.INVISIBLE);

		webview.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				// 页面加载完成时，回调
				progress.setVisibility(View.GONE);
				super.onPageFinished(view, url);
			}
		});

		settings = webview.getSettings();
		settings.setBuiltInZoomControls(true);
		settings.setUseWideViewPort(true);
		settings.setJavaScriptEnabled(true);

		String url = getIntent().getStringExtra("url");

		webview.loadUrl(url);

	}

	@Override
	public void processSuccess(String data) {

	}

	@Override
	public void processFailure() {

	}

}
