package com.yibu.headmaster.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jzjf.headmaster.R;
import com.yibu.headmaster.bean.MonthData;
import com.yibu.headmaster.bean.PassBean;
import com.yibu.headmaster.fragment.PassPercentageFragament;
import com.yibu.headmaster.utils.LogUtil;

/**
 * 考试合格率
 * @author pengdonghua
 *
 */
@SuppressLint("CutPasteId") public class PercentageAdapter extends BaseExpandableListAdapter{
	
	/**
	 * 
	 */
	private Context context;
	/**月份*/
	private List<MonthData> comList = new ArrayList<MonthData>();
	
//	private List<PassBean> detailList = new ArrayList<PassBean>();
	
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
			LogUtil.print(childPosition+"getChild----"+map.get(groupPosition+""));
			return map.get(groupPosition+"").get(childPosition);
		}
		return null;
		
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		LogUtil.print(childPosition+"child-->"+groupPosition);
		
		
		View view = View.inflate(context, R.layout.item_percentage_child, null);
		
		TextView tvTitle = (TextView) view.findViewById(R.id.item_percentage_title);
		TextView tvTime = (TextView) view.findViewById(R.id.item_percentage_time);
		TextView tvRate = (TextView) view.findViewById(R.id.item_percentage_rate);
		TextView tvTotal = (TextView) view.findViewById(R.id.item_percentage_total);
		TextView tvLou = (TextView) view.findViewById(R.id.item_percentage_lou);
		TextView tvNotPass = (TextView) view.findViewById(R.id.item_percentage_notpass);
		
		PassBean bean = getChild(groupPosition, childPosition);
		if(null == bean){
			return view;
		}
		tvTitle.setText("考试"+childPosition);
		tvTime.setText(bean.examdate);
		tvRate.setText(bean.passrate+"%");
		tvTotal.setText("报考\n"+bean.studentcount+"人");
		tvLou.setText("缺考\n"+bean.missexamstudent+"人");
		tvNotPass.setText("未通过\n"+bean.nopassstudent+"人");
		
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

}
