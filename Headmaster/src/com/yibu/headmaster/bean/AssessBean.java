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

		public class Subject {
			public String name;
			public int subjectid;
		}

		public Studentinfo studentinfo;

		public class Studentinfo {

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

	public Commentcount commentcount;

	public class Commentcount {

		public int generalcomment;
		public int goodcommnent;
		public int badcomment;

	}

}