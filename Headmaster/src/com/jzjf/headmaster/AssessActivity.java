package com.jzjf.headmaster;

import java.util.ArrayList;
import java.util.List;

import com.jzjf.headmaster.R;
import com.jzjf.headmaster.adapter.AssessDetailAdapter;
import com.jzjf.headmaster.base.impl.AssessDetailPager;
import com.jzjf.headmaster.lib.PagerSliding.PagerSlidingTab;

import android.support.v4.view.ViewPager;
import android.view.View;

public class AssessActivity extends BaseActivity {

	private PagerSlidingTab slidingTab;
	private ViewPager viewPager;
	private View view;

	private String[] titles;
	private List<AssessDetailPager> tabPagers;

	@Override
	protected void initView() {
		view = View.inflate(getBaseContext(), R.layout.activity_assess_detail,
				null);
		content.addView(view);
		slidingTab = (PagerSlidingTab) view
				.findViewById(R.id.assess_detail_sliding_tab);
		viewPager = (ViewPager) view
				.findViewById(R.id.assess_detail_view_pager);
		
		baseTitle.setText("学员评价");
	}

	@Override
	protected void initData() {
		// 初始化数据
		tabPagers = new ArrayList<AssessDetailPager>();
		titles = new String[] {"上月", "上周", "今日", "本周" ,"本月"};
		int commentlevel = getIntent().getIntExtra("commentlevel", 3);
		for (int i = 0; i < titles.length; i++) {
			tabPagers.add(new AssessDetailPager(getApplicationContext(), i + 1, commentlevel));
		}
		viewPager.setAdapter(new AssessDetailAdapter(getSupportFragmentManager(), titles, tabPagers));
		slidingTab.setViewPager(viewPager);
		viewPager.setCurrentItem(2);
	}

	@Override
	public void processSuccess(String data) {}

	@Override
	public void processFailure() {}

}
