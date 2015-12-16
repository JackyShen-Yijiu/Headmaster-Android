package com.yibu.headmaster.base.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.yibu.headmaster.R;
import com.yibu.headmaster.adapter.AssessAdapter;
import com.yibu.headmaster.api.ApiHttpClient;
import com.yibu.headmaster.base.BasePager;
import com.yibu.headmaster.bean.AssessBean;
import com.yibu.headmaster.bean.AssessBean.Commentcount;
import com.yibu.headmaster.bean.AssessBean.Commentlist;
import com.yibu.headmaster.datachart.DountChartDemo;
import com.yibu.headmaster.global.HeadmasterApplication;
import com.yibu.headmaster.lib.pulltorefresh.PullToRefreshBase.OnLastItemVisibleListener;
import com.yibu.headmaster.lib.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.yibu.headmaster.lib.pulltorefresh.PullToRefreshListView;
import com.yibu.headmaster.utils.JsonUtil;
import com.yibu.headmaster.utils.LogUtil;
import com.yibu.headmaster.utils.ToastUtil;

public class AssessDetailPager extends BasePager {

	private View view;
	private ArrayList<Commentlist> list = new ArrayList<Commentlist>();
	private AssessAdapter adapter;
	private int curpage = 1;
	private int commentlevel = 1; // 评价等级 1 差评2 中评 3 好评
	private View viewHeader;
	// 刷新
	private ProgressBar progressBar_main;
	private PullToRefreshListView pullToRefreshListView;
	private ListView list_assess;
	private RelativeLayout relativeLayout_ring;

	private DountChartDemo assessThan;// 评价比列

	private int searchtype = 1; // 查询时间类型：1 今天2 昨天 3 一周 4 本月5本年
	private boolean hasMoreData = true;

	public AssessDetailPager(Context context, int i, int searchtype) {
		super(context);

		switch (i) {
		case 1:
			this.commentlevel = 3;
			break;
		case 3:
			this.commentlevel = 1;
			break;

		default:
			commentlevel = i;
			break;
		}
		this.searchtype = searchtype;
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
		list_assess.setSelector(R.drawable.listview_selector);

		viewHeader = View.inflate(mContext, R.layout.assess_head_view, null);
		relativeLayout_ring = (RelativeLayout) viewHeader
				.findViewById(R.id.RelativeLayout_ring);
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

						if (!hasMoreData) {
							ToastUtil.showToast(mContext, "没有更多数据了");
						} else {
							loadNetworkData();
							curpage++;
						}
					}
				});
		loadNetworkData();
	}

	private void loadNetworkData() {

		if (commentlevel == 4) {

			ApiHttpClient.get("statistics/commentdetails?userid="
					+ HeadmasterApplication.app.userInfo.userid + "&schoolid="
					+ HeadmasterApplication.app.userInfo.driveschool.schoolid
					+ "&index=" + curpage + "&count=10", handler);
		} else {

			LogUtil.print("评论详情：加载第几页：" + curpage);
			ApiHttpClient.get("statistics/commentdetails?userid="
					+ HeadmasterApplication.app.userInfo.userid + "&schoolid="
					+ HeadmasterApplication.app.userInfo.driveschool.schoolid
					+ "&index=" + curpage + "&count=10&searchtype="
					+ searchtype + "&commentlevel=" + commentlevel, handler);
		}

	}

	@Override
	public void process(String data) {
		// 加载
		if (commentlevel == 4) {

		}
		AssessBean assessBean = JsonUtil
				.parseJsonToBean(data, AssessBean.class);
		List<Commentlist> commentlist = null;
		if (assessBean != null) {
			commentlist = assessBean.commentlist;
		}
		if (curpage == 1) {
			list.clear();
		}
		if (commentlist.size() == 0) {
			hasMoreData = false;
		} else {
			list.addAll(commentlist);
			adapter.notifyDataSetChanged();
			progressBar_main.setVisibility(View.GONE);

			// 评价比列--------------------------
			if (assessBean != null) {
				Commentcount commentcount = assessBean.commentcount;

				assessThan = new DountChartDemo(mContext, commentcount);
				relativeLayout_ring.addView(assessThan);
				LayoutParams params = assessThan.getLayoutParams();
				params.height = LayoutParams.MATCH_PARENT;
				params.width = LayoutParams.MATCH_PARENT;
				assessThan.setLayoutParams(params);
			}
		}
	}
}
