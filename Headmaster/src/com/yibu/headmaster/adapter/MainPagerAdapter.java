package com.yibu.headmaster.adapter;

import java.util.List;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.yibu.headmaster.base.BasePagerFragment;

public class MainPagerAdapter extends FragmentStatePagerAdapter {

	public MainPagerAdapter(FragmentManager fm,List<BasePagerFragment> pagers) {
		super(fm);
		this.pagers = pagers;
	}

	private List<BasePagerFragment> pagers;
	Context context;

//	public MainPagerAdapter(List<BasePager> pagers) {
//		this.pagers = pagers;
//	}


	@Override
	public int getCount() {
		return pagers.size();
	}
	
	

//	@Override
//	public boolean isViewFromObject(View view, Object object) {
//		return view == object;
//	}
//
//	@Override
//	public Object instantiateItem(ViewGroup container, int position) {
//
//		BasePager basePager = pagers.get(position);
//		container.addView(basePager.rootView);
//		// 更新界面
//		basePager.initData();
//		// if (position == 0) {
//		// basePager.initData();
//		// }
//		return basePager.rootView;
//	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}
	

	@Override
	public Fragment getItem(int position) {
		return pagers.get(position);
	}

}
