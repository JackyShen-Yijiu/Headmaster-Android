package com.jzjf.common;

import java.io.File;

import android.os.Environment;

public class Config {

	// 友盟appkey
	public static final String UMENG_APPKEY = "56243b3d67e58eb1ae00419b";
	// 友盟appkey
	public static final String UMENG_CHANNELID = "ceshi";
	//
	public static final String IP = "http://101.200.204.240:8181/";
	// public static final String IP = "http://123.57.63.15:8181/";
	// 默认头像下载地址
	public static final String HEAD_URL = "http://7xnjg0.com1.z0.glb.clouddn.com/";
	// 上次登录用户的手机号
	public static final String LAST_CHANGE_PASSWORD_PHONE = "lastchangepasswordphone";
	// 上次登录用户的手机号
	public static final String LAST_LOGIN_PHONE = "lastloginphone";
	// 用户上次登录的(用户名)
	public static final String LAST_LOGIN_ACCOUNT = "lastloginaccount";
	// 用户上次登录的密码
	public static final String LAST_LOGIN_PASSWORD = "lastloginpassword";
	// 用户自动登录
	public static final String USER_AUTO_LOGIN = "userautologin";
	// 用户当前所在城市
	public static final String USER_CITY = "usercity";

	// 聊天对方的头像url字段
	public static final String CHAT_HEAD_RUL = "headUrl";
	// 聊天对方的昵称
	public static final String CHAT_NICK_NAME = "nickName";
	// 聊天对方的userid
	public static final String CHAT_USERID = "userId";
	// 聊天对方的类型
	public static final String CHAT_USERTYPE = "userType";
	// 聊天对方的名称（对方没有回复用）
	public static final String CHAT_NICK_NAME_NOANSWER = "nickNamenoanswer";
	// 聊天对方的头像（对方没有回复用）
	public static final String CHAT_HEAD_RUL_NOANSWER = "headUrlnoanswer";
	// 聊天对方的头像（对方没有回复用）
	public static final String CHAT_USERTYPE_NOANSWER = "userTypenoanswer";
	// 聊天对方的id（对方没有回复用）
	public static final String CHAT_USERID_NOANSWER = "userIdnoanswer";

	public static final String SYSTEM_PUSH = "systempush";

	public static String path = getSDPath() + File.separator + "BlackCat"
			+ File.separator;
	/**
	 * 用户头像保存的位置
	 */
	public static final String PICPATH = path + "picture";
	/**
	 * apk保存的位置
	 */
	public static final String APKPATH = path + "apk";

	public enum AppointmentResult {

		applying("1"),
		// 学员预约取消
		applycancel("2"),
		// 教练同意
		applyconfirm("3"),
		// 教练拒绝取消
		applyrefuse("4"),
		//
		unconfirmfinish("5"),
		//
		ucomments("6"),
		//
		finish("7");
		private String index;

		private AppointmentResult(String index) {
			this.index = index;
		}

		public String getValue() {
			return index;
		}

	}

	public enum EnrollResult {
		// 报考状态 0 未报名；1 报名中 ；2报名成功
		SUBJECT_NONE("0"), SUBJECT_ENROLLING("1"), SUBJECT_ENROLL_SUCCESS("2");
		private String index;

		private EnrollResult(String index) {
			this.index = index;
		}

		public String getValue() {
			return index;
		}
	}

	public enum SubjectStatu {
		SUBJECT_NONE("0"), SUBJECT_ONE("1"), SUBJECT_THREE("3"), SUBJECT_TWO(
				"2"), SUBJECT_FOUR("4");
		private String index;

		private SubjectStatu(String index) {
			this.index = index;
		}

		public String getValue() {
			return index;
		}
	}

	public enum UserType {
		USER(1), COACH(2);
		private int index;

		private UserType(int index) {
			this.index = index;
		}

		public int getValue() {
			return index;
		}
	}

	public enum WalletType {
		REGISTER("1"), INVATE("2"), BUY("3");
		private String index;

		private WalletType(String index) {
			this.index = index;
		}

		public String getValue() {
			return index;
		}
	}

	public enum PushType {
		userapplysuccess("userapplysuccess"), reservationsucess(
				"reservationsucess"), walletupdate("walletupdate"), newversion(
				"newversion"), reservationcancel("reservationcancel"), reservationcoachcomment(
				"reservationcoachcomment"), systemmsg("systemmsg");
		private String index;

		private PushType(String index) {
			this.index = index;
		}

		public String getValue() {
			return index;
		}
	}

	public static String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取根目录
			return sdDir.toString();
		}
		return null;
	}
}
