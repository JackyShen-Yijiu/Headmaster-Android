package com.yibu.headmaster.fragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ExpandableListView.OnGroupClickListener;

import com.google.gson.reflect.TypeToken;
import com.jzjf.headmaster.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.yibu.headmaster.adapter.PercentageAdapter;
import com.yibu.headmaster.api.ApiHttpClient;
import com.yibu.headmaster.base.BasePagerFragment;
import com.yibu.headmaster.bean.MonthData;
import com.yibu.headmaster.bean.PassBean;
import com.yibu.headmaster.global.HeadmasterApplication;
import com.yibu.headmaster.utils.CommonUtils;
import com.yibu.headmaster.utils.JsonUtil;
import com.yibu.headmaster.utils.LogUtil;
import com.yibu.headmaster.utils.ToastUtil;

/**
 * 合格率
 * 
 * @author pengdonghua
 * 
 */
public class PassPercentageFragament extends BasePagerFragment {

	private ExpandableListView lv;

	PercentageAdapter adapter;
	/** 科目 N */
	private String params1, params2;

	private Map<String, List<PassBean>> map = new HashMap<String, List<PassBean>>();

	// private String tempDate = "";
	/** 当前父 id */
	private int tempDate = 0;

	// List<PassBean> detailList = new ArrayList<PassBean>();

	public static PassPercentageFragament getInstance(Context context,
			String param1, String param2) {
		PassPercentageFragament frag = new PassPercentageFragament(context);
		Bundle b = new Bundle();
		b.putString("params1", param1);
		b.putString("param2", param2);
		frag.setArguments(b);
		return frag;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			params1 = getArguments().getString("params1");
			params2 = getArguments().getString("params1");
		}
	}

	public PassPercentageFragament(Context context) {
		super(context);
	}

	@Override
	public void process(String data) {
		LogUtil.print("json------->" + data);
		try {// 月份
			List<MonthData> comList = (List<MonthData>) JsonUtil
					.parseJsonToList(data, new TypeToken<List<MonthData>>() {
					}.getType());
			adapter.setDataMonth(comList);
			if (comList.size() == 0) {
				blackPageLayout.setVisibility(View.VISIBLE);
			} else {
				blackPageLayout.setVisibility(View.GONE);
			}

		} catch (Exception e) {
			// 详细列表
		}
	}

//	private View error;

	private LinearLayout blackPageLayout;

	private ImageView blackPageIv;

	private TextView blackPageTv;

	@Override
	public View initView() {
		View view = View.inflate(mContext, R.layout.frag_pass_percentage, null);
		lv = (ExpandableListView) view
				.findViewById(R.id.frag_pass_percentage_lv);

		// 空白页
		blackPageLayout = (LinearLayout) view.findViewById(R.id.black_page_ll);
		blackPageIv = (ImageView) view.findViewById(R.id.black_page_iv);
		blackPageTv = (TextView) view.findViewById(R.id.black_page_tv);
		blackPageIv.setBackgroundResource(R.drawable.message_null);
		blackPageTv.setText("暂时还没有学员的考试信息");
		
		blackPageLayout.setVisibility(View.GONE);
//		error = view.findViewById(R.id.error);
		adapter = new PercentageAdapter(getActivity());
		lv.setAdapter(adapter);
		lv.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				if (lv.isGroupExpanded(groupPosition)) {// 关闭
					LogUtil.print("关闭");
				} else {// 展开
					LogUtil.print("展开");
					tempDate = groupPosition;
					loadExamInfor(adapter.getGroup(groupPosition)._id,
							groupPosition);
				}
				return false;
			}
		});
		return view;
	}

	@Override
	public void initData() {
		loadMonthData();
	}

	private int falg = 0;

	private void loadMonthData() {
		falg = 1;
		LogUtil.print("month-->" + params1);
		ApiHttpClient.get("statistics/getexammonth?userid="
				+ HeadmasterApplication.app.userInfo.userid + "&schoolid="
				+ HeadmasterApplication.app.userInfo.driveschool.schoolid
				+ "&subjectid=" + params1, handler);

	}

	/**
	 * 按照月份请求
	 */
	public void loadExamInfor(String date, int id) {
		falg = 2;
		if (map.containsKey(id + "")) {// 如果已经请求过了
			return;
		}

		String[] temp = date.split("-");
		// String url = "statistics/getexaminfo";
		ApiHttpClient.get("statistics/getexaminfo?userid="
				+ HeadmasterApplication.app.userInfo.userid + "&schoolid="
				+ HeadmasterApplication.app.userInfo.driveschool.schoolid
				+ "&subjectid=" + params1 + "&year=" + temp[0] + "&month="
				+ temp[1], new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				String value = parseJson(arg2);
				if (!TextUtils.isEmpty(msg)) {
					// 加载失败，弹出失败对话框
					ToastUtil.showToast(mContext, msg);
				} else {
					List<PassBean> detailList = (List<PassBean>) JsonUtil
							.parseJsonToList(value,
									new TypeToken<List<PassBean>>() {
									}.getType());
					map.put(tempDate + "", detailList);
					adapter.setDataDetail(map);
				}

			}

		});
	}

	@Override
	public void processFailure() {
		blackPageIv.setBackgroundResource(R.drawable.net_null);
		blackPageTv.setText(CommonUtils.getString(R.string.no_network));
	}

}
