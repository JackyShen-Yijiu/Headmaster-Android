package com.yibu.headmaster.listener;

// 对外暴露接口，让外界实现具体的业务
public interface OnRefreshListener {

	// 加载更多
	void onLoadingMore();
}
