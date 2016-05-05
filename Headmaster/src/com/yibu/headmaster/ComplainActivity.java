package com.yibu.headmaster;

import java.util.ArrayList;
import java.util.List;


import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.reflect.TypeToken;
import com.jzjf.headmaster.R;
import com.yibu.headmaster.adapter.ComplainAdapter;
import com.yibu.headmaster.api.ApiHttpClient;
import com.yibu.headmaster.bean.ComplainVO;
import com.yibu.headmaster.global.HeadmasterApplication;
import com.yibu.headmaster.utils.JsonUtil;
import com.yibu.headmaster.utils.LogUtil;
import com.yibu.headmaster.utils.ToastUtil;
import com.yibu.headmaster.view.QuickReturnListView;
import com.yibu.headmaster.view.QuickReturnListView.OnRefreshListener;

public class ComplainActivity extends BaseActivity {


	private ComplainAdapter adapter;
	private ArrayList<ComplainVO> list = new ArrayList<ComplainVO>();

	private boolean moreData = false;

	private int index = 0;

	private Context mContext = null;
	private View view;
	private QuickReturnListView mListView;

	@Override
	protected void initView() {
		view = View.inflate(getBaseContext(), R.layout.complain_activity, null);
		content.addView(view);
		mContext = this;

		mListView = (QuickReturnListView) view
				.findViewById(R.id.lv_publish_complain_list);

		mListView.setCacheColorHint(android.R.color.transparent);
		mListView.setDividerHeight(0);

		setSonsTitle(getString(R.string.complain));

		baseRight.setVisibility(View.GONE);
		
	}


	@Override
	protected void initData() {
		adapter = new ComplainAdapter(this, list);
		mListView.setAdapter(adapter);

		loadNetworkData();
		mListView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onLoadingMore() {

				if (moreData) {
//					index = list.get(list.size() - 1).seqindex;
					if (index == list.size()) {
						ToastUtil.showToast(mContext, "没有更多数据了");
					} else {
						loadNetworkData();
					}
				} else {
					mListView.loadMoreFinished();
					ToastUtil.showToast(mContext, "没有更多数据了");
				}
			}
			@Override
			public void onRefreshing() {

			}
		});

	}

	private void loadNetworkData() {

		
		ApiHttpClient.get("statistics/complaintdetails?userid=" 
				+ HeadmasterApplication.app.userInfo.userid + "&schoolid="
				+ HeadmasterApplication.app.userInfo.driveschool.schoolid+ "&index"+index
				+ "&count=10",
				handler);
	}

	@Override
	public void processSuccess(String data) {

		// 加载
		final ArrayList<ComplainVO> complainBeans = (ArrayList<ComplainVO>) JsonUtil
				.parseJsonToList(data, new TypeToken<List<ComplainVO>>() {
				}.getType());

		if (complainBeans.size() == 0) {
			moreData = false;
		} else {
			LogUtil.print(complainBeans.get(0).complaintcontent);
			moreData = true;
		}
		System.out.println(moreData);

		list.addAll(complainBeans);
		adapter.notifyDataSetChanged();

		// isLoadMore = false;
		mListView.loadMoreFinished();

	}

	@Override
	public void processFailure() {
		// isLoadMore = false;
		mListView.loadMoreFinished();
	}
	
	
	class ListViewOnItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
//			ComplainVO bean = list.get(position);
//			Intent intent = new Intent(mContext, NewsDetailActivity.class);
//			mContext.startActivity(intent);
		}

	}

}
