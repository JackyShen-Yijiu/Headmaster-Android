package com.jzjf.headmaster.fragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;

import com.google.gson.reflect.TypeToken;
import com.jzjf.headmaster.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.jzjf.headmaster.adapter.PercentageAdapter;
import com.jzjf.headmaster.api.ApiHttpClient;
import com.jzjf.headmaster.base.BasePagerFragment;
import com.jzjf.headmaster.bean.MonthData;
import com.jzjf.headmaster.bean.PassBean;
import com.jzjf.headmaster.utils.CommonUtils;
import com.jzjf.headmaster.utils.JsonUtil;
import com.jzjf.headmaster.utils.LogUtil;
import com.jzjf.headmaster.utils.ToastUtil;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 合格率
 * 
 * @author pengdonghua
 * 
 */
public class PassPercentageFragament extends BasePagerFragment {

	private ExpandableListView lv;

	PercentageAdapter adapter;
	
	private String subjectId;

	private Map<String, List<PassBean>> map = new HashMap<String, List<PassBean>>();

	private int tempDate = 0;

	// List<PassBean> detailList = new ArrayList<PassBean>();

	public static PassPercentageFragament getInstance(Context context,
			int subjectId, String param) {
		PassPercentageFragament frag = new PassPercentageFragament(context);
		Bundle b = new Bundle();
		b.putString("subjectId", subjectId + "");
		frag.setArguments(b);
		return frag;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			subjectId = getArguments().getString("subjectId");
		}
	}

	public PassPercentageFragament(Context context) {
		super(context);
	}

	@Override
	public void process(String data) {
		try {// 月份
			@SuppressWarnings("unchecked")
			List<MonthData> comList = (List<MonthData>) JsonUtil
					.parseJsonToList(data, new TypeToken<List<MonthData>>() {}.getType());
			
			adapter.setDataMonth(comList);
			
			if (comList.size() == 0) {
				blackPageLayout.setVisibility(View.VISIBLE);
			} else {
				blackPageLayout.setVisibility(View.GONE);
			}

		} catch (Exception e) {
		}
	}

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
		blackPageIv.setBackgroundResource(R.drawable.text_null);
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
		ApiHttpClient.get(ApiHttpClient.COLLECT_EXAM_MONTH + "?subjectid=" + subjectId, handler);
	}

	/**
	 * 按照月份请求
	 */
	public void loadExamInfor(String date, int id) {
		ApiHttpClient.get(ApiHttpClient.COLLECT_EXAM_PASS 
				+ "?subjectid=" + subjectId + "&examdate=" + date, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {

			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				String value = parseJson(arg2);
				if (!TextUtils.isEmpty(msg)) {
					// 加载失败，弹出失败对话框
					ToastUtil.showToast(mContext, msg);
				} else {
					@SuppressWarnings("unchecked")
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
		lv.setVisibility(View.GONE);
		blackPageLayout.setVisibility(View.VISIBLE);
		blackPageIv.setBackgroundResource(R.drawable.net_null);
		blackPageTv.setText(CommonUtils.getString(R.string.no_network));
	}

}
