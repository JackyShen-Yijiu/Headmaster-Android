package com.yibu.headmaster.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.picasso.Picasso;
import com.yibu.headmaster.R;
import com.yibu.headmaster.bean.CoachBean;
import com.yibu.headmaster.global.HeadmasterApplication;

public class MyCoachAdapter extends BasicAdapter<CoachBean> {

	private CoachHolder mHolder;

	public MyCoachAdapter(Context context, ArrayList<CoachBean> list) {
		super(context, list);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			mHolder = new CoachHolder();
			convertView = View.inflate(context, R.layout.left_coach_list_item,
					null);

			ViewUtils.inject(mHolder, convertView);
			convertView.setTag(mHolder);
		} else {
			mHolder = (CoachHolder) convertView.getTag();
		}
		CoachBean coachBean = list.get(position);
		Picasso.with(HeadmasterApplication.getContext())
				.load(coachBean.headportrait.originalpic)
				.into(mHolder.imageView_head);
		mHolder.textView_name.setText(coachBean.name);
		return convertView;
	}

	static class CoachHolder {
		@ViewInject(R.id.imageView_head)
		ImageView imageView_head;
		@ViewInject(R.id.textView_name)
		TextView textView_name;
		@ViewInject(R.id.imageView_talk)
		TextView imageView_talk;
		@ViewInject(R.id.imageView_star)
		TextView imageView_star;

	}
}
