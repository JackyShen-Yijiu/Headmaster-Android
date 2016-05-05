package com.yibu.headmaster.bean;

public class ComplainVO {

	public CoachBean coachinfo;
	public String complaintDateTime;
	public String complaintcontent;
	public Complainthandinfo complainthandinfo;
	public int feedbacktype;
	public int feedbackusertype;
	public String complaintid;
	public String[] piclistr;
	public Studentinfo studentinfo;
	
	public class Complainthandinfo {

		public String handlemessage;
		public String operator;
		public int handlestate;
	}
}
