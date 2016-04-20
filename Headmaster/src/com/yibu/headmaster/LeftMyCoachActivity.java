package com.yibu.headmaster;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.jzjf.headmaster.R;
import com.yibu.headmaster.adapter.MyCoachAdapter;
import com.yibu.headmaster.api.ApiHttpClient;
import com.yibu.headmaster.bean.CoachBean;
import com.yibu.headmaster.global.HeadmasterApplication;
import com.yibu.headmaster.utils.JsonUtil;
import com.yibu.headmaster.utils.LogUtil;
import com.yibu.headmaster.utils.ToastUtil;
import com.yibu.headmaster.utils.ZProgressHUD;

public class LeftMyCoachActivity extends BaseActivity {

	private View view;
	private PullToRefreshListView pullToRefreshListView;
	private ListView mListView;
	private MyCoachAdapter adapter;
	private ArrayList<CoachBean> list = new ArrayList<CoachBean>();
	private int curpage = 1;
	private Context mContext;
	private EditText searchText;
	private boolean isLoadMoreData;
	@Override
	protected void initView() {
		mContext = this;
		view = View.inflate(getBaseContext(), R.layout.left_my_coach, null);
		content.addView(view);
		pullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.listView1);
		searchText = (EditText) view.findViewById(R.id.search_view);
		searchText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
		// if (searchView != null) {
		// try {
		// Class<?> argClass = searchView.getClass();
		// Field ownField = argClass.getDeclaredField("mSearchPlate");
		// ownField.setAccessible(true);
		// View mView = (View) ownField.get(searchView);
		// mView.setBackgroundColor(Color.TRANSPARENT);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		setSonsTitle(getString(R.string.coach_title)+"(0)");
		
	}

	@Override
	protected void initListener() {

		searchText.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				EditText _v = (EditText) v;
				if (!hasFocus) {// 失去焦点
					_v.setHint(_v.getTag().toString());
				} else {
					String hint = _v.getHint().toString();
					_v.setTag(hint);
					_v.setHint("");
				}
			}
		});
		searchText.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					// 先隐藏键盘
					((InputMethodManager) searchText.getContext()
							.getSystemService(Context.INPUT_METHOD_SERVICE))
							.hideSoftInputFromWindow(LeftMyCoachActivity.this
									.getCurrentFocus().getWindowToken(),
									InputMethodManager.HIDE_NOT_ALWAYS);

					// 实现搜索
					LogUtil.print("搜索");
					searchCoach();
					return true;
				}
				return false;
			}

		});
	}

	@Override
	protected void initData() {
		pullToRefreshListView.setMode(Mode.BOTH);
		mListView = pullToRefreshListView.getRefreshableView();
		mListView.setCacheColorHint(Color.TRANSPARENT);
		mListView.setDividerHeight(0);
		mListView.setSelector(R.drawable.listview_selector);
		pullToRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {
						if (pullToRefreshListView.isHeaderShown()) {
							// 下拉刷新
							curpage = 1;
							loadNetworkData();
						} else {
							// 下拉加载
							if (list.size() == 0) {
								ToastUtil.showToast(mContext, "没有更多数据了");
								isLoadMoreData = true;
							} else {
							}
							curpage++;
							loadNetworkData();
						}
					}
				});
		adapter = new MyCoachAdapter(this, list);
		mListView.setAdapter(adapter);

		loadNetworkData();
	}

	private boolean isSearch = false;

	private void loadNetworkData() {

		ApiHttpClient.getWithFullPath("api/v1/getschoolcoach/"
				+ HeadmasterApplication.app.userInfo.driveschool.schoolid + "/"
				+ curpage, handler);
		LogUtil.print("wqeqwasdasd"+HeadmasterApplication.app.userInfo.driveschool.schoolid);
		// ApiHttpClient.get("getschoolcoach/"
		// + HeadmasterApplication.app.userInfo.driveschool.schoolid + "/"
		// + curpage + "?name=" + HeadmasterApplication.app.userInfo.name,
		// handler);
		// http://101.200.204.240:8181/api/v1/getschoolcoach/562dcc3ccb90f25c3bde40da/1?name=%E7%8E%8B
	}

	private void searchCoach() {
		isSearch = true;
		ApiHttpClient
				.getWithFullPath(
						"api/v1/getschoolcoach/"
								+ HeadmasterApplication.app.userInfo.driveschool.schoolid
								+ "/" + curpage + "?name="
								+ searchText.getText().toString(), handler);

		searchText.setText("");
	}

	@Override
	public void processSuccess(String data) {
		// 加载
		final ArrayList<CoachBean> coachBeans = (ArrayList<CoachBean>) JsonUtil
				.parseJsonToList(data, new TypeToken<List<CoachBean>>() {
				}.getType());

		if (coachBeans != null) {
			if (coachBeans.size() == 0) {
				if (isSearch) {
					ZProgressHUD.getInstance(LeftMyCoachActivity.this).show();
					ZProgressHUD.getInstance(LeftMyCoachActivity.this)
							.dismissWithSuccess("没有搜索到您要找的教练");
					isSearch = false;
				}
			}

			if(curpage == 1){
				list.clear();
			}
			list.addAll(coachBeans);
			adapter.notifyDataSetChanged();
			pullToRefreshListView.onRefreshComplete();
			setSonsTitle(getString(R.string.coach_title)+"("+extra+")");
		}
	}

	@Override
	public void processFailure() {
		ToastUtil.showToast(mContext, "网络异常");
	}

}
