package com.jzjf.headmaster.adapter;

import java.util.ArrayList;

import com.jzjf.headmaster.R;
import com.jzjf.headmaster.bean.BulletinBean;
import com.jzjf.headmaster.utils.UTC2LOC;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class BulletinAdapter extends BasicAdapter<BulletinBean> {

	public BulletinAdapter(Context context, ArrayList<BulletinBean> list) {
		super(context, list);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		BulletinHolder holder = null;
		if (convertView == null) {
			holder = new BulletinHolder();
			convertView = View.inflate(context,
					R.layout.item_listview_publish_bulletin, null);
			ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
		} else {
			holder = (BulletinHolder) convertView.getTag();
		}

		BulletinBean bulletinBean = list.get(position);
		// 设值

		if(!TextUtils.isEmpty(bulletinBean.title)){
			holder.title.setText(bulletinBean.title);
		}else{
			holder.title.setText("无标题");
		}
		holder.date.setText(UTC2LOC.instance.getDate(bulletinBean.createtime, "yyyy/MM/dd"));
		holder.content.setText(bulletinBean.content);
		if(!TextUtils.isEmpty(bulletinBean.name)){
			holder.name.setText("发布者："+bulletinBean.name);
		}else{
			holder.name.setText("发布者：未知");
		}
		
		return convertView;
	}

	static class BulletinHolder {
		@ViewInject(R.id.tv_bulletin_item_content)
		TextView content;
		@ViewInject(R.id.tv_bulletin_item_date)
		TextView date;
		@ViewInject(R.id.tv_bulletin_item_title)
		TextView title;
		@ViewInject(R.id.tv_bulletin_item_name)
		TextView name;
		
	}

}
