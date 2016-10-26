package com.jzjf.headmaster.adapter;

import java.util.ArrayList;

import com.joooonho.SelectableRoundedImageView;
import com.jzjf.headmaster.R;
import com.jzjf.common.Config.UserType;
import com.jzjf.headmaster.ChatActivity;
import com.jzjf.headmaster.bean.AssessBean.Commentlist;
import com.jzjf.headmaster.utils.LogUtil;
import com.jzjf.headmaster.utils.ZProgressHUD;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView.ScaleType;
import android.widget.RatingBar;
import android.widget.TextView;

public class AssessAdapter extends BasicAdapter<Commentlist> {

	public AssessAdapter(Context context, ArrayList<Commentlist> list) {
		super(context, list);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		AssessHolder holder = null;
		if (convertView == null) {
			holder = new AssessHolder();
			convertView = View.inflate(context, R.layout.list_item_assess,
					null);

			holder.commentcontent = (TextView) convertView
					.findViewById(R.id.textView_content);
			holder.studetname = (TextView) convertView
					.findViewById(R.id.textView_student_id);
			holder.coachname = (TextView) convertView
					.findViewById(R.id.textView_coach_id);
//			holder.subjectname = (TextView) convertView
//					.findViewById(R.id.textView_subject);
//			holder.classid = (TextView) convertView
//					.findViewById(R.id.textView_class);
			holder.time = (TextView) convertView
					.findViewById(R.id.textView_time);
			holder.studenthead = (SelectableRoundedImageView) convertView
					.findViewById(R.id.assess_student_image);
//			holder.coachhead = (ImageView) convertView
//					.findViewById(R.id.assess_coach_image);
//			holder.talk = (ImageView) convertView
//					.findViewById(R.id.imageView_talk);
			holder.startLevel = (RatingBar) convertView
					.findViewById(R.id.imageView_star);
//			holder.complaintSwitch = (CheckBox) convertView
//					.findViewById(R.id.checkBox_assess_switch);
//			holder.complaintSwitch.setVisibility(View.GONE);
			holder.startLevel.setVisibility(View.VISIBLE);

			// ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
		} else {
			holder = (AssessHolder) convertView.getTag();
		}
		Commentlist commentlist = list.get(position);

		// 设值
		holder.commentcontent.setText(commentlist.commentcontent);
		holder.studetname.setText(commentlist.studentinfo.name);
		holder.coachname.setText("教练:"+commentlist.coachinfo.name);
//		holder.subjectname.setText(commentlist.subject.name);
//		holder.classid.setText(commentlist.studentinfo.classtype.name);
		holder.startLevel.setRating(commentlist.commentstarlevel);
		
		holder.studenthead.setScaleType(ScaleType.CENTER_CROP);
		holder.studenthead.setImageResource(R.drawable.head_null);
		holder.studenthead.setOval(true);
//		if (!TextUtils
//				.isEmpty(commentlist.studentinfo.headportrait.originalpic)) {
//			Picasso.with(HeadmasterApplication.getContext())
//					.load(commentlist.studentinfo.headportrait.originalpic
//							+ "?imageView2/0/w/39/h/39")
//					.into(holder.studenthead);
//
//		}
//		if (!TextUtils.isEmpty(commentlist.coachinfo.headportrait.originalpic)) {
//			Picasso.with(HeadmasterApplication.getContext())
//					.load(commentlist.coachinfo.headportrait.originalpic
//							+ "?imageView2/0/w/39/h/39").into(holder.coachhead);
//
//		}
//		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//
//		Date date = new Date();
//
//		try {
//			date = format1.parse(commentlist.commenttime);
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}

		holder.time.setText(commentlist.commenttime);

//		holder.coachhead
//				.setOnClickListener(new DeleteOnClickListener(position));
		holder.studenthead.setOnClickListener(new DeleteOnClickListener(
				position));
		// holder.talk.setOnClickListener(new DeleteOnClickListener());
		return convertView;

	}

	class AssessHolder {
		TextView commentcontent;
		TextView studetname;
		TextView coachname;
//		TextView subjectname;
//		TextView classid;
		TextView time;
		SelectableRoundedImageView studenthead;
//		ImageView coachhead;
//		ImageView talk;
		RatingBar startLevel;
//		CheckBox complaintSwitch;
	}

	class DeleteOnClickListener implements OnClickListener {

		private int index = -1;

		public DeleteOnClickListener(int position) {
			index = position;
		}

		@Override
		public void onClick(View v) {

			LogUtil.print("进入聊天");
			Commentlist commentBean = list.get(index);
			String chatId = "";
			String name = "";
			String url = "";

			if (v.getId() == R.id.assess_student_image) {
				chatId = commentBean.studentinfo.userid;
				name = commentBean.studentinfo.name;
				url = commentBean.studentinfo.headportrait.originalpic;
			} 
//			else if (v.getId() == R.id.assess_coach_image) {
//				chatId = commentBean.coachinfo.coachid;
//				name = commentBean.coachinfo.name;
//				url = commentBean.coachinfo.headportrait.originalpic;
//			}
			if (!TextUtils.isEmpty(chatId)) {
				Intent intent = new Intent(context, ChatActivity.class);
				intent.putExtra("chatId", chatId);
				intent.putExtra("chatName", name);
				intent.putExtra("chatUrl", url);
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
