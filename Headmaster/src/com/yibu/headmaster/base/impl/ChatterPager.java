package com.yibu.headmaster.base.impl;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.jzjf.headmaster.R;
import com.yibu.headmaster.base.BasePagerFragment;
import com.yibu.headmaster.fragment.ChatterFragment;
import com.yibu.headmaster.fragment.MailFragment;

//import com.ypy.eventbus.EventBus;

public class ChatterPager extends BasePagerFragment {

	private FragmentActivity activity;

	public ChatterPager(FragmentActivity context) {
		super(context);
		activity = context;
		// refresh();
	}

	@Override
	public void initData() {
		System.out.println("ssssssssssactivity" + activity);
		//聊天界面
//		FragmentTransaction transaction = activity.getFragmentManager()
//				.beginTransaction();
//		transaction.replace(R.id.fragment, new ChatterFragment(mContext));
//		transaction.addToBackStack(null);
//		transaction.commit();
		//信箱界面
		FragmentTransaction transaction = activity.getSupportFragmentManager()
				.beginTransaction();
		transaction.replace(R.id.fragment, new MailFragment(mContext));
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
