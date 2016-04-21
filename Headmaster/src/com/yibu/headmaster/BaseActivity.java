package com.yibu.headmaster;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.jzjf.headmaster.R;
import com.lidroid.xutils.ViewUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;
import com.yibu.headmaster.utils.LogUtil;
import com.yibu.headmaster.utils.ToastUtil;

public abstract class BaseActivity extends FragmentActivity implements OnClickListener {

	private FrameLayout arrowButton;

	public TextView baseTitle;

	public TextView baseRight;

	public FrameLayout content;

	protected String result = "";
	protected String msg = "";
	protected String extra = "";

	protected static int screenWidth;
	protected static int screenHeight;
	protected static float screenDensity;
	protected static int densityDpi;

	protected AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			String value = parseJson(responseBody);
			if (!TextUtils.isEmpty(msg)) {
				// 加载失败，弹出失败对话框
				ToastUtil.showToast(BaseActivity.this, msg);
			} else {
				processSuccess(value);

			}

		}

		private String parseJson(byte[] responseBody) {
			String value = null;
			JSONObject dataObject = null;
			JSONArray dataArray = null;
			String dataString = null;
			try {

				JSONObject jsonObject = new JSONObject(new String(responseBody));
				LogUtil.print("json-->"+new String(responseBody));
				result = jsonObject.getString("type");
				msg = jsonObject.getString("msg");
				extra = jsonObject.getString("extra");
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
			processFailure();
		}
	};

	@Override
	protected void onCreate(Bundle paramBundle) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(paramBundle);
		setContentView(R.layout.activity_base);
		ViewUtils.inject(this);

		if ((screenHeight <= 0) || (screenWidth <= 0)) {
			DisplayMetrics metric = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(metric);
			screenWidth = metric.widthPixels; // 屏幕宽度（像素）
			screenHeight = metric.heightPixels; // 屏幕高度（像素）
			screenDensity = metric.density; // 屏幕密度（0.75 / 1.0 / 1.5）
			densityDpi = metric.densityDpi;
		}

		content = (FrameLayout) findViewById(R.id.fl_base_content);
		arrowButton = (FrameLayout) findViewById(R.id.ib_base_arrow);
		baseTitle = (TextView) findViewById(R.id.tv_base_title);
		baseRight = (TextView) findViewById(R.id.tv_base_right);

		initView();
		initListener();
		initData();
		baseRight.setOnClickListener(this);
		arrowButton.setOnClickListener(this);
		overridePendingTransition(R.anim.next_enter, R.anim.next_enter);
	}

	@Override
	protected void onStart() {
		super.onStart();

	}

	@Override
	protected void onResume() {
		super.onResume();

		// 友盟session统计
		MobclickAgent.onResume(this);
		// 极光统计
		JPushInterface.onResume(this);

	}

	@Override
	protected void onPause() {
		super.onPause();

		// 友盟session统计
		MobclickAgent.onPause(this);
		// 极光统计
		JPushInterface.onPause(this);

	}

	@Override
	protected void onStop() {
		overridePendingTransition(R.anim.pre_enter, R.anim.pre_exit);
		super.onStop();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		LogUtil.print("base----"+v.getId());
		if (R.id.ib_base_arrow == v.getId()) {
			finish();
		}

	}

	public void setSonsTitle(String title) {
		baseTitle.setText(title);
	}

	// 关于我们 页面加载
	protected void addView(int layoutId) {
		LinearLayout.LayoutParams params = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		LayoutInflater inflater = getLayoutInflater();
		content.addView(inflater.inflate(layoutId, null), params);
	}

	protected SpannableString setHint(int stringId) {
		SpannableString name = new SpannableString(getString(stringId));
		name.setSpan(new AbsoluteSizeSpan(15, true), 0, name.length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return name;
	}

	protected abstract void initView();

	protected  void initListener(){
		
	}

	protected abstract void initData();

	public abstract void processSuccess(String data);

	public abstract void processFailure();

}
