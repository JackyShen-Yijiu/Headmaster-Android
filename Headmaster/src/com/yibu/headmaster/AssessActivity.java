package com.yibu.headmaster;

import android.content.Context;
import android.view.View;

public class AssessActivity extends BaseActivity {

	private View view;
	private Context mContext;

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		view = View.inflate(getBaseContext(), R.layout.assess_main, null);
		mContext = this;
		content.addView(view);
		setSonsTitle(getString(R.string.assess));
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
