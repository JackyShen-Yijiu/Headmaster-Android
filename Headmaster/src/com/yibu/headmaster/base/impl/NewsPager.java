package com.yibu.headmaster.base.impl;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.picasso.Picasso;
import com.yibu.headmaster.NewsDetailActivity;
import com.jzjf.headmaster.R;
import com.yibu.headmaster.adapter.NewsInformationAdapter;
import com.yibu.headmaster.api.ApiHttpClient;
import com.yibu.headmaster.base.BasePagerFragment;
import com.yibu.headmaster.bean.NewsBean;
import com.yibu.headmaster.global.HeadmasterApplication;
import com.yibu.headmaster.utils.CommonUtils;
import com.yibu.headmaster.utils.JsonUtil;
import com.yibu.headmaster.utils.LogUtil;
import com.yibu.headmaster.utils.ToastUtil;

public class NewsPager extends BasePagerFragment {

	@ViewInject(R.id.pullToRefreshListView)
	private PullToRefreshListView pullToRefreshListView;
	private ListView listView_show;
	@ViewInject(R.id.progressBar_main)
	private ProgressBar progressBar_main;

	private NewsInformationAdapter adapter = null;
	private int seqindex = 0;
	private ArrayList<NewsBean> totalList;

	private boolean isLoadMoreData = false;

	private ViewPager topImage; // 轮播图
	private TextView topImgInfo; // 轮播图图片描述
	private LinearLayout topPoints; // 轮播图的点
	private int prePointIndex;// 记录前一个红点的位置

	private List<NewsBean> topImageNewsData = new ArrayList<NewsBean>();
	private MyHandler topHandler;

	public NewsPager(Context context) {
		super(context);
	}

	@Override
	public View initView() {
		View view = View.inflate(HeadmasterApplication.getContext(),
				R.layout.news_information, null);
		ViewUtils.inject(this, view);
		return view;
	}

	@Override
	public void initData() {
		pullToRefreshListView.setMode(Mode.BOTH);
		listView_show = pullToRefreshListView.getRefreshableView();
		listView_show.setCacheColorHint(Color.TRANSPARENT);
		listView_show.setDividerHeight(0);
		// listView_show.setSelector(android.R.color.transparent);

		listView_show.setOnItemClickListener(new ListViewOnItemClickListener());
		listView_show.setSelector(R.drawable.listview_selector);
		// listView_show.setDrawSelectorOnTop(true);

		totalList = new ArrayList<NewsBean>();
		adapter = new NewsInformationAdapter(mContext, totalList);
		listView_show.setAdapter(adapter);

		// 添加头部轮播图
		View headView = View.inflate(mContext, R.layout.top_view_pager, null);
		listView_show.addHeaderView(headView);
		topImage = (ViewPager) headView.findViewById(R.id.vp_news_topimage);
		topImgInfo = (TextView) headView.findViewById(R.id.tv_news_imginfo);
		topPoints = (LinearLayout) headView.findViewById(R.id.ll_news_points);

		pullToRefreshListView
				.setOnRefreshListener(new OnRefreshListener<ListView>() {

					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {
						if (pullToRefreshListView.isHeaderShown()) {
							// 下拉刷新
							seqindex = 0;
							loadNetworkData();
						} else {
							// 下拉加载
							seqindex = totalList.get(totalList.size() - 1).seqindex;
							isLoadMoreData = true;
							if (seqindex == 0) {
								ToastUtil.showToast(mContext, "没有更多数据了");

							} else {

							}
							loadNetworkData();
						}
					}
				});
		if (topHandler == null) {
			topHandler = new MyHandler(this);
		}
		loadNetworkData();
	}

	private void loadNetworkData() {

		if (pullToRefreshListView.isFooterShown() && seqindex == 0) {
			pullToRefreshListView.postDelayed(new Runnable() {

				@Override
				public void run() {
					pullToRefreshListView.onRefreshComplete();
				}
			}, 100);
		} else {

			ApiHttpClient.get(
					"info/getnews?seqindex=" + seqindex + "&count=10", handler);
		}
	}

	@Override
	public void process(String data) {

		final ArrayList<NewsBean> newsBean = (ArrayList<NewsBean>) JsonUtil
				.parseJsonToList(data, new TypeToken<List<NewsBean>>() {
				}.getType());

		if (!isLoadMoreData) {
			// 不是加载更多时更新轮播图

			// 加载前三条信息
			topImageNewsData.clear();
			for (int i = 0; i < 3; i++) {
				if (newsBean.size() >= i) {
					topImageNewsData.add(newsBean.get(i));
				}
			}

			topImage.setAdapter(new TopImageAdapter());
			topImage.setOnPageChangeListener(new MyOnPageChangeListener());
			topImgInfo.setText(topImageNewsData.get(0).title);
			prePointIndex = 0;
			// 删除以前的点
			topPoints.removeAllViews();
			// 添加轮播图的点
			for (int i = 0; i < topImageNewsData.size(); i++) {
				ImageView imageView = new ImageView(mContext);
				imageView.setBackgroundResource(R.drawable.point);
				LayoutParams params = new LayoutParams(8, 8);
				imageView.setLayoutParams(params);
				params.leftMargin = 6;
				imageView.setEnabled(false);
				topPoints.addView(imageView);
			}
			setPointSize(0, true);
			if (topHandler == null) {
				topHandler = new MyHandler(this);
			}
			topHandler.removeCallbacksAndMessages(null);// null是删掉所有已发送的消息
		}
		
		LogUtil.print("ssss----"+seqindex);

		if (seqindex == 0) {
			totalList.clear();
		}
		totalList.addAll(newsBean);
		adapter.notifyDataSetChanged();
		if (progressBar_main.getVisibility() == View.VISIBLE) {
			progressBar_main.setVisibility(View.GONE);
		}
		pullToRefreshListView.onRefreshComplete();
		// seqindex = totalList.get(totalList.size() - 1).seqindex;
		isLoadMoreData = false;
	}

	//设置轮播图的圆点
	private void setPointSize(int position,boolean isSelected){
		LayoutParams layoutParams = (LayoutParams) topPoints.getChildAt(position).getLayoutParams();
		if(isSelected){
			layoutParams.height = 12;
			layoutParams.width = 12;
		}else{
			layoutParams.height = 8;
			layoutParams.width = 8;
			
		}
		topPoints.getChildAt(position).requestLayout();
	}
	class ListViewOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if(position <2){
				return;
			}
			NewsBean bean = totalList.get(position-2);

			Intent intent = new Intent(mContext, NewsDetailActivity.class);
			// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("url", bean.contenturl);
			mContext.startActivity(intent);
		}

	}

	class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {

		}

		@Override
		public void onPageSelected(int position) {
			setPointSize(prePointIndex, false);
			topImgInfo.setText(topImageNewsData.get(position).title);
			setPointSize(position, true);
			prePointIndex = position;

		}

		@Override
		public void onPageScrollStateChanged(int state) {

		}

	}

	class TopImageAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return topImageNewsData.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView imageView = new ImageView(mContext);
			container.addView(imageView);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			Picasso.with(mContext).load(topImageNewsData.get(position).logimg)
					.into(imageView);
			imageView.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						topHandler.removeCallbacksAndMessages(null);
						break;
					case MotionEvent.ACTION_UP:
						topHandler.sendMessageDelayed(
								Message.obtain(topHandler), 3000);
						break;
					case MotionEvent.ACTION_CANCEL:
						topHandler.sendMessageDelayed(
								Message.obtain(topHandler), 3000);
						break;

					default:
						break;
					}
					return false;
				}
			});
			final int index = position;
			imageView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					LogUtil.print("onClicksdbdfb    --");
					NewsBean bean = totalList.get(index);
					Intent intent = new Intent(mContext, NewsDetailActivity.class);
					// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra("url", bean.contenturl);
					mContext.startActivity(intent);
				}
			});
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}

	static class MyHandler extends Handler {
		WeakReference<NewsPager> mActivity;
		MyHandler(NewsPager activity) {
			mActivity = new WeakReference<NewsPager>(activity);
		}
		
		@Override
		public void handleMessage(Message msg) {
			NewsPager pager = mActivity.get();
			if (pager.topImage.getWindowVisibility() == View.GONE) {
				pager.topHandler.removeCallbacksAndMessages(null);
				return;
			}
			int nextIndex = (pager.topImage.getCurrentItem() + 1)
					% pager.topImageNewsData.size();
			pager.topImage.setCurrentItem(nextIndex);
			pager.topHandler.sendMessageDelayed(Message.obtain(pager.topHandler), 3000);
			super.handleMessage(msg);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		
		topHandler.removeCallbacksAndMessages(null);
	}

	@Override
	public void onResume() {
		super.onResume();
		topHandler.sendMessageDelayed(Message.obtain(topHandler), 3000);
	}

	@Override
	public void processFailure() {
		
	}
}
