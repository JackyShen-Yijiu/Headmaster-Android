package com.yibu.headmaster.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.yibu.headmaster.base.BasePager;

public class MainPagerAdapter extends PagerAdapter {

	private List<BasePager> pagers;

	public MainPagerAdapter(List<BasePager> pagers) {
		super();
		this.pagers = pagers;
	}

	@Override
	public int getCount() {
		return pagers.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {

		BasePager basePager = pagers.get(position);
		container.addView(basePager.rootView);
		// 更新界面
		basePager.initData();
		// if (position == 0) {
		// basePager.initData();
		// }
		return basePager.rootView;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

}
