package com.yibu.headmaster;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.yibu.headmaster.utils.ToastUtil;

public abstract class BaseActivity extends Activity implements OnClickListener {

	// @ViewInject(R.id.ib_base_arrow)
	private ImageButton arrowButton;

	// @ViewInject(R.id.tv_base_title)
	public TextView baseTitle;

	// @ViewInject(R.id.tv_base_right)
	public TextView baseRight;

	// @ViewInject(R.id.fl_base_content)
	public FrameLayout content;

	protected String result = "";
	protected String msg = "";

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
			processFailure();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);
		ViewUtils.inject(this);

		content = (FrameLayout) findViewById(R.id.fl_base_content);
		arrowButton = (ImageButton) findViewById(R.id.ib_base_arrow);
		baseTitle = (TextView) findViewById(R.id.tv_base_title);
		baseRight = (TextView) findViewById(R.id.tv_base_right);

		arrowButton.setOnClickListener(this);
		initView();
		initListener();
		initData();
		overridePendingTransition(R.anim.next_enter, R.anim.next_enter);
	}

	@Override
	protected void onStart() {
		super.onStart();

	}

	@Override
	protected void onStop() {
		super.onStop();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		overridePendingTransition(R.anim.pre_enter, R.anim.pre_exit);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.ib_base_arrow:
			finish();
			break;

		default:
			break;
		}
	}

	public void setSonsTitle(String title) {
		baseTitle.setText(title);
	}

	protected abstract void initView();

	protected abstract void initListener();

	protected abstract void initData();

	public abstract void processSuccess(String data);

	public abstract void processFailure();

}
