package com.yibu.headmaster;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import com.yibu.headmaster.global.HeadmasterApplication;

public class LeftSettingActivity extends BaseActivity implements
		OnClickListener {

	private RelativeLayout setting_aboutus;
	private RelativeLayout setting_callback;
	private RelativeLayout setting_rate;
	private View view;
	private CheckBox appointmentCk;
	private CheckBox messageCk;

	@Override
	protected void initView() {
		view = View.inflate(getBaseContext(), R.layout.left_setting, null);
		content.addView(view);
		setSonsTitle(getString(R.string.setting_title));
		setting_aboutus = (RelativeLayout) view
				.findViewById(R.id.setting_aboutus_tv);
		setting_callback = (RelativeLayout) view
				.findViewById(R.id.setting_callback_tv);
		setting_rate = (RelativeLayout) view.findViewById(R.id.setting_rate_tv);

		appointmentCk = (CheckBox) findViewById(R.id.imageView_open);
		messageCk = (CheckBox) findViewById(R.id.imageView_open1);

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
		case R.id.setting_rate_tv:
			break;
		}
	}

	@Override
	protected void initListener() {

		setting_aboutus.setOnClickListener(this);
		setting_callback.setOnClickListener(this);
		setting_rate.setOnClickListener(this);
	}

	@Override
	public void processSuccess(String data) {
	}

	@Override
	public void processFailure() {
	}

	@Override
	protected void initData() {

	}
}
