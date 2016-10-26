package com.jzjf.headmaster.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jzjf.headmaster.R;
import com.jzjf.headmaster.bean.MonthData;
import com.jzjf.headmaster.bean.PassBean;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 考试合格率
 * @author pengdonghua
 *
 */
@SuppressLint("CutPasteId") public class PercentageAdapter extends BaseExpandableListAdapter{
	
	private Context context;
	
	private List<MonthData> comList = new ArrayList<MonthData>();
	
	private Map<String,List<PassBean>> map = new HashMap<String,List<PassBean>>();
	
	
	public PercentageAdapter(Context context){
		this.context = context;
	}
	
	public void setDataMonth(List<MonthData> c){
		comList = c;
		notifyDataSetChanged();
	}
	
	public void setDataDetail(Map<String,List<PassBean>> map){
		this.map = map;
		notifyDataSetChanged();
	}

	@Override
	public PassBean getChild(int groupPosition, int childPosition) {
	
		if(map!=null && map.containsKey(groupPosition+"") && map.get(groupPosition+"")!=null){//包含该条
			return map.get(groupPosition+"").get(childPosition);
		}
		return null;
		
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		View view = View.inflate(context, R.layout.item_percentage_child, null);
		
		TextView tvTitle = (TextView) view.findViewById(R.id.item_percentage_title);
		TextView tvTime = (TextView) view.findViewById(R.id.item_percentage_time);
		TextView tvRate = (TextView) view.findViewById(R.id.item_percentage_rate);
		TextView tvTotal = (TextView) view.findViewById(R.id.item_percentage_total);
		TextView tvLou = (TextView) view.findViewById(R.id.item_percentage_lou);
		TextView tvNotPass = (TextView) view.findViewById(R.id.item_percentage_notpass);
		
		PassBean bean = getChild(groupPosition, childPosition);
		if(null == bean) {
			return view;
		}
		tvTitle.setText("第" + (this.getChildrenCount(groupPosition) - childPosition) + "次考试");
		tvTime.setText(bean.examdate);
		tvRate.setText(bean.passrate + "%");
		tvTotal.setText("报考\n" + bean.studentcount + "人");
		tvLou.setText("缺考\n" + bean.missexamstudent + "人");
		tvNotPass.setText("未通过\n" + bean.nopassstudent + "人");
		
		return view;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if(map!=null && map.containsKey(groupPosition+"")){
			return map.get(groupPosition+"").size();
		}
		return 0;
	}

	@Override
	public MonthData getGroup(int groupPosition) {
		
		return comList.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return comList.size();
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
		ImageView img = (ImageView) view.findViewById(R.id.item_percentage_arrow);
		
//		tv.setText(comList.get(groupPosition)._id + "（" + comList.get(groupPosition).examcount + "）");
		tv.setText(comList.get(groupPosition)._id);
		if(isExpanded){
			img.setImageResource(R.drawable.arrow_up);
		}else{
			img.setImageResource(R.drawable.arrow_down);
		}
		return view;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

}
