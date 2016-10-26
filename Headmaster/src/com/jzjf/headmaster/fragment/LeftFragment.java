package com.jzjf.headmaster.fragment;

import com.joooonho.SelectableRoundedImageView;
import com.jzjf.headmaster.R;
import com.squareup.picasso.Picasso;
import com.jzjf.headmaster.CommonWebActivity;
import com.jzjf.headmaster.LeftMyCoachActivity;
import com.jzjf.headmaster.LeftSettingActivity;
import com.jzjf.headmaster.global.HeadmasterApplication;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

public class LeftFragment extends BaseFragment implements OnClickListener {

	ImageView ivBack;
	SelectableRoundedImageView ivHeadPortrait;
//	private TextView rlMyNotice;
	private TextView rlMyCoach;
	private TextView rlSetting;
//	private TextView rlData;
	private TextView rlComplain;
	private TextView name;
	private TextView school_name;

	@Override
	protected View initView(LayoutInflater inflater, ViewGroup container) {
		View view = inflater.inflate(R.layout.left_content, container, false);
		// View view =View.inflate(getActivity(), R.layout.left_content, null);
		ivHeadPortrait = (SelectableRoundedImageView) view
				.findViewById(R.id.iv_left_head_portrait);
//		rlMyNotice = (TextView) view.findViewById(R.id.my_rl_notice);
		rlMyCoach = (TextView) view.findViewById(R.id.my_rl_coach);
		rlSetting = (TextView) view.findViewById(R.id.my_rl_setting);
//		rlData = (TextView) view.findViewById(R.id.my_rl_data);
		rlComplain = (TextView) view.findViewById(R.id.my_rl_complain);
		name = (TextView)view.findViewById(R.id.coach_name);
		school_name=(TextView)view.findViewById(R.id.school_name);
		name.setText(HeadmasterApplication.app.userInfo.name);
		school_name.setText(HeadmasterApplication.app.userInfo.driveschool.name);
		initOnClick();
		return view;
	}

	private void initOnClick() {
		ivHeadPortrait.setOnClickListener(this);
//		rlMyNotice.setOnClickListener(this);
		rlMyCoach.setOnClickListener(this);
		rlSetting.setOnClickListener(this);
//		rlData.setOnClickListener(this);
		rlComplain.setOnClickListener(this);
		

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		//投诉
		case R.id.my_rl_complain:
//			Intent intent5 = new Intent(getActivity(), AssessActivity.class);
//			intent5.putExtra("commentlevel", 4);
//			startActivity(intent5);
			
			Intent intents = new Intent(getActivity(), CommonWebActivity.class);
			startActivity(intents);
			break;
		//更多数据
//		case R.id.my_rl_data:
//			Intent intent1 = new Intent(getActivity(), DataChartActivity.class);
//			startActivity(intent1);
//			break;
		// 打开个人设置
//		case R.id.iv_left_head_portrait:
//
//			Intent intent_persion = new Intent(
//					HeadmasterApplication.getContext(),
//					PersonSettingActivity.class);
//			startActivity(intent_persion);
//			break;
		// 公告
//		case R.id.my_rl_notice:
//			LogUtil.print("我的公告");
//			Intent intent = new Intent(HeadmasterApplication.getContext(),
//					PublishBulletinActivity.class);
//			startActivity(intent);
//			break;
		// 教练
		case R.id.my_rl_coach:
			Intent intent_coach = new Intent(
					HeadmasterApplication.getContext(),
					LeftMyCoachActivity.class);
			startActivity(intent_coach);
			break;

		// 设置
		case R.id.my_rl_setting:
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
		ivHeadPortrait.setImageResource(R.drawable.head_headmaster_null);
		ivHeadPortrait.setOval(true);
		if(!TextUtils.isEmpty(HeadmasterApplication.app.userInfo.headportrait)){
			Picasso.with(getActivity()).
			load(HeadmasterApplication.app.userInfo.headportrait)
			.placeholder(R.drawable.head_headmaster_null)
			.error(R.drawable.head_headmaster_null)
			.into(ivHeadPortrait);
		}
	}

}
