package com.yibu.headmaster;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PersonSettingActivity extends BaseActivity {

	private View view;
	private Button exit;
	private TextView name;
	private ImageView headImage;

	@Override
	protected void initView() {

		view = View.inflate(getBaseContext(),
				R.layout.acrivity_persion_setting, null);
		content.addView(view);
		headImage = (ImageView) view.findViewById(R.id.persion_head_iv);
		name = (TextView) view.findViewById(R.id.persion_name_iv);
		exit = (Button) view.findViewById(R.id.persion_exit_but);
	}

	@Override
	protected void initListener() {
		exit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 退出，返回登录界面

				exit();
			}

		});
	}

	public void exit() {
		PersonSettingActivity.this.finish();
		Intent intent = new Intent(PersonSettingActivity.this,
				LoginActivity.class);
		PersonSettingActivity.this.startActivity(intent);
	}

	@Override
	protected void initData() {
		baseTitle.setText("个人信息");

	}

	@Override
	public void processSuccess(String data) {

	}

	@Override
	public void processFailure() {

	}

}
