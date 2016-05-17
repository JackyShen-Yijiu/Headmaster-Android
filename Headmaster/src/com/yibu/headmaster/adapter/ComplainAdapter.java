package com.yibu.headmaster.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.joooonho.SelectableRoundedImageView;
import com.jzjf.headmaster.R;
import com.squareup.picasso.Picasso;
import com.yibu.headmaster.ComplainDetailActivity;
import com.yibu.headmaster.bean.ComplainBean;
import com.yibu.headmaster.bean.ComplainVO;
import com.yibu.headmaster.fragment.MailFragment;
import com.yibu.headmaster.global.HeadmasterApplication;
import com.yibu.headmaster.utils.LogUtil;
import com.yibu.headmaster.utils.SharedPreferencesUtil;
import com.yibu.headmaster.utils.UTC2LOC;

public class ComplainAdapter extends BasicAdapter<ComplainVO> {

	public ComplainAdapter(Context context, ArrayList<ComplainVO> list) {
		super(context, list);
	}

	private int index = -1;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		index = position;
		ComplainHolder holder = null;
		if (convertView == null) {
			holder = new ComplainHolder();
			convertView = View.inflate(context, R.layout.complain_list_item,
					null);
			ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
		} else {
			holder = (ComplainHolder) convertView.getTag();
		}

		ComplainVO complainVO = list.get(position);
		// 设值

		if (complainVO.feedbackusertype == 0) {
			holder.name.setText("匿名学员");
		} else {
			holder.name.setText(complainVO.studentinfo.name);
		}
		// 圆形头像
		holder.headIv.setScaleType(ScaleType.CENTER_CROP);
		holder.headIv.setImageResource(R.drawable.head_null);
		holder.headIv.setOval(true);
		String url = complainVO.studentinfo.headportrait.originalpic;
		if (!TextUtils.isEmpty(url)) {
			Picasso.with(context).load(url).into(holder.headIv);
		}

		holder.time.setText(UTC2LOC.instance.getDate(
				complainVO.complaintDateTime, "yyyy/MM/dd HH:ss"));
		holder.content.setText(complainVO.complaintcontent);

		if (complainVO.feedbacktype == 1) {
			holder.stype.setText("投诉教练：" + complainVO.coachinfo.name);
		} else {
			holder.stype.setText("投诉驾校："
					+ HeadmasterApplication.app.userInfo.driveschool.name);
		}
		LogUtil.print("asdzxcasd" + complainVO.feedbacktype
				+ complainVO.feedbackusertype);

		String[] url1 = complainVO.piclistr;

		switch (url1.length) {
		case 0:
			holder.iv_one.setVisibility(View.GONE);
			holder.iv_two.setVisibility(View.GONE);
			break;

		case 1:
			// 第一个显示
			if (TextUtils.isEmpty(url1[0])) {
				LogUtil.print(position + "url22...." + url1[0]);
			} else {
				LogUtil.print(position + "url111...." + url1[0]);
				holder.iv_one.setVisibility(View.VISIBLE);
				Picasso.with(context).load(url1[0]).into(holder.iv_one);
				break;
			}
			break;
		case 2:
			// 第二个显示
			if (TextUtils.isEmpty(url1[1])) {
				LogUtil.print(position + "url33...." + url1[1]);
			} else {
				LogUtil.print(position + "url3...." + url1[1]);
				holder.iv_one.setVisibility(View.VISIBLE);
				holder.iv_one.setVisibility(View.VISIBLE);
				Picasso.with(context).load(url1[0]).into(holder.iv_one);

				holder.iv_two.setVisibility(View.VISIBLE);
				holder.iv_one.setVisibility(View.VISIBLE);
				Picasso.with(context).load(url1[1]).into(holder.iv_two);
				break;
			}

		default:
			break;
		}
		// 判断是否未读
		String unReadString = SharedPreferencesUtil.getString(context,
				ComplainDetailActivity.UNREADCOMPLAINDETAIL, "");
		if (!TextUtils.isEmpty(unReadString)) {
			String[] unReads = unReadString.split(",");
			for (int i = 0; i < unReads.length; i++) {
				if (complainVO.complaintid.equals(unReads[i])) {
					holder.unRead.setVisibility(View.INVISIBLE);
					break;
				}
			}
		}
		return convertView;
	}

	static class ComplainHolder {
		@ViewInject(R.id.complain_content)
		TextView content;
		@ViewInject(R.id.complain_student_name)
		TextView name;
		@ViewInject(R.id.complain_time)
		TextView time;
		@ViewInject(R.id.complain_stype)
		TextView stype;
		@ViewInject(R.id.student_head_iv)
		SelectableRoundedImageView headIv;
		@ViewInject(R.id.iv_one)
		ImageView iv_one;
		@ViewInject(R.id.iv_two)
		ImageView iv_two;
		@ViewInject(R.id.iv_unread_tag)
		ImageView unRead;

	}
}
