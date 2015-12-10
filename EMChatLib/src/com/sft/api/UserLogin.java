package com.sft.api;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.sft.library.DemoHXSDKHelper;
import com.sft.listener.EMLoginListener;

import android.content.Context;

public class UserLogin {

	private Context context;

	public UserLogin(Context context) {
		this.context = context;
	}

	public void userLogin(String userName, String userPassword) {
		userLogin(userName, userPassword, null);
	}

	public void userLogin(String userName, String userPassword, String nickName) {
		if (DemoHXSDKHelper.getInstance().isLogined()) {
			// ** 免登陆情况 加载所有本地群和会话
			// 不是必须的，不加sdk也会自动异步去加载(不会重复加载)；
			// 加上的话保证进了主页面会话和群组都已经load完毕
			EMGroupManager.getInstance().loadAllGroups();
			EMChatManager.getInstance().loadAllConversations();
			if (context instanceof EMLoginListener) {
				EMLoginListener listener = (EMLoginListener) context;
				listener.loginResult(true, 0, null);
			}
		} else {
			// 调用sdk登陆方法登陆聊天服务器
			EMChatManager.getInstance().login(userName, userPassword, new LoginListener(nickName));
		}
	}

	private class LoginListener implements EMCallBack {

		private String nickname;

		public LoginListener(String nickName) {
			this.nickname = nickName;
		}

		@Override
		public void onSuccess() {
			try {
				// ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
				EMGroupManager.getInstance().loadAllGroups();
				EMChatManager.getInstance().loadAllConversations();
			} catch (Exception e) {
				e.printStackTrace();
				// 取好友或者群聊失败，不让进入主页面
				return;
			}
			// 更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
			if (nickname != null) {
				EMChatManager.getInstance().updateCurrentUserNick(nickname);
			}

			if (context instanceof EMLoginListener) {
				EMLoginListener listener = (EMLoginListener) context;
				listener.loginResult(true, 0, null);
			}
		}

		@Override
		public void onProgress(int progress, String status) {
		}

		@Override
		public void onError(final int code, final String message) {
			if (context instanceof EMLoginListener) {
				EMLoginListener listener = (EMLoginListener) context;
				listener.loginResult(false, code, message);
			}
		}

	}
}
