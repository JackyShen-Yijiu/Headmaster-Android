package com.yibu.headmaster.base.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.yibu.headmaster.AssessActivity;
import com.yibu.headmaster.R;
import com.yibu.headmaster.adapter.AssessAdapter;
import com.yibu.headmaster.api.ApiHttpClient;
import com.yibu.headmaster.base.BasePager;
import com.yibu.headmaster.bean.AssessBean;
import com.yibu.headmaster.bean.AssessBean.Commentlist;
import com.yibu.headmaster.global.HeadmasterApplication;
import com.yibu.headmaster.lib.pulltorefresh.PullToRefreshBase.OnLastItemVisibleListener;
import com.yibu.headmaster.lib.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.yibu.headmaster.lib.pulltorefresh.PullToRefreshListView;
import com.yibu.headmaster.utils.JsonUtil;

public class AssessDetailPager extends BasePager implements OnClickListener {

	private View view;
	private ArrayList<Commentlist> list = new ArrayList<Commentlist>();
	private AssessAdapter adapter;
	private int curpage = 1;
	private int commentlevel = 1;
	private View viewHeader;
	// 刷新
	private ProgressBar progressBar_main;
	private PullToRefreshListView pullToRefreshListView;
	private ListView list_assess;

	private int searchtype = 1; // 查询时间类型：1 今天2 昨天 3 一周 4 本月5本年

	public AssessDetailPager(Context context) {
		super(context);
	}

	@Override
	public View initView() {
		view = View.inflate(mContext, R.layout.assess_main, null);
		pullToRefreshListView = (PullToRefreshListView) view
				.findViewById(R.id.assess_pullToRefreshListView);
		progressBar_main = (ProgressBar) view
				.findViewById(R.id.assess_progressBar_main);
		list_assess = pullToRefreshListView.getRefreshableView();
		list_assess.setCacheColorHint(Color.TRANSPARENT);
		list_assess.setDividerHeight(0);

		viewHeader = View.inflate(mContext, R.layout.assess_head_view, null);
		list_assess.addHeaderView(viewHeader);
		return view;
	}

	@Override
	public void initData() {
		adapter = new AssessAdapter(mContext, list);
		list_assess.setAdapter(adapter);

		// 给PullToRefreshListView设置监听器
		pullToRefreshListView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				curpage = 1;
				loadNetworkData();
				pullToRefreshListView.onRefreshComplete();
			}
		});
		pullToRefreshListView
				.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
					@Override
					public void onLastItemVisible() {
						curpage++;
						loadNetworkData();
					}
				});
		loadNetworkData();
	}

	private void loadNetworkData() {

		ApiHttpClient.get("statistics/commentdetails?userid="
				+ HeadmasterApplication.app.userInfo.userid + "&schoolid="
				+ HeadmasterApplication.app.userInfo.driveschool.schoolid
				+ "&index=" + curpage + "&count=10&searchtype=" + searchtype
				+ "&commentlevel=" + commentlevel, handler);
	}

	@Override
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
		// 加载
		AssessBean assessBean = JsonUtil
				.parseJsonToBean(data, AssessBean.class);

		List<Commentlist> commentlist = null;
		if (assessBean != null) {
			commentlist = assessBean.commentlist;
		}
		if (curpage == 1) {
			list.clear();
		} else {
			list.addAll(commentlist);
			// adapter.reloadListView(newsBean, false);
		}
		if (adapter != null) {
		}
		list.addAll(commentlist);
		adapter.notifyDataSetChanged();

		progressBar_main.setVisibility(View.GONE);
	}
}
