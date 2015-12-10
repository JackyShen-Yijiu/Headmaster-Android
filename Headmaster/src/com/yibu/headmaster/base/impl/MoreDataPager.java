package com.yibu.headmaster.base.impl;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.yibu.headmaster.AssessActivity;
import com.yibu.headmaster.R;
import com.yibu.headmaster.base.BasePager;

public class MoreDataPager extends BasePager implements OnClickListener {

	private View view;
	private RelativeLayout relativeLayout_textView_shouke;
	private RelativeLayout relativeLayout_textView_pingjia;

	public MoreDataPager(Context context) {
		super(context);
	}

	@Override
	public View initView() {
		view = View.inflate(mContext, R.layout.activity_data_chart, null);
		relativeLayout_textView_shouke = (RelativeLayout) view
				.findViewById(R.id.relativeLayout_textView_shouke);
		relativeLayout_textView_pingjia = (RelativeLayout) view
				.findViewById(R.id.relativeLayout_textView_pingjia);
		relativeLayout_textView_shouke.setOnClickListener(this);
		relativeLayout_textView_pingjia.setOnClickListener(this);
		return view;
	}

	@Override
	public void initData() {
	}

	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.relativeLayout_textView_shouke:
			Intent shouke = new Intent(mContext, AssessActivity.class);
			shouke.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(shouke);
			break;
		case R.id.relativeLayout_textView_pingjia:
			Intent pingjia = new Intent(mContext, AssessActivity.class);
			pingjia.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(pingjia);
			break;
		}
	}

	@Override
	public void process(String data) {

	}

}
