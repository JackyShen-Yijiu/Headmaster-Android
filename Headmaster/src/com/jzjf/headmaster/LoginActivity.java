package com.jzjf.headmaster;

import java.util.Set;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import com.jzjf.headmaster.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.jzjf.common.Config;
import com.jzjf.headmaster.api.ApiHttpClient;
import com.jzjf.headmaster.bean.UserBean;
import com.jzjf.headmaster.global.HeadmasterApplication;
import com.jzjf.headmaster.utils.CommonUtils;
import com.jzjf.headmaster.utils.JsonUtil;
import com.jzjf.headmaster.utils.LogUtil;
import com.jzjf.headmaster.utils.MD5;
import com.jzjf.headmaster.utils.SharedPreferencesUtil;
import com.jzjf.headmaster.utils.ToastUtil;
import com.jzjf.headmaster.utils.ZProgressHUD;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class LoginActivity extends Activity implements OnClickListener {

	// @ViewInject(R.id.login_logo_iv)
	// private ImageView loginLogo;
	@ViewInject(R.id.login_phone_et)
	private EditText phoneEt;
	@ViewInject(R.id.login_password_et)
	private EditText passwordEt;
	@ViewInject(R.id.login_login_btn)
	private Button loginBtn;
	// @ViewInject(R.id.login_rootview_rl)
	// RelativeLayout rootView;
	@ViewInject(R.id.login_service_phone)
	TextView servicePhone;

	public static final String USER_INFO = "userInfo";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_login);
		ViewUtils.inject(this);
		initData();
		initListener();
	}

	private void initData() {
		String username = SharedPreferencesUtil.getString(getBaseContext(), Config.LAST_LOGIN_ACCOUNT, "");
		String password = SharedPreferencesUtil.getString(getBaseContext(), Config.LAST_LOGIN_PASSWORD, "");
		if(!TextUtils.isEmpty(username)) {
			phoneEt.setText(username);
		}
		if (!TextUtils.isEmpty(password)) {
			passwordEt.setText(password);
		}
	}

	private void initListener() {
		loginBtn.setOnClickListener(this);
		// rootView.setOnClickListener(this);
		servicePhone.setOnClickListener(this);
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
		// case R.id.login_rootview_rl:
		// InputMethodManager imm = (InputMethodManager)
		// getSystemService(Context.INPUT_METHOD_SERVICE);
		// imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		// break;
		case R.id.login_service_phone:
			callPhone();

		default:
			break;
		}
	}

	private void callPhone() {
		String phonenum = CommonUtils.getString(R.string.service_phone_number);

		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_CALL);
		intent.setData(Uri.parse("tel:" + phonenum));
		startActivity(intent);
	}

	private void login() {
		String checkResult = checkLoginInfo();
		if (checkResult == null) {

			AsyncHttpResponseHandler handler = new MyAsyncHttpResponseHandler();

			RequestParams params = new RequestParams();
			params.put("mobile", phoneEt.getText().toString());
			params.put("password", MD5.Md5(passwordEt.getText().toString()));
			ApiHttpClient.post(ApiHttpClient.USER_LOGIN, params, handler);
			ZProgressHUD.getInstance(this).setMessage("登录中...").show();
			// LogUtil.print("login--->"+ZProgressHUD.getInstance(this).isShowing());
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
		public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
			ToastUtil.showToast(LoginActivity.this, "网络异常");
			ZProgressHUD.getInstance(LoginActivity.this).dismiss();
		}

		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
			// 解析数据
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
			} else if (value != null) {
				process(value);
			} else {
				ToastUtil.showToast(LoginActivity.this, "网络异常");
				ZProgressHUD.getInstance(LoginActivity.this).dismiss();
			}

		}

		private void process(String value) {
			UserBean userBean = JsonUtil.parseJsonToBean(value, UserBean.class);
			LogUtil.print("xxxx" + value);
			if (userBean != null) {
				// 保存用户信息
				SharedPreferencesUtil.putString(LoginActivity.this, LoginActivity.USER_INFO, value);
				HeadmasterApplication.app.userInfo = userBean;
				ApiHttpClient.setHeader(new String[] { "authorization", HeadmasterApplication.app.userInfo.token });
//				LogUtil.print("-----" + userBean.userid);
				// new Thread(new Runnable() {
				//
				// @Override
				// public void run() {
				// // 登录环信
				// new UserLogin(LoginActivity.this).userLogin(
				// HeadmasterApplication.app.userInfo.userid,
				// MD5.Md5(passwordEt.getText().toString()),
				// HeadmasterApplication.app.userInfo.name);
				//
				// }
				// }).start();
				
				JPushInterface.setAlias(LoginActivity.this, HeadmasterApplication.app.userInfo.userid,
						new MyTagAliasCallback());
				
				toMainActivity();
			}

		}
	}
	
	private class MyTagAliasCallback implements TagAliasCallback {

		@Override
		public void gotResult(int arg0, String arg1, Set<String> arg2) {
			LogUtil.print("----------TagAliasCallback============");
		}

	}

	protected SpannableString setHint(String stringId) {
		SpannableString name = new SpannableString(stringId);
		name.setSpan(new RelativeSizeSpan(2.5f), 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return name;
	}

	// // 环信登录的回调方法
	// @Override
	// public void loginResult(boolean result, int code, String message) {
	//
	// if (result) {
	// // 保存用户名和密码到本地
	// LogUtil.print("登录环信成功！");
	// runOnUiThread(new Runnable() {
	// @Override
	// public void run() {
	// ZProgressHUD.getInstance(LoginActivity.this).dismiss();
	// toMainActivity();
	// }
	// });
	// } else {
	// LogUtil.print("登录环信失败！");
	// runOnUiThread(new Runnable() {
	// @Override
	// public void run() {
	// ZProgressHUD.getInstance(LoginActivity.this).dismiss();
	// ZProgressHUD.getInstance(LoginActivity.this).show();
	// ZProgressHUD.getInstance(LoginActivity.this)
	// .dismissWithFailure("初始化聊天失败");
	// toMainActivity();
	// }
	// });
	// }
	// }

	private void toMainActivity() {
		SharedPreferencesUtil.putString(getBaseContext(), Config.LAST_LOGIN_ACCOUNT, phoneEt.getText().toString());
		SharedPreferencesUtil.putString(getBaseContext(), Config.LAST_LOGIN_PASSWORD, passwordEt.getText().toString());
		HeadmasterApplication.app.isLogin = true;

		// 转到主界面
		Intent intent = new Intent(LoginActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	// 点击空白处收起键盘
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			View v = getCurrentFocus();
			if (isShouldHideInput(v, ev)) {
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				if (imm != null) {
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				}
			}
			return super.dispatchTouchEvent(ev);
		}
		// 必不可少，否则所有的组件都不会有TouchEvent了
		if (getWindow().superDispatchTouchEvent(ev)) {
			return true;
		}
		return onTouchEvent(ev);
	}

	public boolean isShouldHideInput(View v, MotionEvent event) {
		if (v != null && (v instanceof EditText)) {
			int[] leftTop = { 0, 0 };
			// 获取输入框当前的location位置
			v.getLocationInWindow(leftTop);
			int top = leftTop[1];
			int bottom = top + v.getHeight();
			if (event.getY() > top && event.getY() < bottom) {
				// 点击的是输入框区域上下范围之内的
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

}