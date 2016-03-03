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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.jzjf.headmaster.R;
import com.yibu.headmaster.utils.SharedPreferencesUtil;

public class GuideActivity extends Activity implements OnClickListener {
	private List<ImageView> images;
	private LinearLayout ll_guide_points;
	private ImageView iv_guide_redPoint;
	private Button bt_guide_start;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.guide);

		init();
	}

	private void init() {
		ViewPager vp_guide_bg = (ViewPager) findViewById(R.id.vp_guide_bg);
		ll_guide_points = (LinearLayout) findViewById(R.id.ll_guide_points);
		iv_guide_redPoint = (ImageView) findViewById(R.id.iv_guide_front_Point);
		bt_guide_start = (Button) findViewById(R.id.bt_guide_start);
		bt_guide_start.setOnClickListener(this);
		initData();
		vp_guide_bg.setAdapter(new MyAdapter());

		vp_guide_bg.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
			int redPointX = (position) * dp2px(20);
			android.widget.RelativeLayout.LayoutParams layoutParams = (android.widget.RelativeLayout.LayoutParams) iv_guide_redPoint
					.getLayoutParams();
			layoutParams.leftMargin = redPointX;
			iv_guide_redPoint.setLayoutParams(layoutParams);
		}

		@Override
		public void onPageSelected(int position) {
			if (position == images.size() - 1) {
				bt_guide_start.setVisibility(View.VISIBLE);
			} else {
				bt_guide_start.setVisibility(View.INVISIBLE);
			}
		}

		@Override
		public void onPageScrollStateChanged(int state) {

		}

	}

	private void initData() {
		int[] imgIds = new int[] { R.drawable.guide_page_1,
				R.drawable.guide_page_2, R.drawable.guide_page_3,
				R.drawable.guide_page_4 };
		images = new ArrayList<ImageView>();
		for (int i = 0; i < imgIds.length; i++) {
			ImageView imageView = new ImageView(getApplicationContext());
			// imageView.setScaleType(ScaleType.FIT_XY);
			// imageView.setImageResource(imgIds[i]);
			imageView.setBackgroundResource(imgIds[i]);
			images.add(imageView);

			ImageView point = new ImageView(getApplicationContext());
			point.setBackgroundResource(R.drawable.guide_point_normal);
			int dp2px = dp2px(10);
			LayoutParams params = new LayoutParams(dp2px, dp2px);
			if (i != 0) {
				params.leftMargin = dp2px;
			}
			point.setLayoutParams(params);
			ll_guide_points.addView(point);
		}
	}

	public int dp2px(int dp) {
		float density = getResources().getDisplayMetrics().density;
		return (int) (density * dp + 0.5f);
	}

	class MyAdapter extends PagerAdapter {

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

	@Override
	public void onClick(View v) {
		startActivity(new Intent(this, LoginActivity.class));
		SharedPreferencesUtil.putBoolean(getApplicationContext(),
				WelcomeActivity.IS_APP_FIRST_OPEN, false);
		finish();
	}
}
