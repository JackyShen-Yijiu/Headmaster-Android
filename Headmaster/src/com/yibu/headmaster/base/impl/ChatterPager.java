package com.yibu.headmaster.base.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.easemob.chat.EMConversation;
import com.yibu.headmaster.base.BasePager;
import com.yibu.headmaster.utils.LogUtil;

public class ChatterPager extends BasePager {

	private ListView messageListView;
	// private ChatAllHistoryAdapter adapter;
	private List<EMConversation> conversationList = new ArrayList<EMConversation>();

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
