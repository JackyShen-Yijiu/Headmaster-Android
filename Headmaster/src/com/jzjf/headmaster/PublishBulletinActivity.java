package com.jzjf.headmaster;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.google.gson.reflect.TypeToken;
import com.jzjf.headmaster.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.jzjf.headmaster.adapter.BulletinAdapter;
import com.jzjf.headmaster.api.ApiHttpClient;
import com.jzjf.headmaster.bean.BulletinBean;
import com.jzjf.headmaster.bean.UserBean;
import com.jzjf.headmaster.global.HeadmasterApplication;
import com.jzjf.headmaster.utils.JsonUtil;
import com.jzjf.headmaster.utils.LogUtil;
import com.jzjf.headmaster.utils.ToastUtil;
import com.jzjf.headmaster.utils.ZProgressHUD;
import com.jzjf.headmaster.view.QuickReturnListView;
import com.jzjf.headmaster.view.QuickReturnListView.OnRefreshListener;

public class PublishBulletinActivity extends BaseActivity {

	private EditText publishContent;


	private Context mContext = null;

	private int bulletinObject = 2;
	private View view;


	private EditText publishTitle;


	private Button publishBulletin;

	@Override
	protected void initView() {
		view = View.inflate(getBaseContext(), R.layout.publish_bulletin, null);
		content.addView(view);
		mContext = this;

		publishContent = (EditText)findViewById(R.id.et_publish_bulletin_content);
		publishTitle = (EditText)findViewById(R.id.et_publish_bulletin_title);
		publishBulletin = (Button) findViewById(R.id.btn_publish_bulletin);


		setSonsTitle(getString(R.string.publish_bulletin));

		baseRight.setVisibility(View.VISIBLE);
		baseRight.setText(getString(R.string.history_bulletin));
	}

	@Override
	protected void initListener() {
		publishBulletin.setOnClickListener(this);
		publishContent.setOnFocusChangeListener(new OnFocusChangeListener() {

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
	protected void initData() {
		

	}


	@Override
	public void processSuccess(String data) {

		// 加载

	}

	@Override
	public void processFailure() {

	}

	class PublishBulletinOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.tv_base_right) {
				PublishBulletin();
			}
		}

	}

	private void PublishBulletin() {

		String content = publishContent.getText().toString();
		String title = publishTitle.getText().toString();
		if (TextUtils.isEmpty(content.trim())) {

			ZProgressHUD.getInstance(this).show();
			ZProgressHUD.getInstance(this).dismissWithSuccess("请输入公告！");

		} else {
			// 发布公告
			RequestParams params = new RequestParams();
			UserBean userInfo = HeadmasterApplication.app.userInfo;
			if (userInfo != null) {
				LogUtil.print(userInfo.userid);
				params.put("userid", userInfo.userid);
				params.put("schoolid", userInfo.driveschool.schoolid);
				params.put("content", content);
				params.put("bulletobject", bulletinObject);
				params.put("title", title);
			}

			AsyncHttpResponseHandler handler1 = new AsyncHttpResponseHandler() {

				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] responseBody) {

					String msg2 = null;
					String data = null;

					try {
						JSONObject jsonObject = new JSONObject(new String(
								responseBody));
						msg2 = jsonObject.getString("msg");
						data = jsonObject.getString("data");

					} catch (Exception e) {
						e.printStackTrace();
					}
					// 发布公告
					if (!TextUtils.isEmpty(msg2)) {
						// ToastUtil.showToast(mContext, msg2);
						ZProgressHUD.getInstance(PublishBulletinActivity.this)
								.show();
						ZProgressHUD.getInstance(PublishBulletinActivity.this)
								.dismissWithSuccess(msg2);
					}
					if (!TextUtils.isEmpty(data)) {
						ZProgressHUD.getInstance(PublishBulletinActivity.this)
								.show();
						ZProgressHUD.getInstance(PublishBulletinActivity.this)
								.dismissWithSuccess(data);
						// ToastUtil.showToast(mContext, data);

						publishContent.setText("");
						publishTitle.setText("");
						// 隐藏软键盘
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(
								publishContent.getWindowToken(), 0);
					}

				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {

				}
			};

			ApiHttpClient.post("userinfo/publishbulletin", params, handler1);

		}
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		LogUtil.print("----"+v.getId());
		Intent intent = null;
		switch (v.getId()) {
		case R.id.tv_base_right:
			//历史公告
			intent = new Intent(this,BulletinHistoryActivity.class);
			break;
		case R.id.ib_base_arrow :
				finish();
				break;
		case R.id.btn_publish_bulletin:
			//发布公告
			PublishBulletin();
			break;
		default:
			break;
		}
		
		if(intent !=null){
			startActivity(intent);
		}
	}

}
