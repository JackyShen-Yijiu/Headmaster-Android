package com.yibu.headmaster;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.joooonho.SelectableRoundedImageView;
import com.jzjf.headmaster.R;
import com.squareup.picasso.Picasso;
import com.yibu.headmaster.bean.ComplainVO;
import com.yibu.headmaster.fragment.MailFragment;
import com.yibu.headmaster.utils.LogUtil;
import com.yibu.headmaster.utils.SharedPreferencesUtil;
import com.yibu.headmaster.utils.UTC2LOC;

public class ComplainDetailActivity extends BaseActivity {

	public static final String UNREADCOMPLAINDETAIL = "unReadComplainDetail";
	private Context mContext = null;
	private View view;
	private SelectableRoundedImageView iv_student, iv_coach;
	private ComplainVO complainVO;
	private TextView tv_student, tv_coach, complain_time, complain_content;
	private ImageView iv_one, iv_two;

	@Override
	protected void initView() {
		view = View.inflate(getBaseContext(), R.layout.complain_detail, null);
		mContext = this;
		content.addView(view);
		setSonsTitle(getString(R.string.complain_detail));

		iv_student = (SelectableRoundedImageView) findViewById(R.id.iv_student);
		iv_coach = (SelectableRoundedImageView) findViewById(R.id.iv_coach);
		tv_student = (TextView) findViewById(R.id.tv_student);
		tv_coach = (TextView) findViewById(R.id.tv_coach);
		complain_time = (TextView) findViewById(R.id.complain_time);
		complain_content = (TextView) findViewById(R.id.complain_content);
		iv_one = (ImageView) findViewById(R.id.iv_one);
		iv_two = (ImageView) findViewById(R.id.iv_two);
	}

	@Override
	protected void initData() {
		// String detailString = getIntent().getStringExtra("complaindrtail");
		complainVO = (ComplainVO) getIntent().getExtras().getSerializable(
				"item");
		// 圆形头像
		iv_student.setScaleType(ScaleType.CENTER_CROP);
		iv_student.setImageResource(R.drawable.head_null);
		iv_student.setOval(true);
		String url = complainVO.studentinfo.headportrait.originalpic;
		if (!TextUtils.isEmpty(url)) {
			Picasso.with(mContext).load(url).into(iv_student);
		}
		iv_coach.setScaleType(ScaleType.CENTER_CROP);
		iv_coach.setImageResource(R.drawable.head_null);
		iv_coach.setOval(true);
		String url1 = complainVO.coachinfo.headportrait.originalpic;
		if (!TextUtils.isEmpty(url1)) {
			Picasso.with(mContext).load(url1).into(iv_coach);
		}
		complain_time.setText(UTC2LOC.instance.getDate(
				complainVO.complaintDateTime, "yyyy/MM/dd HH:ss"));

		if (complainVO.feedbackusertype == 0) {
			tv_student.setText("匿名学员");
		} else {
			tv_student.setText(complainVO.studentinfo.name);
		}
		tv_coach.setText(complainVO.coachinfo.name);
		complain_content.setText(complainVO.complaintcontent);

		String[] urls = complainVO.piclistr;

		LogUtil.print("url...." + urls);
		switch (urls.length) {
		case 1:
			// 第一个显示
			if (TextUtils.isEmpty(urls[0])) {
			} else {
				iv_one.setVisibility(View.VISIBLE);
				Picasso.with(mContext).load(urls[0]).into(iv_one);
				break;
			}
			break;
		case 2:
			// 第二个显示
			if (TextUtils.isEmpty(urls[1])) {
			} else {
				iv_one.setVisibility(View.VISIBLE);
				iv_one.setVisibility(View.VISIBLE);
				Picasso.with(mContext).load(urls[0]).into(iv_one);

				iv_two.setVisibility(View.VISIBLE);
				iv_one.setVisibility(View.VISIBLE);
				Picasso.with(mContext).load(urls[1]).into(iv_two);
				break;
			}

		default:
			break;
		}

		// 保存该投诉详情（即已读）
		String unReadString = SharedPreferencesUtil.getString(mContext,
				UNREADCOMPLAINDETAIL, "");
		if (TextUtils.isEmpty(unReadString)) {
			unReadString = complainVO.complaintid;
		} else {
			String[] unReads = unReadString.split(",");
			int flag = 0; // 0--没有该详情 1---存在该详情
			for (int i = 0; i < unReads.length; i++) {
				if (complainVO.complaintid.equals(unReads[i])) {
					flag = 1;
					break;
				}
			}
			if (flag == 0) {
				unReadString += "," + complainVO.complaintid;
			}
		}
		SharedPreferencesUtil.putString(mContext, UNREADCOMPLAINDETAIL,
				unReadString);
	}

	public void onClick(View view) {
		super.onClick(view);

		switch (view.getId()) {
		case R.id.iv_one:
			showDialog(complainVO.piclistr[0]);
			break;
		case R.id.iv_two:
			showDialog(complainVO.piclistr[1]);
			break;
		}
	}

	@Override
	public void processSuccess(String data) {

	}

	@Override
	public void processFailure() {

	}

	Dialog d;

	private void showDialog(String url) {
		final Dialog dialog = new Dialog(this, R.style.dialog);
		View view = View.inflate(this, R.layout.pop_pic, null);
		ImageView img = (ImageView) view.findViewById(R.id.pop_pic_img);
		Picasso.with(this).load(url).into(img);
		dialog.setContentView(view);
		dialog.setContentView(view, new LayoutParams(
				LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		//
		dialog.show();
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}

	private void show(String url) {
		final PopupWindow pop = new PopupWindow(this);
		pop.setHeight(android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		pop.setWidth(android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		View view = View.inflate(this, R.layout.pop_pic, null);

		ImageView img = (ImageView) view.findViewById(R.id.pop_pic_img);
		Picasso.with(this).load(url).into(img);
		pop.setContentView(view);
		pop.setFocusable(true);
		pop.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				pop.dismiss();
				
			}
		});
	}

}
