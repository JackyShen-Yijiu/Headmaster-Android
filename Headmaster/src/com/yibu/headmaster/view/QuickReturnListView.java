package com.yibu.headmaster.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yibu.headmaster.R;

public class QuickReturnListView extends ListView {

	private int headerHeight;
	private int downY = -1;
	private View header;

	private static final int PULLDOWN_STATE = 0;// 下拉刷新状态
	private static final int RELEASE_STATE = 1;// 松开刷新状态
	private static final int REFRESHING_STATE = 2;// 正在刷新状态
	private int current_state = PULLDOWN_STATE;// 当前状态

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
		initHeader();
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

	private void initHeader() {
		header = View.inflate(getContext(), R.layout.refresh_header, null);
		ViewUtils.inject(this, header);
		header.measure(0, 0);
		headerHeight = header.getMeasuredHeight();
		header.setPadding(0, -headerHeight, 0, 0);
		this.addHeaderView(header);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			if (current_state == REFRESHING_STATE) {
				break;
			}
			if (getFirstVisiblePosition() != 0) {
				break;
			}
			if (downY == -1) {
				downY = (int) ev.getY();
			}
			int moveY = (int) ev.getY();

			int diffY = moveY - downY;
			if (diffY > 0) {
				int paddingTop = diffY - headerHeight;
				if (paddingTop < 0 && current_state != PULLDOWN_STATE) {
					current_state = PULLDOWN_STATE;
					switchState(current_state);
				} else if (paddingTop > 0 && current_state != RELEASE_STATE) {
					current_state = RELEASE_STATE;
					switchState(current_state);
				}

				header.setPadding(0, paddingTop, 0, 0);
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
			downY = -1;
			if (current_state == PULLDOWN_STATE) {
				header.setPadding(0, -headerHeight, 0, 0);
			} else if (current_state == RELEASE_STATE) {
				current_state = REFRESHING_STATE;
				System.out.println("切换到正在刷新状态");
				header.setPadding(0, 0, 0, 0);
				switchState(current_state);
				if (mOnRefreshListener != null) {
					mOnRefreshListener.onRefreshing();
				}
			}
			break;

		default:
			break;
		}
		return super.onTouchEvent(ev);
	}

	public interface OnRefreshListener {
		void onRefreshing();

		void onLoadingMore();
	}

	public void setOnRefreshListener(OnRefreshListener listener) {
		this.mOnRefreshListener = listener;
	}

	public void refreshFinished(boolean success) {
		tv_refresh_state.setText("下拉刷新");
		iv_refresh_image.setVisibility(View.VISIBLE);
		pb_refresh_progress.setVisibility(View.INVISIBLE);
		current_state = PULLDOWN_STATE;
		header.setPadding(0, -headerHeight, 0, 0);
		if (success) {
		} else {
			Toast.makeText(getContext(), "您的网络出问题了", 0).show();
		}
	}

	private void switchState(int currentState) {
		switch (currentState) {
		case PULLDOWN_STATE:
			tv_refresh_state.setText("下拉可刷新");
			iv_refresh_image.setVisibility(View.VISIBLE);
			pb_refresh_progress.setVisibility(View.INVISIBLE);
			iv_refresh_image.startAnimation(down);
			break;
		case RELEASE_STATE:
			tv_refresh_state.setText("松开以刷新");
			iv_refresh_image.startAnimation(up);
			break;
		case REFRESHING_STATE:
			// 干掉动画
			iv_refresh_image.clearAnimation();
			tv_refresh_state.setText("正在载入...");
			iv_refresh_image.setVisibility(View.INVISIBLE);
			pb_refresh_progress.setVisibility(View.VISIBLE);
			break;

		default:
			break;
		}
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
