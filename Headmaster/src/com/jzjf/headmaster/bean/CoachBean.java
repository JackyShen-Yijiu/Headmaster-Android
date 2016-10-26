package com.jzjf.headmaster.bean;

import java.io.Serializable;



public class CoachBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Double latitude;
	public String name;
	public Boolean is_shuttle;
	public String coachid;
	public Double longitude;
	public Headportrait headportrait;
	public int starlevel;
	public int passrate;
	public String mobile;
	public Subject[] subject;
	public int learncount;
	public int passcount;
	public String carmodel;
	
	

	public Driveschoolinfo driveschoolinfo;

	public class Driveschoolinfo implements Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public String name;
		public String id;
	}

}
