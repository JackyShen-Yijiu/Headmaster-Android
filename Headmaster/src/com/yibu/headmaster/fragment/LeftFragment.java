package com.yibu.headmaster.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.yibu.headmaster.LeftMyCoachActivity;
import com.yibu.headmaster.LeftSettingActivity;
import com.yibu.headmaster.PublishBulletinActivity;
import com.yibu.headmaster.R;
import com.yibu.headmaster.global.HeadmasterApplication;
import com.yibu.headmaster.utils.LogUtil;
import com.yibu.headmaster.utils.ToastUtil;

public class LeftFragment extends BaseFragment implements OnClickListener {

	ImageView ivBack;
	ImageView ivHeadPortrait;
	RelativeLayout rlMyNotice;
	RelativeLayout rlMyCoach;
	RelativeLayout rlSetting;

	@Override
	protected View initView(LayoutInflater inflater, ViewGroup container) {
		View view = inflater.inflate(R.layout.left_content, container, false);
		// View view =View.inflate(getActivity(), R.layout.left_content, null);
		ivHeadPortrait = (ImageView) view
				.findViewById(R.id.iv_left_head_portrait);
		rlMyNotice = (RelativeLayout) view.findViewById(R.id.my_rl_notice);
		rlMyCoach = (RelativeLayout) view.findViewById(R.id.my_rl_coach);
		rlSetting = (RelativeLayout) view.findViewById(R.id.my_rl_setting);

		initOnClick();
		return view;
	}

	private void initOnClick() {

		ivHeadPortrait.setOnClickListener(this);
		rlMyNotice.setOnClickListener(this);
		rlMyCoach.setOnClickListener(this);
		rlSetting.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		// 打开个人设置
		case R.id.iv_left_head_portrait:

			ToastUtil.showToast(HeadmasterApplication.getContext(), "个人设置");
			break;
		// 公告
		case R.id.my_rl_notice:
			LogUtil.print("我的公告");
			Intent intent = new Intent(HeadmasterApplication.getContext(),
					PublishBulletinActivity.class);
			startActivity(intent);
			break;
		// 我的教练
		case R.id.my_rl_coach:
			LogUtil.print("我的教练");
			Intent intent_coach = new Intent(
					HeadmasterApplication.getContext(),
					LeftMyCoachActivity.class);
			startActivity(intent_coach);
			break;

		// 设置
		case R.id.my_rl_setting:
			LogUtil.print("设置");
			Intent intent_setting = new Intent(
					HeadmasterApplication.getContext(),
					LeftSettingActivity.class);
			startActivity(intent_setting);
			break;

		}
	}

	@Override
	protected void initData() {
		super.initData();
	}

}
