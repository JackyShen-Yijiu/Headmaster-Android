package com.yibu.headmaster.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.widget.BaseAdapter;

public abstract class BasicAdapter<T> extends BaseAdapter {
	protected Context context;
	protected ArrayList<T> list;

	public BasicAdapter(Context context, ArrayList<T> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

}