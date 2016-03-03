package com.yibu.headmaster.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.yibu.headmaster.base.impl.MoreDataPager;

public class MoreDataAdapter extends FragmentStatePagerAdapter {


	private String[] title = new String[] { "今天", "昨天", "本周", "本月", "本年" };

	private List<MoreDataPager> tabPagers;

	public MoreDataAdapter(FragmentManager fm,String[] title, List<MoreDataPager> tabPagers) {
		super(fm);
		this.tabPagers = tabPagers;
		this.title = title;
	}
	
//	public MoreDataAdapter(String[] title, List<MoreDataPager> tabPagers) {
//		this.tabPagers = tabPagers;
//		this.title = title;
//	}

	@Override
	public CharSequence getPageTitle(int position) {
		return title[position];
	}

	@Override
	public int getCount() {
		return tabPagers.size();
	}

//	@Override
//	public boolean isViewFromObject(View view, Object object) {
//		return view == object;
//	}

	@Override
	public Fragment getItem(int position) {
		return tabPagers.get(position);
	}

//	@Override
//	public Object instantiateItem(ViewGroup container, int position) {
//
//		MoreDataPager detailPager = tabPagers.get(position);
//		container.addView(detailPager.rootView);
//		// 更新详情界面的UI
//		detailPager.initData();
//		return detailPager.rootView;
//	}
//
//	@Override
//	public void destroyItem(ViewGroup container, int position, Object object) {
//		container.removeView((View) object);
//	}

}
