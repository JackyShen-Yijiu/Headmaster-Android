package com.yibu.headmaster;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.RequestParams;
import com.yibu.headmaster.utils.ToastUtil;

public class CallBackActivity extends BaseActivity implements OnClickListener {

	private View view;
	private Button btn;
	private EditText callContent;
	private Context mContext = null;
	private int bulletinObject = 1;

	@Override
	protected void initView() {
		view = View.inflate(getBaseContext(), R.layout.left_setting_callback,
				null);
		mContext = this;
		content.addView(view);
		setSonsTitle(getString(R.string.callback));

		btn = (Button) findViewById(R.id.callback_btn);
		// et = (EditText) findViewById(R.id.callback_et);
		callContent = (EditText) findViewById(R.id.callback_et);
		callContent.setHint(setHint(R.string.opinions_suggestions));
	}

	@Override
	protected void initListener() {
		btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (!onClickSingleView()) {
			return;
		}
		switch (v.getId()) {
		case R.id.ib_base_arrow:
			finish();
			break;
		case R.id.callback_btn:
			callBack();
			// ToastUtil.showToast(HeadmasterApplication.getContext(), "提交成功");
			break;
		}
	}

	private void callBack() {
		String content = callContent.getText().toString();
		if (TextUtils.isEmpty(content)) {
			ToastUtil.showToast(mContext, "请输入信息");

		} else {
			// 发布公告
			RequestParams params = new RequestParams();

			if (params != null) {

				params.put("mobileversion", android.os.Build.MODEL);
				params.put("appversion", android.os.Build.VERSION.RELEASE);

			}
		}
	}

	@Override
	protected void initData() {

	}

	@Override
	public void processSuccess(String data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processFailure() {
		// TODO Auto-generated method stub

	}

}
