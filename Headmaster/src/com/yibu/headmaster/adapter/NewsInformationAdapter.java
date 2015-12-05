package com.yibu.headmaster.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.picasso.Picasso;
import com.yibu.headmaster.R;
import com.yibu.headmaster.bean.NewsBean;
import com.yibu.headmaster.global.HeadmasterApplication;

public class NewsInformationAdapter extends BasicAdapter<NewsBean> {

	public NewsInformationAdapter(Context context, ArrayList<NewsBean> list) {
		super(context, list);
		// LogUtil.print(msg);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		NewsHolder mHolder;

		if (convertView == null) {
			mHolder = new NewsHolder();
			convertView = View.inflate(context, R.layout.item_listview_notice,
					null);

			ViewUtils.inject(mHolder, convertView);
			convertView.setTag(mHolder);
		} else {
			mHolder = (NewsHolder) convertView.getTag();
		}
		NewsBean newsBean = list.get(position);
		Picasso.with(HeadmasterApplication.getContext()).load(newsBean.logimg)
				.into(mHolder.imageView_item_cover);
		mHolder.textView_item_content.setText(newsBean.description);

		String time;
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
		Date dateCreate = null;
		try {
			dateCreate = format1.parse(newsBean.createtime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date dateCurr = new Date();
		long diff = 1;
		if (dateCreate != null && dateCreate != null) {
			diff = (dateCurr.getTime() - dateCreate.getTime()) / 1000 / 60; // 分钟
		}
		if (diff < 60) {
			time = diff + "分钟前";
		} else {
			diff = diff / 60;
			if (diff < 60) {
				time = diff + "小时前";
			} else {
				time = format2.format(dateCreate);
			}
		}

		mHolder.textView_item_time.setText(time);

		return convertView;
	}

	static class NewsHolder {
		@ViewInject(R.id.imageView_item_cover)
		ImageView imageView_item_cover;
		@ViewInject(R.id.textView_item_content)
		TextView textView_item_content;
		@ViewInject(R.id.textView_item_time)
		TextView textView_item_time;

	}

	public void reloadListView(ArrayList<NewsBean> data, boolean isClear) {
		if (isClear) {
			list.clear();
		}
		list.addAll(data);
		notifyDataSetChanged();
	}

}
