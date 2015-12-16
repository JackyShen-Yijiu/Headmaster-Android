package com.yibu.headmaster.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yibu.common.Config.UserType;
import com.yibu.headmaster.ChatActivity;
import com.yibu.headmaster.R;
import com.yibu.headmaster.bean.CoachGiveClassBean;
import com.yibu.headmaster.global.HeadmasterApplication;
import com.yibu.headmaster.utils.ZProgressHUD;

public class CoachGiveClassAdapter extends BasicAdapter<CoachGiveClassBean> {

	public CoachGiveClassAdapter(Context context,
			ArrayList<CoachGiveClassBean> list) {
		super(context, list);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CoachGiveClassHolder holder;

		if (convertView == null) {
			holder = new CoachGiveClassHolder();
			convertView = View.inflate(context,
					R.layout.coach_detail_listview_item, null);

			holder.coachIcon = (ImageView) convertView
					.findViewById(R.id.coach_detail_image);
			holder.coachName = (TextView) convertView
					.findViewById(R.id.textView_coach_name);
			holder.coachHour = (TextView) convertView
					.findViewById(R.id.textView_hour);
			holder.coachGood = (TextView) convertView
					.findViewById(R.id.textView_good);
			holder.coachGeneral = (TextView) convertView
					.findViewById(R.id.textView_general);
			holder.coachBad = (TextView) convertView
					.findViewById(R.id.textView_bad);
			holder.coachComplain = (TextView) convertView
					.findViewById(R.id.textView_complain);
			holder.coachStar = (RatingBar) convertView
					.findViewById(R.id.imageView__coach_star);
			holder.relativeLayoutTalk = (RelativeLayout) convertView
					.findViewById(R.id.coach_icon_talk_rl);
			System.out.println("-------" + position);
			// 进入聊天界面
			holder.relativeLayoutTalk
					.setOnClickListener(new TalkOnClickListener(position));
			convertView.setTag(holder);
		} else {
			holder = (CoachGiveClassHolder) convertView.getTag();
		}

		CoachGiveClassBean coachGiveClassBean = list.get(position);

		if (!TextUtils.isEmpty(coachGiveClassBean.headportrait.originalpic)) {
			Picasso.with(HeadmasterApplication.getContext())
					.load(coachGiveClassBean.headportrait.originalpic
							+ "?imageView2/0/w/45/h/45").into(holder.coachIcon);

		}
		holder.coachStar.setRating(coachGiveClassBean.starlevel);
		holder.coachName.setText(coachGiveClassBean.name);
		holder.coachHour.setText(coachGiveClassBean.coursecount + "");
		holder.coachGood.setText("好评" + coachGiveClassBean.goodcommentcount);
		holder.coachGeneral.setText("中评"
				+ coachGiveClassBean.generalcommentcount);
		holder.coachBad.setText("差评" + coachGiveClassBean.badcommentcount);
		holder.coachComplain.setText("投诉" + coachGiveClassBean.complaintcount);

		return convertView;
	}

	class CoachGiveClassHolder {
		ImageView coachIcon;
		TextView coachName;
		TextView coachHour;
		TextView coachGood;
		TextView coachGeneral;
		TextView coachBad;
		TextView coachComplain;
		RatingBar coachStar;
		RelativeLayout relativeLayoutTalk;
	}

	class TalkOnClickListener implements OnClickListener {

		private int index = -1;

		public TalkOnClickListener(int position) {
			index = position;
		}

		@Override
		public void onClick(View v) {
			CoachGiveClassBean coachBean = list.get(index);
			String chatId = coachBean.coachid;
			if (!TextUtils.isEmpty(chatId)) {
				Intent intent = new Intent(context, ChatActivity.class);
				intent.putExtra("chatId", chatId);
				intent.putExtra("chatName", coachBean.name);
				intent.putExtra("chatUrl", coachBean.headportrait.originalpic);
				intent.putExtra("userTypeNoAnswer", UserType.COACH.getValue());
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
			} else {
				ZProgressHUD.getInstance(context).show();
				ZProgressHUD.getInstance(context)
						.dismissWithFailure("无法获取对方信息");
			}
		}

	}
}
