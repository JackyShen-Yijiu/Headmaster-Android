package com.yibu.headmaster.utils;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.yibu.common.Config;
import com.yibu.headmaster.R;

@SuppressLint("HandlerLeak")
public class DownLoadService extends Service {
	private final static int DOWNLOAD_COMPLETE = -2;
	private final static int DOWNLOAD_FAIL = -1;

	// 自定义通知栏类
	MyNotification myNotification;

	String filePathString; // 下载文件绝对路径(包括文件名)

	// 通知栏跳转Intent
	private PendingIntent updatePendingIntent = null;

	DownFileThread downFileThread; // 自定义文件下载线程

	private Handler updateHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWNLOAD_COMPLETE:
				// 点击安装PendingIntent
				Uri uri = Uri.fromFile(downFileThread.getApkFile());
				Intent installIntent = new Intent(Intent.ACTION_VIEW);
				installIntent.setDataAndType(uri,
						"application/vnd.android.package-archive");
				updatePendingIntent = PendingIntent.getActivity(
						DownLoadService.this, 0, installIntent, 0);
				myNotification.changeContentIntent(updatePendingIntent);
				myNotification.notification.defaults = Notification.DEFAULT_SOUND;// 铃声提醒
				myNotification.changeNotificationText("下载完成，请点击安装！");

				// 停止服务
				stopSelf();
				break;
			case DOWNLOAD_FAIL:
				// 下载失败
				myNotification.changeNotificationText("文件下载失败！");
				stopSelf();
				break;
			default: // 下载中
				myNotification.changeProgressStatus(msg.what);
			}
		}
	};

	@Override
	public void onDestroy() {
		if (downFileThread != null)
			downFileThread.interuptThread();
		stopSelf();
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		Log.e("onStartCommand", "onStartCommand");
		myNotification = new MyNotification(this, null, 1);

		myNotification.showCustomizeNotification(R.drawable.app_logo,
				getString(R.string.app_name) + "更新包", R.layout.down_nofity);

		filePathString = Config.APKPATH + File.separator
				+ getString(R.string.app_name) + ".apk";

		// 开启一个新的线程下载，如果使用Service同步下载，会导致ANR问题，Service本身也会阻塞
		String url = intent.getStringExtra("url");
		if (!TextUtils.isEmpty(url)) {
			downFileThread = new DownFileThread(this, updateHandler,
					intent.getStringExtra("url"), filePathString);
			new Thread(downFileThread).start();
		} else {
			stopSelf();
		}

		return START_REDELIVER_INTENT;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

}