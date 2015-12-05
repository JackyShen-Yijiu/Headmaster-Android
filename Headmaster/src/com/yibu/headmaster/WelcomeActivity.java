package com.yibu.headmaster;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

public class WelcomeActivity extends Activity {

	public LocationClient mLocationClient = null;
	public MyLocationListenner myListener = new MyLocationListenner();
	private TextView mTv;
	private Button bt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);
		mTv = (TextView) findViewById(R.id.shoumsg);
		bt = (Button) findViewById(R.id.startdw);

		mLocationClient = new LocationClient(this);// 百度定位对象
		mLocationClient.registerLocationListener(myListener);// 设置监听事件
		setLocationOption();// 设置定位参数

	}

	// 点击开始定位
	public void myclick(View view) {
		mLocationClient.start();// 开始定位
	}

	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			// 定位成功
			mTv.setText(location.getCity());// 显示地址
		}

	}

	@Override
	public void onDestroy() {
		mLocationClient.stop();// 结束定位
		super.onDestroy();
	}

	// 设置相关参数
	private void setLocationOption() {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 打开gps
		option.setServiceName("com.baidu.location.service_v2.9");
		// option.setPoiExtraInfo(true);
		option.setAddrType("all");
		option.setPriority(LocationClientOption.NetWorkFirst);
		option.setPriority(LocationClientOption.GpsFirst); // gps
		// option.setPoiNumber(10);
		option.disableCache(true);
		mLocationClient.setLocOption(option);
	}
}
