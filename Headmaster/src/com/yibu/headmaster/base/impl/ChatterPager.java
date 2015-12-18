package com.yibu.headmaster.base.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import cn.sft.sqlhelper.DBHelper;

import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMConversation.EMConversationType;
import com.easemob.chat.EMMessage;
import com.yibu.common.Config;
import com.yibu.headmaster.ChatActivity;
import com.yibu.headmaster.R;
import com.yibu.headmaster.base.BasePager;
import com.yibu.headmaster.bean.PushInnerVO;
import com.yibu.headmaster.emchat.ChatAllHistoryAdapter;
import com.yibu.headmaster.global.HeadmasterApplication;
import com.yibu.headmaster.utils.CommonUtils;
import com.yibu.headmaster.utils.LogUtil;
import com.yibu.headmaster.utils.ToastUtil;

//import com.ypy.eventbus.EventBus;

public class ChatterPager extends BasePager implements EMEventListener,
		OnItemClickListener {

	private ListView messageListView;
	private ChatAllHistoryAdapter adapter;
	private List<EMConversation> conversationList = new ArrayList<EMConversation>();

	public ChatterPager(Context context) {
		super(context);
		// refresh();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// EventBus.getDefault().register(this);
	}

	@Override
	public void onResume() {
		LogUtil.print("onResumechatS抽屉vity");
		refresh();
		EMChatManager.getInstance().registerEventListener(
				this,
				new EMNotifierEvent.Event[] {
						EMNotifierEvent.Event.EventNewMessage,
						EMNotifierEvent.Event.EventOfflineMessage,
						EMNotifierEvent.Event.EventDeliveryAck,
						EMNotifierEvent.Event.EventReadAck });
		// register(getClass().getName());
		// TODO 这里注释的代码，需要研究
		super.onResume();
	}

	public void checkChatterPagerHasMessage() {
		if (conversationList.size() == 0) {
			ToastUtil.showToast(mContext, "您现在还没有与您的教练聊天的记录");
		}
	}

	@Override
	public void initData() {
		messageListView.setOnItemClickListener(this);

		messageListView.setCacheColorHint(android.R.color.transparent);
		messageListView.setDividerHeight(0);
		if (loadConversationsWithRecentChat() != null) {
			conversationList.addAll(loadConversationsWithRecentChat());

		}

		adapter = new ChatAllHistoryAdapter(mContext, conversationList);
		// 设置adapter
		messageListView.setAdapter(adapter);
	}

	@Override
	public View initView() {
		View view = View.inflate(mContext, R.layout.activity_message, null);

		messageListView = (ListView) view.findViewById(R.id.message_list);

		return view;
	}

	@Override
	public void process(String data) {

	}

	/**
	 * 刷新页面
	 */
	public void refresh() {
		LogUtil.print("refresh");
		conversationList.clear();
		conversationList.addAll(loadConversationsWithRecentChat());
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
	}

	/**
	 * 获取所有会话
	 * 
	 * @param context
	 * @return +
	 */
	private List<EMConversation> loadConversationsWithRecentChat() {
		// 获取所有会话，包括陌生人
		Hashtable<String, EMConversation> conversations = EMChatManager
				.getInstance().getAllConversations();
		// 过滤掉messages size为0的conversation
		/**
		 * 如果在排序过程中有新消息收到，lastMsgTime会发生变化 影响排序过程，Collection.sort会产生异常
		 * 保证Conversation在Sort过程中最后一条消息的时间不变 避免并发问题
		 */
		List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
		synchronized (conversations) {
			for (EMConversation conversation : conversations.values()) {
				if (conversation.getAllMessages().size() != 0) {
					sortList.add(new Pair<Long, EMConversation>(conversation
							.getLastMessage().getMsgTime(), conversation));
				}
			}
		}
		try {
			sortConversationByLastChatTime(sortList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<EMConversation> list = new ArrayList<EMConversation>();
		for (Pair<Long, EMConversation> sortItem : sortList) {
			list.add(sortItem.second);
		}

		PushInnerVO lastPushVO = null;
		// 获取系统消息
		List<PushInnerVO> systemList = DBHelper.getInstance(mContext).query(
				PushInnerVO.class, 1);
		if (systemList != null && systemList.size() > 0) {
			lastPushVO = systemList.get(0);
		}
		if (lastPushVO != null) {
			EMConversation systemConvesation = new EMConversation(
					Config.SYSTEM_PUSH);
			conversationList.add(0, systemConvesation);
		}

		return list;
	}

	/**
	 * 根据最后一条消息的时间排序
	 * 
	 * @param usernames
	 */
	private void sortConversationByLastChatTime(
			List<Pair<Long, EMConversation>> conversationList) {
		Collections.sort(conversationList,
				new Comparator<Pair<Long, EMConversation>>() {
					@Override
					public int compare(final Pair<Long, EMConversation> con1,
							final Pair<Long, EMConversation> con2) {

						if (con1.first == con2.first) {
							return 0;

						} else if (con2.first > con1.first) {
							return 1;
						} else {
							return -1;
						}
					}
				});
	}

	@Override
	public void onEvent(EMNotifierEvent event) {

		switch (event.getEvent()) {
		case EventNewMessage: {
			CommonUtils.runOnUIThread(new Runnable() {
				@Override
				public void run() {
					refresh();
				}
			});

			break;
		}
		case EventDeliveryAck: {
			// 获取到message
			CommonUtils.runOnUIThread(new Runnable() {
				@Override
				public void run() {
					refresh();
				}
			});
			break;
		}
		case EventReadAck: {
			// 获取到message
			CommonUtils.runOnUIThread(new Runnable() {
				@Override
				public void run() {
					refresh();
				}
			});
			break;
		}
		case EventOfflineMessage: {
			refresh();
			break;
		}
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		EMConversation conversation = adapter.getItem(position);
		String username = conversation.getUserName();
		if (username.equals(Config.SYSTEM_PUSH)) {
			// Intent intent = new Intent(this, SystemPushActivity.class);
			// startActivity(intent);
			return;
		}
		if (username.equals(HeadmasterApplication.app.userInfo.userid))
			ToastUtil.showToast(mContext,
					getString(R.string.Cant_chat_with_yourself));
		else {
			// 进入聊天页面
			Intent intent = new Intent(mContext, ChatActivity.class);
			if (conversation.isGroup()) {
				if (conversation.getType() == EMConversationType.ChatRoom) {
					// it is group chat
					intent.putExtra("chatType", ChatActivity.CHATTYPE_CHATROOM);
					intent.putExtra("groupId", username);
				} else {
					// it is group chat
					intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
					intent.putExtra("groupId", username);
				}
			} else {
				// it is single chat
				EMMessage lastMessage = conversation.getLastMessage();
				if (lastMessage.direct == EMMessage.Direct.RECEIVE) {
					String name = lastMessage.getStringAttribute(
							Config.CHAT_NICK_NAME, "陌生人");
					intent.putExtra("chatName", name);
					intent.putExtra("chatUrl", lastMessage.getStringAttribute(
							Config.CHAT_HEAD_RUL, ""));
					intent.putExtra("chatId", username);
				} else {
					String name = lastMessage.getStringAttribute(
							Config.CHAT_NICK_NAME_NOANSWER, "陌生人");
					intent.putExtra("chatName", name);
					intent.putExtra("chatUrl", lastMessage.getStringAttribute(
							Config.CHAT_HEAD_RUL_NOANSWER, ""));
					intent.putExtra("chatId", username);
					intent.putExtra("userIdNoAnswer",
							lastMessage.getStringAttribute(
									Config.CHAT_USERID_NOANSWER, ""));
					intent.putExtra("userTypeNoAnswer", lastMessage
							.getStringAttribute(Config.CHAT_USERTYPE_NOANSWER,
									""));
				}
			}
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(intent);
		}
	}

	// public void onEvent(NewMessageReceiveEvent msg) {
	// LogUtil.print("获取聊天消息");
	// CommonUtils.runOnUIThread(new Runnable() {
	//
	// @Override
	// public void run() {
	// refresh();
	// }
	// });
	// }

	@Override
	public void onPause() {
		EMChatManager.getInstance().unregisterEventListener(this);
		// EventBus.getDefault().unregister(this);
		super.onPause();
	}
}
