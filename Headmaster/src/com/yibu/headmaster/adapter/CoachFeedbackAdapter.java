package com.yibu.headmaster.adapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import u.aly.co;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

import com.baidu.mapapi.map.Text;
import com.joooonho.SelectableRoundedImageView;
import com.jzjf.headmaster.R;
import com.squareup.picasso.Picasso;
import com.yibu.headmaster.bean.CoachFeedbackBean;
import com.yibu.headmaster.fragment.MailFragment;
import com.yibu.headmaster.global.HeadmasterApplication;
import com.yibu.headmaster.utils.LogUtil;
import com.yibu.headmaster.utils.SharedPreferencesUtil;
import com.yibu.headmaster.utils.UTC2LOC;

public class CoachFeedbackAdapter extends BasicAdapter<CoachFeedbackBean> {

	public CoachFeedbackAdapter(Context context,
			ArrayList<CoachFeedbackBean> list) {
		super(context, list);

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LogUtil.print(position + "getView---123456>" + convertView);
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.coach_feedback_item,
					null);
			holder.coachHeadIv = (SelectableRoundedImageView) convertView
					.findViewById(R.id.iv_coach_head_portrait);
			holder.coachName = (TextView) convertView
					.findViewById(R.id.tv_coach_name);
			holder.coachReplied = (ImageView) convertView
					.findViewById(R.id.iv_coach_replied);
			holder.feedbackContent = (TextView) convertView
					.findViewById(R.id.tv_coach_feedback_content);
			holder.feedbackTime = (TextView) convertView
					.findViewById(R.id.tv_coach_feedback_time);
			holder.unRead = (ImageView) convertView
					.findViewById(R.id.iv_unread_tag);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.coachHeadIv.setScaleType(ScaleType.CENTER_CROP);
		holder.coachHeadIv.setImageResource(R.drawable.head_headmaster_null);
		holder.coachHeadIv.setOval(true);

		// 赋值
		CoachFeedbackBean feedbackBean = list.get(position);
		if (!TextUtils.isEmpty(feedbackBean.coachid.headportrait.originalpic)) {
			Picasso.with(context)
					.load(feedbackBean.coachid.headportrait.originalpic)
					.into(holder.coachHeadIv);
		}
		LogUtil.print("dddddd---" + feedbackBean.content);
		holder.coachName.setText(feedbackBean.coachid.name);
		if (feedbackBean.replyflag == 0) {
			// 未回复
			holder.coachReplied.setVisibility(View.INVISIBLE);
		} else {
			holder.coachReplied.setVisibility(View.VISIBLE);

		}
		holder.feedbackContent.setText(feedbackBean.content);
		holder.feedbackTime.setText(UTC2LOC.instance.getDate(
				feedbackBean.createtime, "yyyy/MM/dd  HH:mm"));
		LogUtil.print(position + "getView--->" + convertView);
		holder.unRead.setVisibility(View.VISIBLE);
		// 判断是否未读
		String unReadString = SharedPreferencesUtil.getString(context,
				MailFragment.UNREADFEEBACKID, "");
		if (!TextUtils.isEmpty(unReadString)) {
			String[] unReads = unReadString.split(",");
			for (int i = 0; i < unReads.length; i++) {
				if (feedbackBean._id.equals(unReads[i])) {
					holder.unRead.setVisibility(View.INVISIBLE);
					break;
				}
			}
		}
		return convertView;
	}

	class ViewHolder {
		SelectableRoundedImageView coachHeadIv;
		ImageView unRead;
		TextView coachName;
		ImageView coachReplied;
		TextView feedbackContent;
		TextView feedbackTime;
	}

}
