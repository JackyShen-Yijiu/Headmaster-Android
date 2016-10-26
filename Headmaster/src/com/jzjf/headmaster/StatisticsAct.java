package com.jzjf.headmaster;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.jzjf.headmaster.R;
import com.jzjf.headmaster.api.ApiHttpClient;
import com.jzjf.headmaster.bean.MainPageDataV2Bean;
import com.jzjf.headmaster.bean.MainPageDataV2Bean.Schoolstudentcount;
import com.jzjf.headmaster.bean.MoreDataBean;
import com.jzjf.headmaster.bean.StatisticBean;
import com.jzjf.headmaster.datachart.LineChartDemoOne;
import com.jzjf.headmaster.global.HeadmasterApplication;
import com.jzjf.headmaster.utils.JsonUtil;

import android.content.res.ColorStateList;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

/**
 * 招生统计
 * 
 * @author pengdonghua
 * 
 */
public class StatisticsAct extends BaseActivity implements OnCheckedChangeListener {

	private List<MoreDataBean> lineDataList;

	private LineChartDemoOne encrollStudent;

	private FrameLayout frameLayout;
	
	// 查询时间类型：1 今天 2 昨天 3 一周 4 本月 5 本年
	private int searchtype = 3;
	
	private TextView tvCurrent;

	private TextView tvTotal;

	private TextView tvToday;

	private TextView tvSubject1, tvSubject2, tvSubject3, tvSubject4;

	@Override
	protected void initView() {
		View view = View.inflate(getBaseContext(), R.layout.act_statistics,
				null);
		content.addView(view);
		tvToday = (TextView) view.findViewById(R.id.act_statistics_today);
		tvCurrent = (TextView) view.findViewById(R.id.act_statist_current);
		tvTotal = (TextView) view.findViewById(R.id.act_statistics_total);
		frameLayout = (FrameLayout) view.findViewById(R.id.act_statistics_line);

		RadioGroup radio = ((RadioGroup) view.findViewById(R.id.radioGroup1));
		tvSubject1 = (TextView) findViewById(R.id.act_statist_subject1);
		tvSubject2 = (TextView) findViewById(R.id.act_statist_subject2);
		tvSubject3 = (TextView) findViewById(R.id.act_statist_subject3);
		tvSubject4 = (TextView) findViewById(R.id.act_statist_subject4);
		
		tvCurrent.setText(getCuurentData(R.id.radio0));
		
		radio.setOnCheckedChangeListener(this);
	}
	
	/**
	 * 获取当前第几周 年 月
	 * @param checkedId
	 * @return
	 */
	private String getCuurentData(int checkedId) {
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
	}

	private void loadDataFromWeb() {
		ApiHttpClient.get(ApiHttpClient.COLLECT_APPLY + "?searchtype=" + searchtype, handler);
	}

	@Override
	public void processSuccess(String data) {
		setWeekData(data);
	}

	@Override
	public void processFailure() {
	}

	/**
	 * 本周 本月 本年
	 * 
	 * @param data
	 */
	private void setWeekData(String data) {
		StatisticBean statisticBean = JsonUtil.parseJsonToBean(data, StatisticBean.class);
		if(statisticBean != null && statisticBean.datalist != null) {
			lineDataList.clear();
			String[] datalist = statisticBean.datalist;
			int total = 0;
			for (int i = 0; i < datalist.length; i++) {
				MoreDataBean bean = new MoreDataBean();
				if(searchtype == 3) { // 本周 显示：一周／粒度：天
					bean.timeX = switchWeek(i);
				} else if(searchtype == 4) { // 本月 显示：一年／粒度：月
					bean.timeX = String.valueOf(i + 1);
				} else if(searchtype == 5) { // 本年 显示：一年／粒度：季度
					bean.timeX = String.valueOf(i + 1) + "月";
				}
				bean.countY = Integer.parseInt(datalist[i]);
				total = total + bean.countY;
				lineDataList.add(bean);
			}
			setColor(String.valueOf(total));
		}
		
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

	private void setColor(String text) {
		ColorStateList redColors = ColorStateList.valueOf(0xffff0000);
		SpannableStringBuilder spanBuilder = new SpannableStringBuilder("共" + text + "人");
		// style 为0 即是正常的，还有Typeface.BOLD(粗体) Typeface.ITALIC(斜体)等
		// size 为0 即采用原始的正常的 size大小
		spanBuilder.setSpan(
				new TextAppearanceSpan(null, 0, (int)(15*HeadmasterApplication.density), redColors, null), 1,
				text.length() + 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		tvTotal.setText(spanBuilder);
	}

	private String switchWeek(int day) {
		String dayString = "";
		switch (day) {
		case 0:
			dayString = "周一";
			break;
		case 1:
			dayString = "周二";
			break;
		case 2:
			dayString = "周三";
			break;
		case 3:
			dayString = "周四";
			break;
		case 4:
			dayString = "周五";
			break;
		case 5:
			dayString = "周六";
			break;
		case 6:
			dayString = "周日";
			break;
		default:
			break;

		}
		return dayString;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		tvCurrent.setText(getCuurentData(checkedId));
		switch (checkedId) {
		case R.id.radio0:
			searchtype = 3;
			loadDataFromWeb();
			break;
		case R.id.radio1:
			searchtype = 4;
			loadDataFromWeb();
			break;
		case R.id.radio2:
			searchtype = 5;
			loadDataFromWeb();
			break;
		}
	}

}
