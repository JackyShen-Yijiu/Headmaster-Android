package com.yibu.headmaster.base.impl;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.jzjf.headmaster.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yibu.headmaster.AssessActivity;
import com.yibu.headmaster.MainActivity;
import com.yibu.headmaster.PassPercentageAct;
import com.yibu.headmaster.StatisticsAct;
import com.yibu.headmaster.api.ApiHttpClient;
import com.yibu.headmaster.base.BasePager;
import com.yibu.headmaster.bean.MainOfTodayBean.Schoolstudentcount;
import com.yibu.headmaster.bean.MainOfWeekBean;
import com.yibu.headmaster.bean.MainPageDataV2Bean;
import com.yibu.headmaster.event.ComplaintEvent;
import com.yibu.headmaster.global.HeadmasterApplication;
import com.yibu.headmaster.utils.JsonUtil;
import com.yibu.headmaster.utils.LogUtil;

import de.greenrobot.event.EventBus;

public class DataPager extends BasePager implements OnClickListener {

	@ViewInject(R.id.tv_data_subject1_num)
	private TextView subject1Num;
	@ViewInject(R.id.tv_data_subject2_num)
	private TextView subject2Num;
	@ViewInject(R.id.tv_data_subject3_num)
	private TextView subject3Num;
	@ViewInject(R.id.tv_data_subject4_num)
	private TextView subject4Num;
	@ViewInject(R.id.tv_data_current_num)
	private TextView currentNum;
	
	@ViewInject(R.id.ll_subject_one_student)
	private LinearLayout subjectOneStudent;
	@ViewInject(R.id.ll_subject_two_student)
	private LinearLayout subjectTwoStudent;
	@ViewInject(R.id.ll_subject_three_student)
	private LinearLayout subjectThreeStudent;
	@ViewInject(R.id.ll_subject_four_student)
	private LinearLayout subjectFourStudent;
	
	@ViewInject(R.id.data_goodcommnent_tv)
	private TextView goodCommnent;
	@ViewInject(R.id.data_generalcomment_tv)
	private TextView generalCommnent;
	@ViewInject(R.id.data_badcomment_tv)
	private TextView badCommnent;
	@ViewInject(R.id.data_star_1)
	private LinearLayout datastar1;
	@ViewInject(R.id.data_star_2)
	private LinearLayout datastar2;
	@ViewInject(R.id.data_star_3)
	private LinearLayout datastar3;
	//圆环
	@ViewInject(R.id.dout_progress_subject_one)
	private DonutProgress progressSubjectOne;
	@ViewInject(R.id.dout_progress_subject_two)
	private DonutProgress progressSubjectTwo;
	@ViewInject(R.id.dout_progress_subject_three)
	private DonutProgress progressSubjectThree;
	@ViewInject(R.id.dout_progress_subject_four)
	private DonutProgress progressSubjectFour;
	
	@ViewInject(R.id.tv_subject_one_remaining_num)
	private TextView subjectOneRemainingNum;
	@ViewInject(R.id.tv_subject_two_remaining_num)
	private TextView subjectTwoRemainingNum;
	@ViewInject(R.id.tv_subject_three_remaining_num)
	private TextView subjectThreeRemainingNum;
	@ViewInject(R.id.tv_subject_four_remaining_num)
	private TextView subjectFourRemainingNum;
	
	@ViewInject(R.id.ll_pass_percent_subject_one)
	private LinearLayout passPercentSubjectOne;
	@ViewInject(R.id.ll_pass_percent_subject_two)
	private LinearLayout passPercentSubjectTwo;
	@ViewInject(R.id.ll_pass_percent_subject_three)
	private LinearLayout passPercentSubjectThree;
	@ViewInject(R.id.ll_pass_percent_subject_four)
	private LinearLayout passPercentSubjectFour;

	private int searchtype = 1;// 查询类型 查询时间类型：1 今天2 昨天 3 一周 4 本月5本年

	private MainActivity activity;
	private MainPageDataV2Bean todayBean;

	public DataPager(Context context) {
		super(context);
	}

	@Override
	public void initData() {
		activity = (MainActivity) getActivity();
		loadNetworkData();

	}

	@Override
	public View initView() {
		View view = View.inflate(HeadmasterApplication.getContext(),
				R.layout.data_information, null);
		ViewUtils.inject(this, view);

		datastar1.setOnClickListener(this);
		datastar2.setOnClickListener(this);
		datastar3.setOnClickListener(this);
		subjectOneStudent.setOnClickListener(this);
		subjectTwoStudent.setOnClickListener(this);
		subjectThreeStudent.setOnClickListener(this);
		subjectFourStudent.setOnClickListener(this);
		passPercentSubjectOne.setOnClickListener(this);
		passPercentSubjectTwo.setOnClickListener(this);
		passPercentSubjectThree.setOnClickListener(this);
		passPercentSubjectFour.setOnClickListener(this);
		return view;
	}

	@Override
	public void process(String data) {

		todayBean = JsonUtil.parseJsonToBean(data,
				MainPageDataV2Bean.class);
		//评论数
		setCommnent(todayBean);
		//学生数量
		setSchoolStudentCount(todayBean.schoolstudentcount);
		//当前报名人数
		currentNum.setText(todayBean.applystudentcount + "");
		//合格率
		setProgress(todayBean);
//		// 今日和昨日
//		if (searchtype == 1 || searchtype == 2) {
//			LogUtil.print("=------------" + data);
//			MainOfTodayBean todayBean = JsonUtil.parseJsonToBean(data,
//					MainOfTodayBean.class);
//			if (todayBean != null) {
//
//				LogUtil.print(todayBean.coachstotalcoursecount + "-------");
//
//				if (searchtype == 1) {
//					
//
//				}
////				setCircleData(todayBean);
//				setCommnent(todayBean);
//			}
//		} else if (searchtype == 3) {
//			// 本周
//			MainOfWeekBean weekBean = JsonUtil.parseJsonToBean(data,
//					MainOfWeekBean.class);
//			if (weekBean != null) {
//				setWeekCommnent(weekBean);
//			}
//		}
	}

	//设置合格率
	private void setProgress(MainPageDataV2Bean todayBean) {
		
		progressSubjectOne.setProgress(todayBean.overstockstudent[0]);
		progressSubjectTwo.setProgress(todayBean.overstockstudent[1]);
		progressSubjectThree.setProgress(todayBean.overstockstudent[2]);
		progressSubjectFour.setProgress(todayBean.overstockstudent[3]);
		//积压人数
		subjectOneRemainingNum.setText(todayBean.passrate[0]+"");
		subjectTwoRemainingNum.setText(todayBean.passrate[1]+"");
		subjectThreeRemainingNum.setText(todayBean.passrate[2]+"");
		subjectFourRemainingNum.setText(todayBean.passrate[3]+"");
	}

	// 设置本周的评论数
	private void setWeekCommnent(MainOfWeekBean weekBean) {
		goodCommnent.setText(weekBean.goodcommentcount + "");
		generalCommnent.setText(weekBean.generalcomment + "");
		badCommnent.setText(weekBean.badcommentcount + "");
		
		activity.setComplaintNUm(weekBean.complaintstudentcount);
	}

	// 设置评论数
	private void setCommnent(MainPageDataV2Bean todayBean) {
		goodCommnent.setText(todayBean.commentstudentcount.goodcommnent + "");
		generalCommnent.setText(todayBean.commentstudentcount.generalcomment
				+ "");
		badCommnent.setText(todayBean.commentstudentcount.badcomment + "");
		activity.setComplaintNUm(todayBean.complaintstudentcount);
	}

	// 设置头部学校学生数量
	private void setSchoolStudentCount(
			List<com.yibu.headmaster.bean.MainPageDataV2Bean.Schoolstudentcount> schoolstudentcount) {
		
		if (schoolstudentcount != null) {
			for (int i = 0; i < schoolstudentcount.size(); i++) {
				int subjectid = schoolstudentcount.get(i).subjectid;
				switch (subjectid) {
				case 1:

					subject1Num.setText(schoolstudentcount.get(i).studentcount
							+ "");
					
					break;
				case 2:
					subject2Num.setText(schoolstudentcount.get(i).studentcount
							+ "");
					break;
				case 3:
					subject3Num.setText(schoolstudentcount.get(i).studentcount
							+ "");

					break;
				case 4:
					subject4Num.setText(schoolstudentcount.get(i).studentcount
							+ "");

					break;

				default:
					break;
				}
			}
		}

	}





	private void loadNetworkData() {
		String userId = null;
		String schoolId = null;
		if (HeadmasterApplication.app != null) {

			userId = HeadmasterApplication.app.userInfo.userid;
			schoolId = HeadmasterApplication.app.userInfo.driveschool.schoolid;
		}
		// String schoolId = bean.driveschool.schoolid;

		ApiHttpClient.get("statistics/getmainpagedatav2?userid=" + userId
				+ "&searchtype=" + searchtype + "&schoolid=" + schoolId,
				handler);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.data_star_1:
			Intent intent2 = new Intent(mContext, AssessActivity.class);
			intent2.putExtra("title", searchtype);
			intent2.putExtra("commentlevel", 1);
			mContext.startActivity(intent2);
			break;
		case R.id.data_star_2:
			Intent intent3 = new Intent(mContext, AssessActivity.class);
			intent3.putExtra("title", searchtype);
			intent3.putExtra("commentlevel", 2);
			mContext.startActivity(intent3);
			break;
		case R.id.data_star_3:
			Intent intent4 = new Intent(mContext, AssessActivity.class);
			intent4.putExtra("title", searchtype);
			intent4.putExtra("commentlevel", 3);
			mContext.startActivity(intent4);
			break;
//		case R.id.data_star_4:
//			Intent intent5 = new Intent(mContext, AssessActivity.class);
//			intent5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			intent5.putExtra("title", searchtype);
//			intent5.putExtra("commentlevel", 4);
//			mContext.startActivity(intent5);
//			break;
			
		case R.id.ll_subject_one_student:
			//学生数量
			Intent intent6 = new Intent(mContext, StatisticsAct.class);
			intent6.putExtra("type", 1);
			intent6.putExtra("todayBean", todayBean);
			mContext.startActivity(intent6);
			break;
		case R.id.ll_subject_two_student:
			Intent intent7 = new Intent(mContext, StatisticsAct.class);
			intent7.putExtra("type", 1);
			intent7.putExtra("todayBean", todayBean);
			mContext.startActivity(intent7);
			break;
		case R.id.ll_subject_three_student:
			Intent intent8 = new Intent(mContext, StatisticsAct.class);
			intent8.putExtra("type", 1);
			intent8.putExtra("todayBean", todayBean);
			mContext.startActivity(intent8);
			break;
		case R.id.ll_subject_four_student:
			Intent intent9 = new Intent(mContext, StatisticsAct.class);
			intent9.putExtra("type", 1);
			intent9.putExtra("todayBean", todayBean);
			mContext.startActivity(intent9);
			break;
			
			
		case R.id.ll_pass_percent_subject_one:
			toPercentage(0);
			//考试合格率
			break;
		case R.id.ll_pass_percent_subject_two:
			toPercentage(1);
			break;
		case R.id.ll_pass_percent_subject_three:
			toPercentage(2);
			break;
		case R.id.ll_pass_percent_subject_four:
			toPercentage(3);
			break;
		default:
			break;
		}
	}
	
	private void toPercentage(int type){
		Intent i1 = new Intent(mContext,PassPercentageAct.class);
		i1.putExtra("current", type);
		mContext.startActivity(i1);
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
	public void onEvent(ComplaintEvent event){
		//
		LogUtil.print("----投诉onEvent");
		Intent intent = new Intent(mContext, AssessActivity.class);
//		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("title", searchtype);
		intent.putExtra("commentlevel", 4);
		mContext.startActivity(intent);
	}

}
