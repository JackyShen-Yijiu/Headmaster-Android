package com.yibu.headmaster;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.yibu.headmaster.adapter.MyCoachAdapter;
import com.yibu.headmaster.api.ApiHttpClient;
import com.yibu.headmaster.bean.CoachBean;
import com.yibu.headmaster.global.HeadmasterApplication;
import com.yibu.headmaster.utils.JsonUtil;
import com.yibu.headmaster.utils.ToastUtil;

public class LeftMyCoachActivity extends BaseActivity {

	private View view;
	private ListView mListView;
	private MyCoachAdapter adapter;
	private ArrayList<CoachBean> list = new ArrayList<CoachBean>();
	private int curpage = 1;
	private Context mContext;

	@Override
	protected void initView() {
		mContext = this;
		// TODO Auto-generated method stub
		view = View.inflate(getBaseContext(), R.layout.left_my_coach, null);
		content.addView(view);

		mListView = (ListView) view.findViewById(R.id.listView1);
		setSonsTitle(getString(R.string.coach_title));
	}

	@Override
	protected void initListener() {

	}

	@Override
	protected void initData() {
		adapter = new MyCoachAdapter(this, list);
		mListView.setAdapter(adapter);

		loadNetworkData();
	}

	private void loadNetworkData() {

		ApiHttpClient.getWithFullPath("api/v1/getschoolcoach/"
				+ HeadmasterApplication.app.userInfo.driveschool.schoolid + "/"
				+ curpage, handler);
		// ApiHttpClient.get("getschoolcoach/"
		// + HeadmasterApplication.app.userInfo.driveschool.schoolid + "/"
		// + curpage + "?name=" + HeadmasterApplication.app.userInfo.name,
		// handler);
		// http://101.200.204.240:8181/api/v1/getschoolcoach/562dcc3ccb90f25c3bde40da/1?name=%E7%8E%8B
	}

	@Override
	public void processSuccess(String data) {
		// 加载
		final ArrayList<CoachBean> coachBeans = (ArrayList<CoachBean>) JsonUtil
				.parseJsonToList(data, new TypeToken<List<CoachBean>>() {
				}.getType());

		System.out.println(coachBeans.size() + "-----");
		list.addAll(coachBeans);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void processFailure() {
		ToastUtil.showToast(mContext, "网络异常");
	}

}
