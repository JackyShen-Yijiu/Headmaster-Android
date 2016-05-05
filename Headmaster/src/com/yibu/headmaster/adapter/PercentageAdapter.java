package com.yibu.headmaster.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.jzjf.headmaster.R;
import com.yibu.headmaster.utils.LogUtil;

/**
 * 考试合格率
 * @author pengdonghua
 *
 */
public class PercentageAdapter extends BaseExpandableListAdapter{
	
	/**
	 * 
	 */
	private Context context;
	
	public PercentageAdapter(Context context){
		this.context = context;
	}
	
	public void setData(){
		
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		LogUtil.print("child-->"+convertView);
		View view = View.inflate(context, R.layout.item_percentage_child, null);
		
		TextView tvTitle = (TextView) view.findViewById(R.id.item_percentage_title);
		TextView tvTime = (TextView) view.findViewById(R.id.item_percentage_time);
		TextView tvRate = (TextView) view.findViewById(R.id.item_percentage_rate);
		TextView tvTotal = (TextView) view.findViewById(R.id.item_percentage_total);
		TextView tvLou = (TextView) view.findViewById(R.id.item_percentage_lou);
		TextView tvNotPass = (TextView) view.findViewById(R.id.item_percentage_notpass);
		
		
		tvTotal.setText("报考\n"+1+"人");
		tvLou.setText("缺考\n"+1+"人");
		tvNotPass.setText("报考\n"+1+"人");
		
		return view;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return 5;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return null;
	}

	@Override
	public int getGroupCount() {
		return 2;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		View view = View.inflate(context, R.layout.item_percentage_parent, null);
		TextView tv = (TextView) view.findViewById(R.id.item_percentage_title);
		tv.setText("2016.3.3");
		return view;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

}
