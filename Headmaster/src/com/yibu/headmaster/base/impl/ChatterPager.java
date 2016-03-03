package com.yibu.headmaster.base.impl;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.view.View;

import com.jzjf.headmaster.R;
import com.yibu.headmaster.base.BasePager;
import com.yibu.headmaster.fragment.ChatterFragment;

//import com.ypy.eventbus.EventBus;

public class ChatterPager extends BasePager {

	private Activity activity;

	public ChatterPager(Activity context) {
		super(context);
		activity = context;
		// refresh();
	}

	@Override
	public void initData() {
		System.out.println("ssssssssssactivity" + activity);
		FragmentTransaction transaction = activity.getFragmentManager()
				.beginTransaction();
		transaction.replace(R.id.fragment, new ChatterFragment(mContext));
		transaction.addToBackStack(null);
		transaction.commit();
	}

	@Override
	public View initView() {
		View view = View.inflate(mContext, R.layout.chat_fragment, null);

		return view;
	}

	@Override
	public void process(String data) {

	}

}
