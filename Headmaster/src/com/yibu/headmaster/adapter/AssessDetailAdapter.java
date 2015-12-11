package com.yibu.headmaster.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.yibu.headmaster.base.impl.AssessDetailPager;

public class AssessDetailAdapter extends PagerAdapter {

	private String[] title;

	private List<AssessDetailPager> tabPagers;

	public AssessDetailAdapter(String[] title, List<AssessDetailPager> tabPagers) {
		this.tabPagers = tabPagers;
		this.title = title;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return title[position];
	}

	@Override
	public int getCount() {
		return tabPagers.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {

		AssessDetailPager detailPager = tabPagers.get(position);
		container.addView(detailPager.rootView);
		// 更新详情界面的UI
		detailPager.initData();
		return detailPager.rootView;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

}
