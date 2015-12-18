package com.yibu.headmaster.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.yibu.headmaster.R;

public class QuickReturnListView extends ListView {

	@ViewInject(R.id.iv_refresh_image)
	private ImageView iv_refresh_image;

	@ViewInject(R.id.pb_refresh_progress)
	private ProgressBar pb_refresh_progress;

	@ViewInject(R.id.tv_refresh_state)
	private TextView tv_refresh_state;

	private RotateAnimation down;
	private RotateAnimation up;
	private OnRefreshListener mOnRefreshListener;
	private View footer;
	private int footerHeight;

	public QuickReturnListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initAnimation();
		initFooter();
	}

	private void initFooter() {
		footer = View.inflate(getContext(), R.layout.refresh_footer, null);
		footer.measure(0, 0);
		footerHeight = footer.getMeasuredHeight();
		footer.setPadding(0, -footerHeight, 0, 0);
		this.addFooterView(footer);

		this.setOnScrollListener(new MyOnScrollListener());
	}

	private void initAnimation() {
		up = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		up.setDuration(500);
		up.setFillAfter(true);
		down = new RotateAnimation(-180, -360, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		down.setDuration(500);
		down.setFillAfter(true);

	}

	public interface OnRefreshListener {
		void onRefreshing();

		void onLoadingMore();
	}

	public void setOnRefreshListener(OnRefreshListener listener) {
		this.mOnRefreshListener = listener;
	}

	private boolean isLoadingMore = false;// 是否正在加载更多

	public void loadMoreFinished() {
		footer.setPadding(0, -footerHeight, 0, 0);
		isLoadingMore = false;
	}

	class MyOnScrollListener implements OnScrollListener {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if (OnScrollListener.SCROLL_STATE_IDLE == scrollState
					|| OnScrollListener.SCROLL_STATE_FLING == scrollState) {
				if (getLastVisiblePosition() == getAdapter().getCount() - 1
						&& !isLoadingMore) {
					isLoadingMore = true;
					System.out.println("加载更多中");
					footer.setPadding(0, 0, 0, 0);
					setSelection(getAdapter().getCount());
					if (mOnRefreshListener != null) {
						mOnRefreshListener.onLoadingMore();
					}
				}
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {

		}

	}
}
