package com.yibu.headmaster.base.impl;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;

import com.yibu.headmaster.AssessActivity;
import com.yibu.headmaster.R;
import com.yibu.headmaster.api.ApiHttpClient;
import com.yibu.headmaster.base.BasePager;
import com.yibu.headmaster.bean.MoreDataBean;
import com.yibu.headmaster.bean.MoreDataBean.Applystuentlist;
import com.yibu.headmaster.bean.MoreDataBean.Badcommentlist;
import com.yibu.headmaster.bean.MoreDataBean.Coachcourselist;
import com.yibu.headmaster.bean.MoreDataBean.Complaintlist;
import com.yibu.headmaster.bean.MoreDataBean.Generalcommentlist;
import com.yibu.headmaster.bean.MoreDataBean.Goodcommentlist;
import com.yibu.headmaster.bean.MoreDataBean.Reservationstudentlist;
import com.yibu.headmaster.datachart.BarChartDemo;
import com.yibu.headmaster.datachart.LineChartDemoOne;
import com.yibu.headmaster.datachart.LineChartDemoThree;
import com.yibu.headmaster.datachart.LineChartDemoTwo;
import com.yibu.headmaster.global.HeadmasterApplication;
import com.yibu.headmaster.utils.JsonUtil;

public class MoreDataPager extends BasePager implements OnClickListener {

	private View view;
	private RelativeLayout relativeLayout_textView_shouke;
	private RelativeLayout relativeLayout_textView_pingjia;
	private RelativeLayout relativeLayout_chartone, relativeLayout_charttwo,
			relativeLayout_chartthree, relativeLayout_chartbar;
	private int searchtype = 1; // 查询时间类型：1 今天2 昨天 3 一周 4 本月5本年
	private LineChartDemoOne encrollStudent;// 招生
	private LineChartDemoTwo aboutClass;// 约课
	private BarChartDemo giveLesoons;// 授课
	private LineChartDemoThree assess;// 评价

	private int current = 0; // 当前的查询时间

	public MoreDataPager(Context context, int i) {
		super(context);
		current = i;
	}

	@Override
	public View initView() {
		view = View.inflate(mContext, R.layout.activity_data_chart, null);
		relativeLayout_textView_shouke = (RelativeLayout) view
				.findViewById(R.id.relativeLayout_textView_shouke);
		relativeLayout_textView_pingjia = (RelativeLayout) view
				.findViewById(R.id.relativeLayout_textView_pingjia);

		relativeLayout_chartone = (RelativeLayout) view
				.findViewById(R.id.RelativeLayout_chartone);
		relativeLayout_charttwo = (RelativeLayout) view
				.findViewById(R.id.RelativeLayout_charttwo);
		relativeLayout_chartthree = (RelativeLayout) view
				.findViewById(R.id.RelativeLayout_chartthree);
		relativeLayout_chartbar = (RelativeLayout) view
				.findViewById(R.id.RelativeLayout_chartbar);

		relativeLayout_textView_shouke.setOnClickListener(this);
		relativeLayout_textView_pingjia.setOnClickListener(this);
		return view;
	}

	@Override
	public void initData() {
		loadDataFromWeb();
	}

	private void loadDataFromWeb() {

		searchtype = current;
		String userId = HeadmasterApplication.app.userInfo.userid;
		String schoolId = HeadmasterApplication.app.userInfo.driveschool.schoolid;
		ApiHttpClient.get("statistics/getmoredata?userid=" + userId
				+ "&searchtype=" + searchtype + "&schoolid=" + schoolId,
				handler);
	}

	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.relativeLayout_textView_shouke:
			Intent shouke = new Intent(mContext, AssessActivity.class);
			shouke.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			shouke.putExtra("title", searchtype);
			mContext.startActivity(shouke);
			break;
		case R.id.relativeLayout_textView_pingjia:
			Intent pingjia = new Intent(mContext, AssessActivity.class);
			pingjia.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			pingjia.putExtra("title", searchtype);
			mContext.startActivity(pingjia);
			break;
		}
	}

	// private String getTitle(int searchtype) {
	// String title = null;
	// switch (searchtype) {
	// case 1:
	// title = "今天";
	// break;
	// case 2:
	// title = "昨天";
	// break;
	// case 3:
	// title = "本周";
	// break;
	// case 4:
	// title = "本月";
	// break;
	// case 5:
	// title = "本年";
	// break;
	//
	// default:
	// break;
	// }
	// return title;
	// }

	@Override
	public void process(String data) {
		MoreDataBean moreDataBean = JsonUtil.parseJsonToBean(data,
				MoreDataBean.class);
		// 招生---------------------------
		if (moreDataBean != null) {
			List<Applystuentlist> applystuentlist = moreDataBean.applystuentlist;
			encrollStudent = new LineChartDemoOne(mContext, applystuentlist);
			relativeLayout_chartone.addView(encrollStudent);
			LayoutParams params = encrollStudent.getLayoutParams();
			params.height = LayoutParams.MATCH_PARENT;
			params.width = LayoutParams.MATCH_PARENT;
			encrollStudent.setLayoutParams(params);
		}
		// 约课------------------------
		if (moreDataBean != null) {
			List<Reservationstudentlist> reservationstudentlist = moreDataBean.reservationstudentlist;
			aboutClass = new LineChartDemoTwo(mContext, reservationstudentlist);
			relativeLayout_charttwo.addView(aboutClass);
			LayoutParams params = aboutClass.getLayoutParams();
			params.height = LayoutParams.MATCH_PARENT;
			params.width = LayoutParams.MATCH_PARENT;
			aboutClass.setLayoutParams(params);
		}
		// 教练授课---------------------------
		if (moreDataBean != null) {
			List<Coachcourselist> coachcourselist = moreDataBean.coachcourselist;
			giveLesoons = new BarChartDemo(mContext, coachcourselist);
			relativeLayout_chartbar.addView(giveLesoons);
			LayoutParams params = giveLesoons.getLayoutParams();
			params.height = LayoutParams.MATCH_PARENT;
			params.width = LayoutParams.MATCH_PARENT;
			giveLesoons.setLayoutParams(params);
		}
		// 评价---------------------
		if (moreDataBean != null) {
			List<Goodcommentlist> goodcommentlist = moreDataBean.goodcommentlist;
			List<Badcommentlist> badcommentlist = moreDataBean.badcommentlist;
			List<Generalcommentlist> generalcommentlist = moreDataBean.generalcommentlist;
			List<Complaintlist> complaintlist = moreDataBean.complaintlist;
			assess = new LineChartDemoThree(mContext, goodcommentlist,
					generalcommentlist, badcommentlist, complaintlist);
			relativeLayout_chartthree.addView(assess);
			LayoutParams params = assess.getLayoutParams();
			params.height = LayoutParams.MATCH_PARENT;
			params.width = LayoutParams.MATCH_PARENT;
			assess.setLayoutParams(params);
		}
	}
}
