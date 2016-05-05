package com.yibu.headmaster.fragment;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.jzjf.headmaster.R;
import com.yibu.headmaster.adapter.AssessAdapter;
import com.yibu.headmaster.adapter.ComplaintAdapter;
import com.yibu.headmaster.adapter.PercentageAdapter;
import com.yibu.headmaster.api.ApiHttpClient;
import com.yibu.headmaster.base.BasePager;
import com.yibu.headmaster.bean.ComplaintBean;
import com.yibu.headmaster.global.HeadmasterApplication;
import com.yibu.headmaster.utils.JsonUtil;
import com.yibu.headmaster.utils.LogUtil;
import com.yibu.headmaster.utils.ToastUtil;
/**
 * 合格率
 * @author pengdonghua
 *
 */
public class PassPercentageFragament extends BasePager{
	
	private ExpandableListView lv;
	
	PercentageAdapter adapter;
	
	private String params1,params2;
	
	public static PassPercentageFragament getInstance(Context context,String param1,String param2){
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
		// TODO Auto-generated constructor stub
	}

	@Override
	public void process(String data) {
		
//		List<ComplaintBean> comList = (List<ComplaintBean>) JsonUtil
//				.parseJsonToList(data,
//						new TypeToken<List<ComplaintBean>>() {
//						}.getType());
//		adapter.setData();
	}
	
	@Override
	public View initView() {
		View view = View.inflate(mContext, R.layout.frag_pass_percentage, null);
		lv = (ExpandableListView) view.findViewById(R.id.frag_pass_percentage_lv);
		adapter = new PercentageAdapter(getActivity());
		lv.setAdapter(adapter);

		return view;
	}

	@Override
	public void initData() {
		loadNetworkData();
	}

	private void loadNetworkData() {
		ApiHttpClient.get("statistics/complaintdetails?userid="
				+ HeadmasterApplication.app.userInfo.userid + "&schoolid="
				+ HeadmasterApplication.app.userInfo.driveschool.schoolid
				+ "&index="  + "&count=10", handler);

	}


}
