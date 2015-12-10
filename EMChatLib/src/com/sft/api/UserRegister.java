package com.sft.api;

import android.content.Context;
import android.text.TextUtils;

import com.easemob.EMError;
import com.easemob.chat.EMChatManager;
import com.easemob.exceptions.EaseMobException;
import com.sft.listener.EMRegisterListener;

public class UserRegister {

	private Context context;

	public UserRegister(Context context) {
		this.context = context;
	}

	public void userRegister(final String userName, final String password) {
		if (TextUtils.isEmpty(userName)) {
			return;
		}
		if (TextUtils.isEmpty(password)) {
			return;
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				int errorCode = 0;
				try {
					EMChatManager.getInstance().createAccountOnServer(userName,
							password);
				} catch (EaseMobException e) {
					errorCode = e.getErrorCode();
					if (errorCode == EMError.NONETWORK_ERROR) {
						// 网络异常
					} else if (errorCode == EMError.USER_ALREADY_EXISTS) {
						// 重复注册
					} else if (errorCode == EMError.UNAUTHORIZED) {
						// 注册失败，无权限！
					} else if (errorCode == EMError.ILLEGAL_USER_NAME) {
						// 用户名不合法
					} else {
						// 注册失败
					}
				}
				if (context instanceof EMRegisterListener) {
					EMRegisterListener registerListener = (EMRegisterListener) context;
					registerListener.registerResult(errorCode);
				}
			}
		}).start();

	}
}
