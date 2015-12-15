package com.yibu.headmaster;

import android.view.View;

public class GiveClassActivity extends BaseActivity {

	private View view;
	private int currentTime = 1;

	@Override
	protected void initView() {
		view = View.inflate(getBaseContext(), R.layout.activity_coach_detail,
				null);
		content.addView(view);
		currentTime = getIntent().getIntExtra("title", 1);
		baseTitle.setText(getTitle(currentTime) + "教练详情");
	}

	private String getTitle(int searchtype) {
		String title = null;
		switch (searchtype) {
		case 1:
			title = "今天";
			break;
		case 2:
			title = "昨天";
			break;
		case 3:
			title = "本周";
			break;
		case 4:
			title = "本月";
			break;
		case 5:
			title = "本年";
			break;

		default:
			break;
		}
		return title;

	}

	@Override
	protected void initListener() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

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
