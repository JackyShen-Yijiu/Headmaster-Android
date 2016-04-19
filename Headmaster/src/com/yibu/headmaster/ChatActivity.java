package com.yibu.headmaster;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMError;
import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMConversation.EMConversationType;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.LocationMessageBody;
import com.easemob.chat.NormalFileMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.VideoMessageBody;
import com.easemob.chat.VoiceMessageBody;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.PathUtil;
import com.easemob.util.VoiceRecorder;
import com.jzjf.headmaster.R;
import com.sft.Utils.CommonUtils;
import com.sft.Utils.HXSDKHelper;
import com.yibu.common.Config;
import com.yibu.common.Config.UserType;
import com.yibu.headmaster.emchat.MessageAdapter;
import com.yibu.headmaster.emchat.VoicePlayClickListener;
import com.yibu.headmaster.global.HeadmasterApplication;
import com.yibu.headmaster.utils.LogUtil;
import com.yibu.headmaster.utils.ToastUtil;

//import com.ypy.eventbus.EventBus;

/**
 * 聊天
 * 
 * @author Administrator
 * 
 */
@SuppressLint({ "Wakelock", "HandlerLeak", "ShowToast" })
@SuppressWarnings({ "unused", "deprecation" })
public class ChatActivity extends BaseActivity implements
		OnCheckedChangeListener, TextWatcher, OnRefreshListener,
		EMEventListener {

	private static final int REQUEST_CODE_EMPTY_HISTORY = 2;
	public static final int REQUEST_CODE_CONTEXT_MENU = 3;
	private static final int REQUEST_CODE_MAP = 4;
	public static final int REQUEST_CODE_TEXT = 5;
	public static final int REQUEST_CODE_VOICE = 6;
	public static final int REQUEST_CODE_PICTURE = 7;
	public static final int REQUEST_CODE_LOCATION = 8;
	public static final int REQUEST_CODE_NET_DISK = 9;
	public static final int REQUEST_CODE_FILE = 10;
	public static final int REQUEST_CODE_COPY_AND_PASTE = 11;
	public static final int REQUEST_CODE_PICK_VIDEO = 12;
	public static final int REQUEST_CODE_DOWNLOAD_VIDEO = 13;
	public static final int REQUEST_CODE_VIDEO = 14;
	public static final int REQUEST_CODE_DOWNLOAD_VOICE = 15;
	public static final int REQUEST_CODE_SELECT_USER_CARD = 16;
	public static final int REQUEST_CODE_SEND_USER_CARD = 17;
	public static final int REQUEST_CODE_CAMERA = 18;
	public static final int REQUEST_CODE_LOCAL = 19;
	public static final int REQUEST_CODE_CLICK_DESTORY_IMG = 20;
	public static final int REQUEST_CODE_GROUP_DETAIL = 21;
	public static final int REQUEST_CODE_SELECT_VIDEO = 23;
	public static final int REQUEST_CODE_SELECT_FILE = 24;
	public static final int REQUEST_CODE_ADD_TO_BLACKLIST = 25;

	public static final int RESULT_CODE_COPY = 1;
	public static final int RESULT_CODE_DELETE = 2;
	public static final int RESULT_CODE_FORWARD = 3;
	public static final int RESULT_CODE_OPEN = 4;
	public static final int RESULT_CODE_DWONLOAD = 5;
	public static final int RESULT_CODE_TO_CLOUD = 6;
	public static final int RESULT_CODE_EXIT_GROUP = 7;

	public static final int CHATTYPE_SINGLE = 1;
	public static final int CHATTYPE_GROUP = 2;
	public static final int CHATTYPE_CHATROOM = 3;

	public static final String COPY_IMAGE = "EASEMOBIMG";

	private SwipeRefreshLayout swipeRefreshLayout;
	private ListView chatListView;
	private TextView titleTv, timeTv;

	private LinearLayout sendTextLayout, sendVoiceLayout, moreLayout;
	// 语音文字转换按钮
	private CheckBox changeCk;

	// 发送文字布局控件
	private EditText sendTextEt;
	private Button sendTextBtn;
	private Button moreTextBtn;
	// 发送语音 布局控件
	private Button sendVoiceBtn;
	private Button moreVoiceBtn;
	// 更多布局控件
	private ImageView sendPicIm;
	private ImageView sendTakePicIm;

	private String chatId = "";
	private String chatName = "18562172892";
	private String chatUrl = "";

	private int userTypeNoAnswer = 0;

	private EMConversation conversation;

	// 正在播放语音的消息id
	public String playMsgId;
	//
	public static int resendPos;
	// 录音状态提示布局
	private View recordingContainer;
	// 录音状态图片
	private ImageView micImage;
	// 录音文字提示
	private TextView recordingHint;
	// 环信自带的录音工具
	private VoiceRecorder voiceRecorder;

	// 每次获取历史消息的条数
	private int size = 10;
	// 聊天消息
	private List<EMMessage> messageList = new ArrayList<EMMessage>();
	//
	private MessageAdapter adapter;

	private boolean haveMoreData = true;
	private ClipboardManager clipboard;
	private File cameraFile;
	private PowerManager.WakeLock wakeLock;
	private HeadmasterApplication app = HeadmasterApplication.app;

	@Override
	protected void onResume() {
		// register(getClass().getName());
		if (adapter != null) {
			adapter.refresh();
		}
		super.onResume();
	}

	@Override
	protected void initView() {
		// view = View.inflate(getBaseContext(), R.layout.activity_chat, null);
		addView(R.layout.activity_chat);
		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.chat_swipe_layout);
		chatListView = (ListView) findViewById(R.id.chat_listview);
		titleTv = (TextView) findViewById(R.id.chat_title_tv);
		timeTv = (TextView) findViewById(R.id.chat_time_tv);

		sendTextLayout = (LinearLayout) findViewById(R.id.chat_text_layout);
		sendVoiceLayout = (LinearLayout) findViewById(R.id.chat_voice_layout);
		moreLayout = (LinearLayout) findViewById(R.id.chat_more_layout);

		sendTextEt = (EditText) findViewById(R.id.chat_et);
		sendTextBtn = (Button) findViewById(R.id.chat_send_btn);
		moreTextBtn = (Button) findViewById(R.id.chat_text_more_btn);
		sendVoiceBtn = (Button) findViewById(R.id.chat_voice_btn);
		moreVoiceBtn = (Button) findViewById(R.id.chat_voice_more_btn);
		sendPicIm = (ImageView) findViewById(R.id.chat_more_picture_im);
		sendTakePicIm = (ImageView) findViewById(R.id.chat_more_takepic_im);

		changeCk = (CheckBox) findViewById(R.id.chat_style_ck);

		recordingContainer = findViewById(R.id.chat_recording_container);
		recordingHint = (TextView) findViewById(R.id.chat_recording_hint);
		micImage = (ImageView) findViewById(R.id.chat_mic_image);

	}

	@Override
	protected void initData() {
		chatName = getIntent().getStringExtra("chatName");
		chatUrl = getIntent().getStringExtra("chatUrl");
		chatId = getIntent().getStringExtra("chatId");
		LogUtil.print("chatName=" + chatName + " chatid=" + chatId);
		userTypeNoAnswer = getIntent().getIntExtra("userTypeNoAnswer", 0);

		// setTitleText(chatName);
		baseTitle.setText(chatName);

		EMChatManager.getInstance().registerEventListener(
				this,
				new EMNotifierEvent.Event[] {
						EMNotifierEvent.Event.EventNewMessage,
						EMNotifierEvent.Event.EventOfflineMessage,
						EMNotifierEvent.Event.EventDeliveryAck,
						EMNotifierEvent.Event.EventReadAck });
		wakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE))
				.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "blackcat");
		clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		conversation = EMChatManager.getInstance().getConversationByType(
				chatId, EMConversationType.Chat);
		// 把此会话的未读数置为0
		conversation.markAllMessagesAsRead();
		// 显示消息
		adapter = new MessageAdapter(ChatActivity.this, chatId, chatUrl);
		chatListView.setAdapter(adapter);
		adapter.refreshSelectLast();

		// 动画资源文件,用于录制语音时
		final Drawable[] micImages = new Drawable[] {
				getResources().getDrawable(R.drawable.record_animate_01),
				getResources().getDrawable(R.drawable.record_animate_02),
				getResources().getDrawable(R.drawable.record_animate_03),
				getResources().getDrawable(R.drawable.record_animate_04),
				getResources().getDrawable(R.drawable.record_animate_05),
				getResources().getDrawable(R.drawable.record_animate_06),
				getResources().getDrawable(R.drawable.record_animate_07),
				getResources().getDrawable(R.drawable.record_animate_08),
				getResources().getDrawable(R.drawable.record_animate_09),
				getResources().getDrawable(R.drawable.record_animate_10),
				getResources().getDrawable(R.drawable.record_animate_11),
				getResources().getDrawable(R.drawable.record_animate_12),
				getResources().getDrawable(R.drawable.record_animate_13),
				getResources().getDrawable(R.drawable.record_animate_14), };

		Handler micImageHandler = new Handler() {
			@Override
			public void handleMessage(android.os.Message msg) {
				// 切换msg切换图片
				micImage.setImageDrawable(micImages[msg.what]);
			}
		};
		voiceRecorder = new VoiceRecorder(micImageHandler);

	}

	public ListView getListView() {
		return chatListView;
	}

	private void setListener() {
		sendPicIm.setOnClickListener(this);
		sendTakePicIm.setOnClickListener(this);
		moreVoiceBtn.setOnClickListener(this);
		sendVoiceBtn.setOnClickListener(this);
		moreTextBtn.setOnClickListener(this);
		sendTextBtn.setOnClickListener(this);

		changeCk.setOnCheckedChangeListener(this);
		sendTextEt.addTextChangedListener(this);

		swipeRefreshLayout.setOnRefreshListener(this);

		sendVoiceBtn.setOnTouchListener(new PressToSpeakListen());
		// 默认文字
		changeCk.setChecked(true);
		sendTextEt.setText("");
	}

	// @Override
	// public void onClick(View v) {
	//
	// }

	@Override
	public void onClick(View v) {
		super.onClick(v);
		LogUtil.print("点击事件");
		switch (v.getId()) {
		// case R.id.base_left_btn:
		// finish();
		// break;
		case R.id.chat_send_btn:
			sendText();
			break;
		case R.id.chat_more_picture_im:
			// 发送图片
			selectPicFromLocal();
			break;
		case R.id.chat_more_takepic_im:
			selectPicFromCamera();
			break;
		case R.id.chat_text_more_btn:
			// 文字更多按钮
			if (moreLayout.getVisibility() == View.GONE) {
				moreLayout.setVisibility(View.VISIBLE);
				// util.hideInputMethod(this);
			} else {
				moreLayout.setVisibility(View.GONE);
			}
			break;
		case R.id.chat_voice_more_btn:
			if (moreLayout.getVisibility() == View.GONE) {
				moreLayout.setVisibility(View.VISIBLE);
				// util.hideInputMethod(this);
			} else {
				moreLayout.setVisibility(View.GONE);
			}
			break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		moreLayout.setVisibility(View.GONE);
		// util.hideInputMethod(this);
		if (isChecked) {
			sendTextLayout.setVisibility(View.VISIBLE);
			sendVoiceLayout.setVisibility(View.GONE);
		} else {
			sendTextLayout.setVisibility(View.GONE);
			sendVoiceLayout.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (s == null || s.toString().length() == 0) {
			// 发送文字布局，输入框没有文字，显示更多按钮，隐藏发送按钮
			sendTextBtn.setVisibility(View.GONE);
			moreTextBtn.setVisibility(View.VISIBLE);
		} else {
			sendTextBtn.setVisibility(View.VISIBLE);
			moreTextBtn.setVisibility(View.GONE);
		}
	}

	@Override
	public void afterTextChanged(Editable s) {

	}

	private void sendText() {
		sendText(sendTextEt.getText().toString());
		sendTextEt.setText("");
	}

	/**
	 * 按住说话listener
	 * 
	 */
	private class PressToSpeakListen implements View.OnTouchListener {

		public boolean isExitsSdcard() {
			if (android.os.Environment.getExternalStorageState().equals(
					android.os.Environment.MEDIA_MOUNTED))
				return true;
			else
				return false;
		}

		@SuppressLint("ClickableViewAccessibility")
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (!isExitsSdcard()) {
					ToastUtil.showToast(getBaseContext(),
							getString(R.string.Send_voice_need_sdcard_support));
					return false;
				}
				try {
					v.setPressed(true);
					((Button) v).setText(R.string.unpress_end);
					wakeLock.acquire();
					if (VoicePlayClickListener.isPlaying)
						VoicePlayClickListener.currentPlayListener
								.stopPlayVoice();
					recordingContainer.setVisibility(View.VISIBLE);
					recordingHint
							.setText(getString(R.string.move_up_to_cancel));
					recordingHint.setBackgroundColor(Color.TRANSPARENT);
					voiceRecorder.startRecording(null, chatName,
							ChatActivity.this);
				} catch (Exception e) {
					e.printStackTrace();
					v.setPressed(false);
					((Button) v).setText(R.string.press_speak);
					if (wakeLock.isHeld())
						wakeLock.release();
					if (voiceRecorder != null)
						voiceRecorder.discardRecording();
					recordingContainer.setVisibility(View.GONE);
					ToastUtil.showToast(getBaseContext(),
							getString(R.string.recoding_fail));
					return false;
				}

				return true;
			case MotionEvent.ACTION_MOVE: {
				if (event.getY() < 0) {
					((Button) v).setText(R.string.unpress_cancel);
					recordingHint
							.setText(getString(R.string.release_to_cancel));
					recordingHint
							.setBackgroundResource(R.drawable.recording_text_hint_bg);
				} else {
					((Button) v).setText(R.string.unpress_end);
					recordingHint
							.setText(getString(R.string.move_up_to_cancel));
					recordingHint.setBackgroundColor(Color.TRANSPARENT);
				}
				return true;
			}
			case MotionEvent.ACTION_UP:
				v.setPressed(false);
				((Button) v).setText(R.string.press_speak);
				recordingContainer.setVisibility(View.GONE);
				if (wakeLock.isHeld())
					wakeLock.release();
				if (event.getY() < 0) {
					voiceRecorder.discardRecording();
				} else {
					try {
						int length = voiceRecorder.stopRecoding();
						if (length > 0) {
							sendVoice(voiceRecorder.getVoiceFilePath(),
									voiceRecorder.getVoiceFileName(chatName),
									Integer.toString(length));
							refreshUI();
						} else if (length == EMError.INVALID_FILE) {
							ToastUtil
									.showToast(
											getBaseContext(),
											getResources()
													.getString(
															R.string.Recording_without_permission));
						} else {
							ToastUtil
									.showToast(
											getBaseContext(),
											getResources()
													.getString(
															R.string.The_recording_time_is_too_short));
						}
					} catch (Exception e) {
						e.printStackTrace();
						ToastUtil.showToast(getBaseContext(), getResources()
								.getString(R.string.send_failure_please));
					}

				}
				return true;
			default:
				recordingContainer.setVisibility(View.GONE);
				if (voiceRecorder != null)
					voiceRecorder.discardRecording();
				return false;
			}
		}
	}

	/**
	 * 照相获取图片
	 */
	public void selectPicFromCamera() {
		if (!CommonUtils.isExitsSdcard()) {
			ToastUtil.showToast(getBaseContext(),
					getString(R.string.sd_card_does_not_exist));
			return;
		}

		if (HeadmasterApplication.app.userInfo == null) {
			System.out.println("HeadmasterApplication.app.userInfo");
		}
		cameraFile = new File(PathUtil.getInstance().getImagePath(),
				HeadmasterApplication.app.userInfo.userid
						+ System.currentTimeMillis() + ".jpg");
		cameraFile.getParentFile().mkdirs();
		startActivityForResult(
				new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(
						MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)),
				REQUEST_CODE_CAMERA);
	}

	/**
	 * 从图库获取图片
	 */
	@SuppressLint("InlinedApi")
	public void selectPicFromLocal() {
		Intent intent;
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
			intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");
		} else {
			intent = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					"image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
		}
		startActivityForResult(intent, REQUEST_CODE_LOCAL);
	}

	/**
	 * 根据图库图片uri发送图片
	 * 
	 * @param selectedImage
	 */
	private void sendPicByUri(Uri selectedImage) {
		String picPath = com.yibu.headmaster.utils.PathUtil.getPath(
				getBaseContext(), selectedImage);
		sendPicture(picPath);
		// Cursor cursor = getContentResolver().query(selectedImage, null, null,
		// null, null);
		// String st8 = getResources().getString(R.string.cant_find_pictures);
		// if (cursor != null) {
		// cursor.moveToFirst();
		// util.print(MediaStore.Images.Media.DATA);
		// int columnIndex =
		// cursor.getColumnIndex(MediaStore.Images.Media.DATA);
		// String picturePath = cursor.getString(columnIndex);
		// cursor.close();
		// cursor = null;
		//
		// if (picturePath == null || picturePath.equals("null")) {
		// Toast toast = Toast.makeText(this, st8, Toast.LENGTH_SHORT);
		// toast.setGravity(Gravity.CENTER, 0, 0);
		// toast.show();
		// return;
		// }
		// sendPicture(picturePath);
		// } else {
		// File file = new File(selectedImage.getPath());
		// if (!file.exists()) {
		// Toast toast = Toast.makeText(this, st8, Toast.LENGTH_SHORT);
		// toast.setGravity(Gravity.CENTER, 0, 0);
		// toast.show();
		// return;
		// }
		// sendPicture(file.getAbsolutePath());
		// }
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_CONTEXT_MENU) {
			switch (resultCode) {
			case RESULT_CODE_COPY: // 复制消息
				EMMessage copyMsg = (adapter.getItem(data.getIntExtra(
						"position", -1)));
				clipboard.setText(((TextMessageBody) copyMsg.getBody())
						.getMessage());
				break;
			case RESULT_CODE_DELETE: // 删除消息
				EMMessage deleteMsg = adapter.getItem(data.getIntExtra(
						"position", -1));
				conversation.removeMessage(deleteMsg.getMsgId());
				adapter.refreshSeekTo(data.getIntExtra("position",
						adapter.getCount()) - 1);
				break;
			default:
				break;
			}
		}
		if (resultCode == RESULT_OK) { // 清空消息
			if (requestCode == REQUEST_CODE_EMPTY_HISTORY) {
				// 清空会话
				EMChatManager.getInstance().clearConversation(chatName);
				adapter.refresh();
			} else if (requestCode == REQUEST_CODE_CAMERA) { // 发送照片
				LogUtil.print("sssssssssssssssssssssssssss");
				if (cameraFile != null && cameraFile.exists()) {
					LogUtil.print("2222222222222222");
					sendPicture(cameraFile.getAbsolutePath());
				}
			} else if (requestCode == REQUEST_CODE_SELECT_VIDEO) { // 发送本地选择的视频
				int duration = data.getIntExtra("dur", 0);
				String videoPath = data.getStringExtra("path");
				File file = new File(PathUtil.getInstance().getImagePath(),
						"thvideo" + System.currentTimeMillis());
				Bitmap bitmap = null;
				FileOutputStream fos = null;
				try {
					if (!file.getParentFile().exists()) {
						file.getParentFile().mkdirs();
					}
					bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, 3);
					if (bitmap == null) {
						bitmap = BitmapFactory.decodeResource(getResources(),
								R.drawable.app_panel_video_icon);
					}
					fos = new FileOutputStream(file);
					bitmap.compress(CompressFormat.JPEG, 100, fos);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (fos != null) {
						try {
							fos.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						fos = null;
					}
					if (bitmap != null) {
						bitmap.recycle();
						bitmap = null;
					}
				}
				sendVideo(videoPath, file.getAbsolutePath(), duration / 1000);

			} else if (requestCode == REQUEST_CODE_LOCAL) { // 发送本地图片
				if (data != null) {
					Uri selectedImage = data.getData();
					if (selectedImage != null) {
						sendPicByUri(selectedImage);
					}
				}
			} else if (requestCode == REQUEST_CODE_SELECT_FILE) { // 发送选择的文件
				if (data != null) {
					Uri uri = data.getData();
					if (uri != null) {
						sendFile(uri);
					}
				}

			} else if (requestCode == REQUEST_CODE_MAP) { // 地图
				double latitude = data.getDoubleExtra("latitude", 0);
				double longitude = data.getDoubleExtra("longitude", 0);
				String locationAddress = data.getStringExtra("address");
				if (locationAddress != null && !locationAddress.equals("")) {
					sendLocationMsg(latitude, longitude, "", locationAddress);
				} else {
					String st = getResources().getString(
							R.string.unable_to_get_loaction);
					Toast.makeText(this, st, 0).show();
				}
				// 重发消息
			} else if (requestCode == REQUEST_CODE_TEXT
					|| requestCode == REQUEST_CODE_VOICE
					|| requestCode == REQUEST_CODE_PICTURE
					|| requestCode == REQUEST_CODE_LOCATION
					|| requestCode == REQUEST_CODE_VIDEO
					|| requestCode == REQUEST_CODE_FILE) {
				resendMessage();
			} else if (requestCode == REQUEST_CODE_COPY_AND_PASTE) {
				// 粘贴
				if (!TextUtils.isEmpty(clipboard.getText())) {
					String pasteText = clipboard.getText().toString();
					if (pasteText.startsWith(COPY_IMAGE)) {
						// 把图片前缀去掉，还原成正常的path
						sendPicture(pasteText.replace(COPY_IMAGE, ""));
					}

				}
			} else if (requestCode == REQUEST_CODE_ADD_TO_BLACKLIST) { // 移入黑名单
				EMMessage deleteMsg = adapter.getItem(data.getIntExtra(
						"position", -1));
				addUserToBlacklist(deleteMsg.getFrom());
			} else if (conversation.getMsgCount() > 0) {
				adapter.refresh();
				setResult(RESULT_OK);
			} else if (requestCode == REQUEST_CODE_GROUP_DETAIL) {
				adapter.refresh();
			}
		}
	}

	@Override
	public void onRefresh() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if (haveMoreData) {
					List<EMMessage> messages;
					try {
						messages = conversation.loadMoreMsgFromDB(adapter
								.getItem(0).getMsgId(), size);
						if (messages.size() > 0) {
							adapter.notifyDataSetChanged();
							adapter.refreshSeekTo(messages.size() - 1);
							if (messages.size() != size) {
								haveMoreData = false;
							}
						} else {
							ToastUtil.showToast(getBaseContext(),
									getString(R.string.no_more_messages));
							haveMoreData = false;
						}
					} catch (Exception e1) {
						e1.printStackTrace();
						swipeRefreshLayout.setRefreshing(false);
						return;
					}
				} else {
					ToastUtil.showToast(getBaseContext(),
							getString(R.string.no_more_messages));
				}
				swipeRefreshLayout.setRefreshing(false);
			}
		}, 1000);
	}

	/**
	 * 重发消息
	 */
	private void resendMessage() {
		EMMessage msg = null;
		msg = conversation.getMessage(resendPos);
		msg.status = EMMessage.Status.CREATE;
		adapter.refreshSeekTo(resendPos);
	}

	/**
	 * 加入到黑名单
	 * 
	 * @param username
	 */
	private void addUserToBlacklist(final String username) {
		final ProgressDialog pd = new ProgressDialog(this);
		pd.setMessage(getString(R.string.Is_moved_into_blacklist));
		pd.setCanceledOnTouchOutside(false);
		pd.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					EMContactManager.getInstance().addUserToBlackList(username,
							false);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							pd.dismiss();
							ToastUtil
									.showToast(
											getBaseContext(),
											getString(R.string.Move_into_blacklist_success));
						}
					});
				} catch (EaseMobException e) {
					e.printStackTrace();
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							pd.dismiss();
							ToastUtil
									.showToast(
											getBaseContext(),
											getString(R.string.Move_into_blacklist_failure));
						}
					});
				}
			}
		}).start();
	}

	@Override
	public void onEvent(EMNotifierEvent event) {
		switch (event.getEvent()) {
		case EventNewMessage: {
			// 获取到message
			EMMessage message = (EMMessage) event.getData();

			String username = null;
			// 群组消息
			if (message.getChatType() == ChatType.GroupChat
					|| message.getChatType() == ChatType.ChatRoom) {
				username = message.getTo();
			} else {
				// 单聊消息
				username = message.getFrom();
			}

			// 如果是当前会话的消息，刷新聊天页面
			if (username.equals(chatName)) {
				refreshUIWithNewMessage();
				// 声音和震动提示有新消息
				HXSDKHelper.getInstance().getNotifier()
						.viberateAndPlayTone(message);
			} else {
				// 如果消息不是和当前聊天ID的消息
				HXSDKHelper.getInstance().getNotifier().onNewMsg(message);
			}
			refreshUI();
			break;
		}
		case EventDeliveryAck: {
			// 获取到message
			LogUtil.print("EventDeliveryAck");
			EMMessage message = (EMMessage) event.getData();
			refreshUI();
			break;
		}
		case EventReadAck: {
			// 获取到message
			LogUtil.print("EventReadAck");
			EMMessage message = (EMMessage) event.getData();
			refreshUI();
			break;
		}
		case EventOfflineMessage: {
			LogUtil.print("EventOfflineMessage");
			// a list of offline messages
			// List<EMMessage> offlineMessages = (List<EMMessage>)
			// event.getData();
			refreshUI();
			break;
		}
		default:
			break;
		}

	}

	private void refreshUIWithNewMessage() {
		if (adapter == null) {
			return;
		}

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				adapter.refreshSelectLast();
			}
		});
	}

	private void refreshUI() {
		if (adapter == null) {
			return;
		}

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				adapter.refresh();
			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (wakeLock.isHeld())
			wakeLock.release();
		if (VoicePlayClickListener.isPlaying
				&& VoicePlayClickListener.currentPlayListener != null) {
			// 停止语音播放
			VoicePlayClickListener.currentPlayListener.stopPlayVoice();
		}

		try {
			// 停止录音
			if (voiceRecorder.isRecording()) {
				voiceRecorder.discardRecording();
				recordingContainer.setVisibility(View.INVISIBLE);
			}
		} catch (Exception e) {
		}
		// EventBus.getDefault().post(new NewMessageReceiveEvent());
	}

	@Override
	protected void onDestroy() {
		EMChatManager.getInstance().unregisterEventListener(this);
		super.onDestroy();
	}

	private void setExt(EMMessage message) {
		message.setAttribute(Config.CHAT_HEAD_RUL, app.userInfo.headportrait);
		message.setAttribute(Config.CHAT_NICK_NAME, getChatNickName());
		message.setAttribute(Config.CHAT_USERID, app.userInfo.userid);
		message.setAttribute(Config.CHAT_USERTYPE, UserType.USER.getValue());
		message.setAttribute(Config.CHAT_HEAD_RUL_NOANSWER, chatUrl);
		message.setAttribute(Config.CHAT_NICK_NAME_NOANSWER, chatName);
		message.setAttribute(Config.CHAT_USERTYPE_NOANSWER, userTypeNoAnswer);
		message.setAttribute(Config.CHAT_USERID_NOANSWER, chatId);
	}

	public void sendText(String content) {
		try {
			EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
			TextMessageBody txtBody = new TextMessageBody(content);
			message.addBody(txtBody);
			message.setReceipt(chatId);
			setExt(message);
			conversation.addMessage(message);
			adapter.refreshSelectLast();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendVoice(String filePath, String fileName, String length) {
		if (!(new File(filePath).exists())) {
			return;
		}
		try {
			final EMMessage message = EMMessage
					.createSendMessage(EMMessage.Type.VOICE);
			message.setReceipt(chatId);
			int len = Integer.parseInt(length);
			VoiceMessageBody body = new VoiceMessageBody(new File(filePath),
					len);
			message.addBody(body);
			setExt(message);
			conversation.addMessage(message);
			// EMChatManager.getInstance().sendMessage(message);
			adapter.refreshSelectLast();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendPicture(final String filePath) {
		try {
			final EMMessage message = EMMessage
					.createSendMessage(EMMessage.Type.IMAGE);
			message.setReceipt(chatId);
			System.out.println("chatId" + chatId);
			ImageMessageBody body = new ImageMessageBody(new File(filePath));
			// 默认超过100k的图片会压缩后发给对方，可以设置成发送原图
			// body.setSendOriginalImage(true);
			message.addBody(body);
			setExt(message);
			conversation.addMessage(message);
			// EMChatManager.getInstance().sendMessage(message);
			adapter.refreshSelectLast();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送视频消息
	 */
	public void sendVideo(final String filePath, final String thumbPath,
			final int length) {
		final File videoFile = new File(filePath);
		if (!videoFile.exists()) {
			return;
		}
		try {
			EMMessage message = EMMessage
					.createSendMessage(EMMessage.Type.VIDEO);
			message.setReceipt(chatId);
			VideoMessageBody body = new VideoMessageBody(videoFile, thumbPath,
					length, videoFile.length());
			message.addBody(body);
			setExt(message);
			conversation.addMessage(message);
			adapter.refreshSelectLast();
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
				cursor = getContentResolver().query(uri, projection, null,
						null, null);
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
			String st7 = getResources().getString(R.string.File_does_not_exist);
			ToastUtil.showToast(getBaseContext(), st7);
			return;
		}
		if (file.length() > 10 * 1024 * 1024) {
			String st6 = getResources().getString(
					R.string.The_file_is_not_greater_than_10_m);
			ToastUtil.showToast(getBaseContext(), st6);
			return;
		}

		// 创建一个文件消息
		EMMessage message = EMMessage.createSendMessage(EMMessage.Type.FILE);
		message.setReceipt(chatId);
		// add message body
		NormalFileMessageBody body = new NormalFileMessageBody(new File(
				filePath));
		message.addBody(body);
		setExt(message);
		conversation.addMessage(message);
		adapter.refreshSelectLast();
	}

	/**
	 * 发送位置信息
	 * 
	 * @param latitude
	 * @param longitude
	 * @param imagePath
	 * @param locationAddress
	 */
	public void sendLocationMsg(double latitude, double longitude,
			String imagePath, String locationAddress) {
		EMMessage message = EMMessage
				.createSendMessage(EMMessage.Type.LOCATION);
		LocationMessageBody locBody = new LocationMessageBody(locationAddress,
				latitude, longitude);
		message.addBody(locBody);
		setExt(message);
		message.setReceipt(chatId);
		conversation.addMessage(message);
		adapter.refreshSelectLast();
	}

	private String getChatNickName() {
		String nickName = app.userInfo.name;
		if (TextUtils.isEmpty(nickName)) {
			nickName = app.userInfo.name;
			if (TextUtils.isEmpty(nickName)) {
				nickName = app.userInfo.mobile;
			}
		}
		if (TextUtils.isEmpty(nickName)) {
			nickName = "陌生人";
		}
		return nickName;
	}

	@Override
	public void processSuccess(String data) {

	}

	@Override
	public void processFailure() {

	}

	@Override
	protected void initListener() {
		setListener();
	}

	@Override
	protected void onStop() {
		super.onStop();
		// EventBus.getDefault().post(new NewMessageReceiveEvent());
	}
}
