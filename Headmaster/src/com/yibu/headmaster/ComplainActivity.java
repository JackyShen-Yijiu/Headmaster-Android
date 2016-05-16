package com.yibu.headmaster;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.jzjf.headmaster.R;
import com.yibu.headmaster.adapter.ComplainAdapter;
import com.yibu.headmaster.api.ApiHttpClient;
import com.yibu.headmaster.bean.ComplainBean;
import com.yibu.headmaster.bean.ComplainVO;
import com.yibu.headmaster.bean.NewsBean;
import com.yibu.headmaster.global.HeadmasterApplication;
import com.yibu.headmaster.utils.CommonUtils;
import com.yibu.headmaster.utils.JsonUtil;
import com.yibu.headmaster.utils.LogUtil;
import com.yibu.headmaster.utils.ToastUtil;
import com.yibu.headmaster.view.QuickReturnListView;
import com.yibu.headmaster.view.QuickReturnListView.OnRefreshListener;

public class ComplainActivity extends BaseActivity implements
		OnItemClickListener {
	private ComplainAdapter adapter;
	private ArrayList<ComplainVO> list = new ArrayList<ComplainVO>();

	private boolean moreData = false;

	private int index = 1;

	private Context mContext = null;
	private View view;
	private QuickReturnListView mListView;
	private LinearLayout blackPageLayout;
	private ImageView blackPageIv;
	private TextView blackPageTv;

	@Override
	protected void initView() {
		view = View.inflate(getBaseContext(), R.layout.complain_activity, null);
		content.addView(view);
		mContext = this;
		mListView = (QuickReturnListView) view
				.findViewById(R.id.lv_publish_complain_list);
		mListView.setCacheColorHint(R.color.line_color);
		mListView.setDividerHeight(1);
		setSonsTitle(getString(R.string.complain));
		baseRight.setVisibility(View.GONE);

		// 空白页
		blackPageLayout = (LinearLayout) view.findViewById(R.id.black_page_ll);
		blackPageIv = (ImageView) view.findViewById(R.id.black_page_iv);
		blackPageTv = (TextView) view.findViewById(R.id.black_page_tv);
		blackPageIv.setBackgroundResource(R.drawable.complaint_null);
		blackPageTv.setText("暂时还没有收到学员投诉");
		mListView.setVisibility(View.VISIBLE);
	}

	@Override
	protected void initData() {
		adapter = new ComplainAdapter(this, list);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(this);
		loadNetworkData();
		mListView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onLoadingMore() {

				if (moreData) {
					index++;
					loadNetworkData();
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
		ApiHttpClient.get("statistics/complaintlist?userid="
				+ HeadmasterApplication.app.userInfo.userid + "&schoolid="
				+ HeadmasterApplication.app.userInfo.driveschool.schoolid
				+ "&index=" + index + "&count=10", handler);
	}

	@Override
	public void processSuccess(String data) {
		ComplainBean complainBeans = JsonUtil.parseJsonToBean(data,
				ComplainBean.class);
		
		if (index == 1) {
			if (complainBeans.complaintlist.size() == 0) {
				blackPageLayout.setVisibility(View.VISIBLE);
				mListView.setVisibility(View.GONE);
			} else {
				blackPageLayout.setVisibility(View.GONE);
				mListView.setVisibility(View.VISIBLE);

			}
		}
		if (complainBeans.complaintlist.size() == 0) {
			moreData = false;
		} else {
//			LogUtil.print(complainBeans.complaintlist.get(0).complaintcontent);
			moreData = true;
		}
		System.out.println(moreData);

		list.addAll(complainBeans.complaintlist);
		adapter.notifyDataSetChanged();

		// isLoadMore = false;
		mListView.loadMoreFinished();

		setSonsTitle(getString(R.string.complain) + "(" + complainBeans.count
				+ ")");
	}

	@Override
	public void processFailure() {
		// isLoadMore = false;
		blackPageLayout.setVisibility(View.VISIBLE);
		mListView.setVisibility(View.GONE);
		blackPageIv.setBackgroundResource(R.drawable.net_null);
		blackPageTv.setText(CommonUtils.getString(R.string.no_network));
		mListView.loadMoreFinished();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ComplainVO bean = list.get(position);
		Intent intent = new Intent(mContext, ComplainDetailActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("item", bean);
		// intent.putExtra("item", bean);
		intent.putExtras(bundle);
		mContext.startActivity(intent);
	}

}
