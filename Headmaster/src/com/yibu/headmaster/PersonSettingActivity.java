package com.yibu.headmaster;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.jzjf.headmaster.R;
import com.yibu.headmaster.global.HeadmasterApplication;
import com.yibu.headmaster.utils.ZProgressHUD;

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
		ZProgressHUD.getInstance(this).setMessage("正在退出登录...");
		ZProgressHUD.getInstance(this).show();
		// 退出环信
		EMChatManager.getInstance().logout(null);

		ZProgressHUD.getInstance(PersonSettingActivity.this).dismiss();

		PersonSettingActivity.this.finish();
		Intent intent = new Intent(PersonSettingActivity.this,
				LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PersonSettingActivity.this.startActivity(intent);
	}

	@Override
	protected void initData() {
		baseTitle.setText("个人信息");
		name.setText(HeadmasterApplication.app.userInfo.name);
	}

	@Override
	public void processSuccess(String data) {

	}

	@Override
	public void processFailure() {

	}

}
