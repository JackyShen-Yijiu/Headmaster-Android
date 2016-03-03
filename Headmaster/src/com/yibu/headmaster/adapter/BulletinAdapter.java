package com.yibu.headmaster.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.jzjf.headmaster.R;
import com.yibu.headmaster.bean.BulletinBean;
import com.yibu.headmaster.utils.CommonUtils;
import com.yibu.headmaster.utils.LogUtil;

public class BulletinAdapter extends BasicAdapter<BulletinBean> {

	public BulletinAdapter(Context context, ArrayList<BulletinBean> list) {
		super(context, list);
	}

	private int index = -1;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		index = position;
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

		holder.content.setText(bulletinBean.content);
		if (bulletinBean.bulletobject == 2) {
			holder.bulletinObject
					.setText(CommonUtils.getString(R.string.coach));
		} else {
			holder.bulletinObject.setText(CommonUtils
					.getString(R.string.student));

		}

		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat format2 = new SimpleDateFormat("HH:mm");
		SimpleDateFormat format3 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

		Date date = new Date();
		try {
			date = format3.parse(bulletinBean.createtime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		holder.date.setText(format1.format(date));
		String timeString = format2.format(date);
		if (Integer.parseInt(timeString.substring(0, 2)) <= 12) {
			holder.time.setText("上午" + format2.format(date));
		} else if (Integer.parseInt(timeString.substring(0, 2)) >= 22) {
			holder.time.setText("下午"
					+ (Integer.parseInt(timeString.substring(0, 2)) - 12)
					+ timeString.substring(2));

		} else {
			holder.time.setText("下午0"
					+ (Integer.parseInt(timeString.substring(0, 2)) - 12)
					+ timeString.substring(2));
		}

		holder.delete.setOnClickListener(new DeleteOnClickListener());
		return convertView;
	}

	static class BulletinHolder {
		@ViewInject(R.id.tv_bulletin_item_content)
		TextView content;
		@ViewInject(R.id.tv_bulletin_item_object)
		TextView bulletinObject;
		@ViewInject(R.id.tv_bulletin_item_time)
		TextView time;
		@ViewInject(R.id.tv_bulletin_item_date)
		TextView date;
		@ViewInject(R.id.fl_bulletin_item_delete)
		FrameLayout delete;

	}

	class DeleteOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {

			LogUtil.print("删除成功");
			/*
			 * AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler()
			 * {
			 * 
			 * @Override public void onSuccess(int statusCode, Header[] headers,
			 * byte[] responseBody) {
			 * 
			 * // 刷新列表 if (index > -1) { list.remove(index);
			 * notifyDataSetChanged(); } }
			 * 
			 * @Override public void onFailure(int statusCode, Header[] headers,
			 * byte[] responseBody, Throwable error) {
			 * 
			 * } }; RequestParams params = new RequestParams(); // 删除公告
			 * ApiHttpClient.post("", params, handler);
			 */
		}

	}
}
