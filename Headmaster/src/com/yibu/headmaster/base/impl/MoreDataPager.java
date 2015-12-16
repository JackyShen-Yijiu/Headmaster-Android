package com.yibu.headmaster.base.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yibu.headmaster.AssessActivity;
import com.yibu.headmaster.GiveClassActivity;
import com.yibu.headmaster.R;
import com.yibu.headmaster.api.ApiHttpClient;
import com.yibu.headmaster.base.BasePager;
import com.yibu.headmaster.bean.MoreDataBean;
import com.yibu.headmaster.bean.MoreDataOfTodayBean;
import com.yibu.headmaster.bean.MoreDataOfTodayBean.Applystuentlist;
import com.yibu.headmaster.bean.MoreDataOfTodayBean.Badcommentlist;
import com.yibu.headmaster.bean.MoreDataOfTodayBean.Coachcourselist;
import com.yibu.headmaster.bean.MoreDataOfTodayBean.Complaintlist;
import com.yibu.headmaster.bean.MoreDataOfTodayBean.Generalcommentlist;
import com.yibu.headmaster.bean.MoreDataOfTodayBean.Goodcommentlist;
import com.yibu.headmaster.bean.MoreDataOfTodayBean.Reservationstudentlist;
import com.yibu.headmaster.bean.MoreDataOfWeekBean;
import com.yibu.headmaster.bean.MoreDataOfWeekBean.Coursedata;
import com.yibu.headmaster.bean.MoreDataOfWeekBean.Datalist;
import com.yibu.headmaster.datachart.BarChartDemo;
import com.yibu.headmaster.datachart.LineChartDemoOne;
import com.yibu.headmaster.datachart.LineChartDemoThree;
import com.yibu.headmaster.datachart.LineChartDemoTwo;
import com.yibu.headmaster.global.HeadmasterApplication;
import com.yibu.headmaster.utils.JsonUtil;
import com.yibu.headmaster.utils.LogUtil;

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
	private List<MoreDataBean> lineDataList;
	private MoreDataBean bean;
	private TextView zhaoshengSumTextView;
	private TextView yuekeSumTextView;

	private int zhaoshengSum;
	private int yuekeSum;

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

		zhaoshengSumTextView = (TextView) view
				.findViewById(R.id.textView_zhaosheng1);
		yuekeSumTextView = (TextView) view.findViewById(R.id.textView_yueke1);

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

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.relativeLayout_textView_shouke:
			Intent shouke = new Intent(mContext, GiveClassActivity.class);
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

	@Override
	public void process(String data) {

		lineDataList = new ArrayList<MoreDataBean>();

		zhaoshengSum = 0;
		yuekeSum = 0;
		switch (searchtype) {
		case 3:
			setWeekData(data);
			break;
		case 4:
			setMonthData(data);
			break;
		case 5:
			setYearData(data);
			break;

		default:
			setTodayData(data);
			break;
		}

		zhaoshengSumTextView.setText("共" + zhaoshengSum + "人");
		yuekeSumTextView.setText("共" + yuekeSum + "人");
	}

	// 设置今天和昨天的数据
	private void setTodayData(String data) {
		MoreDataOfTodayBean moreDataBean = JsonUtil.parseJsonToBean(data,
				MoreDataOfTodayBean.class);
		// 招生---------------------------
		if (moreDataBean != null) {

			LogUtil.print("昨天" + searchtype + "-----" + moreDataBean);

			lineDataList.clear();
			for (Applystuentlist applystuent : moreDataBean.applystuentlist) {
				zhaoshengSum += applystuent.applystudentcount;
				bean = new MoreDataBean();
				bean.timeX = applystuent.hour + ":00";
				bean.countY = applystuent.applystudentcount;
				lineDataList.add(bean);
			}

			encrollStudent = new LineChartDemoOne(mContext, lineDataList);
			relativeLayout_chartone.removeAllViews();
			relativeLayout_chartone.addView(encrollStudent);
			LayoutParams params = encrollStudent.getLayoutParams();
			params.height = LayoutParams.MATCH_PARENT;
			params.width = LayoutParams.MATCH_PARENT;
			encrollStudent.setLayoutParams(params);
		}
		// 约课------------------------
		if (moreDataBean != null) {
			List<Reservationstudentlist> reservationstudentlist = moreDataBean.reservationstudentlist;

			lineDataList.clear();
			for (Reservationstudentlist reservationstudent : moreDataBean.reservationstudentlist) {

				yuekeSum += reservationstudent.studentcount;
				bean = new MoreDataBean();
				bean.timeX = reservationstudent.hour + ":00";
				bean.countY = reservationstudent.studentcount;

				lineDataList.add(bean);
			}

			aboutClass = new LineChartDemoTwo(mContext, lineDataList);
			relativeLayout_charttwo.removeAllViews();
			relativeLayout_charttwo.addView(aboutClass);
			LayoutParams params = aboutClass.getLayoutParams();
			params.height = LayoutParams.MATCH_PARENT;
			params.width = LayoutParams.MATCH_PARENT;
			aboutClass.setLayoutParams(params);
		}
		// 教练授课---------------------------
		if (moreDataBean != null) {
			List<Coachcourselist> coachcourselist = moreDataBean.coachcourselist;

			lineDataList.clear();
			for (Coachcourselist coachcourse : moreDataBean.coachcourselist) {
				bean = new MoreDataBean();
				bean.timeX = coachcourse.coachcount + "";
				bean.countY = coachcourse.coursecount;

				lineDataList.add(bean);
			}

			giveLesoons = new BarChartDemo(mContext, lineDataList);
			relativeLayout_chartbar.removeAllViews();
			relativeLayout_chartbar.addView(giveLesoons);
			LayoutParams params = giveLesoons.getLayoutParams();
			params.height = LayoutParams.MATCH_PARENT;
			params.width = LayoutParams.MATCH_PARENT;
			giveLesoons.setLayoutParams(params);
		}
		// 评价---------------------
		if (moreDataBean != null) {

			// 好评
			List<MoreDataBean> goodcommentlist = new ArrayList<MoreDataBean>();
			for (Goodcommentlist goodcomment : moreDataBean.goodcommentlist) {
				bean = new MoreDataBean();
				bean.timeX = goodcomment.hour + ":00";
				bean.countY = goodcomment.commnetcount;

				goodcommentlist.add(bean);
			}
			// 中评
			List<MoreDataBean> generalcommentlist = new ArrayList<MoreDataBean>();
			for (Generalcommentlist generalcomment : moreDataBean.generalcommentlist) {
				bean = new MoreDataBean();
				bean.timeX = generalcomment.hour + ":00";
				bean.countY = generalcomment.commnetcount;

				generalcommentlist.add(bean);
			}
			// 差评
			List<MoreDataBean> badcommentlist = new ArrayList<MoreDataBean>();
			for (Badcommentlist badcomment : moreDataBean.badcommentlist) {
				bean = new MoreDataBean();
				bean.timeX = badcomment.hour + ":00";
				bean.countY = badcomment.commnetcount;

				badcommentlist.add(bean);
			}
			// 投诉
			List<MoreDataBean> complaintlist = new ArrayList<MoreDataBean>();
			for (Complaintlist complaint : moreDataBean.complaintlist) {
				bean = new MoreDataBean();
				bean.timeX = complaint.hour + ":00";
				bean.countY = complaint.complaintcount;

				complaintlist.add(bean);
			}

			assess = new LineChartDemoThree(mContext, goodcommentlist,
					generalcommentlist, badcommentlist, complaintlist);
			relativeLayout_chartthree.removeAllViews();
			relativeLayout_chartthree.addView(assess);
			LayoutParams params = assess.getLayoutParams();
			params.height = LayoutParams.MATCH_PARENT;
			params.width = LayoutParams.MATCH_PARENT;
			assess.setLayoutParams(params);
		}
	}

	// 设置本周的数据
	private void setWeekData(String data) {
		MoreDataOfWeekBean moreDataBean = JsonUtil.parseJsonToBean(data,
				MoreDataOfWeekBean.class);
		// 招生---------------------------
		if (moreDataBean != null) {

			lineDataList.clear();
			for (Datalist datalist : moreDataBean.datalist) {
				zhaoshengSum += datalist.applystudentcount;
				bean = new MoreDataBean();
				bean.timeX = switchWeek(datalist.day);
				bean.countY = datalist.applystudentcount;
				lineDataList.add(bean);
			}

			encrollStudent = new LineChartDemoOne(mContext, lineDataList);
			relativeLayout_chartone.removeAllViews();
			relativeLayout_chartone.addView(encrollStudent);
			LayoutParams params = encrollStudent.getLayoutParams();
			params.height = LayoutParams.MATCH_PARENT;
			params.width = LayoutParams.MATCH_PARENT;
			encrollStudent.setLayoutParams(params);
		}
		// 约课------------------------
		if (moreDataBean != null) {

			lineDataList.clear();
			for (Datalist datalist : moreDataBean.datalist) {
				yuekeSum += datalist.reservationcoursecount;
				bean = new MoreDataBean();
				bean.timeX = switchWeek(datalist.day);
				bean.countY = datalist.reservationcoursecount;
				lineDataList.add(bean);
			}

			aboutClass = new LineChartDemoTwo(mContext, lineDataList);
			relativeLayout_charttwo.removeAllViews();
			relativeLayout_charttwo.addView(aboutClass);
			LayoutParams params = aboutClass.getLayoutParams();
			params.height = LayoutParams.MATCH_PARENT;
			params.width = LayoutParams.MATCH_PARENT;
			aboutClass.setLayoutParams(params);
		}
		// 教练授课---------------------------
		if (moreDataBean != null) {

			lineDataList.clear();
			lineDataList.clear();
			for (Coursedata datalist : moreDataBean.coursedata) {
				bean = new MoreDataBean();
				bean.timeX = datalist.coachcount + "";
				bean.countY = datalist.coursecount;
				lineDataList.add(bean);
			}

			giveLesoons = new BarChartDemo(mContext, lineDataList);
			relativeLayout_chartbar.removeAllViews();
			relativeLayout_chartbar.addView(giveLesoons);
			LayoutParams params = giveLesoons.getLayoutParams();
			params.height = LayoutParams.MATCH_PARENT;
			params.width = LayoutParams.MATCH_PARENT;
			giveLesoons.setLayoutParams(params);
		}
		// 评价---------------------
		if (moreDataBean != null) {

			// 好评
			List<MoreDataBean> goodcommentlist = new ArrayList<MoreDataBean>();
			for (Datalist goodcomment : moreDataBean.datalist) {
				bean = new MoreDataBean();
				bean.timeX = switchWeek(goodcomment.day);
				bean.countY = goodcomment.goodcommentcount;

				goodcommentlist.add(bean);
			}
			// 中评
			List<MoreDataBean> generalcommentlist = new ArrayList<MoreDataBean>();
			for (Datalist generalcomment : moreDataBean.datalist) {
				bean = new MoreDataBean();
				bean.timeX = switchWeek(generalcomment.day);
				bean.countY = generalcomment.generalcomment;

				generalcommentlist.add(bean);
			}
			// 差评
			List<MoreDataBean> badcommentlist = new ArrayList<MoreDataBean>();
			for (Datalist badcomment : moreDataBean.datalist) {
				bean = new MoreDataBean();
				bean.timeX = switchWeek(badcomment.day);
				bean.countY = badcomment.badcommentcount;

				badcommentlist.add(bean);
			}
			// 投诉
			List<MoreDataBean> complaintlist = new ArrayList<MoreDataBean>();
			for (Datalist complaint : moreDataBean.datalist) {
				bean = new MoreDataBean();
				bean.timeX = switchWeek(complaint.day);
				bean.countY = complaint.complaintcount;

				complaintlist.add(bean);
			}

			assess = new LineChartDemoThree(mContext, goodcommentlist,
					generalcommentlist, badcommentlist, complaintlist);
			relativeLayout_chartthree.removeAllViews();
			relativeLayout_chartthree.addView(assess);
			LayoutParams params = assess.getLayoutParams();
			params.height = LayoutParams.MATCH_PARENT;
			params.width = LayoutParams.MATCH_PARENT;
			assess.setLayoutParams(params);
		}
	}

	// 设置本月的数据
	private void setMonthData(String data) {
		MoreDataOfWeekBean moreDataBean = JsonUtil.parseJsonToBean(data,
				MoreDataOfWeekBean.class);
		// 招生---------------------------
		if (moreDataBean != null) {

			lineDataList.clear();
			for (Datalist datalist : moreDataBean.datalist) {
				zhaoshengSum += datalist.applystudentcount;
				bean = new MoreDataBean();
				bean.timeX = switchMonth(datalist.weekindex);
				bean.countY = datalist.applystudentcount;
				lineDataList.add(bean);
			}

			encrollStudent = new LineChartDemoOne(mContext, lineDataList);
			relativeLayout_chartone.removeAllViews();
			relativeLayout_chartone.addView(encrollStudent);
			LayoutParams params = encrollStudent.getLayoutParams();
			params.height = LayoutParams.MATCH_PARENT;
			params.width = LayoutParams.MATCH_PARENT;
			encrollStudent.setLayoutParams(params);
		}
		// 约课------------------------
		if (moreDataBean != null) {

			lineDataList.clear();
			for (Datalist datalist : moreDataBean.datalist) {
				yuekeSum += datalist.reservationcoursecount;
				bean = new MoreDataBean();
				bean.timeX = switchMonth(datalist.weekindex);
				bean.countY = datalist.reservationcoursecount;
				lineDataList.add(bean);
			}

			aboutClass = new LineChartDemoTwo(mContext, lineDataList);
			relativeLayout_charttwo.removeAllViews();
			relativeLayout_charttwo.addView(aboutClass);
			LayoutParams params = aboutClass.getLayoutParams();
			params.height = LayoutParams.MATCH_PARENT;
			params.width = LayoutParams.MATCH_PARENT;
			aboutClass.setLayoutParams(params);
		}
		// 教练授课---------------------------
		if (moreDataBean != null) {

			lineDataList.clear();
			lineDataList.clear();
			for (Coursedata datalist : moreDataBean.coursedata) {
				bean = new MoreDataBean();
				bean.timeX = datalist.coachcount + "";
				bean.countY = datalist.coursecount;
				lineDataList.add(bean);
			}

			giveLesoons = new BarChartDemo(mContext, lineDataList);
			relativeLayout_chartbar.removeAllViews();
			relativeLayout_chartbar.addView(giveLesoons);
			LayoutParams params = giveLesoons.getLayoutParams();
			params.height = LayoutParams.MATCH_PARENT;
			params.width = LayoutParams.MATCH_PARENT;
			giveLesoons.setLayoutParams(params);
		}
		// 评价---------------------
		if (moreDataBean != null) {

			// 好评
			List<MoreDataBean> goodcommentlist = new ArrayList<MoreDataBean>();
			for (Datalist goodcomment : moreDataBean.datalist) {
				bean = new MoreDataBean();
				bean.timeX = switchMonth(goodcomment.weekindex);
				bean.countY = goodcomment.goodcommentcount;

				goodcommentlist.add(bean);
			}
			// 中评
			List<MoreDataBean> generalcommentlist = new ArrayList<MoreDataBean>();
			for (Datalist generalcomment : moreDataBean.datalist) {
				bean = new MoreDataBean();
				bean.timeX = switchMonth(generalcomment.weekindex);
				bean.countY = generalcomment.generalcomment;

				generalcommentlist.add(bean);
			}
			// 差评
			List<MoreDataBean> badcommentlist = new ArrayList<MoreDataBean>();
			for (Datalist badcomment : moreDataBean.datalist) {
				bean = new MoreDataBean();
				bean.timeX = switchMonth(badcomment.weekindex);
				bean.countY = badcomment.badcommentcount;

				badcommentlist.add(bean);
			}
			// 投诉
			List<MoreDataBean> complaintlist = new ArrayList<MoreDataBean>();
			for (Datalist complaint : moreDataBean.datalist) {
				bean = new MoreDataBean();
				bean.timeX = switchMonth(complaint.weekindex);
				bean.countY = complaint.complaintcount;

				complaintlist.add(bean);
			}

			assess = new LineChartDemoThree(mContext, goodcommentlist,
					generalcommentlist, badcommentlist, complaintlist);
			relativeLayout_chartthree.removeAllViews();
			relativeLayout_chartthree.addView(assess);
			LayoutParams params = assess.getLayoutParams();
			params.height = LayoutParams.MATCH_PARENT;
			params.width = LayoutParams.MATCH_PARENT;
			assess.setLayoutParams(params);
		}
	}

	// 设置本年的数据
	private void setYearData(String data) {
		MoreDataOfWeekBean moreDataBean = JsonUtil.parseJsonToBean(data,
				MoreDataOfWeekBean.class);
		// 招生---------------------------
		if (moreDataBean != null) {

			lineDataList.clear();
			for (Datalist datalist : moreDataBean.datalist) {
				zhaoshengSum += datalist.applystudentcount;
				bean = new MoreDataBean();
				bean.timeX = switchYear(datalist.month);
				bean.countY = datalist.applystudentcount;
				lineDataList.add(bean);
			}

			encrollStudent = new LineChartDemoOne(mContext, lineDataList);
			relativeLayout_chartone.removeAllViews();
			relativeLayout_chartone.addView(encrollStudent);
			LayoutParams params = encrollStudent.getLayoutParams();
			params.height = LayoutParams.MATCH_PARENT;
			params.width = LayoutParams.MATCH_PARENT;
			encrollStudent.setLayoutParams(params);
		}
		// 约课------------------------
		if (moreDataBean != null) {

			lineDataList.clear();
			for (Datalist datalist : moreDataBean.datalist) {
				yuekeSum += datalist.reservationcoursecount;
				bean = new MoreDataBean();
				bean.timeX = switchYear(datalist.month);
				bean.countY = datalist.reservationcoursecount;
				lineDataList.add(bean);
			}

			aboutClass = new LineChartDemoTwo(mContext, lineDataList);
			relativeLayout_charttwo.removeAllViews();
			relativeLayout_charttwo.addView(aboutClass);
			LayoutParams params = aboutClass.getLayoutParams();
			params.height = LayoutParams.MATCH_PARENT;
			params.width = LayoutParams.MATCH_PARENT;
			aboutClass.setLayoutParams(params);
		}
		// 教练授课---------------------------
		if (moreDataBean != null) {

			lineDataList.clear();
			lineDataList.clear();
			for (Coursedata datalist : moreDataBean.coursedata) {
				bean = new MoreDataBean();
				bean.timeX = datalist.coachcount + "";
				bean.countY = datalist.coursecount;
				lineDataList.add(bean);
			}

			giveLesoons = new BarChartDemo(mContext, lineDataList);
			relativeLayout_chartbar.removeAllViews();
			relativeLayout_chartbar.addView(giveLesoons);
			LayoutParams params = giveLesoons.getLayoutParams();
			params.height = LayoutParams.MATCH_PARENT;
			params.width = LayoutParams.MATCH_PARENT;
			giveLesoons.setLayoutParams(params);
		}
		// 评价---------------------
		if (moreDataBean != null) {

			// 好评
			List<MoreDataBean> goodcommentlist = new ArrayList<MoreDataBean>();
			for (Datalist goodcomment : moreDataBean.datalist) {
				bean = new MoreDataBean();
				bean.timeX = switchYear(goodcomment.month);
				bean.countY = goodcomment.goodcommentcount;

				goodcommentlist.add(bean);
			}
			// 中评
			List<MoreDataBean> generalcommentlist = new ArrayList<MoreDataBean>();
			for (Datalist generalcomment : moreDataBean.datalist) {
				bean = new MoreDataBean();
				bean.timeX = switchYear(generalcomment.month);
				bean.countY = generalcomment.generalcomment;

				generalcommentlist.add(bean);
			}
			// 差评
			List<MoreDataBean> badcommentlist = new ArrayList<MoreDataBean>();
			for (Datalist badcomment : moreDataBean.datalist) {
				bean = new MoreDataBean();
				bean.timeX = switchYear(badcomment.month);
				bean.countY = badcomment.badcommentcount;

				badcommentlist.add(bean);
			}
			// 投诉
			List<MoreDataBean> complaintlist = new ArrayList<MoreDataBean>();
			for (Datalist complaint : moreDataBean.datalist) {
				bean = new MoreDataBean();
				bean.timeX = switchYear(complaint.month);
				bean.countY = complaint.complaintcount;

				complaintlist.add(bean);
			}

			assess = new LineChartDemoThree(mContext, goodcommentlist,
					generalcommentlist, badcommentlist, complaintlist);
			relativeLayout_chartthree.removeAllViews();
			relativeLayout_chartthree.addView(assess);
			LayoutParams params = assess.getLayoutParams();
			params.height = LayoutParams.MATCH_PARENT;
			params.width = LayoutParams.MATCH_PARENT;
			assess.setLayoutParams(params);
		}
	}

	private String switchWeek(int day) {
		String dayString = "";
		switch (day) {
		case 1:
			dayString = "周一";
			break;
		case 2:
			dayString = "周二";
			break;
		case 3:
			dayString = "周三";
			break;
		case 4:
			dayString = "周四";
			break;
		case 5:
			dayString = "周五";
			break;
		case 6:
			dayString = "周六";
			break;
		case 7:
			dayString = "周天";
			break;

		default:
			break;

		}
		return dayString;
	}

	private String switchMonth(int weekindex) {
		String dayString = "";
		switch (weekindex) {
		case 1:
			dayString = "第一周";
			break;
		case 2:
			dayString = "第二周";
			break;
		case 3:
			dayString = "第三周";
			break;
		case 4:
			dayString = "第四周";
			break;
		case 5:
			dayString = "第五周";
			break;

		default:
			break;

		}
		return dayString;
	}

	private String switchYear(int month) {
		String dayString = "";
		switch (month) {
		case 1:
			dayString = "第1月";
			break;
		case 2:
			dayString = "第2月";
			break;
		case 3:
			dayString = "第3月";
			break;
		case 4:
			dayString = "第4月";
			break;
		case 6:
			dayString = "第6月";
			break;
		case 7:
			dayString = "第7月";
			break;
		case 8:
			dayString = "第8月";
			break;
		case 9:
			dayString = "第9月";
			break;
		case 10:
			dayString = "第10月";
			break;
		case 11:
			dayString = "第11月";
			break;
		case 12:
			dayString = "第12月";
			break;

		default:
			break;

		}
		return dayString;
	}
}
