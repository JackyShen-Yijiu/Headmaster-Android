package com.yibu.headmaster.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.Header;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.yibu.common.Config.UserType;
import com.yibu.headmaster.ChatActivity;
import com.yibu.headmaster.R;
import com.yibu.headmaster.api.ApiHttpClient;
import com.yibu.headmaster.bean.ComplaintBean;
import com.yibu.headmaster.global.HeadmasterApplication;
import com.yibu.headmaster.utils.LogUtil;
import com.yibu.headmaster.utils.ZProgressHUD;

public class ComplaintAdapter extends BasicAdapter<ComplaintBean> {

	private boolean handleMessageFaile = false;

	public ComplaintAdapter(Context context, ArrayList<ComplaintBean> list) {
		super(context, list);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		AssessHolder holder = null;
		if (convertView == null) {
			holder = new AssessHolder();
			convertView = View.inflate(context, R.layout.assess_listview_item,
					null);

			holder.commentcontent = (TextView) convertView
					.findViewById(R.id.textView_content);
			holder.studetname = (TextView) convertView
					.findViewById(R.id.textView_student_id);
			holder.coachname = (TextView) convertView
					.findViewById(R.id.textView_coach_id);
			holder.subjectname = (TextView) convertView
					.findViewById(R.id.textView_subject);
			holder.classid = (TextView) convertView
					.findViewById(R.id.textView_class);
			holder.time = (TextView) convertView
					.findViewById(R.id.textView_time);
			holder.studenthead = (ImageView) convertView
					.findViewById(R.id.assess_student_image);
			holder.coachhead = (ImageView) convertView
					.findViewById(R.id.assess_coach_image);
			holder.talk = (ImageView) convertView
					.findViewById(R.id.imageView_talk);
			holder.startLevel = (RatingBar) convertView
					.findViewById(R.id.imageView_star);

			holder.complaintSwitch = (CheckBox) convertView
					.findViewById(R.id.checkBox_assess_switch);
			holder.complaintSwitch.setVisibility(View.VISIBLE);
			holder.startLevel.setVisibility(View.GONE);

			// ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
		} else {
			holder = (AssessHolder) convertView.getTag();
		}
		final ComplaintBean complaintBean = list.get(position);

		// 设值
		holder.commentcontent.setText(complaintBean.complaintcontent);
		holder.studetname.setText(complaintBean.studentinfo.name);
		holder.coachname.setText(complaintBean.coachinfo.name);
		holder.subjectname.setText(complaintBean.subject.name);
		holder.classid.setText(complaintBean.studentinfo.classtype.name);
		// holder.startLevel.setRating(commentlist.commentstarlevel);

		// 处理投诉信息
		if (complaintBean.complainthandlestate == 1) {

			holder.complaintSwitch.setChecked(false);
			holder.complaintSwitch.setEnabled(false);
		} else {

			// 处理投诉
			holder.complaintSwitch
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {

							RequestParams params = new RequestParams();
							params.put("userid",
									HeadmasterApplication.app.userInfo.userid);
							params.put("reservationid",
									complaintBean.reservationid);
							params.put("complainthandlemessage",
									complaintBean.complainthandlemessage);

							AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

								@Override
								public void onSuccess(int arg0, Header[] arg1,
										byte[] arg2) {
									ZProgressHUD.getInstance(context).show();
									ZProgressHUD.getInstance(context)
											.dismissWithSuccess("处理成功！");
								}

								@Override
								public void onFailure(int arg0, Header[] arg1,
										byte[] arg2, Throwable arg3) {
									ZProgressHUD.getInstance(context).show();
									ZProgressHUD.getInstance(context)
											.dismissWithSuccess("处理失败！");
									handleMessageFaile = true;
								}
							};

							ApiHttpClient.post("statistics/handlecomplaint",
									params, handler);

						}
					});
			if (handleMessageFaile) {
				holder.complaintSwitch.setEnabled(true);
				holder.complaintSwitch.setChecked(true);
			}
			holder.complaintSwitch.setChecked(false);

		}
		if (!TextUtils
				.isEmpty(complaintBean.studentinfo.headportrait.originalpic)) {
			Picasso.with(HeadmasterApplication.getContext())
					.load(complaintBean.studentinfo.headportrait.originalpic
							+ "?imageView2/0/w/39/h/39")
					.into(holder.studenthead);

		}
		if (!TextUtils
				.isEmpty(complaintBean.coachinfo.headportrait.originalpic)) {
			Picasso.with(HeadmasterApplication.getContext())
					.load(complaintBean.coachinfo.headportrait.originalpic
							+ "?imageView2/0/w/39/h/39").into(holder.coachhead);

		}
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");

		Date date = new Date();

		try {
			date = format1.parse(complaintBean.complaintDateTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		holder.time.setText(format1.format(date));

		holder.studenthead.setOnClickListener(new DeleteOnClickListener(
				position));
		holder.coachhead
				.setOnClickListener(new DeleteOnClickListener(position));
		return convertView;

	}

	class AssessHolder {
		TextView commentcontent;
		TextView studetname;
		TextView coachname;
		TextView subjectname;
		TextView classid;
		TextView time;
		ImageView studenthead;
		ImageView coachhead;
		ImageView talk;
		RatingBar startLevel;
		CheckBox complaintSwitch;
	}

	class DeleteOnClickListener implements OnClickListener {

		private int index = -1;

		public DeleteOnClickListener(int position) {
			index = position;
		}

		@Override
		public void onClick(View v) {

			LogUtil.print("进入聊天");
			ComplaintBean coachBean = list.get(index);
			String chatId = "";
			String name = "";
			String url = "";

			if (v.getId() == R.id.assess_student_image) {
				chatId = coachBean.studentinfo.userid;
				name = coachBean.studentinfo.name;
				url = coachBean.studentinfo.headportrait.originalpic;
			} else if (v.getId() == R.id.assess_coach_image) {
				chatId = coachBean.coachinfo.coachid;
				name = coachBean.coachinfo.name;
				url = coachBean.coachinfo.headportrait.originalpic;
			}
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
