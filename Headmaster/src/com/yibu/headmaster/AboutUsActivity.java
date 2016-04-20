package com.yibu.headmaster;


import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import cn.sft.baseactivity.util.Util;

import com.jzjf.headmaster.R;
import com.yibu.headmaster.bean.DataBean;

public class AboutUsActivity extends BaseActivity implements OnClickListener {

	private View view;
	private TextView versionTv;
	private TextView protocalTv;
	private Util util;
	@Override
	protected void initView() {
		
		Util util=new Util(this);
		
		view = View.inflate(getBaseContext(), R.layout.left_setting_about_us,
				null);
		content.addView(view);
		setSonsTitle(getString(R.string.setting_aboutus));

		versionTv = (TextView) findViewById(R.id.aboutus_version_tv);
		protocalTv = (TextView) findViewById(R.id.aboutus_protocal_tv);
		
		versionTv.setText(util.getAppVersion());
	}

	@Override
	protected void initListener() {
		protocalTv.setOnClickListener(this);

	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_base_arrow:
			finish();
			break;
		case R.id.aboutus_protocal_tv:
			Intent intent = new Intent(this, TermsActivity.class);
			startActivity(intent);
			break;
		}
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
