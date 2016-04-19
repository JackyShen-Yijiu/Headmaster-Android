package com.yibu.headmaster.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.jzjf.headmaster.R;
import com.yibu.headmaster.bean.BulletinBean;
import com.yibu.headmaster.utils.CommonUtils;
import com.yibu.headmaster.utils.LogUtil;
import com.yibu.headmaster.utils.UTC2LOC;

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

		if(position==0){
			holder.deviderline.setVisibility(View.INVISIBLE);
		}else{
			holder.deviderline.setVisibility(View.VISIBLE);
		}
		if(!TextUtils.isEmpty(bulletinBean.title)){
			holder.title.setText(bulletinBean.title);
		}else{
			holder.title.setText("无标题");
		}
		holder.date.setText(UTC2LOC.instance.getDate(bulletinBean.createtime, "yyyy/MM/dd"));
		holder.content.setText(bulletinBean.content);
		return convertView;
	}

	static class BulletinHolder {
		@ViewInject(R.id.tv_bulletin_item_content)
		TextView content;
		@ViewInject(R.id.tv_bulletin_item_date)
		TextView date;
		@ViewInject(R.id.tv_bulletin_item_title)
		TextView title;
		@ViewInject(R.id.iv_bulletin_item_deviderline)
		ImageView deviderline;

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
