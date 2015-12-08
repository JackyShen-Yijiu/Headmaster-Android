package com.yibu.headmaster;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.yibu.headmaster.adapter.MainPagerAdapter;
import com.yibu.headmaster.api.ApiHttpClient;
import com.yibu.headmaster.base.BasePager;
import com.yibu.headmaster.base.impl.ChatterPager;
import com.yibu.headmaster.base.impl.DataPager;
import com.yibu.headmaster.base.impl.NewsPager;
import com.yibu.headmaster.bean.WeatherBean;
import com.yibu.headmaster.global.HeadmasterApplication;
import com.yibu.headmaster.utils.JsonUtil;
import com.yibu.headmaster.utils.LogUtil;
import com.yibu.headmaster.utils.SharedPreferencesUtil;

public class MainActivity extends Activity implements OnClickListener {
	private DrawerLayout drawerLayout;
	ViewPager contentPager;
	RadioGroup contentBottom;
	LinearLayout layoutDrawer;
	ImageView ivLeftBack;
	ImageButton leftBtn;
	TextView title;
	LinearLayout weather;
	RadioButton raButton1;
	RadioButton raButton2;
	RadioButton raButton3;
	private TextView weatherDegree;
	private ImageView weatherIcon;

	String currCity = null;
	private ArrayList<BasePager> pagers;
	public static String WEATHER_TEMPERATURE = "weather_temperature";
	public static String WEATHER_PIC = "weather_pic";

	public LocationClient mLocationClient = null;
	public MyLocationListenner myListener = new MyLocationListenner();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();
		initListener();
		initData();

		initMyLocation();

	}

	protected void initView() {

		drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout_main);
		layoutDrawer = (LinearLayout) findViewById(R.id.layout_drawer);
		contentPager = (ViewPager) findViewById(R.id.vp_content_pagers);
		contentBottom = (RadioGroup) findViewById(R.id.rg_content_bottom);
		ivLeftBack = (ImageView) findViewById(R.id.iv_left_back);
		leftBtn = (ImageButton) findViewById(R.id.title_left_btn);
		title = (TextView) findViewById(R.id.base_title_tv);
		weather = (LinearLayout) findViewById(R.id.weather);
		weatherDegree = (TextView) findViewById(R.id.weather_degree);
		weatherIcon = (ImageView) findViewById(R.id.weather_icon);
		raButton1 = (RadioButton) findViewById(R.id.rb_bottom_data);
		raButton2 = (RadioButton) findViewById(R.id.rb_bottom_news);
		raButton3 = (RadioButton) findViewById(R.id.rb_bottom_chatter);
		raButton1.setBackgroundResource(R.drawable.bottom_1);
		contentPager.setOffscreenPageLimit(2);
	}

	protected void initListener() {
		ivLeftBack.setOnClickListener(this);
		leftBtn.setOnClickListener(this);
	}

	protected void initData() {

		String weather_temp = SharedPreferencesUtil.getString(
				HeadmasterApplication.getContext(), WEATHER_TEMPERATURE, "");
		String weather_pic = SharedPreferencesUtil.getString(
				HeadmasterApplication.getContext(), WEATHER_PIC, "");
		if (!TextUtils.isEmpty(weather_temp) && TextUtils.isEmpty(weather_pic)) {
			weatherDegree.setText(weather_temp + "°C");
			Picasso.with(getApplicationContext()).load(weather_pic)
					.into(weatherIcon);
			LogUtil.print("-----00+++" + weather_pic);
		}

		pagers = new ArrayList<BasePager>();

		pagers.add(new DataPager(getApplicationContext()));
		pagers.add(new NewsPager(getApplicationContext()));
		pagers.add(new ChatterPager(getApplicationContext()));
		// 设置数据适配器
		contentPager.setAdapter(new MainPagerAdapter(pagers));

		// 监听单选按钮
		contentBottom
				.setOnCheckedChangeListener(new BottomOnCheckedChangeListener());
	}

	class BottomOnCheckedChangeListener implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			setBottom();
			switch (checkedId) {
			case R.id.rb_bottom_data:
				contentPager.setCurrentItem(0, false);// 参数2 是否带滑动效果
				title.setText(getString(R.string.data_title));
				raButton1.setBackgroundResource(R.drawable.bottom_1);
				weather.setVisibility(View.VISIBLE);
				break;
			case R.id.rb_bottom_news:
				contentPager.setCurrentItem(1, false);
				title.setText(getString(R.string.industry_consult));
				raButton2.setBackgroundResource(R.drawable.bottom_2);
				weather.setVisibility(View.INVISIBLE);
				break;
			case R.id.rb_bottom_chatter:
				contentPager.setCurrentItem(2, false);
				title.setText(getString(R.string.my_chat_messages));
				raButton3.setBackgroundResource(R.drawable.bottom_3);
				weather.setVisibility(View.INVISIBLE);
				break;

			default:
				break;
			}
		}
	}

	private void setBottom() {
		raButton1.setBackgroundColor(getResources().getColor(
				android.R.color.transparent));
		raButton2.setBackgroundColor(getResources().getColor(
				android.R.color.transparent));
		raButton3.setBackgroundColor(getResources().getColor(
				android.R.color.transparent));

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			moveTaskToBack(true);// 相当于home键的效果

			// if(pressedTime!=0 &&
			// (System.currentTimeMillis()-pressedTime)<2000){
			// System.exit(0);
			// }else {
			// ToastUtil.showToast(this, "再按一次退出程序");
			// pressedTime = System.currentTimeMillis();
			// }
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left_btn:
			drawerLayout.openDrawer(Gravity.START);
			break;
		case R.id.iv_left_back:
			drawerLayout.closeDrawer(Gravity.START);
			break;
		}
	}

	private void initMyLocation() {
		mLocationClient = new LocationClient(this);// 百度定位对象
		mLocationClient.registerLocationListener(myListener);// 设置监听事件
		setLocationOption();

		mLocationClient.start();

	}

	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			// 定位成功
			currCity = location.getCity();
			LogUtil.print(location.getCity());
			if (mLocationClient != null) {
				mLocationClient.stop();// 结束定位

			}
			getWeatherInfo();
		}

	}

	@Override
	public void onDestroy() {
		if (mLocationClient != null) {
			mLocationClient.stop();// 结束定位

		}
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

	private void getWeatherInfo() {

		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				String weather_temp = null;
				String weather_pic = null;
				WeatherBean weatherBean = null;
				String value = parseJson(responseBody);
				if (!TextUtils.isEmpty(value)) {
					weatherBean = JsonUtil.parseJsonToBean(value,
							WeatherBean.class);
					LogUtil.print(weatherBean.now.weather_pic);

					if (weatherBean != null) {
						weather_temp = weatherBean.now.temperature;
						weather_pic = weatherBean.now.weather_pic;

						// 保存天气
						SharedPreferencesUtil.putString(
								HeadmasterApplication.getContext(),
								WEATHER_TEMPERATURE, weather_temp);
						SharedPreferencesUtil.putString(
								HeadmasterApplication.getContext(),
								WEATHER_PIC, weather_pic);
					}
				} else {

				}
				if (weather_pic != null && weather_temp != null) {
					weatherDegree.setText(weather_temp + "°C");
					Picasso.with(getApplicationContext()).load(weather_pic)
							.into(weatherIcon);

				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {

				String weather_temp = SharedPreferencesUtil.getString(
						HeadmasterApplication.getContext(),
						WEATHER_TEMPERATURE, "");
				String weather_pic = SharedPreferencesUtil.getString(
						HeadmasterApplication.getContext(), WEATHER_PIC, "");
				if (weather_pic != null && weather_temp != null) {
					weatherDegree.setText(weather_temp + "°C");
					Picasso.with(getApplicationContext()).load(weather_pic)
							.into(weatherIcon);

				}

			}
		};
		if (currCity != null) {
			ApiHttpClient.get("info/getweather?cityname=" + currCity, handler);
		}
	}

	String result = null;
	String msg = null;

	private String parseJson(byte[] responseBody) {
		String value = null;
		JSONObject dataObject = null;
		JSONArray dataArray = null;
		String dataString = null;
		try {

			JSONObject jsonObject = new JSONObject(new String(responseBody));
			// result = jsonObject.getString("type");
			msg = jsonObject.getString("msg");
			try {
				dataObject = jsonObject.getJSONObject("data");

			} catch (Exception e2) {
				try {
					dataArray = jsonObject.getJSONArray("data");
				} catch (Exception e3) {
					dataString = jsonObject.getString("data");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (dataObject != null) {
			value = dataObject.toString();
		} else if (dataArray != null) {
			value = dataArray.toString();

		} else if (dataString != null) {
			value = dataString;
		}
		return value;
	}
}
