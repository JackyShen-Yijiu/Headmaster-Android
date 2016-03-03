package com.yibu.headmaster;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.jzjf.headmaster.R;
import com.yibu.headmaster.utils.SharedPreferencesUtil;

public class HomeGuideActivity extends Activity {
	private List<ImageView> images;

	public static String IS_HELP_PAGE_OPENED = "is_help_page_opened";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.home_guide);

		init();
	}

	private void init() {
		ViewPager vp_guide_bg = (ViewPager) findViewById(R.id.vp_home_guide_bg);
		initData();
		vp_guide_bg.setAdapter(new HomeGuideAdapter());

		vp_guide_bg.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
		}

		@Override
		public void onPageSelected(int position) {
			// if (position == images.size() - 1) {
			// images.get(images.size() - 1).setOnClickListener(l)
			// }
		}

		@Override
		public void onPageScrollStateChanged(int state) {

		}

	}

	private void initData() {
		int[] imgIds = new int[] { R.drawable.home_guide_1,
				R.drawable.home_guide_2 };
		images = new ArrayList<ImageView>();
		for (int i = 0; i < imgIds.length; i++) {
			ImageView imageView = new ImageView(getApplicationContext());
			imageView.setBackgroundResource(imgIds[i]);
			if (i == (imgIds.length - 1)) {
				imageView.setEnabled(true);
			} else {
				imageView.setEnabled(false);
			}
			images.add(imageView);

		}

		images.get(images.size() - 1).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(HomeGuideActivity.this,
						MainActivity.class));
				SharedPreferencesUtil.putBoolean(getApplicationContext(),
						HomeGuideActivity.IS_HELP_PAGE_OPENED, true);
				HomeGuideActivity.this.finish();
			}
		});
	}

	class HomeGuideAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return images.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(images.get(position));
			return images.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}
}
