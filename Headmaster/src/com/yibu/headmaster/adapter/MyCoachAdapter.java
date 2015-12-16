package com.yibu.headmaster.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yibu.common.Config.UserType;
import com.yibu.headmaster.ChatActivity;
import com.yibu.headmaster.R;
import com.yibu.headmaster.bean.CoachBean;
import com.yibu.headmaster.global.HeadmasterApplication;
import com.yibu.headmaster.utils.LogUtil;
import com.yibu.headmaster.utils.ZProgressHUD;

public class MyCoachAdapter extends BasicAdapter<CoachBean> {

	public MyCoachAdapter(Context context, ArrayList<CoachBean> list) {
		super(context, list);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CoachHolder mHolder = null;
		if (convertView == null) {
			mHolder = new CoachHolder();
			convertView = View.inflate(context, R.layout.left_coach_list_item,
					null);
			mHolder.imageView_head = (ImageView) convertView
					.findViewById(R.id.imageView_head);
			mHolder.textView_name = (TextView) convertView
					.findViewById(R.id.textView_name);
			mHolder.imageView_talk = (FrameLayout) convertView
					.findViewById(R.id.imageView_talk);
			mHolder.imageView_star = (RatingBar) convertView
					.findViewById(R.id.imageView_star);

			// ViewUtils.inject(mHolder, convertView);
			convertView.setTag(mHolder);
		} else {
			mHolder = (CoachHolder) convertView.getTag();
		}
		if (mHolder.imageView_head == null) {
			System.out.println("mHolder.imageView_head");
		}
		CoachBean coachBean = list.get(position);
		System.out.println(coachBean.headportrait.originalpic);
		if (!TextUtils.isEmpty(coachBean.headportrait.originalpic)) {
			Picasso.with(HeadmasterApplication.getContext())
					.load(coachBean.headportrait.originalpic)
					.into(mHolder.imageView_head);

		}
		mHolder.imageView_star.setRating(coachBean.starlevel);
		mHolder.textView_name.setText(coachBean.name);

		mHolder.imageView_talk.setOnClickListener(new TalkOnClickListener(
				position));
		return convertView;
	}

	class CoachHolder {

		ImageView imageView_head;

		TextView textView_name;

		FrameLayout imageView_talk;

		RatingBar imageView_star;

	}

	class TalkOnClickListener implements OnClickListener {

		private int index = -1;

		public TalkOnClickListener(int position) {
			index = position;
		}

		@Override
		public void onClick(View v) {
			LogUtil.print("消息");
			CoachBean coachBean = list.get(index);
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
