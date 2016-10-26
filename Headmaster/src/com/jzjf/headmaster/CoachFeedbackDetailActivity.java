package com.jzjf.headmaster;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joooonho.SelectableRoundedImageView;
import com.jzjf.headmaster.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.jzjf.headmaster.api.ApiHttpClient;
import com.jzjf.headmaster.bean.CoachFeedbackBean;
import com.jzjf.headmaster.bean.ComplainVO;
import com.jzjf.headmaster.bean.UserBean;
import com.jzjf.headmaster.fragment.MailFragment;
import com.jzjf.headmaster.global.HeadmasterApplication;
import com.jzjf.headmaster.utils.LogUtil;
import com.jzjf.headmaster.utils.SharedPreferencesUtil;
import com.jzjf.headmaster.utils.ToastUtil;
import com.jzjf.headmaster.utils.UTC2LOC;
import com.jzjf.headmaster.utils.ZProgressHUD;

public class CoachFeedbackDetailActivity extends BaseActivity {

	@ViewInject(R.id.ll_feedback_content)
	private LinearLayout feedbackLayout;
	@ViewInject(R.id.iv_coach_head_portrait)
	private SelectableRoundedImageView coachHead;
	@ViewInject(R.id.tv_feedback_name)
	private TextView feedbackName;
	@ViewInject(R.id.tv_coach_feedback_content)
	private TextView feedbackContent;
	@ViewInject(R.id.tv_coach_feedback_time)
	private TextView feedbackTime;

	@ViewInject(R.id.ll_headmaster_reply_content)
	private LinearLayout replyLayout;
	@ViewInject(R.id.iv_headmaster_head_portrait)
	private SelectableRoundedImageView replyHead;
	@ViewInject(R.id.tv_headmaster_name)
	private TextView replyName;
	@ViewInject(R.id.tv_headmaster_reply_content)
	private TextView replyContent;
	@ViewInject(R.id.tv_headmaster_reply_time)
	private TextView replyTime;

	@ViewInject(R.id.feedback_reply_content)
	private EditText sendReplyContent;
	@ViewInject(R.id.iv_send_feedback)
	private ImageView sendFeedback;
	@ViewInject(R.id.feedback_repay_layout)
	LinearLayout sendReplyLayout;

	private Context mContext = null;
	private View view;
	private CoachFeedbackBean coachFeedbackBean;

	@Override
	protected void initView() {
		view = View.inflate(getBaseContext(), R.layout.coach_feedback_detail,
				null);
		ViewUtils.inject(this, view);
		mContext = this;
		content.addView(view);
		setSonsTitle(getString(R.string.coach_feedback));

	}

	@Override
	protected void initData() {

		coachFeedbackBean = (CoachFeedbackBean) getIntent()
				.getSerializableExtra("coachFeedback");
		// 保存该反馈详情（即已读）
		String unReadString = SharedPreferencesUtil.getString(mContext,
				MailFragment.UNREADFEEBACKID, "");
		if (TextUtils.isEmpty(unReadString)) {
			unReadString = coachFeedbackBean._id;
		} else {
			String[] unReads = unReadString.split(",");
			int flag = 0; // 0--没有该详情 1---存在该详情
			for (int i = 0; i < unReads.length; i++) {
				if (coachFeedbackBean._id.equals(unReads[i])) {
					flag = 1;
					break;
				}
			}
			if(flag == 0){
				unReadString += ","+coachFeedbackBean._id;
			}
		}
		SharedPreferencesUtil.putString(mContext, MailFragment.UNREADFEEBACKID, unReadString);

		coachHead.setScaleType(ScaleType.CENTER_CROP);
		coachHead.setImageResource(R.drawable.head_headmaster_null);
		coachHead.setOval(true);
		if (!TextUtils
				.isEmpty(coachFeedbackBean.coachid.headportrait.originalpic)) {
			Picasso.with(mContext)
					.load(coachFeedbackBean.coachid.headportrait.originalpic)
					.into(coachHead);
		}
		feedbackName.setText(coachFeedbackBean.coachid.name);
		feedbackContent.setText(coachFeedbackBean.content);
		feedbackTime.setText(UTC2LOC.instance.getDate(
				coachFeedbackBean.createtime, "yyyy/MM/dd  HH:mm"));

		if (coachFeedbackBean.replyflag == 0) {
			// 未回复
			replyLayout.setVisibility(View.GONE);
		} else {

			sendReplyLayout.setVisibility(View.GONE);
			replyLayout.setVisibility(View.VISIBLE);
			replyHead.setScaleType(ScaleType.CENTER_CROP);
			replyHead.setImageResource(R.drawable.head_headmaster_null);
			replyHead.setOval(true);
			if (coachFeedbackBean.replyid != null) {
				if (!TextUtils.isEmpty(coachFeedbackBean.replyid.headportrait)) {
					Picasso.with(mContext)
							.load(coachFeedbackBean.replyid.headportrait)
							.into(replyHead);
				}

				replyName.setText(coachFeedbackBean.replyid.name);
			}
			replyContent.setText(coachFeedbackBean.replycontent);
			replyTime.setText(UTC2LOC.instance.getDate(
					coachFeedbackBean.replytime, "yyyy/MM/dd  HH:mm"));
		}
		sendFeedback.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.iv_send_feedback:
			LogUtil.print("huiffu xheng ");
			sendFeedback();
			break;

		default:
			break;
		}
	}

	private void sendFeedback() {
		String replyString = sendReplyContent.getText().toString();
		if (TextUtils.isEmpty(replyString)) {
			ZProgressHUD.getInstance(mContext).show();
			ZProgressHUD.getInstance(mContext).dismissWithFailure("请输入反馈内容！");
			return;
		}

		RequestParams params = new RequestParams();
		UserBean userInfo = HeadmasterApplication.app.userInfo;
		if (userInfo != null) {
			LogUtil.print(userInfo.userid);
			params.put("userid", userInfo.userid);
			params.put("replycontent", replyString);
			params.put("feedbackid", coachFeedbackBean._id);
			params.put("schoolid", userInfo.driveschool.schoolid);
		}
		ApiHttpClient.post("statistics/replycoachfeedback", params, handler);
	}

	@Override
	public void processSuccess(String data) {

		if (!TextUtils.isEmpty(data)) {
			ZProgressHUD.getInstance(mContext).show();
			ZProgressHUD.getInstance(mContext).dismissWithSuccess("回复成功！");
		}

		// 显示反馈信息
		if (HeadmasterApplication.app.userInfo != null) {
			if (!TextUtils
					.isEmpty(HeadmasterApplication.app.userInfo.headportrait)) {
				Picasso.with(mContext)
						.load(HeadmasterApplication.app.userInfo.headportrait)
						.into(replyHead);
			}

			replyName.setText(HeadmasterApplication.app.userInfo.name);
		}
		replyContent.setText(sendReplyContent.getText().toString());
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd  HH:mm");
		replyTime.setText(format.format(new Date()));

		sendReplyContent.setText("");
		// 隐藏软键盘
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(sendReplyContent.getWindowToken(), 0);
	}

	@Override
	public void processFailure() {

	}

}
