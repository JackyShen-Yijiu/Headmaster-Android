package com.yibu.headmaster;

import android.view.View;

public class LeftSettingActivity extends BaseActivity {

	private View view;

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		view = View.inflate(getBaseContext(), R.layout.left_setting, null);
		content.addView(view);
	}

	@Override
	protected void initListener() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void processSuccess(String data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processFailure() {
		// TODO Auto-generated method stub

	}

}
