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

	private int currentTime = 1; // 当前时间
	private int commentlevel = 1;

	@Override
	protected void initView() {
		view = View.inflate(getBaseContext(), R.layout.activity_assess_detail,
				null);
		content.addView(view);
		slidingTab = (PagerSlidingTab) view
				.findViewById(R.id.assess_detail_sliding_tab);
		viewPager = (ViewPager) view
				.findViewById(R.id.assess_detail_view_pager);

		currentTime = getIntent().getIntExtra("title", 1);
		commentlevel = getIntent().getIntExtra("commentlevel", 1);
		baseTitle.setText(getTitle(currentTime) + "评价详情");

	}

	private String getTitle(int searchtype) {
		String title = null;
		switch (searchtype) {
		case 1:
			title = "今天";
			break;
		case 2:
			title = "昨天";
			break;
		case 3:
			title = "本周";
			break;
		case 4:
			title = "本月";
			break;
		case 5:
			title = "本年";
			break;

		default:
			break;
		}
		return title;
	}

	@Override
	protected void initListener() {

	}

	@Override
	protected void initData() {

		// 初始化数据
		tabPagers = new ArrayList<AssessDetailPager>();
		titles = new String[] { "好评", "中评", "差评", "投诉" };
		for (int i = 0; i < titles.length; i++) {
			tabPagers.add(new AssessDetailPager(getApplicationContext(), i + 1,
					currentTime));
		}

		viewPager.setAdapter(new AssessDetailAdapter(titles, tabPagers));
		viewPager.setCurrentItem(commentlevel - 1);
		slidingTab.setViewPager(viewPager);

	}

	@Override
	public void processSuccess(String data) {

	}

	@Override
	public void processFailure() {

	}

}
