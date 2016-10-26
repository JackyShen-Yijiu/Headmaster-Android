package com.jzjf.headmaster.bean;

import java.io.Serializable;
/** 教练反馈 */
public class CoachFeedbackBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String createtime;
	public String replytime;
	public String replycontent;
	public int replyflag; // 1---已回复  0---未回复
	public String _id;
	public String content;
	public Replyid replyid;

	public class Replyid implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public String headportrait;
		public String name;
		public String _id;

	}

	public Coachid coachid;

	public class Coachid implements Serializable {

		public String name;
		public String _id;
		public Headportrait headportrait;
	}
}