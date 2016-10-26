package com.jzjf.headmaster;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.reflect.TypeToken;
import com.jzjf.headmaster.adapter.BulletinAdapter;
import com.jzjf.headmaster.api.ApiHttpClient;
import com.jzjf.headmaster.bean.BulletinBean;
import com.jzjf.headmaster.fragment.MailFragment;
import com.jzjf.headmaster.global.HeadmasterApplication;
import com.jzjf.headmaster.utils.CommonUtils;
import com.jzjf.headmaster.utils.JsonUtil;
import com.jzjf.headmaster.utils.SharedPreferencesUtil;
import com.jzjf.headmaster.utils.ToastUtil;
import com.jzjf.headmaster.view.QuickReturnListView;
import com.jzjf.headmaster.view.QuickReturnListView.OnRefreshListener;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BulletinHistoryActivity extends BaseActivity {

	private BulletinAdapter adapter;
	private ArrayList<BulletinBean> list = new ArrayList<BulletinBean>();

	private boolean moreData = false;

	private int seqindex = 0;
	private int bulletinObject = 1;

	private Context mContext = null;
	private View view;
	private QuickReturnListView mListView;
	private LinearLayout blackPageLayout;
	private ImageView blackPageIv;
	private TextView blackPageTv;

	@Override
	protected void initView() {
		view = View.inflate(getBaseContext(), R.layout.bulletin_history, null);
		content.addView(view);
		mContext = this;

		mListView = (QuickReturnListView) view
				.findViewById(R.id.lv_publish_bulletin_list);

		mListView.setCacheColorHint(android.R.color.tertiary_text_light);
		mListView.setDividerHeight(1);

		setSonsTitle(getString(R.string.history_bulletin));

		baseRight.setVisibility(View.GONE);

		//空白页
		blackPageLayout = (LinearLayout) view.findViewById(R.id.black_page_ll);
		blackPageIv = (ImageView) view.findViewById(R.id.black_page_iv);
		blackPageTv = (TextView) view.findViewById(R.id.black_page_tv);
		blackPageIv.setBackgroundResource(R.drawable.announcement_null);
		blackPageTv.setText("暂时还没有驾校公告");
		mListView.setVisibility(View.VISIBLE);
		blackPageLayout.setVisibility(View.GONE);
	}

	@Override
	protected void initData() {
		adapter = new BulletinAdapter(this, list);
		mListView.setAdapter(adapter);

		loadNetworkData();
		mListView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onLoadingMore() {

				if (moreData) {
					seqindex = list.get(list.size() - 1).seqindex;
					if (seqindex == 0) {
						ToastUtil.showToast(mContext, "没有更多数据了");
					} else {
						loadNetworkData();
					}
				} else {
					mListView.loadMoreFinished();
					ToastUtil.showToast(mContext, "没有更多数据了");
				}
			}

			@Override
			public void onRefreshing() {

			}
		});

	}

	private void loadNetworkData() {

		ApiHttpClient.get("userinfo/getbulletin?seqindex=" + seqindex
				+ "&count=10&userid="
				+ HeadmasterApplication.app.userInfo.userid + "&schoolid="
				+ HeadmasterApplication.app.userInfo.driveschool.schoolid + "",
				handler);
	}

	@Override
	public void processSuccess(String data) {

		// 加载
		final ArrayList<BulletinBean> bulletinBeans = (ArrayList<BulletinBean>) JsonUtil
				.parseJsonToList(data, new TypeToken<List<BulletinBean>>() {
				}.getType());

		if (seqindex == 0) {
			if (bulletinBeans.size() == 0) {
				blackPageLayout.setVisibility(View.VISIBLE);
				mListView.setVisibility(View.GONE);
			} else {
				blackPageLayout.setVisibility(View.GONE);
				mListView.setVisibility(View.VISIBLE);

			}
		}
		if (bulletinBeans.size() == 0) {

			moreData = false;
		} else {
			// 保存第一条公告的sqlindexid
			SharedPreferencesUtil.putInt(mContext,
					MailFragment.UNREADNOTICECOUNT,
					bulletinBeans.get(0).seqindex);
			moreData = true;
		}
		System.out.println(moreData);

		list.addAll(bulletinBeans);
		adapter.notifyDataSetChanged();

		// isLoadMore = false;
		mListView.loadMoreFinished();

	}

	@Override
	public void processFailure() {

		// isLoadMore = false;
		blackPageLayout.setVisibility(View.VISIBLE);
		mListView.setVisibility(View.GONE);
		blackPageIv.setBackgroundResource(R.drawable.net_null);
		blackPageTv.setText(CommonUtils.getString(R.string.no_network));
		mListView.loadMoreFinished();
	}

}
