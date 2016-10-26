package com.jzjf.headmaster;

import java.util.Set;

import com.jzjf.headmaster.R;
import com.loopj.android.http.RequestParams;
import com.sft.listener.EMLoginListener;
import com.jzjf.common.Config;
import com.jzjf.headmaster.api.ApiHttpClient;
import com.jzjf.headmaster.bean.UserBean;
import com.jzjf.headmaster.global.HeadmasterApplication;
import com.jzjf.headmaster.utils.JsonUtil;
import com.jzjf.headmaster.utils.LogUtil;
import com.jzjf.headmaster.utils.MD5;
import com.jzjf.headmaster.utils.SharedPreferencesUtil;
import com.jzjf.headmaster.utils.ZProgressHUD;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class WelcomeActivity extends BaseActivity implements EMLoginListener {

	private HeadmasterApplication app = HeadmasterApplication.app;
	private String password;
	private String lastLoginPhone;

	public static String IS_APP_FIRST_OPEN = "is_app_first_open";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);

//		ImageView image = (ImageView) findViewById(R.id.welcome_image);
//		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) image
//				.getLayoutParams();
//		params.width = (int) (screenWidth * 5 / 12f);
//		params.height = (int) (params.width * 243 / 509f);
//
//		ImageView devider = (ImageView) findViewById(R.id.welcom_devider);
//		RelativeLayout.LayoutParams deviderparams = (RelativeLayout.LayoutParams) devider
//				.getLayoutParams();
//		deviderparams.height = (int) (screenHeight * 41 / 128f);
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

		boolean isFirstOpen = SharedPreferencesUtil.getBoolean(
				getApplicationContext(), IS_APP_FIRST_OPEN, true);
		if (isFirstOpen) {
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					Intent intent = new Intent(WelcomeActivity.this,
							GuideActivity.class);
					startActivity(intent);
					WelcomeActivity.this.finish();
				}
			}, 1000);
			// startActivity(new Intent(WelcomeActivity.this,
			// GuideActivity.class));
		} else {
			lastLoginPhone = SharedPreferencesUtil.getString(getBaseContext(),
					Config.LAST_LOGIN_ACCOUNT, "");
			password = SharedPreferencesUtil.getString(getBaseContext(),
					Config.LAST_LOGIN_PASSWORD, "");

//			LogUtil.print("用户名：密码：" + lastLoginPhone + password);
			if (!TextUtils.isEmpty(lastLoginPhone)
					&& !TextUtils.isEmpty(password)) {
				login(lastLoginPhone, password);
			} else {
				// 转到登录界面
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

	private void login(String phone, String password) {

		RequestParams params = new RequestParams();
		params.put("mobile", phone);
		params.put("password", MD5.Md5(password));
		ApiHttpClient.post(ApiHttpClient.USER_LOGIN, params, handler);
		
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
			
			JPushInterface.setAlias(this, app.userInfo.userid, new MyTagAliasCallback());
			
			// 登录环信
//			new UserLogin(WelcomeActivity.this).userLogin(
//					HeadmasterApplication.app.userInfo.userid,
//					MD5.Md5(password), HeadmasterApplication.app.userInfo.name);
			loginResult(true, 0, "");
		}

	}
	
	private class MyTagAliasCallback implements TagAliasCallback {

		@Override
		public void gotResult(int arg0, String arg1, Set<String> arg2) {
			LogUtil.print("----------TagAliasCallback============");
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

	private Handler myHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
			startActivity(intent);
			WelcomeActivity.this.finish();
		};
	};

	// 环信登录的回调方法
	@Override
	public void loginResult(boolean result, int code, String message) {

		if (result) {
			// 保存用户名和密码到本地
			SharedPreferencesUtil.putString(getBaseContext(),
					Config.LAST_LOGIN_ACCOUNT, lastLoginPhone);
			SharedPreferencesUtil.putString(getBaseContext(),
					Config.LAST_LOGIN_PASSWORD, password);

//			LogUtil.print("登录环信成功！");
			HeadmasterApplication.app.isLogin = true;
			// 转到主界面
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						Thread.sleep(1000);
						myHandler.sendMessage(myHandler.obtainMessage());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}).start();
			// new Handler().postDelayed(new Runnable() {
			//
			// @Override
			// public void run() {
			// Intent intent = new Intent(WelcomeActivity.this,
			// MainActivity.class);
			// startActivity(intent);
			// WelcomeActivity.this.finish();
			// }
			// }, 1000);

		} else {
//			LogUtil.print("登录环信失败！");
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					ZProgressHUD.getInstance(WelcomeActivity.this).show();
					ZProgressHUD.getInstance(WelcomeActivity.this)
							.dismissWithFailure("初始化聊天失败");
				}
			});
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						Thread.sleep(1000);
						myHandler.sendMessage(myHandler.obtainMessage());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
	}

}
