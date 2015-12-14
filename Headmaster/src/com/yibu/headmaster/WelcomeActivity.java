package com.yibu.headmaster;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.loopj.android.http.RequestParams;
import com.sft.api.UserLogin;
import com.sft.listener.EMLoginListener;
import com.yibu.common.Config;
import com.yibu.headmaster.api.ApiHttpClient;
import com.yibu.headmaster.bean.UserBean;
import com.yibu.headmaster.global.HeadmasterApplication;
import com.yibu.headmaster.utils.JsonUtil;
import com.yibu.headmaster.utils.LogUtil;
import com.yibu.headmaster.utils.MD5;
import com.yibu.headmaster.utils.SharedPreferencesUtil;
import com.yibu.headmaster.utils.ZProgressHUD;

public class WelcomeActivity extends BaseActivity implements EMLoginListener {

	private HeadmasterApplication app = HeadmasterApplication.app;
	private String password;
	private String lastLoginPhone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);

		ImageView image = (ImageView) findViewById(R.id.welcome_image);
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) image
				.getLayoutParams();
		params.width = (int) (screenWidth * 5 / 12f);
		params.height = (int) (params.width * 243 / 509f);

		ImageView devider = (ImageView) findViewById(R.id.welcom_devider);
		RelativeLayout.LayoutParams deviderparams = (RelativeLayout.LayoutParams) devider
				.getLayoutParams();
		deviderparams.height = (int) (screenHeight * 41 / 128f);
	}

	@Override
	protected void initView() {

	}

	@Override
	protected void initListener() {

	}

	@Override
	protected void initData() {
		app.userInfo = null;
		app.isLogin = false;
		lastLoginPhone = SharedPreferencesUtil.getString(getBaseContext(),
				Config.LAST_LOGIN_ACCOUNT, "");
		password = SharedPreferencesUtil.getString(getBaseContext(),
				Config.LAST_LOGIN_PASSWORD, "");

		LogUtil.print("用户名：密码：" + lastLoginPhone + password);
		if (!TextUtils.isEmpty(lastLoginPhone) && !TextUtils.isEmpty(password)) {
			login(lastLoginPhone, password);
		} else {
			// 转到主界面
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					Intent intent = new Intent(WelcomeActivity.this,
							LoginActivity.class);
					startActivity(intent);
					WelcomeActivity.this.finish();
				}
			}, 1000);
		}
	}

	private void login(String phone, String password) {

		RequestParams params = new RequestParams();
		params.put("mobile", phone);
		params.put("password", MD5.Md5(password));
		ApiHttpClient.post("userinfo/userlogin", params, handler);

	}

	@Override
	public void processSuccess(String data) {
		UserBean userBean = JsonUtil.parseJsonToBean(data, UserBean.class);

		if (userBean != null) {
			// 保存用户信息
			SharedPreferencesUtil.putString(WelcomeActivity.this,
					LoginActivity.USER_INFO, data);
			app.userInfo = userBean;
			ApiHttpClient.setHeader(new String[] { "authorization",
					HeadmasterApplication.app.userInfo.token });

			// 登录环信
			new UserLogin(WelcomeActivity.this).userLogin(
					HeadmasterApplication.app.userInfo.userid,
					MD5.Md5(password), HeadmasterApplication.app.userInfo.name);
		}

	}

	@Override
	public void processFailure() {
		// 登录失败后，转到登录界面手动登录
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				Intent intent = new Intent(WelcomeActivity.this,
						LoginActivity.class);
				startActivity(intent);
				WelcomeActivity.this.finish();
			}
		}, 1000);

	}

	// 环信登录的回调方法
	@Override
	public void loginResult(boolean result, int code, String message) {

		if (result) {
			// 保存用户名和密码到本地
			SharedPreferencesUtil.putString(getBaseContext(),
					Config.LAST_LOGIN_ACCOUNT, lastLoginPhone);
			SharedPreferencesUtil.putString(getBaseContext(),
					Config.LAST_LOGIN_PASSWORD, password);

			LogUtil.print("登录环信成功！");
			HeadmasterApplication.app.isLogin = true;
			// 转到主界面
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					Intent intent = new Intent(WelcomeActivity.this,
							MainActivity.class);
					startActivity(intent);
					WelcomeActivity.this.finish();
				}
			}, 1000);
		} else {
			LogUtil.print("登录环信失败！");
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					ZProgressHUD.getInstance(WelcomeActivity.this).show();
					ZProgressHUD.getInstance(WelcomeActivity.this)
							.dismissWithFailure("初始化聊天失败");
				}
			});
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					Intent intent = new Intent(WelcomeActivity.this,
							LoginActivity.class);
					startActivity(intent);
					WelcomeActivity.this.finish();
				}
			}, 1000);
		}
	}

}
