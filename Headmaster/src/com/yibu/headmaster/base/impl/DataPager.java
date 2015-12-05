package com.yibu.headmaster.base.impl;

import android.content.Context;
import android.view.View;

import com.lidroid.xutils.ViewUtils;
import com.yibu.headmaster.R;
import com.yibu.headmaster.base.BasePager;
import com.yibu.headmaster.global.HeadmasterApplication;
import com.yibu.headmaster.utils.LogUtil;

public class DataPager extends BasePager {

	public DataPager(Context context) {
		super(context);
	}

	@Override
	public void initData() {
		LogUtil.print("数据页");
	}

	@Override
	public View initView() {
		View view = View.inflate(HeadmasterApplication.getContext(),
				R.layout.datas_information, null);
		ViewUtils.inject(this, view);

		return view;
	}

	@Override
	public void process(String data) {
		// TODO Auto-generated method stub

	}

}
