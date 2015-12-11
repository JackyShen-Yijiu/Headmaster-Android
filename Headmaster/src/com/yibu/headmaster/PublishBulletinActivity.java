package com.yibu.headmaster;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.yibu.headmaster.adapter.BulletinAdapter;
import com.yibu.headmaster.api.ApiHttpClient;
import com.yibu.headmaster.bean.BulletinBean;
import com.yibu.headmaster.bean.UserBean;
import com.yibu.headmaster.global.HeadmasterApplication;
import com.yibu.headmaster.listener.OnRefreshListener;
import com.yibu.headmaster.utils.JsonUtil;
import com.yibu.headmaster.utils.LogUtil;
import com.yibu.headmaster.utils.ToastUtil;
import com.yibu.headmaster.view.QuickReturnListView;

public class PublishBulletinActivity extends BaseActivity {

	private RadioGroup radioGroup;
	private EditText pulishContent;

	private BulletinAdapter adapter;
	private ArrayList<BulletinBean> list = new ArrayList<BulletinBean>();

	private boolean moreData = false;

	private int seqindex = 0;
	private int bulletinObject = 1;

	private Context mContext = null;
	private View view;
	private QuickReturnListView mListView;
	private View viewHeader;

	@Override
	protected void initView() {
		view = View.inflate(getBaseContext(), R.layout.publish_bulletin, null);
		content.addView(view);
		mContext = this;

		mListView = (QuickReturnListView) view
				.findViewById(R.id.lv_publish_bulletin_list);

		viewHeader = View.inflate(getBaseContext(), R.layout.bulletin_header,
				null);

		radioGroup = (RadioGroup) viewHeader
				.findViewById(R.id.rg_publish_bulletin_select);
		// rbCoach = (RadioButton) viewHeader
		// .findViewById(R.id.rb_publish_bulletin_coach);
		// rbStudent = (RadioButton) viewHeader
		// .findViewById(R.id.rb_publish_bulletin_student);
		pulishContent = (EditText) viewHeader
				.findViewById(R.id.et_publish_bulletin_content);

		mListView.addHeaderView(viewHeader);

		setSonsTitle(getString(R.string.publish_bulletin));

		baseRight.setVisibility(View.VISIBLE);
		baseRight.setText(getString(R.string.publish));
		baseRight.setOnClickListener(new PublishBulletinOnClickListener());
	}

	@Override
	protected void initListener() {
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.rb_publish_bulletin_student) {
					bulletinObject = 2;
				} else {
					bulletinObject = 1;

				}
			}
		});
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
			LogUtil.print(bulletinBeans.get(0).content);
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

	class PublishBulletinOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.tv_base_right) {
				PublishBulletin();
			}
		}

	}

	private void PublishBulletin() {

		String content = pulishContent.getText().toString();
		if (TextUtils.isEmpty(content)) {
			ToastUtil.showToast(mContext, "请输入公告");

		} else {
			// 发布公告
			RequestParams params = new RequestParams();
			UserBean userInfo = HeadmasterApplication.app.userInfo;
			if (userInfo != null) {
				LogUtil.print(userInfo.userid);
				params.put("userid", userInfo.userid);
				params.put("schoolid", userInfo.driveschool.schoolid);
				params.put("content", content);
				params.put("bulletobject", bulletinObject);
			}

			AsyncHttpResponseHandler handler1 = new AsyncHttpResponseHandler() {

				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] responseBody) {

					String msg2 = null;
					String data = null;

					try {
						JSONObject jsonObject = new JSONObject(new String(
								responseBody));
						msg2 = jsonObject.getString("msg");
						data = jsonObject.getString("data");

					} catch (Exception e) {
						e.printStackTrace();
					}
					// 发布公告
					if (!TextUtils.isEmpty(msg2)) {
						ToastUtil.showToast(mContext, msg2);
					}
					if (!TextUtils.isEmpty(data)) {
						ToastUtil.showToast(mContext, data);

						// 发布成功后，刷新列表
						BulletinBean bean = new BulletinBean();
						bean.content = pulishContent.getText().toString();
						SimpleDateFormat format = new SimpleDateFormat(
								"yyyy-MM-dd'T'HH:mm:ss");
						bean.createtime = format.format(new Date());
						if (radioGroup.getCheckedRadioButtonId() == R.id.rb_publish_bulletin_coach) {
							bean.bulletobject = 1;
						} else {
							bean.bulletobject = 1;

						}
						list.add(0, bean);
						adapter.notifyDataSetChanged();
						pulishContent.setText("");
						// 隐藏软键盘
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(
								pulishContent.getWindowToken(), 0);
					}

				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {

				}
			};

			ApiHttpClient.post("userinfo/publishbulletin", params, handler1);

		}
	}
}
