package com.yibu.headmaster.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.jzjf.headmaster.R;

public class ZProgressHUD extends Dialog {

	static ZProgressHUD instance;
	View view;
	TextView tvMessage;
	ImageView ivSuccess;
	ImageView ivFailure;
	ImageView ivProgressSpinner;
	AnimationDrawable adProgressSpinner;
	static Context mContext;

	public static ZProgressHUD getInstance(Context context) {
		if(mContext!=null && !mContext.equals(context)){
			
			synchronized (ZProgressHUD.class) {
				if(instance!=null && instance.isShowing()){
					instance.dismiss();
				}
//				if (instance == null) {
					instance = new ZProgressHUD(context);
//				}
			}
	}
		if (instance == null) {
			synchronized (ZProgressHUD.class) {
				if (instance == null) {
					instance = new ZProgressHUD(context);
				}
			}
		}
		return instance;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	@SuppressLint("InflateParams")
	private ZProgressHUD(Context context) {
		super(context, R.style.DialogTheme);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setCanceledOnTouchOutside(false);
		this.mContext = context;
		view = getLayoutInflater().inflate(R.layout.dialog_progress, null);
		tvMessage = (TextView) view.findViewById(R.id.textview_message);
		ivSuccess = (ImageView) view.findViewById(R.id.imageview_success);
		ivFailure = (ImageView) view.findViewById(R.id.imageview_failure);
		ivProgressSpinner = (ImageView) view
				.findViewById(R.id.imageview_progress_spinner);

		ivProgressSpinner.setBackgroundResource(R.drawable.round_spinner);
		adProgressSpinner = (AnimationDrawable) ivProgressSpinner
				.getBackground();

		this.setContentView(view);

		adProgressSpinner.stop();
		adProgressSpinner.start();
	}

	public ZProgressHUD setMessage(String message) {
		tvMessage.setText(message);
		return instance;
	}

	@Override
	public void show() {
		if (!((Activity) mContext).isFinishing()) {
			super.show();
		} else {
			System.out.println("instance = null");
			instance = null;
		}
	}

	public void dismissWithSuccess() {
		dismissWithSuccess("Success");
	}

	public void dismissWithSuccess(String message) {
		showSuccessImage();
		if (message != null) {
			tvMessage.setText(message);
		} else {
			tvMessage.setText("");
		}
		dismissHUD();
	}

	public void dismissWithSuccess(String message, int time) {
		showSuccessImage();
		if (message != null) {
			tvMessage.setText(message);
		} else {
			tvMessage.setText("");
		}
		dismissHUD(time);
	}

	public void dismissWithFailure() {
		dismissWithFailure("Failure");
	}

	public void dismissWithFailure(String message) {
		showFailureImage();
		if (message != null) {
			tvMessage.setText(message);
		} else {
			tvMessage.setText("");
		}
		dismissHUD();
	}

	public void dismissWithFailure(String message, int time) {
		showFailureImage();
		if (message != null) {
			tvMessage.setText(message);
		} else {
			tvMessage.setText("");
		}
		dismissHUD(time);
	}

	protected void showSuccessImage() {
		ivProgressSpinner.setVisibility(View.GONE);
		ivSuccess.setVisibility(View.VISIBLE);
	}

	protected void showFailureImage() {
		ivProgressSpinner.setVisibility(View.GONE);
		ivFailure.setVisibility(View.VISIBLE);
	}

	protected void reset() {
		instance = null;
		ivProgressSpinner.setVisibility(View.VISIBLE);
		ivFailure.setVisibility(View.GONE);
		ivSuccess.setVisibility(View.GONE);
		tvMessage.setText("");
	}

	protected void dismissHUD(int time) {

		final int t = time < 1000 ? 1000 : time;
		AsyncTask<String, Integer, Long> task = new AsyncTask<String, Integer, Long>() {

			@Override
			protected Long doInBackground(String... params) {
				SystemClock.sleep(t);
				return null;
			}

			@Override
			protected void onPostExecute(Long result) {
				super.onPostExecute(result);
				try {
					dismiss();
				} catch (Exception e) {
					e.printStackTrace();
				}
				reset();
			}
		};
		task.execute();
	}

	protected void dismissHUD() {
		dismissHUD(1000);
	}
}
