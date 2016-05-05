package com.yibu.headmaster.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.joooonho.SelectableRoundedImageView;
import com.jzjf.headmaster.R;
import com.squareup.picasso.Picasso;
import com.yibu.headmaster.bean.ComplainVO;
import com.yibu.headmaster.global.HeadmasterApplication;
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
			convertView = View.inflate(context,
					R.layout.complain_list_item, null);
			ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
		} else {
			holder = (ComplainHolder) convertView.getTag();
		}

		ComplainVO complainVO = list.get(position);
		// 设值
		
		if (complainVO.feedbackusertype==0) {
			holder.name.setText("匿名学员");
		}else {
			holder.name.setText(complainVO.studentinfo.name);
		}
		//圆形头像
		holder.headIv.setScaleType(ScaleType.CENTER_CROP);
		holder.headIv.setImageResource(R.drawable.head_null);
		holder.headIv.setOval(true);
		String url = complainVO.studentinfo.headportrait.originalpic;
		if(!TextUtils.isEmpty(url)){
			Picasso.with(context).load(url).into(holder.headIv);
		}
		
		holder.time.setText(UTC2LOC.instance.getDate(complainVO.complaintDateTime, "yyyy/MM/dd"));
		holder.content.setText(complainVO.complaintcontent);
		if (complainVO.feedbacktype==1) {
			holder.stype.setText("投诉教练："+complainVO.coachinfo.name);
		}else {
			holder.stype.setText("投诉驾校："+HeadmasterApplication.app.userInfo.driveschool.name);
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

	}
}
