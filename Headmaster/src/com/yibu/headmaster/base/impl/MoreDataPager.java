package com.yibu.headmaster.base.impl;

import android.content.Context;
import android.view.View;

import com.yibu.headmaster.R;
import com.yibu.headmaster.base.BasePager;

public class MoreDataPager extends BasePager {

	public MoreDataPager(Context context) {
		super(context);
	}

	@Override
	public View initView() {
		View view = View.inflate(mContext, R.layout.activity_data_chart, null);
		return view;
	}

	@Override
	public void initData() {
		super.initData();
	}

	@Override
	public void process(String data) {

	}

}
