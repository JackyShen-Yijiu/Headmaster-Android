package com.yibu.headmaster;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.yibu.headmaster.adapter.AssessDetailAdapter;
import com.yibu.headmaster.base.impl.AssessDetailPager;
import com.yibu.headmaster.lib.PagerSliding.PagerSlidingTab;

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

		baseTitle.setText("本周评价详情");

	}

	@Override
	protected void initListener() {

	}

	@Override
	protected void initData() {

		// 初始化数据
		tabPagers = new ArrayList<AssessDetailPager>();
		titles = new String[] { "好评", "中评", "本周", "差评" };
		for (int i = 0; i < titles.length; i++) {
			tabPagers.add(new AssessDetailPager(getApplicationContext()));
		}

		viewPager.setAdapter(new AssessDetailAdapter(titles, tabPagers));
		slidingTab.setViewPager(viewPager);
	}

	@Override
	public void processSuccess(String data) {

	}

	@Override
	public void processFailure() {

	}

}
