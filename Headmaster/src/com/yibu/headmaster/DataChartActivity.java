package com.yibu.headmaster;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.yibu.headmaster.adapter.MoreDataAdapter;
import com.yibu.headmaster.base.impl.MoreDataPager;
import com.yibu.headmaster.lib.PagerSliding.PagerSlidingTab;

public class DataChartActivity extends BaseActivity {

	private View view;
	private List<MoreDataPager> tabPagers;
	private PagerSlidingTab slidingTab;
	private ViewPager viewPager;

	@Override
	protected void initView() {
		view = View
				.inflate(getBaseContext(), R.layout.activity_more_data, null);
		content.addView(view);
		slidingTab = (PagerSlidingTab) view
				.findViewById(R.id.more_data_sliding_tab);
		viewPager = (ViewPager) view.findViewById(R.id.more_data_view_pager);

	}

	@Override
	protected void initListener() {

	}

	@Override
	protected void initData() {

		// 初始化数据
		tabPagers = new ArrayList<MoreDataPager>();
		String[] title = new String[] { "今天", "昨天", "本周", "本月", "本年" };
		for (int i = 0; i < title.length; i++) {
			tabPagers.add(new MoreDataPager(getApplicationContext()));
		}

		viewPager.setAdapter(new MoreDataAdapter(title, tabPagers));
		slidingTab.setViewPager(viewPager);
	}

	@Override
	public void processSuccess(String data) {

	}

	@Override
	public void processFailure() {

	}

}
