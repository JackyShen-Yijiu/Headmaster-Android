package com.jzjf.headmaster.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.umeng.analytics.MobclickAgent;

public abstract class BaseFragment extends Fragment {

	public BaseFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		return initView(inflater, container);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData();
	}

	/**
	 * 子类覆盖，更新自己的控件，可以不覆盖
	 */
	protected void initData() {

	}

	/**
	 * 子类必须实现，返回具体的控件
	 * 
	 * @param inflater
	 * @param container
	 * @return
	 */
	protected abstract View initView(LayoutInflater inflater,
			ViewGroup container);

	@Override
	public void onResume() {
		super.onResume();
		// 统计页面
		MobclickAgent.onPageStart(this.getClass().getSimpleName());
	}

	@Override
	public void onPause() {
		super.onPause();
		// 统计页面
		MobclickAgent.onPageStart(this.getClass().getSimpleName());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
