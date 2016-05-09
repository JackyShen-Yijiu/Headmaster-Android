package com.yibu.headmaster;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

import com.jzjf.headmaster.R;
import com.yibu.headmaster.api.ApiHttpClient;
import com.yibu.headmaster.bean.MoreDataBean;
import com.yibu.headmaster.bean.MoreDataOfTodayBean;
import com.yibu.headmaster.bean.MoreDataOfTodayBean.Applystuentlist;
import com.yibu.headmaster.datachart.LineChartDemoOne;
import com.yibu.headmaster.global.HeadmasterApplication;
import com.yibu.headmaster.utils.JsonUtil;
import com.yibu.headmaster.utils.LogUtil;

/**
 * 招生统计
 * @author pengdonghua
 *
 */
public class StatisticsAct extends BaseActivity{

	private List<MoreDataBean> lineDataList;
	
	private int totalWeek,totalMonth,totalYear;
	
	private LineChartDemoOne encrollStudent;
	
	private FrameLayout frameLayout;
	private int searchtype = 3; // 查询时间类型：1 今天2 昨天 3 一周 4 本月5本年
	
	@Override
	protected void initView() {
		View view = View.inflate(getBaseContext(), R.layout.act_statistics,
				null);
		content.addView(view);
		frameLayout = (FrameLayout) view.findViewById(R.id.act_statistics_line);
		
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		lineDataList = new ArrayList<MoreDataBean>();
		loadDataFromWeb();
	}
	
	private void loadDataFromWeb() {
//		searchtype = current;
		String userId = HeadmasterApplication.app.userInfo.userid;
		String schoolId = HeadmasterApplication.app.userInfo.driveschool.schoolid;
		ApiHttpClient.get("statistics/getmoredata?userid=" + userId
				+ "&searchtype=" + searchtype + "&schoolid=" + schoolId,
				handler);
	}

	@Override
	public void processSuccess(String data) {
		LogUtil.print("processSuccess-->>"+data);
		setTodayData(data);
	}

	@Override
	public void processFailure() {
		// TODO Auto-generated method stub
		
	}
	
	
	/**
	 * 本周 本月  本年
	 * @param data
	 */
	private void setTodayData(String data) {
		MoreDataOfTodayBean moreDataBean = JsonUtil.parseJsonToBean(data,
				MoreDataOfTodayBean.class);
		// 招生---------------------------
		if (moreDataBean != null) {

//			LogUtil.print("昨天" + searchtype + "-----" + moreDataBean);
			MoreDataBean bean;
			lineDataList.clear();
			for (Applystuentlist applystuent : moreDataBean.applystuentlist) {
				totalWeek += applystuent.applystudentcount;
				bean = new MoreDataBean();
				bean.timeX = applystuent.hour + ":00";
				bean.countY = applystuent.applystudentcount;
				lineDataList.add(bean);
			}

			encrollStudent = new LineChartDemoOne(this, lineDataList);
			frameLayout.removeAllViews();
			frameLayout.addView(encrollStudent);
			LayoutParams params = encrollStudent.getLayoutParams();
			params.height = LayoutParams.MATCH_PARENT;
			params.width = LayoutParams.MATCH_PARENT;
			encrollStudent.setLayoutParams(params);
		}
	}

}
