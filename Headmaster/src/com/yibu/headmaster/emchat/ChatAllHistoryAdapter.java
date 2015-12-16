/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yibu.headmaster.emchat;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sft.sqlhelper.DBHelper;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatRoom;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMConversation.EMConversationType;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.util.DateUtils;
import com.easemob.util.EMLog;
import com.sft.Utils.Constant;
import com.sft.Utils.UserUtils;
import com.yibu.common.Config;
import com.yibu.common.Config.UserType;
import com.yibu.headmaster.R;
import com.yibu.headmaster.bean.PushInnerVO;
import com.yibu.headmaster.utils.ZProgressHUD;

/**
 * 显示所有聊天记录adpater
 * 
 */
public class ChatAllHistoryAdapter extends ArrayAdapter<EMConversation> {

	private static final String TAG = "ChatAllHistoryAdapter";
	private LayoutInflater inflater;
	// private List<EMConversation> conversationList;
	// private List<EMConversation> copyConversationList;
	// private ConversationFilter conversationFilter;
	// private boolean notiyfyByFilter;
	private Context context;

	public ChatAllHistoryAdapter(Context context, List<EMConversation> objects) {
		super(context, R.layout.row_chat_history, objects);
		// this.conversationList = objects;
		// copyConversationList = new ArrayList<EMConversation>();
		// copyConversationList.addAll(objects);
		inflater = LayoutInflater.from(context);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.row_chat_history, parent,
					false);
		}
		ViewHolder holder = (ViewHolder) convertView.getTag();
		if (holder == null) {
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.unreadLabel = (TextView) convertView
					.findViewById(R.id.unread_msg_number);
			holder.message = (TextView) convertView.findViewById(R.id.message);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
			holder.msgState = convertView.findViewById(R.id.msg_state);
			holder.list_item_layout = (RelativeLayout) convertView
					.findViewById(R.id.list_item_layout);
			convertView.setTag(holder);
		}
		// if (position % 2 == 0) {
		// holder.list_item_layout
		// .setBackgroundResource(R.drawable.mm_listitem);
		// } else {
		// holder.list_item_layout
		// .setBackgroundResource(R.drawable.mm_listitem_grey);
		// }

		// 获取与此用户/群组的会话
		EMConversation conversation = getItem(position);
		// 获取用户username或者群组groupid
		String username = conversation.getUserName();

		if (conversation.getType() == EMConversationType.GroupChat) {
			// 群聊消息，显示群聊头像
			holder.avatar.setImageResource(R.drawable.group_icon);
			EMGroup group = EMGroupManager.getInstance().getGroup(username);
			holder.name
					.setText(group != null ? group.getGroupName() : username);
		} else if (conversation.getType() == EMConversationType.ChatRoom) {
			holder.avatar.setImageResource(R.drawable.group_icon);
			EMChatRoom room = EMChatManager.getInstance().getChatRoom(username);
			holder.name.setText(room != null
					&& !TextUtils.isEmpty(room.getName()) ? room.getName()
					: username);
		} else {
			// UserUtils.setUserAvatar(getContext(), username, holder.avatar);
			if (username.equals(Constant.GROUP_USERNAME)) {
				holder.name.setText("群聊");

			} else if (username.equals(Constant.NEW_FRIENDS_USERNAME)) {
				holder.name.setText("申请与通知");
			}

			if (username.equals(Config.SYSTEM_PUSH)) {
				holder.name.setText("系统消息");
			} else {
				holder.name.setText(username);
			}
		}

		if (conversation.getUnreadMsgCount() > 0) {
			// 显示与此用户的消息未读数
			holder.unreadLabel.setText(String.valueOf(conversation
					.getUnreadMsgCount()));
			holder.unreadLabel.setVisibility(View.VISIBLE);

		} else {
			holder.unreadLabel.setVisibility(View.INVISIBLE);
		}

		if (username.equals(Config.SYSTEM_PUSH)) {
			holder.msgState.setVisibility(View.GONE);
			holder.avatar.setBackgroundResource(R.drawable.system_message_icon);

			List<PushInnerVO> list = DBHelper.getInstance(context).query(
					PushInnerVO.class, 1);
			holder.message.setText(list.get(0).getMsg_content());
		} else if (conversation.getMsgCount() != 0) {
			// 把最后一条消息的内容作为item的message内容
			EMMessage lastMessage = conversation.getLastMessage();
			holder.message.setText(getMessageDigest(lastMessage,
					(this.getContext())));

			holder.time.setText(DateUtils.getTimestampString(new Date(
					lastMessage.getMsgTime())));
			if (lastMessage.direct == EMMessage.Direct.SEND
					&& lastMessage.status == EMMessage.Status.FAIL) {
				holder.msgState.setVisibility(View.VISIBLE);
			} else {
				holder.msgState.setVisibility(View.GONE);
			}

			if (lastMessage.direct == EMMessage.Direct.RECEIVE) {
				holder.name.setText(lastMessage.getStringAttribute(
						Config.CHAT_NICK_NAME, "陌生人"));
				UserUtils.setUserAvatar(context, lastMessage
						.getStringAttribute(Config.CHAT_HEAD_RUL, ""),
						holder.avatar);
			} else {
				holder.name.setText(lastMessage.getStringAttribute(
						Config.CHAT_NICK_NAME_NOANSWER, "陌生人"));
				UserUtils.setUserAvatar(context, lastMessage
						.getStringAttribute(Config.CHAT_HEAD_RUL_NOANSWER, ""),
						holder.avatar);
			}
			holder.avatar
					.setOnClickListener(new MyOnclickListener(lastMessage));
		}

		return convertView;
	}

	private class MyOnclickListener implements OnClickListener {

		private EMMessage lastMessage;

		public MyOnclickListener(EMMessage lastMessage) {
			this.lastMessage = lastMessage;
		}

		@Override
		public void onClick(View v) {
			int userType = 0;
			if (lastMessage.direct == EMMessage.Direct.RECEIVE) {
				userType = lastMessage.getIntAttribute(Config.CHAT_USERTYPE, 0);
			} else {
				userType = lastMessage.getIntAttribute(
						Config.CHAT_USERTYPE_NOANSWER, 0);
			}
			if (userType == UserType.USER.getValue()
					|| userType == UserType.COACH.getValue()) {
				String userid = lastMessage.getStringAttribute(
						Config.CHAT_USERID, "");

				if (userType == UserType.USER.getValue()) {
					if (!TextUtils.isEmpty(userid)) {
						// Intent intent = new Intent(context,
						// StudentInfoActivity.class);
						// intent.putExtra("studentId", userid);
						// context.startActivity(intent);
					}
				} else {
					String coachid = lastMessage.getStringAttribute(
							Config.CHAT_USERID_NOANSWER, "");
					if (!TextUtils.isEmpty(coachid)) {
						// Intent intent = new Intent(context,
						// CoachDetailActivity.class);
						// CoachVO coach = new CoachVO();
						// coach.setCoachid(lastMessage.getStringAttribute(
						// Config.CHAT_USERID_NOANSWER, ""));
						// intent.putExtra("coach", coach);
						// context.startActivity(intent);
					}
				}

			} else {
				ZProgressHUD.getInstance(context).show();
				ZProgressHUD.getInstance(context)
						.dismissWithFailure("无法确定对方身份");
			}
		}

	}

	/**
	 * 根据消息内容和消息类型获取消息内容提示
	 * 
	 * @param message
	 * @param context
	 * @return
	 */
	private String getMessageDigest(EMMessage message, Context context) {
		String digest = "";
		switch (message.getType()) {
		case LOCATION: // 位置消息
			if (message.direct == EMMessage.Direct.RECEIVE) {
				// 从sdk中提到了ui中，使用更简单不犯错的获取string的方法
				// digest = EasyUtils.getAppResourceString(context,
				// "location_recv");
				digest = getStrng(context, R.string.location_recv);
				digest = String.format(digest, message.getStringAttribute(
						Config.CHAT_NICK_NAME, "陌生人"));
				return digest;
			} else {
				// digest = EasyUtils.getAppResourceString(context,
				// "location_prefix");
				digest = getStrng(context, R.string.location_prefix);
			}
			break;
		case IMAGE: // 图片消息
			ImageMessageBody imageBody = (ImageMessageBody) message.getBody();
			digest = getStrng(context, R.string.picture)
					+ imageBody.getFileName();
			break;
		case VOICE:// 语音消息
			digest = getStrng(context, R.string.voice);
			break;
		case VIDEO: // 视频消息
			digest = getStrng(context, R.string.video);
			break;
		case TXT: // 文本消息
			if (!message.getBooleanAttribute(
					Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
				TextMessageBody txtBody = (TextMessageBody) message.getBody();
				digest = txtBody.getMessage();
			} else {
				TextMessageBody txtBody = (TextMessageBody) message.getBody();
				digest = getStrng(context, R.string.voice_call)
						+ txtBody.getMessage();
			}
			break;
		case FILE: // 普通文件消息
			digest = getStrng(context, R.string.file);
			break;
		default:
			EMLog.e(TAG, "unknow type");
			return "";
		}

		return digest;
	}

	private static class ViewHolder {
		/** 和谁的聊天记录 */
		TextView name;
		/** 消息未读数 */
		TextView unreadLabel;
		/** 最后一条消息的内容 */
		TextView message;
		/** 最后一条消息的时间 */
		TextView time;
		/** 用户头像 */
		ImageView avatar;
		/** 最后一条消息的发送状态 */
		View msgState;
		/** 整个list中每一行总布局 */
		RelativeLayout list_item_layout;

	}

	String getStrng(Context context, int resId) {
		return context.getResources().getString(resId);
	}

	// @Override
	// public Filter getFilter() {
	// if (conversationFilter == null) {
	// conversationFilter = new ConversationFilter(conversationList);
	// }
	// return conversationFilter;
	// }
	//
	// private class ConversationFilter extends Filter {
	// List<EMConversation> mOriginalValues = null;
	//
	// public ConversationFilter(List<EMConversation> mList) {
	// mOriginalValues = mList;
	// }
	//
	// @Override
	// protected FilterResults performFiltering(CharSequence prefix) {
	// FilterResults results = new FilterResults();
	//
	// if (mOriginalValues == null) {
	// mOriginalValues = new ArrayList<EMConversation>();
	// }
	// if (prefix == null || prefix.length() == 0) {
	// results.values = copyConversationList;
	// results.count = copyConversationList.size();
	// } else {
	// String prefixString = prefix.toString();
	// final int count = mOriginalValues.size();
	// final ArrayList<EMConversation> newValues = new
	// ArrayList<EMConversation>();
	//
	// for (int i = 0; i < count; i++) {
	// final EMConversation value = mOriginalValues.get(i);
	// String username = value.getUserName();
	//
	// EMGroup group = EMGroupManager.getInstance().getGroup(username);
	// if (group != null) {
	// username = group.getGroupName();
	// }
	//
	// // First match against the whole ,non-splitted value
	// if (username.startsWith(prefixString)) {
	// newValues.add(value);
	// } else {
	// final String[] words = username.split(" ");
	// final int wordCount = words.length;
	//
	// // Start at index 0, in case valueText starts with
	// // space(s)
	// for (int k = 0; k < wordCount; k++) {
	// if (words[k].startsWith(prefixString)) {
	// newValues.add(value);
	// break;
	// }
	// }
	// }
	// }
	//
	// results.values = newValues;
	// results.count = newValues.size();
	// }
	// return results;
	// }
	//
	// @SuppressWarnings("unchecked")
	// @Override
	// protected void publishResults(CharSequence constraint, FilterResults
	// results) {
	// conversationList.clear();
	// conversationList.addAll((List<EMConversation>) results.values);
	// if (results.count > 0) {
	// notiyfyByFilter = true;
	// notifyDataSetChanged();
	// } else {
	// notifyDataSetInvalidated();
	// }
	//
	// }
	//
	// }

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		// if (!notiyfyByFilter) {
		// copyConversationList.clear();
		// copyConversationList.addAll(conversationList);
		// notiyfyByFilter = false;
		// }
	}
}
