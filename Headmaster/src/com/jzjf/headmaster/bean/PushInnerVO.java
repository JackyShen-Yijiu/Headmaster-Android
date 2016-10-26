package com.jzjf.headmaster.bean;

import cn.sft.sqlhelper.DBVO;

public class PushInnerVO extends DBVO {

	private static final long serialVersionUID = 1L;

	private String userid;
	private String reservationid;
	private String type;
	private String msg_content;
	private String title;

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getReservationid() {
		return reservationid;
	}

	public void setReservationid(String reservationid) {
		this.reservationid = reservationid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMsg_content() {
		return msg_content;
	}

	public void setMsg_content(String msg_content) {
		this.msg_content = msg_content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
