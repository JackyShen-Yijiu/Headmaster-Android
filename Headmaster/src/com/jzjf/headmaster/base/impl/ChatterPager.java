package com.jzjf.headmaster.base.impl;

import com.jzjf.headmaster.R;
import com.jzjf.headmaster.base.BasePagerFragment;
import com.jzjf.headmaster.fragment.MessageFragment;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

//import com.ypy.eventbus.EventBus;

public class ChatterPager extends BasePagerFragment {

	private FragmentActivity activity;

	public ChatterPager(FragmentActivity context) {
		super(context);
		activity = context;
	}

	@Override
	public void initData() {
		FragmentTransaction transaction = activity.getSupportFragmentManager()
				.beginTransaction();
		transaction.replace(R.id.fragment, new MessageFragment(mContext));
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

	@Override
	public void processFailure() {
		
	}

}
