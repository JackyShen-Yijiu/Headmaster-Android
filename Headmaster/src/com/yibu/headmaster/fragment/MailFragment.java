package com.yibu.headmaster.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ListView;

import cn.jpush.android.util.l;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.jzjf.headmaster.R;
import com.yibu.headmaster.GiveClassActivity;
import com.yibu.headmaster.adapter.CoachFeedbackAdapter;
import com.yibu.headmaster.adapter.CoachGiveClassAdapter;
import com.yibu.headmaster.api.ApiHttpClient;
import com.yibu.headmaster.base.BasePagerFragment;
import com.yibu.headmaster.bean.CoachFeedbackBean;
import com.yibu.headmaster.bean.CoachGiveClassBean;
import com.yibu.headmaster.global.HeadmasterApplication;
import com.yibu.headmaster.utils.JsonUtil;
import com.yibu.headmaster.utils.LogUtil;
import com.yibu.headmaster.utils.ToastUtil;

/**信箱*/
public class MailFragment extends BasePagerFragment{

	private PullToRefreshListView pullToRefreshListView;
	private ListView list_coach;
	private CoachFeedbackAdapter adapter;
	
	private int index=1;
	private boolean hasMoreData = true;
	private ArrayList<CoachFeedbackBean> list = new ArrayList<CoachFeedbackBean>();
	public MailFragment(Context context) {
		super(context);
	}

	@Override
	public View initView() {
		LogUtil.print("initView-------");
		View view =View.inflate(mContext, R.layout.fragment_mail, null);
		pullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.lv_mail_coach_feedback);
		pullToRefreshListView.setVisibility(View.VISIBLE);
		pullToRefreshListView.setMode(Mode.BOTH);
		list_coach = pullToRefreshListView.getRefreshableView();
		list_coach.setCacheColorHint(Color.TRANSPARENT);
		list_coach.setDividerHeight(0);
		list_coach.setSelector(R.drawable.listview_selector);
		
		//添加头布局
		View headerView = View.inflate(mContext, R.layout.mail_header, null);
		list_coach.addHeaderView(headerView);
		
		return view;
	}
	
	@Override
	public void initData() {
		adapter = new CoachFeedbackAdapter(mContext, list);
		list_coach.setAdapter(adapter);

		// 给PullToRefreshListView设置监听器
		pullToRefreshListView
				.setOnRefreshListener(new OnRefreshListener<ListView>() {

					

					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {
						if (pullToRefreshListView.isHeaderShown()) {
							index = 1;
							loadNetworkData();

						} else {
							if (!hasMoreData) {
								ToastUtil.showToast(mContext,
										"没有更多数据了");
							} else {
								index++;
							}
							loadNetworkData();
						}
					}
				});

		loadNetworkData();
	}
	
	
	//加载教练反馈数据
	private void loadNetworkData() {
		ApiHttpClient.get("statistics/getcoachfeedback?userid="
				+ HeadmasterApplication.app.userInfo.userid + "&schoolid="
				+ HeadmasterApplication.app.userInfo.driveschool.schoolid
				+ "&index=" + index + "&count=10",
				handler);
	}

	@Override
	public void process(String data) {
		List<CoachFeedbackBean> coachFeedbackList = (List<CoachFeedbackBean>) JsonUtil
				.parseJsonToList(data,
						new TypeToken<List<CoachFeedbackBean>>() {
						}.getType());
		LogUtil.print("ddddddd---"+coachFeedbackList.size());
		if(coachFeedbackList.size() == 0){
			hasMoreData = false;
		}else{
			list.addAll(coachFeedbackList);
			adapter.notifyDataSetChanged();
		}
		LogUtil.print("ddddddd--list-"+list.size());
		pullToRefreshListView.onRefreshComplete();
	}
	
	@Override
	public void processFailure() {

		ToastUtil.showToast(mContext, "网络异常");
	}


}
