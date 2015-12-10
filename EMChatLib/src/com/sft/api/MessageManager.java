package com.sft.api;

import java.io.File;
import java.util.List;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMConversation.EMConversationType;
import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.LocationMessageBody;
import com.easemob.chat.NormalFileMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.VideoMessageBody;
import com.easemob.chat.VoiceMessageBody;
import com.sft.emchatlib.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.widget.Toast;

@SuppressLint({ "ClickableViewAccessibility", "ShowToast" })
public class MessageManager {

	private String chatName;
	private EMConversation conversation;
	private Context context;

	public MessageManager(Context context, String chatName) {
		this.context = context;
		this.chatName = chatName;
		conversation = EMChatManager.getInstance().getConversationByType(chatName, EMConversationType.Chat);
	}

	public EMMessage getLastMessage() {
		return conversation.getLastMessage();
	}

	public EMConversation getConversation() {
		return conversation;
	}

	public List<EMMessage> getMessages(String msgId, int size) {
		List<EMMessage> list = conversation.loadMoreMsgFromDB(msgId, size);
		for (int i = 0; i < list.size(); i++) {
			conversation.getMessage(i);
		}
		return list;
	}

	public List<EMMessage> getMessages(int size) {
		EMMessage message = getLastMessage();
		try {
			List<EMMessage> list = conversation.loadMoreMsgFromDB(message.getMsgId(), size);
			list.add(message);
			for (int i = 0; i < list.size(); i++) {
				conversation.getMessage(i);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void sendText(String content) {
		try {
			EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
			TextMessageBody txtBody = new TextMessageBody(content);
			message.addBody(txtBody);
			message.setReceipt(chatName);
			conversation.addMessage(message);
			EMChatManager.getInstance().sendMessage(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendVoice(String filePath, String fileName, String length) {
		if (!(new File(filePath).exists())) {
			return;
		}
		try {
			final EMMessage message = EMMessage.createSendMessage(EMMessage.Type.VOICE);
			message.setReceipt(chatName);
			int len = Integer.parseInt(length);
			VoiceMessageBody body = new VoiceMessageBody(new File(filePath), len);
			message.addBody(body);
			conversation.addMessage(message);
			EMChatManager.getInstance().sendMessage(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendPicture(final String filePath) {
		try {
			final EMMessage message = EMMessage.createSendMessage(EMMessage.Type.IMAGE);
			message.setReceipt(chatName);
			ImageMessageBody body = new ImageMessageBody(new File(filePath));
			// 默认超过100k的图片会压缩后发给对方，可以设置成发送原图
			// body.setSendOriginalImage(true);
			message.addBody(body);
			conversation.addMessage(message);
			EMChatManager.getInstance().sendMessage(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送视频消息
	 */
	public void sendVideo(final String filePath, final String thumbPath, final int length) {
		final File videoFile = new File(filePath);
		if (!videoFile.exists()) {
			return;
		}
		try {
			EMMessage message = EMMessage.createSendMessage(EMMessage.Type.VIDEO);
			message.setReceipt(chatName);
			VideoMessageBody body = new VideoMessageBody(videoFile, thumbPath, length, videoFile.length());
			message.addBody(body);
			conversation.addMessage(message);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 发送文件
	 * 
	 * @param uri
	 */
	public void sendFile(Uri uri) {
		String filePath = null;
		if ("content".equalsIgnoreCase(uri.getScheme())) {
			String[] projection = { "_data" };
			Cursor cursor = null;

			try {
				cursor = context.getContentResolver().query(uri, projection, null, null, null);
				int column_index = cursor.getColumnIndexOrThrow("_data");
				if (cursor.moveToFirst()) {
					filePath = cursor.getString(column_index);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("file".equalsIgnoreCase(uri.getScheme())) {
			filePath = uri.getPath();
		}
		File file = new File(filePath);
		if (file == null || !file.exists()) {
			String st7 = context.getResources().getString(R.string.File_does_not_exist);
			Toast.makeText(context, st7, 0).show();
			return;
		}
		if (file.length() > 10 * 1024 * 1024) {
			String st6 = context.getResources().getString(R.string.The_file_is_not_greater_than_10_m);
			Toast.makeText(context, st6, 0).show();
			return;
		}

		// 创建一个文件消息
		EMMessage message = EMMessage.createSendMessage(EMMessage.Type.FILE);
		message.setReceipt(chatName);
		// add message body
		NormalFileMessageBody body = new NormalFileMessageBody(new File(filePath));
		message.addBody(body);
		conversation.addMessage(message);
	}

	/**
	 * 发送位置信息
	 * 
	 * @param latitude
	 * @param longitude
	 * @param imagePath
	 * @param locationAddress
	 */
	public void sendLocationMsg(double latitude, double longitude, String imagePath, String locationAddress) {
		EMMessage message = EMMessage.createSendMessage(EMMessage.Type.LOCATION);
		LocationMessageBody locBody = new LocationMessageBody(locationAddress, latitude, longitude);
		message.addBody(locBody);
		message.setReceipt(chatName);
		conversation.addMessage(message);
	}
}
