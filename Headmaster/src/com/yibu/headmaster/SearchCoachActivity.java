package com.yibu.headmaster;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.jzjf.headmaster.R;
import com.yibu.headmaster.adapter.MyCoachAdapter;
import com.yibu.headmaster.api.ApiHttpClient;
import com.yibu.headmaster.bean.CoachBean;
import com.yibu.headmaster.global.HeadmasterApplication;
import com.yibu.headmaster.utils.JsonUtil;
import com.yibu.headmaster.utils.LogUtil;
import com.yibu.headmaster.utils.ToastUtil;
import com.yibu.headmaster.utils.ZProgressHUD;

public class SearchCoachActivity extends BaseActivity{
	private EditText searchText;
	private ArrayList<CoachBean> list = new ArrayList<CoachBean>();
	private int curpage = 1;
	private MyCoachAdapter adapter;
	private Context mContext;
	private PullToRefreshListView pullToRefreshListView;
	private View view;
	private TextView search_cancel;
	
	@Override
	protected void initView() {
		setContentView(R.layout.search_coach_main);
		searchText = (EditText)findViewById(R.id.search_view);
		searchText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
		pullToRefreshListView = (PullToRefreshListView)findViewById(R.id.listView1);
		search_cancel = (TextView)findViewById(R.id.search_cancel);
		search_cancel.setOnClickListener(this);
		
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
							.hideSoftInputFromWindow(SearchCoachActivity.this
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
	
	private boolean isSearch = false;
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
	protected void initData() {
		pullToRefreshListView.setMode(Mode.BOTH);
		adapter = new MyCoachAdapter(this, list);
		pullToRefreshListView.setAdapter(adapter);
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
					ZProgressHUD.getInstance(SearchCoachActivity.this).show();
					ZProgressHUD.getInstance(SearchCoachActivity.this)
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
		}
		
	}

	@Override
	public void processFailure() {
		ToastUtil.showToast(mContext, "网络异常");
		
	}
	public void onClick(View view){
		switch(view.getId()){
		case R.id.search_cancel:
			finish();
			break;
			
		}
	}
}
