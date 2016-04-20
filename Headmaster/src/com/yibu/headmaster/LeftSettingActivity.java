package com.yibu.headmaster;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;

import cn.sft.baseactivity.util.Util;

import com.easemob.chat.EMChatManager;
import com.google.gson.JsonObject;
import com.jzjf.headmaster.R;
import com.loopj.android.http.RequestParams;
import com.yibu.headmaster.api.ApiHttpClient;
import com.yibu.headmaster.bean.DataBean;
import com.yibu.headmaster.bean.UserBean;
import com.yibu.headmaster.cache.DataCleanManager;
import com.yibu.headmaster.global.HeadmasterApplication;
import com.yibu.headmaster.utils.JsonUtil;
import com.yibu.headmaster.utils.LogUtil;
import com.yibu.headmaster.utils.SharedPreferencesUtil;
import com.yibu.headmaster.utils.ToastUtil;
import com.yibu.headmaster.utils.ZProgressHUD;

public class LeftSettingActivity extends BaseActivity  {

	private RelativeLayout setting_aboutus;
	private RelativeLayout setting_callback;
	private RelativeLayout setting_rate;
	private View view;
	private CheckBox messageCb;
	private CheckBox commplaintCb;
	private CheckBox studentEnrollCb;

	public static String SETTING_MESSAGE = "setting_message";
	public static String SETTING_COMMPLAINT = "setting_commplaint";
	public static String SETTING_STUDENT_ENROLL = "setting_student_enroll";
	private int messageInt;
	private int complaintInt;
	private int studentEnrollInt;
	private RelativeLayout setting_cache;
	private TextView tv_number;
	private RelativeLayout setting_finish;
	private RelativeLayout setting_pingfen;
	private RelativeLayout setting_update;
	private TextView banben_code;
	private DataBean dataBean;
	private Util util;	
	private PackageInfo packageInfo;
	
	@Override
	protected void initView() {
		Util util=new Util(this);
		DataBean dataBean=new DataBean();
		view = View.inflate(getBaseContext(), R.layout.left_setting, null);
		content.addView(view);
		setSonsTitle(getString(R.string.setting_title));
		setting_aboutus = (RelativeLayout) view
				.findViewById(R.id.setting_aboutus_tv);
		setting_callback = (RelativeLayout) view
				.findViewById(R.id.setting_callback_tv);
		setting_cache=(RelativeLayout)view.findViewById(R.id.setting_cache);
		setting_update=(RelativeLayout)view.findViewById(R.id.setting_update);
		setting_finish=(RelativeLayout)view.findViewById(R.id.setting_finish);
		setting_pingfen=(RelativeLayout)view.findViewById(R.id.setting_pingfen);
		tv_number=(TextView)view.findViewById(R.id.tv_number);
		banben_code=(TextView)view.findViewById(R.id.banben_code);
		messageCb = (CheckBox) findViewById(R.id.setting_every_day_cb);
		commplaintCb = (CheckBox) findViewById(R.id.setting_commplaint_cb);
		studentEnrollCb = (CheckBox) findViewById(R.id.setting_new_student_enroll);
		banben_code.setText(util.getAppVersion());
		 try {
			tv_number.setText(DataCleanManager.getTotalCacheSize(LeftSettingActivity.this));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// 获取版本号
		private void update() {
			if(dataBean ==null){
				return;
			}
			String urlString=dataBean.downloadUrl;
			getVersionCode(this);
			//本地
			int curVersion = getVersionCode(this);;
			//服务器
			int newVersion = dataBean.innerversionCode;
			if (newVersion>curVersion) {
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setTitle("发现新版本");
					builder.setMessage(getString(R.string.app_name) + "有新版本啦！");
					builder.setPositiveButton("立即更新",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int whichButton) {
									if(url(dataBean.downloadUrl)){
										Uri uri = Uri.parse(dataBean.downloadUrl);       
										Intent it = new Intent(Intent.ACTION_VIEW, uri);
										startActivity(it); 
									}else{
										 ToastUtil.showToast(getBaseContext(), "下载地址失效！");
									}
									dialog.dismiss();
								}
							});
					builder.setNegativeButton("以后再说",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int whichButton) {
									
									dialog.dismiss();
								}
							});
					Dialog dialog = builder.create();
					dialog.show();
				} else {
					   ToastUtil.showToast(getBaseContext(), "已是最新版本！");
				}
			
		}
		
		public static int getVersionCode(Context context) {//获取版本号(内部识别号)  
			
		    try {  
		        PackageInfo pi=context.getPackageManager().getPackageInfo(context.getPackageName(), 0);  
		        return pi.versionCode;  
		    } catch (PackageManager.NameNotFoundException e) {  
		        e.printStackTrace();  
		        return 0;  
		    }  
		} 
	// 获取版本号
	private void obtainCode() {
		RequestParams params = new RequestParams();
//		params.put("type", 5);
		ApiHttpClient.getAbsolute("api/v1/appversion/5",params, handler);
		
		
	}
	private boolean url(String urlString) {
		if (urlString!=null) {
			if(urlString.startsWith("http://") ||urlString.startsWith("https://")){
				return true;
			}
		}
		return false;
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_base_arrow:
			finish();
			break;
		case R.id.setting_update:
			obtainCode();
			break;
		case R.id.setting_aboutus_tv:
			Intent abouts = new Intent(HeadmasterApplication.getContext(),
					AboutUsActivity.class);
			startActivity(abouts);
			break;
		case R.id.setting_callback_tv:
			Intent callback = new Intent(HeadmasterApplication.getContext(),
					CallBackActivity.class);
			startActivity(callback);
			break;
		case R.id.setting_finish:
			exit();
			break;
		case R.id.setting_pingfen:
			rateAppInMarket(this);
			break;
		case R.id.setting_cache:
			  try {
                  //查看缓存的大小
                  Log.e("YQY", DataCleanManager.getTotalCacheSize(LeftSettingActivity.this));
              } catch (Exception e) {
                  e.printStackTrace();
              }
              DataCleanManager.clearAllCache(LeftSettingActivity.this);
//              ToastHelper.getInstance(CarCoachApplication.getInstance()).toast(R.string.clean_ok);
              ToastUtil.showToast(getBaseContext(), "清除成功");
              try {
                  //清除后的操作
                  Log.e("YQY", DataCleanManager.getTotalCacheSize(LeftSettingActivity.this));
                  tv_number.setText(DataCleanManager.getTotalCacheSize(LeftSettingActivity.this));
              } catch (Exception e) {
                  e.printStackTrace();
              }
			break;
		default:
			break;
		}
	}
	public void exit() {
		ZProgressHUD.getInstance(this).setMessage("正在退出登录...");
		ZProgressHUD.getInstance(this).show();
		// 退出环信
		EMChatManager.getInstance().logout(null);

		ZProgressHUD.getInstance(LeftSettingActivity.this).dismiss();

		LeftSettingActivity.this.finish();
		Intent intent = new Intent(LeftSettingActivity.this,
				LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		LeftSettingActivity.this.startActivity(intent);
	}
	@Override
	protected void initListener() {

		setting_aboutus.setOnClickListener(this);
		setting_callback.setOnClickListener(this);
		setting_cache.setOnClickListener(this);
		setting_finish.setOnClickListener(this);
		setting_pingfen.setOnClickListener(this);
		setting_update.setOnClickListener(this);
		messageCb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				RequestParams params = new RequestParams();
				if (isChecked) {
					messageInt = 1;
				} else {
					messageInt = 0;
				}
				params.put("newmessagereminder", messageInt);
				params.put("complaintreminder", complaintInt);
				params.put("applyreminder", studentEnrollInt);
				params.put("userid", HeadmasterApplication.app.userInfo.userid);
				ApiHttpClient.post("userinfo/personalsetting", params, handler);
			}
		});
		commplaintCb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				RequestParams params = new RequestParams();
				if (isChecked) {
					complaintInt = 1;
				} else {
					complaintInt = 0;
				}
				params.put("newmessagereminder", messageInt);
				params.put("complaintreminder", complaintInt);
				params.put("applyreminder", studentEnrollInt);
				params.put("userid", HeadmasterApplication.app.userInfo.userid);
				ApiHttpClient.post("userinfo/personalsetting", params, handler);
			}
		});
		studentEnrollCb
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						RequestParams params = new RequestParams();
						if (isChecked) {
							studentEnrollInt = 1;
						} else {
							studentEnrollInt = 0;
						}
						params.put("newmessagereminder", messageInt);
						params.put("complaintreminder", complaintInt);
						params.put("applyreminder", studentEnrollInt);
						params.put("userid",
								HeadmasterApplication.app.userInfo.userid);
						ApiHttpClient.post("userinfo/personalsetting", params,
								handler);
					}
				});
	}

	
	@Override
	public void processSuccess(String data) {
		dataBean = JsonUtil.parseJsonToBean(data, DataBean.class);
		update();
		// 设置成功，保存到本地
		SharedPreferencesUtil.putInt(getBaseContext(), SETTING_MESSAGE,
				messageInt);
		SharedPreferencesUtil.putInt(getBaseContext(), SETTING_COMMPLAINT,
				complaintInt);
		SharedPreferencesUtil.putInt(getBaseContext(), SETTING_STUDENT_ENROLL,
				studentEnrollInt);
	}
	

	@Override
	public void processFailure() {
		ToastUtil.showToast(getBaseContext(), "网络异常");
	}

	@Override
	protected void initData() {
		messageInt = SharedPreferencesUtil.getInt(getBaseContext(),
				SETTING_MESSAGE, 1);
		complaintInt = SharedPreferencesUtil.getInt(getBaseContext(),
				SETTING_COMMPLAINT, 1);
		studentEnrollInt = SharedPreferencesUtil.getInt(getBaseContext(),
				SETTING_STUDENT_ENROLL, 1);

		if (messageInt == 1) {
			messageCb.setChecked(true);
		} else {
			messageCb.setChecked(false);
		}
		if (complaintInt == 1) {
			commplaintCb.setChecked(true);
		} else {
			commplaintCb.setChecked(false);
		}
		if (studentEnrollInt == 1) {
			studentEnrollCb.setChecked(true);
		} else {
			studentEnrollCb.setChecked(false);
		}
	}
	
	  /*
     * 给APP打分
     */
    public static void rateAppInMarket(Activity activity) {
        if (activity == null) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id="
                + activity.getPackageName()));
        try {
            activity.startActivity(intent);
            activity.overridePendingTransition(0, 0);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }
}
