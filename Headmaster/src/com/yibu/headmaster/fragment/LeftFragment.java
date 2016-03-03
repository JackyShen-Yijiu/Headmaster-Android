package com.yibu.headmaster.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;
import com.yibu.headmaster.LeftMyCoachActivity;
import com.yibu.headmaster.LeftSettingActivity;
import com.yibu.headmaster.PersonSettingActivity;
import com.yibu.headmaster.PublishBulletinActivity;
import com.joooonho.SelectableRoundedImageView;
import com.jzjf.headmaster.R;
import com.yibu.headmaster.global.HeadmasterApplication;
import com.yibu.headmaster.utils.LogUtil;

public class LeftFragment extends BaseFragment implements OnClickListener {

	ImageView ivBack;
	SelectableRoundedImageView ivHeadPortrait;
	RelativeLayout rlMyNotice;
	RelativeLayout rlMyCoach;
	RelativeLayout rlSetting;

	@Override
	protected View initView(LayoutInflater inflater, ViewGroup container) {
		View view = inflater.inflate(R.layout.left_content, container, false);
		// View view =View.inflate(getActivity(), R.layout.left_content, null);
		ivHeadPortrait = (SelectableRoundedImageView) view
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

			Intent intent_persion = new Intent(
					HeadmasterApplication.getContext(),
					PersonSettingActivity.class);
			startActivity(intent_persion);
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
		ivHeadPortrait.setScaleType(ScaleType.CENTER_CROP);
		ivHeadPortrait.setImageResource(R.drawable.left_title);
		ivHeadPortrait.setOval(true);
		if(!TextUtils.isEmpty(HeadmasterApplication.app.userInfo.headportrait)){
			Picasso.with(getActivity()).load(HeadmasterApplication.app.userInfo.headportrait).into(ivHeadPortrait);
		}
	}

}
