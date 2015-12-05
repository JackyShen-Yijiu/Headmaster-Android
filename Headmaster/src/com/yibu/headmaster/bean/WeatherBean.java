package com.yibu.headmaster.bean;

public class WeatherBean {

	public String time;
	public int ret_code;
	public Now now;
	public CityInfo cityInfo;
	public F1 f1;
	public F2 f2;
	public F3 f3;

	public class Now {

		public String sd;
		public String temperature_time;
		public int aqi;
		public String temperature;
		public String weather;
		public String wind_direction;
		public String weather_pic;
		public String weather_code;
		public String wind_power;
		public AqiDetail aqiDetail;

		public class AqiDetail {

			public String area;
			public int no2;
			public int o3;
			public String area_code;
			public int pm2_5;
			public int so2;
			public int aqi;
			public int pm10;
			public String primary_pollutant;
			public Double co;
			public int o3_8h;
			public String quality;
		}
	}

	public class CityInfo {

		public String c11;
		public String c10;
		public String c12;
		public String c15;
		public Double latitude;
		public String c17;
		public String c16;
		public String c1;
		public String c2;
		public String c3;
		public String c4;
		public String c5;
		public String c6;
		public String c7;
		public String c8;
		public String c9;
		public Double longitude;
	}

	public class F1 {

		public String jiangshui;
		public String air_press;
		public String night_wind_direction;
		public int weekday;
		public String night_air_temperature;
		public String night_weather_pic;
		public String day_weather_code;
		public String night_weather;
		public String night_weather_code;
		public String ziwaixian;
		public String day_weather;
		public String day_wind_power;
		public String day_air_temperature;
		public String day_weather_pic;
		public String day_wind_direction;
		public String night_wind_power;
		public String sun_begin_end;
		public String day;
	}

	public class F2 {

		public String night_wind_direction;
		public int weekday;
		public String night_air_temperature;
		public String night_weather_pic;
		public String day_weather_code;
		public String night_weather;
		public String night_weather_code;
		public String day_weather;
		public String day_wind_power;
		public String day_air_temperature;
		public String day_weather_pic;
		public String day_wind_direction;
		public String night_wind_power;
		public String sun_begin_end;
		public String day;
	}

	public class F3 {

		public String night_wind_direction;
		public int weekday;
		public String night_air_temperature;
		public String night_weather_pic;
		public String day_weather_code;
		public String night_weather;
		public String night_weather_code;
		public String day_weather;
		public String day_wind_power;
		public String day_air_temperature;
		public String day_weather_pic;
		public String day_wind_direction;
		public String night_wind_power;
		public String sun_begin_end;
		public String day;
	}
}
