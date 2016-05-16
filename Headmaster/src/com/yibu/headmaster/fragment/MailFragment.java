package com.yibu.headmaster.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.joooonho.SelectableRoundedImageView;
import com.jzjf.headmaster.R;
import com.yibu.headmaster.BulletinHistoryActivity;
import com.yibu.headmaster.CoachFeedbackDetailActivity;
import com.yibu.headmaster.adapter.CoachFeedbackAdapter;
import com.yibu.headmaster.api.ApiHttpClient;
import com.yibu.headmaster.base.BasePagerFragment;
import com.yibu.headmaster.bean.CoachFeedbackBean;
import com.yibu.headmaster.global.HeadmasterApplication;
import com.yibu.headmaster.utils.CommonUtils;
import com.yibu.headmaster.utils.JsonUtil;
import com.yibu.headmaster.utils.LogUtil;
import com.yibu.headmaster.utils.SharedPreferencesUtil;
import com.yibu.headmaster.utils.ToastUtil;

/** 信箱 */
public class MailFragment extends BasePagerFragment implements
		OnItemClickListener, OnClickListener {

	public static final String UNREADNOTICECOUNT = "unreadNoticeCount";
	private PullToRefreshListView pullToRefreshListView;
	private ListView list_coach;
	private CoachFeedbackAdapter adapter;
	private int type;

	private int index = 1;
	private boolean hasMoreData = true;
	private ArrayList<CoachFeedbackBean> list = new ArrayList<CoachFeedbackBean>();
	private TextView unreadNoticeNumTv;
	private ImageView blackPageIv;
	private TextView blackPageTv;
	private LinearLayout blackPageLayout;

	public MailFragment(Context context) {
		super(context);
	}

	@Override
	public View initView() {
		LogUtil.print("initView-------");
		View view = View.inflate(mContext, R.layout.fragment_mail, null);
		pullToRefreshListView = (PullToRefreshListView) view
				.findViewById(R.id.lv_mail_coach_feedback);
		pullToRefreshListView.setVisibility(View.VISIBLE);
		pullToRefreshListView.setMode(Mode.PULL_UP_TO_REFRESH);
		list_coach = pullToRefreshListView.getRefreshableView();
		list_coach.setCacheColorHint(Color.TRANSPARENT);
		list_coach.setDividerHeight(0);
		list_coach.setSelector(R.drawable.listview_selector);
		list_coach.setOnItemClickListener(this);
		// 添加头布局
//		 View headerView = View.inflate(mContext, R.layout.mail_header, null);
		view.findViewById(R.id.rl_mail_school_announcement).setOnClickListener(this);
		unreadNoticeNumTv = (TextView) view
				.findViewById(R.id.tv_mail_school_announcement_num);
		SelectableRoundedImageView noticeIv = (SelectableRoundedImageView) view
				.findViewById(R.id.iv_mail_announcement);
		noticeIv.setScaleType(ScaleType.CENTER_CROP);
		noticeIv.setImageResource(R.drawable.announcement);
		noticeIv.setOval(true);
//		 list_coach.addHeaderView(headerView);

		//空白页
		blackPageLayout = (LinearLayout) view.findViewById(R.id.black_page_ll);
		blackPageIv = (ImageView) view.findViewById(R.id.black_page_iv);
		blackPageTv = (TextView) view.findViewById(R.id.black_page_tv);
		blackPageIv.setBackgroundResource(R.drawable.message_null);
		blackPageTv.setText("暂时还没有反馈信息");
		pullToRefreshListView.setVisibility(View.VISIBLE);
		blackPageLayout.setVisibility(View.GONE);
		return view;
	}

	@Override
	public void initData() {
		loadUnreadNotice();
		adapter = new CoachFeedbackAdapter(mContext, list);
		list_coach.setAdapter(adapter);

		// 给PullToRefreshListView设置监听器
		pullToRefreshListView
				.setOnRefreshListener(new OnRefreshListener<ListView>() {

					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {
						if (pullToRefreshListView.isHeaderShown()) {
							index = 1;
							loadNetworkData();

						} else {
							if (!hasMoreData) {
								ToastUtil.showToast(mContext, "没有更多数据了");
							} else {
								index++;
							}
							loadNetworkData();
						}
					}
				});

	}

	// 加载教练反馈数据
	private void loadNetworkData() {
		type = 1;
		ApiHttpClient.get("statistics/getcoachfeedback?userid="
				+ HeadmasterApplication.app.userInfo.userid + "&schoolid="
				+ HeadmasterApplication.app.userInfo.driveschool.schoolid
				+ "&index=" + index + "&count=10", handler);
	}

	// 加载未读公告数量
	private void loadUnreadNotice() {
		type = 2;
		// 最后一个公告的sqlindexid
		int sqlindexid = SharedPreferencesUtil.getInt(mContext,
				UNREADNOTICECOUNT, 0);
		LogUtil.print("sqlindexid===" + sqlindexid);
		ApiHttpClient.get("statistics/getbulletincount?userid="
				+ HeadmasterApplication.app.userInfo.userid + "&schoolid="
				+ HeadmasterApplication.app.userInfo.driveschool.schoolid
				+ "&seqindex=" + sqlindexid, handler);
	}

	@Override
	public void process(String data) {
		if (type == 1) {

			List<CoachFeedbackBean> coachFeedbackList = (List<CoachFeedbackBean>) JsonUtil
					.parseJsonToList(data,
							new TypeToken<List<CoachFeedbackBean>>() {
							}.getType());
			if (index == 1) {
				if (coachFeedbackList.size() == 0) {
					pullToRefreshListView.setVisibility(View.GONE);
					blackPageLayout.setVisibility(View.VISIBLE);
				} else {
					pullToRefreshListView.setVisibility(View.VISIBLE);
					blackPageLayout.setVisibility(View.GONE);
				}
				list.clear();
				list.addAll(coachFeedbackList);
				adapter.notifyDataSetChanged();
			} else {
				
				if (coachFeedbackList.size() == 0) {
					hasMoreData = false;
				} else {
					list.addAll(coachFeedbackList);
					adapter.notifyDataSetChanged();
				}
			}

			LogUtil.print("ddddddd--list-" + list.size());
			pullToRefreshListView.onRefreshComplete();
		} else if (type == 2) {
			// 未读公告
			int unreadNotice = Integer.parseInt(data);
			if (unreadNotice == 0) {
				unreadNoticeNumTv.setVisibility(View.INVISIBLE);
			} else {
				unreadNoticeNumTv.setVisibility(View.VISIBLE);
				unreadNoticeNumTv.setText(unreadNotice + "");
			}
			loadNetworkData();
		}
	}

	@Override
	public void processFailure() {

		pullToRefreshListView.setVisibility(View.GONE);
		blackPageLayout.setVisibility(View.VISIBLE);
		blackPageIv.setBackgroundResource(R.drawable.net_null);
		blackPageTv.setText(CommonUtils.getString(R.string.no_network));
//		ToastUtil.showToast(mContext, "网络异常");
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		LogUtil.print("onItemClick" + position);
		Intent intent = new Intent(mContext, CoachFeedbackDetailActivity.class);
		intent.putExtra("coachFeedback", list.get(position-1 ));
		startActivityForResult(intent, 1);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			// 刷新列表
			index = 1;
			loadNetworkData();
		} else if (requestCode == 2) {
			unreadNoticeNumTv.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_mail_school_announcement:
			// 公告详情
			Intent intents = new Intent(getActivity(),
					BulletinHistoryActivity.class);
			startActivityForResult(intents, 2);

			break;

		default:
			break;
		}
	}

}
