package com.yibu.headmaster;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.yibu.common.Config;
import com.yibu.headmaster.api.ApiHttpClient;
import com.yibu.headmaster.bean.UserBean;
import com.yibu.headmaster.global.HeadmasterApplication;
import com.yibu.headmaster.utils.JsonUtil;
import com.yibu.headmaster.utils.LogUtil;
import com.yibu.headmaster.utils.MD5;
import com.yibu.headmaster.utils.SharedPreferencesUtil;
import com.yibu.headmaster.utils.ToastUtil;

public class LoginActivity extends Activity implements OnClickListener {

	@ViewInject(R.id.login_logo_iv)
	private ImageView loginLogo;
	@ViewInject(R.id.login_phone_et)
	private EditText phoneEt;
	@ViewInject(R.id.login_password_et)
	private EditText passwordEt;
	@ViewInject(R.id.login_login_btn)
	private Button loginBtn;
	@ViewInject(R.id.login_rootview_rl)
	RelativeLayout rootView;

	public static final String USER_INFO = "userInfo";
	public HeadmasterApplication app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		if (app == null) {
			app = HeadmasterApplication.getInstance();
		}
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		ViewUtils.inject(this);

		initView();
		initListener();
	}

	private void initView() {
		// phoneEt.setHint("男");
	}

	private void initListener() {
		loginBtn.setOnClickListener(this);
		rootView.setOnClickListener(this);
		phoneEt.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				EditText _v = (EditText) v;
				if (!hasFocus) {// 失去焦点
					_v.setHint(_v.getTag().toString());
				} else {
					String hint = _v.getHint().toString();
					_v.setTag(hint);
					_v.setHint("");
				}
			}
		});
		passwordEt.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				EditText _v = (EditText) v;
				if (!hasFocus) {// 失去焦点
					_v.setHint(_v.getTag().toString());
				} else {
					String hint = _v.getHint().toString();
					_v.setTag(hint);
					_v.setHint("");
				}
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_login_btn:
			login();
			break;
		case R.id.login_rootview_rl:
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			break;

		default:
			break;
		}
	}

	private void login() {
		String checkResult = checkLoginInfo();
		if (checkResult == null) {

			AsyncHttpResponseHandler handler = new MyAsyncHttpResponseHandler();

			RequestParams params = new RequestParams();
			params.put("mobile", phoneEt.getText().toString());
			params.put("password", MD5.Md5(passwordEt.getText().toString()));
			ApiHttpClient.post("userinfo/userlogin", params, handler);
		} else {
			ToastUtil.showToast(this, checkResult);
		}

	}

	private String checkLoginInfo() {
		String mobile = phoneEt.getText().toString();
		if (TextUtils.isEmpty(mobile)) {
			return "用户名不能为空";
		}
		String password = passwordEt.getText().toString();
		if (TextUtils.isEmpty(password)) {
			return "密码不能为空";
		}
		return null;
	}

	class MyAsyncHttpResponseHandler extends AsyncHttpResponseHandler {

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			ToastUtil.showToast(LoginActivity.this, "网络异常");
		}

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			// 解析数据
			String value = null;
			JSONObject dataObject = null;
			JSONArray dataArray = null;
			String dataString = null;
			String result = null;
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
			} else if (value != null) {
				process(value);
			} else {
				ToastUtil.showToast(LoginActivity.this, "网络异常");
			}

		}

		private void process(String value) {
			UserBean userBean = JsonUtil.parseJsonToBean(value, UserBean.class);
			LogUtil.print("用户：" + userBean.headportrait);
			// 保存用户名和密码到本地
			SharedPreferencesUtil.putString(getBaseContext(),
					Config.LAST_LOGIN_ACCOUNT, phoneEt.getText().toString());
			SharedPreferencesUtil
					.putString(getBaseContext(), Config.LAST_LOGIN_PASSWORD,
							passwordEt.getText().toString());
			// 保存用户信息
			SharedPreferencesUtil.putString(LoginActivity.this,
					LoginActivity.USER_INFO, value);
			app.userInfo = userBean;
			ApiHttpClient.setHeader(new String[] { "authorization",
					HeadmasterApplication.app.userInfo.token });
			// 转到主界面
			Intent intent = new Intent(LoginActivity.this, MainActivity.class);
			startActivity(intent);
			finish();

		}
	}

	protected SpannableString setHint(String stringId) {
		SpannableString name = new SpannableString(stringId);
		name.setSpan(new RelativeSizeSpan(2.5f), 0, name.length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return name;
	}
}
