package com.yibu.headmaster;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.yibu.headmaster.global.HeadmasterApplication;

public class LeftSettingActivity extends BaseActivity implements
		OnClickListener {

	private ImageView ib_base_arrow;
	private RelativeLayout setting_aboutus;
	private RelativeLayout setting_callback;
	private RelativeLayout setting_rate;
	private View view;

	@Override
	protected void initView() {
		// View view = inflater.inflate(R.layout.left_setting, container,
		// false);
		// View view =View.inflate(getActivity(), R.layout.left_content, null);
		view = View.inflate(getBaseContext(), R.layout.left_setting, null);
		content.addView(view);
		ib_base_arrow = (ImageView) view.findViewById(R.id.ib_base_arrow);
		setting_aboutus = (RelativeLayout) view
				.findViewById(R.id.setting_aboutus_tv);
		setting_callback = (RelativeLayout) view
				.findViewById(R.id.setting_callback_tv);
		setting_rate = (RelativeLayout) view.findViewById(R.id.setting_rate_tv);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {

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
		// TODO Auto-generated method stub

		setting_aboutus.setOnClickListener(this);
		setting_callback.setOnClickListener(this);
		setting_rate.setOnClickListener(this);
	}

	@Override
	public void processSuccess(String data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processFailure() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}
}
