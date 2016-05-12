package com.yibu.headmaster;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.jzjf.headmaster.R;
import com.yibu.headmaster.adapter.AssessDetailAdapter;
import com.yibu.headmaster.fragment.PassPercentageFragament;
import com.yibu.headmaster.lib.PagerSliding.PagerSlidingTab;

/**
 * 考试合格率
 * @author pengdonghua
 *
 */
public class PassPercentageAct extends BaseActivity{

	private PagerSlidingTab slidingTab;
	private ViewPager viewPager;
	private View view;

	private String[] titles;
	private List<PassPercentageFragament> tabPagers;

	
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		View view = View.inflate(getBaseContext(), R.layout.activity_percentage,
				null);
		content.addView(view);
		slidingTab = (PagerSlidingTab) view
				.findViewById(R.id.assess_detail_sliding_tab);
		
		viewPager = (ViewPager) view	
				.findViewById(R.id.assess_detail_view_pager);
		baseTitle.setText("考试合格率");
	}

	@Override
	protected void initData() {
		titles = new String[] { "科目一", "科目二", "科目三", "科目四" };
		tabPagers = new ArrayList<PassPercentageFragament>(titles.length);

		for(int i=0;i<titles.length;i++){
			tabPagers.add(PassPercentageFragament.getInstance(this, ""+i, ""));
//			tabPagers.set(i, PassPercentageFragament.getInstance(this, ""+i, ""));
		}
		
		viewPager.setAdapter(new AssessDetailAdapter(getSupportFragmentManager(),titles, tabPagers));
		
		slidingTab.setViewPager(viewPager);
		
		
		viewPager.setCurrentItem(getIntent().getIntExtra("current", 0));
		
	}

	@Override
	public void processSuccess(String data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processFailure() {
		// TODO Auto-generated method stub
		
	}

}
