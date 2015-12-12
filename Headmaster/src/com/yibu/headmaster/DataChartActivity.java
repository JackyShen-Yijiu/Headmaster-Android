package com.yibu.headmaster;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.RelativeLayout;

import com.yibu.headmaster.adapter.MoreDataAdapter;
import com.yibu.headmaster.base.impl.MoreDataPager;
import com.yibu.headmaster.lib.PagerSliding.PagerSlidingTab;

public class DataChartActivity extends BaseActivity {

	private View view;
	private List<MoreDataPager> tabPagers;
	private PagerSlidingTab slidingTab;
	private ViewPager viewPager;
	private String[] titles;
	private RelativeLayout relativeLayout_textView_shouke;
	private RelativeLayout relativeLayout_textView_pingjia;

	@Override
	protected void initView() {
		view = View
				.inflate(getBaseContext(), R.layout.activity_more_data, null);
		content.addView(view);
		slidingTab = (PagerSlidingTab) view
				.findViewById(R.id.more_data_sliding_tab);
		viewPager = (ViewPager) view.findViewById(R.id.more_data_view_pager);

		if (relativeLayout_textView_shouke == null) {
			System.out.println("sdsfdsfsd");
		}

		baseTitle.setText("今天数据");
	}

	@Override
	protected void initListener() {

		slidingTab.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				baseTitle.setText(titles[position] + "数据");
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	@Override
	protected void initData() {

		// 初始化数据
		tabPagers = new ArrayList<MoreDataPager>();
		titles = new String[] { "今天", "昨天", "本周", "本月", "本年" };
		for (int i = 0; i < titles.length; i++) {
			tabPagers.add(new MoreDataPager(getApplicationContext(), i + 1));
		}

		viewPager.setAdapter(new MoreDataAdapter(titles, tabPagers));
		slidingTab.setViewPager(viewPager);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.ib_base_arrow:
			finish();
			break;
		}
	}

	@Override
	public void processSuccess(String data) {

	}

	@Override
	public void processFailure() {

	}

}
