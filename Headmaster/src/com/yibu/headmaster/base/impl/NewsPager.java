package com.yibu.headmaster.base.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yibu.headmaster.NewsDetailActivity;
import com.yibu.headmaster.R;
import com.yibu.headmaster.adapter.NewsInformationAdapter;
import com.yibu.headmaster.api.ApiHttpClient;
import com.yibu.headmaster.base.BasePager;
import com.yibu.headmaster.bean.NewsBean;
import com.yibu.headmaster.global.HeadmasterApplication;
import com.yibu.headmaster.utils.JsonUtil;
import com.yibu.headmaster.utils.ToastUtil;

public class NewsPager extends BasePager {

	@ViewInject(R.id.pullToRefreshListView)
	private PullToRefreshListView pullToRefreshListView;
	private ListView listView_show;
	@ViewInject(R.id.progressBar_main)
	private ProgressBar progressBar_main;

	private NewsInformationAdapter adapter = null;
	private int seqindex = 0;
	private ArrayList<NewsBean> totalList;

	public NewsPager(Context context) {
		super(context);
	}

	@Override
	public View initView() {
		View view = View.inflate(HeadmasterApplication.getContext(),
				R.layout.news_information, null);
		ViewUtils.inject(this, view);

		return view;
	}

	private boolean isLoadMoreData = false;

	@Override
	public void initData() {
		pullToRefreshListView.setMode(Mode.BOTH);
		listView_show = pullToRefreshListView.getRefreshableView();
		listView_show.setCacheColorHint(Color.TRANSPARENT);
		listView_show.setDividerHeight(0);
		// listView_show.setSelector(android.R.color.transparent);

		listView_show.setOnItemClickListener(new ListViewOnItemClickListener());
		listView_show.setSelector(R.drawable.listview_selector);
		// listView_show.setDrawSelectorOnTop(true);

		totalList = new ArrayList<NewsBean>();
		adapter = new NewsInformationAdapter(mContext, totalList);
		listView_show.setAdapter(adapter);

		pullToRefreshListView
				.setOnRefreshListener(new OnRefreshListener<ListView>() {

					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {
						if (pullToRefreshListView.isHeaderShown()) {
							// 下拉刷新
							seqindex = 0;
							loadNetworkData();
						} else {
							// 下拉加载
							seqindex = totalList.get(totalList.size() - 1).seqindex;
							if (seqindex == 0) {
								ToastUtil.showToast(mContext, "没有更多数据了");
								isLoadMoreData = true;
							} else {
							}
							loadNetworkData();
						}
					}
				});

		loadNetworkData();
	}

	private void loadNetworkData() {

		if (pullToRefreshListView.isFooterShown() && seqindex == 0) {
			pullToRefreshListView.postDelayed(new Runnable() {

				@Override
				public void run() {
					pullToRefreshListView.onRefreshComplete();
				}
			}, 100);
		} else {

			ApiHttpClient.get(
					"info/getnews?seqindex=" + seqindex + "&count=10", handler);
		}
	}

	@Override
	public void process(String data) {

		final ArrayList<NewsBean> newsBean = (ArrayList<NewsBean>) JsonUtil
				.parseJsonToList(data, new TypeToken<List<NewsBean>>() {
				}.getType());

		if (seqindex == 0) {
			totalList.clear();
		}
		totalList.addAll(newsBean);
		adapter.notifyDataSetChanged();
		if (progressBar_main.getVisibility() == View.VISIBLE) {
			progressBar_main.setVisibility(View.GONE);
		}
		pullToRefreshListView.onRefreshComplete();
		// seqindex = totalList.get(totalList.size() - 1).seqindex;
	}

	class ListViewOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			NewsBean bean = totalList.get(position);

			Intent intent = new Intent(mContext, NewsDetailActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("url", bean.contenturl);
			mContext.startActivity(intent);
		}

	}

}
