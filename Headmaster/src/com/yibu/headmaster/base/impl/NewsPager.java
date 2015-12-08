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
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yibu.headmaster.NewsDetailActivity;
import com.yibu.headmaster.R;
import com.yibu.headmaster.adapter.NewsInformationAdapter;
import com.yibu.headmaster.api.ApiHttpClient;
import com.yibu.headmaster.base.BasePager;
import com.yibu.headmaster.bean.NewsBean;
import com.yibu.headmaster.global.HeadmasterApplication;
import com.yibu.headmaster.lib.pulltorefresh.PullToRefreshBase.OnLastItemVisibleListener;
import com.yibu.headmaster.lib.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.yibu.headmaster.lib.pulltorefresh.PullToRefreshListView;
import com.yibu.headmaster.utils.JsonUtil;

public class NewsPager extends BasePager {

	@ViewInject(R.id.pullToRefreshListView)
	private PullToRefreshListView pullToRefreshListView;
	private ListView listView_show;
	@ViewInject(R.id.progressBar_main)
	private ProgressBar progressBar_main;

	private NewsInformationAdapter adapter = null;
	private int curpage = 0;
	private ArrayList<NewsBean> totalList;

	public NewsPager(Context context) {
		super(context);
	}

	@Override
	public void initData() {
		listView_show = pullToRefreshListView.getRefreshableView();
		listView_show.setCacheColorHint(Color.TRANSPARENT);
		listView_show.setDividerHeight(0);
		listView_show.setSelector(android.R.color.transparent);

		listView_show.setOnItemClickListener(new ListViewOnItemClickListener());
		listView_show.setSelector(R.drawable.listview_selector);

		totalList = new ArrayList<NewsBean>();
		adapter = new NewsInformationAdapter(mContext, totalList);
		listView_show.setAdapter(adapter);

		// 给PullToRefreshListView设置监听器
		pullToRefreshListView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				curpage = 0;
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
		// 加载数据
		loadNetworkData();
	}

	private void loadNetworkData() {

		ApiHttpClient.get("info/getnews?seqindex=" + curpage + "&count=10",
				handler);
	}

	@Override
	public void process(String data) {

		final ArrayList<NewsBean> newsBean = (ArrayList<NewsBean>) JsonUtil
				.parseJsonToList(data, new TypeToken<List<NewsBean>>() {
				}.getType());

		if (curpage == 0) {
			totalList.clear();
		} else {
			totalList.addAll(newsBean);
			// adapter.reloadListView(newsBean, false);
		}
		if (adapter != null) {
		}
		totalList.addAll(newsBean);
		adapter.notifyDataSetChanged();
		if (progressBar_main.getVisibility() == View.VISIBLE) {
			progressBar_main.setVisibility(View.GONE);
		}
	}

	@Override
	public View initView() {
		View view = View.inflate(HeadmasterApplication.getContext(),
				R.layout.news_information, null);
		ViewUtils.inject(this, view);

		return view;
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
