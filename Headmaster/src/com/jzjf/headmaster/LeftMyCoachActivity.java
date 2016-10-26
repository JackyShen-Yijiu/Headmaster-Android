package com.jzjf.headmaster;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jzjf.headmaster.R;
import com.jzjf.headmaster.adapter.MyCoachAdapter;
import com.jzjf.headmaster.api.ApiHttpClient;
import com.jzjf.headmaster.bean.CoachBean;
import com.jzjf.headmaster.global.HeadmasterApplication;
import com.jzjf.headmaster.utils.JsonUtil;
import com.jzjf.headmaster.utils.ToastUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

public class LeftMyCoachActivity extends BaseActivity implements OnClickListener{

	private View view;
	private PullToRefreshListView pullToRefreshListView;
	private ListView mListView;
	private MyCoachAdapter adapter;
	private ArrayList<CoachBean> list = new ArrayList<CoachBean>();
	private int curpage = 1;
	private Context mContext;
	@Override
	protected void initView() {
		mContext = this;
		view = View.inflate(getBaseContext(), R.layout.left_my_coach, null);
		content.addView(view);
		pullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.listView1);
		
		baseRight.setVisibility(View.GONE);
//		baseRight.setBackgroundResource(R.drawable.search_white);
		
		// if (searchView != null) {
		// try {
		// Class<?> argClass = searchView.getClass();
		// Field ownField = argClass.getDeclaredField("mSearchPlate");
		// ownField.setAccessible(true);
		// View mView = (View) ownField.get(searchView);
		// mView.setBackgroundColor(Color.TRANSPARENT);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		setSonsTitle(getString(R.string.coach_title)+"(0)");
		
	}


	@Override
	protected void initData() {
		pullToRefreshListView.setMode(Mode.BOTH);
		mListView = pullToRefreshListView.getRefreshableView();
		mListView.setCacheColorHint(Color.TRANSPARENT);
		mListView.setDividerHeight(0);
		mListView.setSelector(R.drawable.listview_selector);
		pullToRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {
						if (pullToRefreshListView.isHeaderShown()) {
							// 下拉刷新
							curpage = 1;
							loadNetworkData();
						} else {
							// 下拉加载
							curpage++;
							loadNetworkData();
						}
					}
				});
		adapter = new MyCoachAdapter(this, list);
		mListView.setAdapter(adapter);

		loadNetworkData();
	}

//	private boolean isSearch = false;

	private void loadNetworkData() {

//		ApiHttpClient.getWithFullPath("api/v1/getschoolcoach/"
//				+ HeadmasterApplication.app.userInfo.driveschool.schoolid + "/"
//				+ curpage, handler);
		
		ApiHttpClient.get(ApiHttpClient.COACH_LIST + "?index=" 
				+ curpage + "&count=10", handler);
		
//		LogUtil.print("wqeqwasdasd"+HeadmasterApplication.app.userInfo.driveschool.schoolid);
		// ApiHttpClient.get("getschoolcoach/"
		// + HeadmasterApplication.app.userInfo.driveschool.schoolid + "/"
		// + curpage + "?name=" + HeadmasterApplication.app.userInfo.name,
		// handler);
		// http://101.200.204.240:8181/api/v1/getschoolcoach/562dcc3ccb90f25c3bde40da/1?name=%E7%8E%8B
	}


	@Override
	public void processSuccess(String data) {
		// 加载
		@SuppressWarnings("unchecked")
		final ArrayList<CoachBean> coachBeans = (ArrayList<CoachBean>) JsonUtil
				.parseJsonToList(data, new TypeToken<List<CoachBean>>() {
				}.getType());

		if (coachBeans != null) {
			if (coachBeans.size() == 0) {
//				if (isSearch) {
//					ZProgressHUD.getInstance(LeftMyCoachActivity.this).show();
//					ZProgressHUD.getInstance(LeftMyCoachActivity.this)
//							.dismissWithSuccess("没有搜索到您要找的教练");
//					isSearch = false;
//				}
				ToastUtil.showToast(mContext, "没有更多数据了");
			}

			if(curpage == 1){
				list.clear();
			}
			list.addAll(coachBeans);
			adapter.notifyDataSetChanged();
			pullToRefreshListView.onRefreshComplete();
			setSonsTitle(getString(R.string.coach_title)+"("+extra+")");
		}
	}

	@Override
	public void processFailure() {
		ToastUtil.showToast(mContext, "网络异常");
	}
	
	public void onClick(View view){
		switch(view.getId()){	
		case R.id.tv_base_right:
			Intent intent = new Intent(HeadmasterApplication.getContext(),
					SearchCoachActivity.class);
			startActivity(intent);
			break;
		case R.id.ib_base_arrow:
			finish();
			break;
			
		}
	}

}
