package com.yibu.headmaster.bean;


public class ComplaintBean {

	public String complaintcontent;
	public String reservationid;
	public String complainthandlemessage;
	public String complaintDateTime;
	public int complainthandlestate;
	public String complaintreason;
	public Subject subject;

	public class Subject {

		public String name;
		public int subjectid;
	}

	public Studentinfo studentinfo;

	public class Studentinfo {

		public String mobile;
		public String name;
		public String userid;
		public Headportrait headportrait;

		public class Headportrait {

			public String thumbnailpic;
			public String width;
			public String originalpic;
			public String height;
		}

		public Classtype classtype;

		public class Classtype {

			public int price;
			public String name;
			public String id;
		}
	}

	public Coachinfo coachinfo;

	public class Coachinfo {

		public String mobile;
		public String name;
		public String coachid;
		public Headportrait headportrait;

		public class Headportrait {

			public String thumbnailpic;
			public String width;
			public String originalpic;
			public String height;
		}
	}

}