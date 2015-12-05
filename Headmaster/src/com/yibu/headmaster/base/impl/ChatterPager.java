package com.yibu.headmaster.base.impl;


import com.yibu.headmaster.base.BasePager;
import com.yibu.headmaster.utils.LogUtil;

import android.content.Context;
import android.view.View;
import android.widget.TextView;


public class ChatterPager extends BasePager {

	public ChatterPager(Context context) {
		super(context);
	}

	@Override
	public void initData() {
		LogUtil.print("数据页");
	}
	
	@Override
	public View initView() {
		TextView view = new TextView(mContext);
		view.setText("数据页");
		return view;
	}

	@Override
	public void process(String data) {
		// TODO Auto-generated method stub
		
	}


}
