package com.yibu.headmaster;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yibu.headmaster.adapter.AssessAdapter;
import com.yibu.headmaster.api.ApiHttpClient;
import com.yibu.headmaster.bean.AssessBean;
import com.yibu.headmaster.bean.AssessBean.Commentlist;
import com.yibu.headmaster.global.HeadmasterApplication;
import com.yibu.headmaster.lib.pulltorefresh.PullToRefreshBase.OnLastItemVisibleListener;
import com.yibu.headmaster.lib.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.yibu.headmaster.lib.pulltorefresh.PullToRefreshListView;
import com.yibu.headmaster.utils.JsonUtil;

public class AssessActivity extends BaseActivity {

	private View view;
	private Context mContext;
	private ArrayList<Commentlist> list = new ArrayList<Commentlist>();
	private AssessAdapter adapter;
	private int curpage = 1;
	private int searchtype = 1;
	private int commentlevel = 1;
	private boolean moreData = false;
	private View viewHeader;
	// 刷新
	@ViewInject(R.id.progressBar_main)
	private ProgressBar progressBar_main;
	@ViewInject(R.id.pullToRefreshListView)
	private PullToRefreshListView pullToRefreshListView;
	private ListView list_assess;

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		view = View.inflate(getBaseContext(), R.layout.assess_main, null);
		ViewUtils.inject(this, view);
		mContext = this;
		content.addView(view);
		pullToRefreshListView = (PullToRefreshListView) view
				.findViewById(R.id.pullToRefreshListView);
		list_assess = pullToRefreshListView.getRefreshableView();
		setSonsTitle(getString(R.string.assess));
		viewHeader = View.inflate(getBaseContext(), R.layout.assess_head_view,
				null);
		list_assess.addHeaderView(viewHeader);

	}

	@Override
	protected void initListener() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initData() {

		adapter = new AssessAdapter(this, list);
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
	public void processSuccess(String data) {
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

	@Override
	public void processFailure() {
		// TODO Auto-generated method stub
		progressBar_main.setVisibility(View.VISIBLE);
	}

}
