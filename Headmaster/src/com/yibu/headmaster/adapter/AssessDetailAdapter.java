package com.yibu.headmaster.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.yibu.headmaster.base.BasePagerFragment;

public class AssessDetailAdapter extends FragmentStatePagerAdapter {

	public AssessDetailAdapter(FragmentManager fm, String[] title,
			List<? extends BasePagerFragment> tabPagers) {
		super(fm);
		this.tabPagers = tabPagers;
		this.title = title;
	}

	private String[] title;

	private List<? extends BasePagerFragment> tabPagers;

	@Override
	public CharSequence getPageTitle(int position) {
		return title[position];
	}

	@Override
	public int getCount() {
		return tabPagers.size();
	}

	@Override
	public Fragment getItem(int position) {
		return tabPagers.get(position);
	}

//	@Override
//	public boolean isViewFromObject(View view, Object object) {
//		return view == object;
//	}
//
//	@Override
//	public Object instantiateItem(ViewGroup container, int position) {
//
//		AssessDetailPager detailPager = tabPagers.get(position);
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
