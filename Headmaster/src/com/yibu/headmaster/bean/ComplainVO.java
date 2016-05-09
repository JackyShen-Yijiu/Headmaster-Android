package com.yibu.headmaster.bean;

import java.io.Serializable;

public class ComplainVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public CoachBean coachinfo;
	public String complaintDateTime;
	public String complaintcontent;
	public Complainthandinfo complainthandinfo;
	public int feedbacktype;
	public int feedbackusertype;
	public String complaintid;
	public String[] piclistr;
	public Studentinfo studentinfo;
	
	public class Complainthandinfo implements Serializable {

		public String handlemessage;
		public String operator;
		public int handlestate;
	}
}
