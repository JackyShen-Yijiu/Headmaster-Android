package com.jzjf.headmaster.api;

interface ICallBack {

	void process(String data,String type);

	void processFailure(String type);
}
