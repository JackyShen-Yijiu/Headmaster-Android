package com.yibu.headmaster;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.res.ColorStateList;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.jzjf.headmaster.R;
import com.yibu.headmaster.api.ApiHttpClient;
import com.yibu.headmaster.bean.MainPageDataV2Bean;
import com.yibu.headmaster.bean.MainPageDataV2Bean.Schoolstudentcount;
import com.yibu.headmaster.bean.MoreDataBean;
import com.yibu.headmaster.bean.MoreDataOfWeekBean;
import com.yibu.headmaster.bean.MoreDataOfWeekBean.Datalist;
import com.yibu.headmaster.bean.StatisticBean;
import com.yibu.headmaster.datachart.LineChartDemoOne;
import com.yibu.headmaster.global.HeadmasterApplication;
import com.yibu.headmaster.utils.JsonUtil;
import com.yibu.headmaster.utils.LogUtil;

/**
 * 招生统计
 * 
 * @author pengdonghua
 * 
 */
public class StatisticsAct extends BaseActivity {

	private List<MoreDataBean> lineDataList;

	private int totalWeek, totalMonth, totalYear;

	private LineChartDemoOne encrollStudent;

	private FrameLayout frameLayout;

	private RadioButton radio1, radio2, radio0;

	private int searchtype = 3; // 查询时间类型：1 今天2 昨天 3 一周 4 本月5本年
	/** 2016年4月第四周 */
	private TextView tvCurrent;

	private TextView tvTotal;

	private TextView tvToday;

	private TextView tvSubject1, tvSubject2, tvSubject3, tvSubject4;

	MoreDataOfWeekBean weekBean = null;
	MoreDataOfWeekBean MonthBean = null;
	MoreDataOfWeekBean YearBean = null;
	/** 月数据 */
	StatisticBean monthBean = null;
	/** 年数据 */
	StatisticBean yearBean = null;

	@Override
	protected void initView() {
		View view = View.inflate(getBaseContext(), R.layout.act_statistics,
				null);
		content.addView(view);
		tvToday = (TextView) view.findViewById(R.id.act_statistics_today);
		tvCurrent = (TextView) view.findViewById(R.id.act_statist_current);
		tvTotal = (TextView) view.findViewById(R.id.act_statistics_total);
		frameLayout = (FrameLayout) view.findViewById(R.id.act_statistics_line);

		radio0 = (RadioButton) view.findViewById(R.id.radio0);
		radio1 = (RadioButton) view.findViewById(R.id.radio1);
		radio2 = (RadioButton) view.findViewById(R.id.radio2);
		tvCurrent.setText(getCuurentData(R.id.radio0));
		RadioGroup radio = ((RadioGroup) view.findViewById(R.id.radioGroup1));
		tvSubject1 = (TextView) findViewById(R.id.act_statist_subject1);
		tvSubject2 = (TextView) findViewById(R.id.act_statist_subject2);
		tvSubject3 = (TextView) findViewById(R.id.act_statist_subject3);
		tvSubject4 = (TextView) findViewById(R.id.act_statist_subject4);

		radio.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				tvCurrent.setText(getCuurentData(checkedId));
				switch (checkedId) {
				case R.id.radio0:
					// radio0.setTextColor(Color.WHITE);
					searchtype = 3;
					loadDataFromWeb();

					// radio1.setTextColor(getResources().getColor(R.color.text_color_light_black));
					break;
				case R.id.radio1:
					// radio1.setTextColor(Color.WHITE);
					searchtype = 4;
					loadDataFromWeb();
					break;
				case R.id.radio2:
					// radio2.setTextColor(Color.WHITE);
					searchtype = 5;
					loadDataFromWeb();
					break;
				}
			}
		});

	}

	private String getCuurentData(int checkedId) {
		long t = System.currentTimeMillis();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		cal.get(Calendar.WEEK_OF_MONTH);
		cal.get(Calendar.YEAR);
		cal.get(Calendar.MONTH);
		switch (checkedId) {
		case R.id.radio0:
			return cal.get(Calendar.YEAR) + "年" + (cal.get(Calendar.MONTH) + 1)
					+ "月第" + cal.get(Calendar.WEEK_OF_MONTH) + "周";
		case R.id.radio1:
			return cal.get(Calendar.YEAR) + "年" + (cal.get(Calendar.MONTH) + 1)
					+ "月";
		case R.id.radio2:
			return cal.get(Calendar.YEAR) + "年";
		}
		return "";
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		lineDataList = new ArrayList<MoreDataBean>();
		setColor("0");
		MainPageDataV2Bean bean = (MainPageDataV2Bean) getIntent()
				.getSerializableExtra("todayBean");
		tvToday.setText(bean.applystudentcount + "");

		for (Schoolstudentcount b : bean.schoolstudentcount) {
			switch (b.subjectid) {
			case 1:
				tvSubject1.setText(b.studentcount + "");
				break;
			case 2:
				tvSubject2.setText(b.studentcount + "");
				break;
			case 3:
				tvSubject3.setText(b.studentcount + "");
				break;
			case 4:
				tvSubject4.setText(b.studentcount + "");
				break;
			}
		}
		loadDataFromWeb();
		// LoginServlet?phone=13120064118&type=1
		// ApiHttpClient.getDirect("http://192.168.0.124:8080/OutDoorComment/servlet/LoginServlet?phone=13120064118&type=1",
		// handler);
	}

	private void loadDataFromWeb() {
		String userId = HeadmasterApplication.app.userInfo.userid;
		String schoolId = HeadmasterApplication.app.userInfo.driveschool.schoolid;

		ApiHttpClient.get("statistics/applyschoolinfo?userid=" + userId
				+ "&searchtype=" + searchtype + "&schoolid=" + schoolId,
				handler);

	}

	@Override
	public void processSuccess(String data) {
		LogUtil.print("processSuccess-->>" + data);
		setWeekData(data);
	}

	@Override
	public void processFailure() {
		// TODO Auto-generated method stub

	}

	/**
	 * 本周 本月 本年
	 * 
	 * @param data
	 */
	private void setWeekData(String data) {
		MoreDataOfWeekBean moreDataBean = null;
		// 月 ，年
		StatisticBean tempBean = null;
		if (searchtype == 3) {// 本周
			weekBean = JsonUtil.parseJsonToBean(data, MoreDataOfWeekBean.class);
			moreDataBean = weekBean;
		} else if (searchtype == 4) {// 本月

			monthBean = JsonUtil.parseJsonToBean(data, StatisticBean.class);
			tempBean = monthBean;
			// MonthBean = JsonUtil.parseJsonToBean(data,
			// MoreDataOfWeekBean.class);
			// moreDataBean = MonthBean;
		} else if (searchtype == 5) {// 本年
			yearBean = JsonUtil.parseJsonToBean(data, StatisticBean.class);
			tempBean = yearBean;
		}

		// 招生---------------------------
		if (moreDataBean != null) {
			MoreDataBean bean = null;
			lineDataList.clear();
			totalWeek = 0;
			// 本周
			for (Datalist datalist : moreDataBean.datalist) {
				totalWeek += datalist.applystudentcount;
				bean = new MoreDataBean();
				if (searchtype == 3) {// 本周
					bean.timeX = switchWeek(datalist.day);
				}
				// }else if(searchtype ==4){//本月
				// bean.timeX = switchMonth(datalist.weekindex);
				// }else if(searchtype ==5){//本年
				// bean.timeX = switchYear(datalist.month);
				// }

				bean.countY = datalist.applystudentcount;
				lineDataList.add(bean);
			}

			LogUtil.print("datasize-->" + lineDataList.size());
			setColor(totalWeek + "");
			// tvTotal.setText("共"+totalWeek+"人");
			encrollStudent = new LineChartDemoOne(this, lineDataList);
			frameLayout.removeAllViews();
			frameLayout.addView(encrollStudent);
			LayoutParams params = encrollStudent.getLayoutParams();
			params.height = LayoutParams.MATCH_PARENT;
			params.width = LayoutParams.MATCH_PARENT;
			// 清空 滑动事件
			encrollStudent.restTouchBind();
			encrollStudent.setLayoutParams(params);
		}
		//
		if (tempBean != null) {
			MoreDataBean bean = null;
			lineDataList.clear();
			totalMonth = 0;
			totalYear = 0;
			// 本周
			for (int i = 0; i < tempBean.datalist.length; i++) {
				bean = new MoreDataBean();
				if (searchtype == 4) {// 本日
					totalMonth += Integer.parseInt(tempBean.datalist[i]);
					bean.timeX = switchMonth(i);
				} else if (searchtype == 5) {// 本年
					totalYear += Integer.parseInt(tempBean.datalist[i]);
					bean.timeX = switchYear(i);
				}
				bean.countY = Integer.parseInt(tempBean.datalist[i]);
				lineDataList.add(bean);

			}

			LogUtil.print("datasize-->" + lineDataList.size());
			if (searchtype == 4) {// 本日
				setColor(totalMonth + "");
			} else if (searchtype == 5) {// 本年
				setColor(totalYear + "");
			}
			
			// tvTotal.setText("共"+totalWeek+"人");
			encrollStudent = new LineChartDemoOne(this, lineDataList);
			frameLayout.removeAllViews();
			frameLayout.addView(encrollStudent);
			LayoutParams params = encrollStudent.getLayoutParams();
			params.height = LayoutParams.MATCH_PARENT;
			params.width = LayoutParams.MATCH_PARENT;
			// 清空 滑动事件
			encrollStudent.restTouchBind();
			encrollStudent.setLayoutParams(params);
		}

	}

	private void setColor(String text) {
		ColorStateList redColors = ColorStateList.valueOf(0xffff0000);
		SpannableStringBuilder spanBuilder = new SpannableStringBuilder("共"
				+ text + "人");
		// style 为0 即是正常的，还有Typeface.BOLD(粗体) Typeface.ITALIC(斜体)等
		// size 为0 即采用原始的正常的 size大小
		spanBuilder.setSpan(
				new TextAppearanceSpan(null, 0, 30, redColors, null), 1,
				text.length() + 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		tvTotal.setText(spanBuilder);
	}

	private String switchWeek(int day) {
		String dayString = "";
		switch (day) {
		case 1:
			dayString = "一";
			break;
		case 2:
			dayString = "二";
			break;
		case 3:
			dayString = "三";
			break;
		case 4:
			dayString = "四";
			break;
		case 5:
			dayString = "五";
			break;
		case 6:
			dayString = "六";
			break;
		case 7:
			dayString = "日";
			break;
		default:
			break;

		}
		return dayString;
	}

	private String switchMonth(int weekindex) {
		String dayString = "";
		switch (weekindex) {
		case 0:
			dayString = "上旬";
			break;
		case 1:
			dayString = "中旬";
			break;
		case 2:
			dayString = "下旬";
			break;
		// case 4:
		// dayString = "第四周";
		// break;
		// case 5:
		// dayString = "第五周";
		// break;

		default:
			break;

		}
		return dayString;
	}

	private String switchYear(int month) {

		String dayString = "";
		switch (month) {
		case 0:
			dayString = "第一季度";
			break;
		case 1:
			dayString = "第二季度";
			break;
		case 2:
			dayString = "第三季度";
			break;
		case 3:
			dayString = "第四季度";
			break;
		// case 6:
		// dayString = "第6月";
		// break;
		// case 7:
		// dayString = "第7月";
		// break;
		// case 8:
		// dayString = "第8月";
		// break;
		// case 9:
		// dayString = "第9月";
		// break;
		// case 10:
		// dayString = "第10月";
		// break;
		// case 11:
		// dayString = "第11月";
		// break;
		// case 12:
		// dayString = "第12月";
		// break;

		default:
			break;

		}
		return dayString;
	}

}
