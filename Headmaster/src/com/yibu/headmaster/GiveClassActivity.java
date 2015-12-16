package com.yibu.headmaster;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.gson.reflect.TypeToken;
import com.yibu.headmaster.adapter.CoachGiveClassAdapter;
import com.yibu.headmaster.api.ApiHttpClient;
import com.yibu.headmaster.bean.CoachGiveClassBean;
import com.yibu.headmaster.global.HeadmasterApplication;
import com.yibu.headmaster.lib.pulltorefresh.PullToRefreshBase.OnLastItemVisibleListener;
import com.yibu.headmaster.lib.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.yibu.headmaster.lib.pulltorefresh.PullToRefreshListView;
import com.yibu.headmaster.utils.JsonUtil;
import com.yibu.headmaster.utils.LogUtil;
import com.yibu.headmaster.utils.ToastUtil;

public class GiveClassActivity extends BaseActivity {

	private View view;

	private ProgressBar progressBar_main;
	private PullToRefreshListView pullToRefreshListView;
	private ListView list_coach;
	private ArrayList<CoachGiveClassBean> list = new ArrayList<CoachGiveClassBean>();

	private CoachGiveClassAdapter adapter;
	private int curpage = 1;
	private int searchtype = 1;
	private boolean hasMoreData = true;

	@Override
	protected void initView() {
		view = View.inflate(getBaseContext(), R.layout.activity_coach_detail,
				null);
		content.addView(view);

		pullToRefreshListView = (PullToRefreshListView) view
				.findViewById(R.id.coach_pullToRefreshListView);
		progressBar_main = (ProgressBar) view
				.findViewById(R.id.coach_progressBar_main);
		list_coach = pullToRefreshListView.getRefreshableView();
		list_coach.setCacheColorHint(Color.TRANSPARENT);
		list_coach.setDividerHeight(0);
		list_coach.setSelector(R.drawable.listview_selector);

		searchtype = getIntent().getIntExtra("title", 1);
		baseTitle.setText(getTitle(searchtype) + "教练详情");
	}

	private String getTitle(int searchtype) {
		String title = null;
		switch (searchtype) {
		case 1:
			title = "今天";
			break;
		case 2:
			title = "昨天";
			break;
		case 3:
			title = "本周";
			break;
		case 4:
			title = "本月";
			break;
		case 5:
			title = "本年";
			break;

		default:
			break;
		}
		return title;

	}

	@Override
	protected void initListener() {

	}

	private boolean isLoadingMore = false;

	@Override
	protected void initData() {

		adapter = new CoachGiveClassAdapter(this, list);
		list_coach.setAdapter(adapter);

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
						if (isLoadingMore) {

							if (!hasMoreData) {
								ToastUtil
										.showToast(getBaseContext(), "没有更多数据了");
							} else {
								curpage++;
								loadNetworkData();
							}
						} else {
							isLoadingMore = true;
						}
					}
				});
		loadNetworkData();

	}

	private void loadNetworkData() {

		LogUtil.print(curpage + "initData" + searchtype);
		ApiHttpClient.get("statistics/coachcoursedetails?userid="
				+ HeadmasterApplication.app.userInfo.userid + "&schoolid="
				+ HeadmasterApplication.app.userInfo.driveschool.schoolid
				+ "&index=" + curpage + "&count=10&searchtype=" + searchtype,
				handler);
	}

	@Override
	public void processSuccess(String data) {

		List<CoachGiveClassBean> coachList = (List<CoachGiveClassBean>) JsonUtil
				.parseJsonToList(data,
						new TypeToken<List<CoachGiveClassBean>>() {
						}.getType());

		if (coachList != null) {
			if (curpage == 1) {
				list.clear();
			}
			if (coachList.size() == 0) {
				hasMoreData = false;
			} else {
				list.addAll(coachList);
				adapter.notifyDataSetChanged();
				progressBar_main.setVisibility(View.GONE);
			}
		}
	}

	@Override
	public void processFailure() {

		ToastUtil.showToast(getBaseContext(), "网络异常");
	}

}
