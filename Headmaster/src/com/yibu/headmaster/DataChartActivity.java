package com.yibu.headmaster;

import android.view.View;

public class DataChartActivity extends BaseActivity {

	private View view;

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		view = View.inflate(getBaseContext(), R.layout.activity_data_chart,
				null);
		content.addView(view);
	}

	@Override
	protected void initListener() {

	}

	@Override
	protected void initData() {

	}

	@Override
	public void processSuccess(String data) {

	}

	@Override
	public void processFailure() {

	}

}
