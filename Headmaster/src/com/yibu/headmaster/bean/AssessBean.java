package com.yibu.headmaster.bean;

import java.util.List;

public class AssessBean {

	public List<Commentlist> commentlist;

	public class Commentlist {
		public int commentstarlevel;
		public String commenttime;
		public String commentcontent;
		public String reservationid;
		public Subject subject;

		public Studentinfo studentinfo;

		public Coachinfo coachinfo;


	}

	public Commentcount commentcount;

	public class Commentcount {

		public int generalcomment;
		public int goodcommnent;
		public int badcomment;

	}

}