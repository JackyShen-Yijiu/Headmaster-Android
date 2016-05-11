package com.yibu.headmaster;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.jzjf.headmaster.R;
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
		
//		tv_assess_number=(TextView)view.findViewById(R.id.tv_assess_number);
		
//		assess_number=getIntent().getIntExtra("assessnumber", 0);
		currentTime = getIntent().getIntExtra("title", 1);
		commentlevel = getIntent().getIntExtra("commentlevel", 1);
		baseTitle.setText("学员评价");

	}


	@Override
	protected void initListener() {

	}

	@Override
	protected void initData() {

		// 初始化数据
		tabPagers = new ArrayList<AssessDetailPager>();
//		titles = new String[] { "好评", "中评", "差评", "投诉" };
		titles = new String[] { "上月", "上周", "今日", "本周" ,"本月"};
		for (int i = 0; i < titles.length; i++) {
			tabPagers.add(new AssessDetailPager(getApplicationContext(), i + 1,
					currentTime));
		}

		viewPager.setAdapter(new AssessDetailAdapter(getSupportFragmentManager(),titles, tabPagers));
		
		slidingTab.setViewPager(viewPager);
		viewPager.setCurrentItem(commentlevel - 1);
		
	}

	@Override
	public void processSuccess(String data) {

	}

	@Override
	public void processFailure() {

	}

}
