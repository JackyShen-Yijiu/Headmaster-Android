package com.yibu.headmaster;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.google.gson.reflect.TypeToken;
import com.jzjf.headmaster.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.yibu.headmaster.adapter.BulletinAdapter;
import com.yibu.headmaster.api.ApiHttpClient;
import com.yibu.headmaster.bean.BulletinBean;
import com.yibu.headmaster.bean.UserBean;
import com.yibu.headmaster.fragment.MailFragment;
import com.yibu.headmaster.global.HeadmasterApplication;
import com.yibu.headmaster.utils.JsonUtil;
import com.yibu.headmaster.utils.LogUtil;
import com.yibu.headmaster.utils.SharedPreferencesUtil;
import com.yibu.headmaster.utils.ToastUtil;
import com.yibu.headmaster.utils.ZProgressHUD;
import com.yibu.headmaster.view.QuickReturnListView;
import com.yibu.headmaster.view.QuickReturnListView.OnRefreshListener;

public class BulletinHistoryActivity extends BaseActivity {


	private BulletinAdapter adapter;
	private ArrayList<BulletinBean> list = new ArrayList<BulletinBean>();

	private boolean moreData = false;

	private int seqindex = 0;
	private int bulletinObject = 1;

	private Context mContext = null;
	private View view;
	private QuickReturnListView mListView;

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

		
		
		if (bulletinBeans.size() == 0) {
			moreData = false;
		} else {
			//保存最后一条公告的sqlindexid
			SharedPreferencesUtil.putInt(mContext, MailFragment.UNREADNOTICECOUNT, bulletinBeans.get(bulletinBeans.size()-1).seqindex);
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
		mListView.loadMoreFinished();
	}

}
