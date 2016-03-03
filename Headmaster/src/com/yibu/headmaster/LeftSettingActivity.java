package com.yibu.headmaster;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;

import com.jzjf.headmaster.R;
import com.loopj.android.http.RequestParams;
import com.yibu.headmaster.api.ApiHttpClient;
import com.yibu.headmaster.global.HeadmasterApplication;
import com.yibu.headmaster.utils.SharedPreferencesUtil;
import com.yibu.headmaster.utils.ToastUtil;

public class LeftSettingActivity extends BaseActivity implements
		OnClickListener {

	private RelativeLayout setting_aboutus;
	private RelativeLayout setting_callback;
	private RelativeLayout setting_rate;
	private View view;
	private CheckBox messageCb;
	private CheckBox commplaintCb;
	private CheckBox studentEnrollCb;

	public static String SETTING_MESSAGE = "setting_message";
	public static String SETTING_COMMPLAINT = "setting_commplaint";
	public static String SETTING_STUDENT_ENROLL = "setting_student_enroll";
	private int messageInt;
	private int complaintInt;
	private int studentEnrollInt;

	@Override
	protected void initView() {
		view = View.inflate(getBaseContext(), R.layout.left_setting, null);
		content.addView(view);
		setSonsTitle(getString(R.string.setting_title));
		setting_aboutus = (RelativeLayout) view
				.findViewById(R.id.setting_aboutus_tv);
		setting_callback = (RelativeLayout) view
				.findViewById(R.id.setting_callback_tv);

		messageCb = (CheckBox) findViewById(R.id.setting_every_day_cb);
		commplaintCb = (CheckBox) findViewById(R.id.setting_commplaint_cb);
		studentEnrollCb = (CheckBox) findViewById(R.id.setting_new_student_enroll);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_base_arrow:
			finish();
			break;
		case R.id.setting_aboutus_tv:
			Intent abouts = new Intent(HeadmasterApplication.getContext(),
					AboutUsActivity.class);
			startActivity(abouts);
			break;
		case R.id.setting_callback_tv:
			Intent callback = new Intent(HeadmasterApplication.getContext(),
					CallBackActivity.class);
			startActivity(callback);
			break;
		default:
			break;
		}
	}

	@Override
	protected void initListener() {

		setting_aboutus.setOnClickListener(this);
		setting_callback.setOnClickListener(this);

		messageCb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				RequestParams params = new RequestParams();
				if (isChecked) {
					messageInt = 1;
				} else {
					messageInt = 0;
				}
				params.put("newmessagereminder", messageInt);
				params.put("complaintreminder", complaintInt);
				params.put("applyreminder", studentEnrollInt);
				params.put("userid", HeadmasterApplication.app.userInfo.userid);
				ApiHttpClient.post("userinfo/personalsetting", params, handler);
			}
		});
		commplaintCb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				RequestParams params = new RequestParams();
				if (isChecked) {
					complaintInt = 1;
				} else {
					complaintInt = 0;
				}
				params.put("newmessagereminder", messageInt);
				params.put("complaintreminder", complaintInt);
				params.put("applyreminder", studentEnrollInt);
				params.put("userid", HeadmasterApplication.app.userInfo.userid);
				ApiHttpClient.post("userinfo/personalsetting", params, handler);
			}
		});
		studentEnrollCb
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						RequestParams params = new RequestParams();
						if (isChecked) {
							studentEnrollInt = 1;
						} else {
							studentEnrollInt = 0;
						}
						params.put("newmessagereminder", messageInt);
						params.put("complaintreminder", complaintInt);
						params.put("applyreminder", studentEnrollInt);
						params.put("userid",
								HeadmasterApplication.app.userInfo.userid);
						ApiHttpClient.post("userinfo/personalsetting", params,
								handler);
					}
				});
	}

	@Override
	public void processSuccess(String data) {
		// 设置成功，保存到本地
		SharedPreferencesUtil.putInt(getBaseContext(), SETTING_MESSAGE,
				messageInt);
		SharedPreferencesUtil.putInt(getBaseContext(), SETTING_COMMPLAINT,
				complaintInt);
		SharedPreferencesUtil.putInt(getBaseContext(), SETTING_STUDENT_ENROLL,
				studentEnrollInt);
	}

	@Override
	public void processFailure() {
		ToastUtil.showToast(getBaseContext(), "网络异常");
	}

	@Override
	protected void initData() {
		messageInt = SharedPreferencesUtil.getInt(getBaseContext(),
				SETTING_MESSAGE, 1);
		complaintInt = SharedPreferencesUtil.getInt(getBaseContext(),
				SETTING_COMMPLAINT, 1);
		studentEnrollInt = SharedPreferencesUtil.getInt(getBaseContext(),
				SETTING_STUDENT_ENROLL, 1);

		if (messageInt == 1) {
			messageCb.setChecked(true);
		} else {
			messageCb.setChecked(false);
		}
		if (complaintInt == 1) {
			commplaintCb.setChecked(true);
		} else {
			commplaintCb.setChecked(false);
		}
		if (studentEnrollInt == 1) {
			studentEnrollCb.setChecked(true);
		} else {
			studentEnrollCb.setChecked(false);
		}
	}
}
