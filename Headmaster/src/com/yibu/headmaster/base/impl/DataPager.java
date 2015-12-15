package com.yibu.headmaster.base.impl;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yibu.headmaster.AssessActivity;
import com.yibu.headmaster.DataChartActivity;
import com.yibu.headmaster.R;
import com.yibu.headmaster.api.ApiHttpClient;
import com.yibu.headmaster.base.BasePager;
import com.yibu.headmaster.bean.MainOfTodayBean;
import com.yibu.headmaster.bean.MainOfTodayBean.Schoolstudentcount;
import com.yibu.headmaster.bean.MainOfWeekBean;
import com.yibu.headmaster.global.HeadmasterApplication;
import com.yibu.headmaster.utils.JsonUtil;
import com.yibu.headmaster.utils.LogUtil;

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
	@ViewInject(R.id.data_arc_progress_outside_forcast)
	private ArcProgress progressOutsideForcast;
	@ViewInject(R.id.data_arc_progress_outside)
	private ArcProgress progressOutside;
	@ViewInject(R.id.data_arc_progress_inside_forcast)
	private ArcProgress progressInsideForcast;
	@ViewInject(R.id.data_arc_progress_inside)
	private ArcProgress progressInside;
	@ViewInject(R.id.data_goodcommnent_tv)
	private TextView goodCommnent;
	@ViewInject(R.id.data_generalcomment_tv)
	private TextView generalCommnent;
	@ViewInject(R.id.data_badcomment_tv)
	private TextView badCommnent;
	@ViewInject(R.id.data_complaintstudentcount_tv)
	private TextView complaintStudentCount;
	@ViewInject(R.id.data_yesterday_ib)
	private ImageButton yesterday;
	@ViewInject(R.id.data_today_ib)
	private ImageButton today;
	@ViewInject(R.id.data_this_week_ib)
	private ImageButton thisWeek;
	@ViewInject(R.id.data_line_left_iv)
	private ImageView leftLine;
	@ViewInject(R.id.data_line_right_iv)
	private ImageView rightLine;
	@ViewInject(R.id.fl_data_circle)
	private FrameLayout dataCircle;
	@ViewInject(R.id.data_star_ll)
	private LinearLayout datastars;

	private int searchtype = 1;// 查询类型 查询时间类型：1 今天2 昨天 3 一周 4 本月5本年

	private enum DayButton {
		TODAY, YESTERDAY, THISWEEK;
	}

	private DayButton currentDay = DayButton.TODAY;

	public DataPager(Context context) {
		super(context);
	}

	@Override
	public void initData() {
		progressOut1 = 0;
		progressOut2 = 0;
		progressIn2 = 0;
		setState(2);
		loadNetworkData();

	}

	@Override
	public View initView() {
		View view = View.inflate(HeadmasterApplication.getContext(),
				R.layout.data_information, null);
		ViewUtils.inject(this, view);

		yesterday.setOnClickListener(this);
		today.setOnClickListener(this);
		thisWeek.setOnClickListener(this);
		dataCircle.setOnClickListener(this);
		datastars.setOnClickListener(this);
		return view;
	}

	@Override
	public void process(String data) {

		// 今日和昨日
		if (searchtype == 1 || searchtype == 2) {
			LogUtil.print("=------------" + data);
			MainOfTodayBean todayBean = JsonUtil.parseJsonToBean(data,
					MainOfTodayBean.class);
			if (todayBean != null) {

				LogUtil.print(todayBean.coachstotalcoursecount + "-------");

				List<Schoolstudentcount> schoolstudentcount = todayBean.schoolstudentcount;
				setSchoolStudentCount(schoolstudentcount);
				currentNum.setText(todayBean.applystudentcount + "");

				setCircleData(todayBean);
				setCommnent(todayBean);
			}
		} else if (searchtype == 3) {
			// 本周
			MainOfWeekBean weekBean = JsonUtil.parseJsonToBean(data,
					MainOfWeekBean.class);
			if (weekBean != null) {
				setWeekCommnent(weekBean);
			}
		}
	}

	// 设置本周的评论数
	private void setWeekCommnent(MainOfWeekBean weekBean) {
		goodCommnent.setText(weekBean.goodcommentcount + "");
		generalCommnent.setText(weekBean.generalcomment + "");
		badCommnent.setText(weekBean.badcommentcount + "");
		complaintStudentCount.setText(weekBean.complaintstudentcount + "");
	}

	// 设置评论数
	private void setCommnent(MainOfTodayBean todayBean) {
		goodCommnent.setText(todayBean.commentstudentcount.goodcommnent + "");
		generalCommnent.setText(todayBean.commentstudentcount.generalcomment
				+ "");
		badCommnent.setText(todayBean.commentstudentcount.badcomment + "");
		complaintStudentCount.setText(todayBean.complaintstudentcount + "");
	}

	// 设置头部学校学生数量
	private void setSchoolStudentCount(
			List<Schoolstudentcount> schoolstudentcount) {
		if (schoolstudentcount != null) {
			for (int i = 0; i < schoolstudentcount.size(); i++) {
				int subjectid = schoolstudentcount.get(i).subjectid;
				switch (subjectid) {
				case 0:

					subject1Num.setText(schoolstudentcount.get(i).studentcount
							+ "");
					break;
				case 1:
					subject2Num.setText(schoolstudentcount.get(i).studentcount
							+ "");
					break;
				case 2:
					subject3Num.setText(schoolstudentcount.get(i).studentcount
							+ "");

					break;
				case 3:
					subject4Num.setText(schoolstudentcount.get(i).studentcount
							+ "");

					break;

				default:
					break;
				}
			}
		}

	}

	// 设置圆盘的值
	// private void setCircleData(final MainOfTodayBean todayBean) {
	//
	// progressOutsideForcast.setMax(todayBean.coachstotalcoursecount);
	// progressOutsideForcast.setProgress(0);
	// // 动画效果
	// new Thread(new Runnable() {
	//
	// @Override
	// public void run() {
	// for (int i = 0; i < todayBean.reservationcoursecountday; i++) {
	// try {
	// Thread.sleep(1000 / todayBean.reservationcoursecountday);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	//
	// progressOut1++;
	// Message msg = Message.obtain();
	// msg.what = 1;
	// msgHandler.sendMessage(msg);
	// }
	// }
	// }).start();
	// progressOutside.setMax(todayBean.coachstotalcoursecount);
	// progressOutside.setProgress(0);
	// // 动画效果
	// new Thread(new Runnable() {
	//
	// @Override
	// public void run() {
	// for (int i = 0; i < todayBean.finishreservationnow; i++) {
	// try {
	// Thread.sleep(1000 / todayBean.finishreservationnow);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	//
	// progressOut2++;
	// Message msg = Message.obtain();
	// msg.what = 2;
	// msgHandler.sendMessage(msg);
	// }
	// }
	// }).start();
	//
	// progressInsideForcast.setMax(todayBean.coachcoursenow);
	// progressInsideForcast.setProgress(todayBean.coachcoursenow);
	// if (todayBean.coachcoursenow == 0) {
	//
	// progressInsideForcast.setMax(100);
	// progressInsideForcast.setProgress(100);
	// }
	//
	// progressInside.setMax(todayBean.coachcoursenow);
	// progressInside.setProgress(0);
	// new Thread(new Runnable() {
	//
	// @Override
	// public void run() {
	// for (int i = 0; i < todayBean.finishreservationnow; i++) {
	// try {
	// Thread.sleep(1000 / todayBean.finishreservationnow);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	//
	// progressIn2++;
	// Message msg = Message.obtain();
	// msg.what = 3;
	// msgHandler.sendMessage(msg);
	// }
	// }
	// }).start();
	// }

	private void setCircleData(final MainOfTodayBean todayBean) {

		progressOutsideForcast.setMax(100);
		progressOutsideForcast.setProgress(0);
		// 动画效果
		new Thread(new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < 0; i++) {
					try {
						Thread.sleep(1000 / 98);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					progressOut1++;
					Message msg = Message.obtain();
					msg.what = 1;
					msg.obj = 0;
					msgHandler.sendMessage(msg);
				}
			}
		}).start();
		progressOutside.setMax(100);
		progressOutside.setProgress(0);
		// 动画效果
		new Thread(new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < 76; i++) {
					try {
						Thread.sleep(1000 / 76);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					progressOut2++;
					Message msg = Message.obtain();
					msg.what = 2;
					msg.obj = 76;
					msgHandler.sendMessage(msg);
				}
			}
		}).start();

		progressInsideForcast.setMax(todayBean.coachcoursenow);
		progressInsideForcast.setProgress(todayBean.coachcoursenow);
		if (todayBean.coachcoursenow == 0) {

			progressInsideForcast.setMax(100);
			progressInsideForcast.setProgress(100);
		}

		progressInside.setMax(100);
		progressInside.setProgress(0);
		// new Thread(new Runnable() {
		//
		// @Override
		// public void run() {
		// for (int i = 0; i < 54; i++) {
		// try {
		// Thread.sleep(1000 / 54);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		//
		// progressIn2++;
		// Message msg = Message.obtain();
		// msg.what = 3;
		// msg.obj = 54;
		// msgHandler.sendMessage(msg);
		// }
		// }
		// }).start();
	}

	private int progressOut1 = 0;
	private int progressOut2 = 0;
	private int progressIn2 = 0;
	private Handler msgHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				progressOutsideForcast.setProgress(progressOut1);
				if (progressOut1 != (Integer) msg.obj) {
					setButtonUnClickable(false);
				} else {
					setButtonUnClickable(true);
				}
			} else if (msg.what == 2) {
				progressOutside.setProgress(progressOut2);
				if (progressOut2 != (Integer) msg.obj) {
					setButtonUnClickable(false);
				} else {
					setButtonUnClickable(true);
				}
			} else if (msg.what == 3) {
				progressInside.setProgress(progressIn2);
				if (progressIn2 != (Integer) msg.obj) {
					setButtonUnClickable(false);
				} else {
					setButtonUnClickable(true);
				}
			}
		}

	};

	public void setButtonUnClickable(boolean enable) {
		yesterday.setClickable(enable);
		today.setClickable(enable);
		thisWeek.setClickable(enable);
	}

	private void loadNetworkData() {
		String userId = null;
		String schoolId = null;
		if (HeadmasterApplication.app != null) {

			userId = HeadmasterApplication.app.userInfo.userid;
			schoolId = HeadmasterApplication.app.userInfo.driveschool.schoolid;
		}
		// String schoolId = bean.driveschool.schoolid;

		ApiHttpClient.get("statistics/getmainpagedata?userid=" + userId
				+ "&searchtype=" + searchtype + "&schoolid=" + schoolId,
				handler);
	}

	// 显示内容：： 1 昨天 ， 2今天 ，3 本周
	private void setState(int curState) {
		yesterday.setSelected(false);
		today.setSelected(false);
		thisWeek.setSelected(false);
		switch (curState) {
		case 1:

			yesterday.setSelected(true);
			leftLine.setBackgroundResource(R.drawable.left);
			rightLine.setBackgroundResource(R.drawable.center);
			break;
		case 2:
			today.setSelected(true);
			leftLine.setBackgroundResource(R.drawable.right);
			rightLine.setBackgroundResource(R.drawable.left);
			break;
		case 3:
			thisWeek.setSelected(true);
			leftLine.setBackgroundResource(R.drawable.center);
			rightLine.setBackgroundResource(R.drawable.right);
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.data_yesterday_ib:
			if (currentDay == DayButton.YESTERDAY) {
				return;
			}
			progressOut1 = 0;
			progressOut2 = 0;
			progressIn2 = 0;
			setState(1);
			searchtype = 2;
			loadNetworkData();
			currentDay = DayButton.YESTERDAY;
			break;
		case R.id.data_today_ib:
			if (currentDay == DayButton.TODAY) {
				return;
			}
			progressOut1 = 0;
			progressOut2 = 0;
			progressIn2 = 0;
			setState(2);
			searchtype = 1;
			loadNetworkData();
			currentDay = DayButton.TODAY;
			break;
		case R.id.data_this_week_ib:
			if (currentDay == DayButton.THISWEEK) {
				return;
			}
			setState(3);
			searchtype = 3;
			loadNetworkData();
			currentDay = DayButton.THISWEEK;
			break;
		case R.id.fl_data_circle:
			LogUtil.print("更多数据");
			Intent intent = new Intent(mContext, DataChartActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("searchtype", searchtype - 1);
			mContext.startActivity(intent);
			break;
		case R.id.data_star_ll:
			Intent intent2 = new Intent(mContext, AssessActivity.class);
			intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent2.putExtra("title", searchtype);
			mContext.startActivity(intent2);
			break;
		default:
			break;
		}
	}

}
