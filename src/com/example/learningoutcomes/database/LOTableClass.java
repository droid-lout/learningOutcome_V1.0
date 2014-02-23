package com.example.learningoutcomes.database;

import android.database.sqlite.SQLiteDatabase;

public class LOTableClass {

	/* Common entries which will get used in multiple tables */
	public static final String TIMESTAMP = "timestamp";
	public static final String NAME = "name";
	public static final String EMAIL = "email";

	/* entries for the school table */

	public static final String TABLE_SCHOOL = "school";
	public static final String SCHOOL_ID = "school_id";
	public static final String SCHOOL_NAME = "school_name";

	public static final String[] ALL_COLUMNS_SCHOOL = { SCHOOL_ID, SCHOOL_NAME,
			TIMESTAMP };

	/* entries for the class table */
	public static final String TABLE_CLASS = "class";
	public static final String CLASS_ID = "class_id";
	public static final String CLASS_NAME = "class_name";
	public static final String SECTION_NAME = "section_name";
	public static final String ACADEMIC_YEAR = "academic year";

	public static final String[] ALL_COLUMNS_CLASS = { CLASS_ID, SCHOOL_ID,
			CLASS_NAME, SECTION_NAME, ACADEMIC_YEAR, TIMESTAMP };

	/* Entries for the subject table */
	public static final String TABLE_SUBJECT = "subject";
	public static final String SUBJECT_ID = "subject_id";
	public static final String SUBJECT_NAME = "subject_name";

	public static final String[] ALL_COLUMNS_SUBJECT = { SUBJECT_ID,
			SUBJECT_NAME, TIMESTAMP };

	/* entries for the Login Table */
	public static final String TABLE_LOGIN = "login";
	public static final String LOGIN_ID = "login_id";
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	public static final String ROLE = "role";

	public static final String[] ALL_COLUMNS_LOGIN = { LOGIN_ID, USERNAME,
			PASSWORD, ROLE, EMAIL, SCHOOL_ID, TIMESTAMP };

	/* Entries for the Teacher table */
	public static final String TABLE_TEACHER = "teacher";
	public static final String TEACHER_ID = "teacher_id";
	public static final String TEACHER_TERM = "teacher_term";

	public static final String[] ALL_COLUMNS_TEACHER = { TEACHER_ID, USERNAME,
			NAME, SUBJECT_ID, CLASS_ID, SCHOOL_ID, TEACHER_TERM, TIMESTAMP };

	/* Entries for the student table */
	public static final String TABLE_STUDENT = "student";
	public static final String STUDENT_ID = "student_id";

	public static final String[] ALL_COLUMNS_STUDENT = { STUDENT_ID, USERNAME,
			NAME, EMAIL, CLASS_ID, SCHOOL_ID, TIMESTAMP };

	/* Entries for the TestName Table */
	public static final String TABLE_TESTNAME = "testname";
	public static final String TESTNAME_ID = "testname_id";
	public static final String TESTNAME = "testname";

	public static final String[] ALL_COLUMNS_TESTNAME = { TESTNAME_ID,
			TESTNAME, SCHOOL_ID, TIMESTAMP };

	/* Entries for the test table */
	public static final String TABLE_TEST = "test";
	public static final String TEST_ID = "test_id";
	public static final String TEST_TYPE = "test_type";
	public static final String TASK_NUMBER = "task_number";
	public static final String TASK_NAME = "task_name";
	public static final String TASK_TOPIC = "task_topic";
	public static final String PROCEDURE = "procedure";
	public static final String COMMENTS = "comments";
	public static final String TOTAL_MARKS = "total_marks";
	public static final String WEIGHTAGE_FACTOR = "weightage_factor";

	public static final String[] ALL_COLUMNS_TEST = { TEST_ID, SUBJECT_ID,
			TEACHER_ID, TEACHER_TERM, TEST_TYPE, TESTNAME, TASK_NUMBER,
			TASK_NAME, TASK_TOPIC, PROCEDURE, COMMENTS, TOTAL_MARKS,
			WEIGHTAGE_FACTOR, TIMESTAMP };
	/* Entries for the question table */
	public static final String TABLE_QUES = "ques";
	public static final String QUES_ID = "ques_id";
	public static final String QUES_NUM = "ques_number";
	public static final String QUES_TEXT = "ques_text";
	public static final String QUES_MAX_SCORE = "ques_max_score";
	public static final String TOPIC_NAME_1 = "topic_name_1";
	public static final String TOPIC_NAME_2 = "topic_name_2";
	public static final String TOPIC_NAME_3 = "topic_name_3";
	public static final String TOPIC_NAME_4 = "topic_name_4";
	public static final String TOPIC_NAME_5 = "topic_name_5";
	public static final String SUBTOPIC_NAME_1 = "subtopic_name_1";
	public static final String SUBTOPIC_NAME_2 = "subtopic_name_2";
	public static final String SUBTOPIC_NAME_3 = "subtopic_name_3";
	public static final String SUBTOPIC_NAME_4 = "subtopic_name_4";
	public static final String SUBTOPIC_NAME_5 = "subtopic_name_5";
	public static final String PARAMETER_1 = "parameter_1";
	public static final String PARAMETER_2 = "parameter_2";
	public static final String PARAMETER_3 = "parameter_3";
	public static final String PARAMETER_4 = "parameter_4";
	public static final String PARAMETER_5 = "parameter_5";
	public static final String QUES_TYPE = "ques_type";
	public static final String DIFFICULTY_LEVEL = "difficulty_level";
	public static final String OPTIONAL = "optional";
	public static final String NEGATIVE_MARKS = "negative_marks";
	public static final String CORRECT_ANSWER = "correct_answer";

	public static final String[] ALL_COLUMNS_QUES = { QUES_ID, TEST_ID,
			QUES_NUM, QUES_TEXT, QUES_MAX_SCORE, TOPIC_NAME_1, TOPIC_NAME_2,
			TOPIC_NAME_3, TOPIC_NAME_4, TOPIC_NAME_5, SUBTOPIC_NAME_1,
			SUBTOPIC_NAME_2, SUBTOPIC_NAME_3, SUBTOPIC_NAME_4, SUBTOPIC_NAME_5,
			PARAMETER_1, PARAMETER_2, PARAMETER_3, PARAMETER_4, PARAMETER_5,
			QUES_TYPE, DIFFICULTY_LEVEL, OPTIONAL, CORRECT_ANSWER, TEACHER_ID,
			TIMESTAMP };
	/* Entries for the response table */
	public static final String TABLE_RESPONSE = "response";
	public static final String RESPONSE_ID = "response_id";
	public static final String RESPONSE = "response";
	public static final String COMMENT = "comment";
	public static final String STATUS = "status";

	public static final String[] ALL_COLUMNS_RESPONSE = { RESPONSE_ID,
			TEACHER_ID, STUDENT_ID, QUES_ID, RESPONSE, COMMENT, STATUS,
			TIMESTAMP };
	/* Entries for the parameter table */
	public static final String TABLE_PARAMETER = "parameter";
	public static final String PARAMETER_ID = "parameter_id";
	public static final String PARAMETER_NAME = "parameter_name";

	public static final String[] ALL_COLUMNS_PARAMETER = { PARAMETER_ID,
			PARAMETER_NAME, TASK_NAME, TIMESTAMP };

	/* Entries for the group table */
	public static final String TABLE_GROUP = "groups";
	public static final String GROUP_ID = "group_id";
	public static final String GROUP_NAME = "group_name";
	public static final String[] ALL_COLUMNS_GROUP = { GROUP_ID, STUDENT_ID,
			GROUP_NAME, TEST_ID, TIMESTAMP };

	/* Entries for the Topic name table */
	public static final String TABLE_TOPIC = "topic";
	public static final String TOPIC_ID = "topic_id";
	public static final String TOPIC_NAME = "topic_name";
	public static final String[] ALL_COLUMNS_TOPIC = { TOPIC_ID, TOPIC_NAME,
			SUBJECT_ID, TIMESTAMP };

	/* Entries for the Sub topic name table */
	public static final String TABLE_SUBTOPIC = "subtopic";
	public static final String SUBTOPIC_ID = "subtopic_id";
	public static final String SUBTOPIC_NAME = "subtopic_name";

	public static final String[] ALL_COLUMNS_SUBTOPIC = { SUBTOPIC_ID,
			TOPIC_ID, SUBTOPIC_NAME, SUBJECT_ID, TIMESTAMP };;
	/* Strings to be used for creating tables in the database */

	/** < School Table */
	public static final String CREATE_TABLE_SCHOOL = "create table "
			+ TABLE_SCHOOL + "(" + SCHOOL_ID + " integer primary key, "
			+ SCHOOL_NAME + " text not null, " + TIMESTAMP + " text not null"
			+ ");";

	/** < Class Table */
	public static final String CREATE_TABLE_CLASS = "create table "
			+ TABLE_CLASS + "(" + CLASS_ID + " integer primary key, "
			+ SCHOOL_ID + " integer not null, " + CLASS_NAME
			+ " text not null, " + SECTION_NAME + " text not null, "
			+ ACADEMIC_YEAR + " text not null, " + TIMESTAMP + " text not null"
			+ ");";

	/** < Subject table */
	public static final String CREATE_TABLE_SUBJECT = "create table "
			+ TABLE_SUBJECT + "(" + SUBJECT_ID + " integer primary key, "
			+ SUBJECT_NAME + " text not null, " + TIMESTAMP + " text not null"
			+ ");";

	/** < Login table */
	public static final String CREATE_TABLE_LOGIN = "create table "
			+ TABLE_LOGIN + "(" + LOGIN_ID + " integer primary key, "
			+ USERNAME + " text not null, " + PASSWORD + " text not null, "
			+ ROLE + " text not null, " + EMAIL + " text not null, "
			+ SCHOOL_ID + " integer not null, " + TIMESTAMP + " text not null"
			+ ");";

	/** < Teacher table */
	public static final String CREATE_TABLE_TEACHER = "create table "
			+ TABLE_TEACHER + "(" + TEACHER_ID + " integer primary key, "
			+ USERNAME + " text not null, " + NAME + " text not null, "
			+ SUBJECT_ID + " integer not null, " + CLASS_ID
			+ " integer not null, " + SCHOOL_ID + " integer not null, "
			+ TEACHER_TERM + " text not null, " + TIMESTAMP + " text not null"
			+ ");";

	/** < Student Table */
	public static final String CREATE_TABLE_STUDENT = "create table "
			+ TABLE_STUDENT + "(" + STUDENT_ID + " integer primary key, "
			+ USERNAME + " text not null, " + NAME + " text not null, " + EMAIL
			+ " text not null ," + CLASS_ID + " integer not null, " + SCHOOL_ID
			+ " integer not null, " + TIMESTAMP + "text not null" + "" + ");";

	/** < TESTNAME Table */
	public static final String CREATE_TABLE_TESTNAME = "create table "
			+ TABLE_TESTNAME + "(" + TESTNAME_ID + " text primary key, "
			+ TESTNAME + " text not null, " + SCHOOL_ID + " integer not null, "
			+ TIMESTAMP + " text not null" + ");";

	/** < Test Table */
	public static final String CREATE_TABLE_TEST = "create table " + TABLE_TEST
			+ "(" + TEST_ID + " text primary key, " + SUBJECT_ID
			+ " integer not null, " + CLASS_ID + " integer not null, "
			+ TEACHER_TERM + " text not null, " + TEST_TYPE
			+ " text not null, " + TESTNAME + " text not null, " + TASK_NUMBER
			+ " integer not null, " + TASK_NAME + " text not null, "
			+ TASK_TOPIC + " text not null, " + PROCEDURE + " text, " + COMMENT
			+ " integer, " + TOTAL_MARKS
			+ " integer not null, group_task integer not null, "
			+ WEIGHTAGE_FACTOR + " integer not null, username text not null, "
			+ TIMESTAMP + "text not null" + ");";

	/** < Ques Table */
	public static final String CREATE_TABLE_QUES = "create table " + TABLE_QUES
			+ "(" + QUES_ID + " text primary key, " + TEST_ID
			+ " text not null, " + QUES_NUM + " integer not null, " + QUES_TEXT
			+ " text, " + QUES_MAX_SCORE + " integer not null, " + TOPIC_NAME_1
			+ " text, " + TOPIC_NAME_2 + " text, " + TOPIC_NAME_3 + " text, "
			+ TOPIC_NAME_4 + " text, " + TOPIC_NAME_5 + " text, "
			+ SUBTOPIC_NAME_1 + " text, " + SUBTOPIC_NAME_2 + " text, "
			+ SUBTOPIC_NAME_3 + " text, " + SUBTOPIC_NAME_4 + " text, "
			+ SUBTOPIC_NAME_5 + " text, " + PARAMETER_1 + " text, "
			+ PARAMETER_2 + " text, " + PARAMETER_3 + " text, " + PARAMETER_4
			+ " text, " + PARAMETER_5 + " text, " + QUES_TYPE + " text, "
			+ DIFFICULTY_LEVEL + " text, " + OPTIONAL + " integer not null, "
			+ NEGATIVE_MARKS + " integer not null, " + CORRECT_ANSWER
			+ " text, " + USERNAME + " text not null, " + TIMESTAMP
			+ "text not null" + ");";

	/** < Response Table */
	public static final String CREATE_TABLE_RESPONSE = "create table "
			+ TABLE_RESPONSE + "(" + RESPONSE_ID + " text primary key, "
			+ USERNAME + " text not null, " + STUDENT_ID
			+ " integer not null, " + QUES_ID + " text not null, " + RESPONSE
			+ " integer not null, " + COMMENT + " text, " + STATUS
			+ " text not null, " + TIMESTAMP + " text not null" + ");";

	/** < Parameter table */
	public static final String CREATE_TABLE_PARAMETER = "create table "
			+ TABLE_PARAMETER + "(" + PARAMETER_ID + " text primary key, "
			+ TASK_NAME + " text not null, " + PARAMETER_NAME
			+ " text not null," + TIMESTAMP + " text not null" + ");";

	/** < Group Table */
	public static final String CREATE_TABLE_GROUP = "create table "
			+ TABLE_GROUP + "(" + GROUP_ID + " text primary key, " + STUDENT_ID
			+ " integer not null, " + GROUP_NAME + " text not null, " + TEST_ID
			+ " integer not null, " + TIMESTAMP + " text not null" + ");";

	/** < Topic Table */
	public static final String CREATE_TABLE_TOPIC = "create table "
			+ TABLE_TOPIC + "(" + TOPIC_ID + " text primary key, " + TOPIC_NAME
			+ " text not null, " + SUBJECT_ID + " integer not null, "
			+ TIMESTAMP + " text not null" + ");";

	/** < Subtopic Table */
	public static final String CREATE_TABLE_SUBTOPIC = "create table "
			+ TABLE_SUBTOPIC + "(" + SUBTOPIC_ID + " text primary key, "
			+ TOPIC_NAME + " text not null, " + SUBTOPIC_NAME
			+ " text not null," + TIMESTAMP + "text not null" + ");";

	public static final String Insert12 = "INSERT INTO " + TABLE_TOPIC
			+ " VALUES ('1', 'Debate', '6','TimeStamp');";
	public static final String Insert13 = "INSERT INTO " + TABLE_SUBTOPIC
			+ " VALUES ('1', 'Debate', 'Elections','TimeStamp');";
	public static final String Insert14 = "INSERT INTO " + TABLE_SUBTOPIC
			+ " VALUES ('2', 'Debate', 'Elections 1','TimeStamp');";
	public static final String Insert15 = "INSERT INTO " + TABLE_SUBTOPIC
			+ " VALUES ('3', 'Debate', 'Elections 2','TimeStamp');";
	public static final String Insert16 = "INSERT INTO " + TABLE_SUBTOPIC
			+ " VALUES ('4', 'Debate', 'Elections 3','TimeStamp');";
	public static final String Insert17 = "INSERT INTO " + TABLE_SUBTOPIC
			+ " VALUES ('5', 'Debate', 'Elections 4','TimeStamp');";

	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(CREATE_TABLE_SCHOOL);
		database.execSQL(CREATE_TABLE_CLASS);
		database.execSQL(CREATE_TABLE_SUBJECT);
		database.execSQL(CREATE_TABLE_LOGIN);
		database.execSQL(CREATE_TABLE_TEACHER);
		database.execSQL(CREATE_TABLE_STUDENT);
		database.execSQL(CREATE_TABLE_TESTNAME);
		database.execSQL(CREATE_TABLE_TEST);
		database.execSQL(CREATE_TABLE_QUES);
		database.execSQL(CREATE_TABLE_RESPONSE);
		database.execSQL(CREATE_TABLE_PARAMETER);
		database.execSQL(CREATE_TABLE_GROUP);
		database.execSQL(CREATE_TABLE_TOPIC);
		database.execSQL(CREATE_TABLE_SUBTOPIC);

		dummyLogin(database);
		dummySchool(database);
		dummyTeacher(database);
		dummyClass(database);
		dummySubject(database);
		dummyTestName(database);
		dummyTest(database);
		dummyParamter(database);

		database.execSQL(Insert12);
		database.execSQL(Insert13);
		database.execSQL(Insert14);
		database.execSQL(Insert15);
		database.execSQL(Insert16);
		database.execSQL(Insert17);

	}

	private static void dummyLogin(SQLiteDatabase database) {
		String InsertLoginA = "INSERT INTO "
				+ TABLE_LOGIN
				+ " VALUES (1, 'harman', 'sohanpal','admin' , 'harman@gmail.com', '1' , 'TimeStamp');";
		String InsertLoginB = "INSERT INTO "
				+ TABLE_LOGIN
				+ " VALUES (4, 'nitish', 'bazinga','Root' , 'nitishmehrotra@gmail.com', '2' , 'TimeStamp');";

		database.execSQL(InsertLoginA);
		database.execSQL(InsertLoginB);
	}

	private static void dummySchool(SQLiteDatabase database) {
		String InsertSchoolAlpha = "INSERT INTO " + TABLE_SCHOOL
				+ " VALUES (1, 'Ajit Karam Singh', 'TimeStamp');";
		String InsertSchoolBravo = "INSERT INTO " + TABLE_SCHOOL
				+ " VALUES (2, 'St. Johns High School', 'TimeStamp');";
		database.execSQL(InsertSchoolAlpha);
		database.execSQL(InsertSchoolBravo);
	}

	private static void dummyTeacher(SQLiteDatabase database) {
		String InsertTeacherA = "INSERT INTO "
				+ TABLE_TEACHER
				+ " VALUES (1, 'nitish', 'Nitish Mehrotra', 1, 1, 2, 'Term 1', 'TimeStamp' )";
		String InsertTeacherF = "INSERT INTO "
				+ TABLE_TEACHER
				+ " VALUES (6, 'nitish', 'Nitish Mehrotra', 5, 1, 2, 'Term 1', 'TimeStamp' )";
		String InsertTeacherC = "INSERT INTO "
				+ TABLE_TEACHER
				+ " VALUES (3, 'nitish', 'Nitish Mehrotra', 6, 2, 2, 'Term 2', 'TimeStamp' )";
		String InsertTeacherE = "INSERT INTO "
				+ TABLE_TEACHER
				+ " VALUES (5, 'nitish', 'Nitish Mehrotra', 4, 3, 2, 'Term 2', 'TimeStamp' )";
		String InsertTeacherD = "INSERT INTO "
				+ TABLE_TEACHER
				+ " VALUES (4, 'harman', 'Harman Sohanpal', 2, 1, 1, 'Term 2', 'TimeStamp' )";
		String InsertTeacherB = "INSERT INTO "
				+ TABLE_TEACHER
				+ " VALUES (2, 'harman', 'Harman Sohanpal', 2, 4, 1, 'Term 2', 'TimeStamp' )";
		String InsertTeacherG = "INSERT INTO "
				+ TABLE_TEACHER
				+ " VALUES (7, 'harman', 'Harman Sohanpal', 1, 5, 1, 'Term 2', 'TimeStamp' )";
		String InsertTeacherH = "INSERT INTO "
				+ TABLE_TEACHER
				+ " VALUES (8, 'harman', 'Harman Sohanpal', 8, 5, 1, 'Term 1', 'TimeStamp' )";
		String InsertTeacherI = "INSERT INTO "
				+ TABLE_TEACHER
				+ " VALUES (9, 'harman', 'Harman Sohanpal', 6, 2, 1, 'Term 2', 'TimeStamp' )";
		String InsertTeacherJ = "INSERT INTO "
				+ TABLE_TEACHER
				+ " VALUES (10, 'harman', 'Harman Sohanpal', 10, 6, 1, 'Term 1', 'TimeStamp' )";
		String InsertTeacherK = "INSERT INTO "
				+ TABLE_TEACHER
				+ " VALUES (11, 'nitish', 'Nitish Mehrotra', 1, 1, 2, 'Term 2', 'TimeStamp' )";
		String InsertTeacherL = "INSERT INTO "
				+ TABLE_TEACHER
				+ " VALUES (12, 'nitish', 'Nitish Mehrotra', 5, 2, 2, 'Term 2', 'TimeStamp' )";
		String InsertTeacherM = "INSERT INTO "
				+ TABLE_TEACHER
				+ " VALUES (13, 'nitish', 'Nitish Mehrotra', 8, 6, 2, 'Term 1', 'TimeStamp' )";
		

		database.execSQL(InsertTeacherA);
		database.execSQL(InsertTeacherB);
		database.execSQL(InsertTeacherC);
		database.execSQL(InsertTeacherD);
		database.execSQL(InsertTeacherE);
		database.execSQL(InsertTeacherF);
		database.execSQL(InsertTeacherG);
		database.execSQL(InsertTeacherH);
		database.execSQL(InsertTeacherI);
		database.execSQL(InsertTeacherJ);
		database.execSQL(InsertTeacherK);
		database.execSQL(InsertTeacherL);
		database.execSQL(InsertTeacherM);

	}

	private static void dummyClass(SQLiteDatabase database) {
		/* Inserting dummy student data */

		String Student = "INSERT INTO "
				+ TABLE_STUDENT
				+ " VALUES (4, 'Harman1', 'Harman1', 'harman@gmail.com', 1, 1, 'TimeStamp')";

		String Student1 = "INSERT INTO "
				+ TABLE_STUDENT
				+ " VALUES (5, 'Nitish1', 'Nitish1', 'harman@gmail.com', 1, 1, 'TimeStamp')";
		String Student2 = "INSERT INTO "
				+ TABLE_STUDENT
				+ " VALUES (6, 'Ravia1', 'Ravia1', 'harman@gmail.com', 1, 1, 'TimeStamp')";

		database.execSQL(Student);
		database.execSQL(Student1);
		database.execSQL(Student2);

		Student = "INSERT INTO "
				+ TABLE_STUDENT
				+ " VALUES (7, 'Harman2', 'Harman2', 'harman@gmail.com', 2, 1, 'TimeStamp')";

		Student1 = "INSERT INTO "
				+ TABLE_STUDENT
				+ " VALUES (8, 'Nitish2', 'Nitish2', 'harman@gmail.com', 2, 1, 'TimeStamp')";
		Student2 = "INSERT INTO "
				+ TABLE_STUDENT
				+ " VALUES (9, 'Ravia2', 'Ravia2', 'harman@gmail.com', 2, 1, 'TimeStamp')";

		database.execSQL(Student);
		database.execSQL(Student1);
		database.execSQL(Student2);

		Student = "INSERT INTO "
				+ TABLE_STUDENT
				+ " VALUES (10, 'Harman3', 'Harman3', 'harman@gmail.com', 3, 1, 'TimeStamp')";

		Student1 = "INSERT INTO "
				+ TABLE_STUDENT
				+ " VALUES (11, 'Nitish3', 'Nitish3', 'harman@gmail.com', 3, 1, 'TimeStamp')";
		Student2 = "INSERT INTO "
				+ TABLE_STUDENT
				+ " VALUES (12, 'Ravia3', 'Ravia3', 'harman@gmail.com', 3, 1, 'TimeStamp')";

		database.execSQL(Student);
		database.execSQL(Student1);
		database.execSQL(Student2);

		Student = "INSERT INTO "
				+ TABLE_STUDENT
				+ " VALUES (13, 'Harman4', 'Harman4', 'harman@gmail.com', 4, 1, 'TimeStamp')";

		Student1 = "INSERT INTO "
				+ TABLE_STUDENT
				+ " VALUES (14, 'Nitish4', 'Nitish4', 'harman@gmail.com', 4, 1, 'TimeStamp')";
		Student2 = "INSERT INTO "
				+ TABLE_STUDENT
				+ " VALUES (15, 'Ravia4', 'Ravia4', 'harman@gmail.com', 4, 1, 'TimeStamp')";

		database.execSQL(Student);
		database.execSQL(Student1);
		database.execSQL(Student2);

		Student = "INSERT INTO "
				+ TABLE_STUDENT
				+ " VALUES (16, 'Harman5', 'Harman5', 'harman@gmail.com', 5, 1, 'TimeStamp')";

		Student1 = "INSERT INTO "
				+ TABLE_STUDENT
				+ " VALUES (17, 'Nitish5', 'Nitish5', 'harman@gmail.com', 5, 1, 'TimeStamp')";
		Student2 = "INSERT INTO "
				+ TABLE_STUDENT
				+ " VALUES (18, 'Ravia5', 'Ravia5', 'harman@gmail.com', 5, 1, 'TimeStamp')";

		database.execSQL(Student);
		database.execSQL(Student1);
		database.execSQL(Student2);

		Student = "INSERT INTO "
				+ TABLE_STUDENT
				+ " VALUES (19, 'Harman6', 'Harman6', 'harman@gmail.com', 6, 1, 'TimeStamp')";

		Student1 = "INSERT INTO "
				+ TABLE_STUDENT
				+ " VALUES (20, 'Nitish6', 'Nitish6', 'harman@gmail.com', 6, 1, 'TimeStamp')";
		Student2 = "INSERT INTO "
				+ TABLE_STUDENT
				+ " VALUES (21, 'Ravia6', 'Ravia6', 'harman@gmail.com', 6, 1, 'TimeStamp')";

		database.execSQL(Student);
		database.execSQL(Student1);
		database.execSQL(Student2);
		String InsertClassAlpha = "INSERT INTO " + TABLE_CLASS
				+ " VALUES (1, 2, '8', 'C', '2013', 'TimeStamp')";
		String InsertClassBravo = "INSERT INTO " + TABLE_CLASS
				+ " VALUES (2, 2, '9', 'B', '2014', 'TimeStamp')";
		String InsertClassCharlie = "INSERT INTO " + TABLE_CLASS
				+ " VALUES (3, 2, '10', 'B', '2014', 'TimeStamp')";
		String InsertClassDelta = "INSERT INTO " + TABLE_CLASS
				+ " VALUES (4, 1, '11', 'D', '2012', 'TimeStamp')";
		String InsertClassEcho = "INSERT INTO " + TABLE_CLASS
				+ " VALUES (5, 1, '12', 'A', '2015', 'TimeStamp')";
		String InsertClassFoxtrot = "INSERT INTO " + TABLE_CLASS
				+ " VALUES (6, 1, '7', 'E', '2012', 'TimeStamp')";

		database.execSQL(InsertClassAlpha);
		database.execSQL(InsertClassBravo);
		database.execSQL(InsertClassCharlie);
		database.execSQL(InsertClassDelta);
		database.execSQL(InsertClassEcho);
		database.execSQL(InsertClassFoxtrot);
	}

	private static void dummySubject(SQLiteDatabase database) {
		String InsertSubjectA = "INSERT INTO " + TABLE_SUBJECT
				+ " VALUES (1, 'English', 'TimeStamp')";
		String InsertSubjectB = "INSERT INTO " + TABLE_SUBJECT
				+ " VALUES (2, 'Social', 'TimeStamp')";
		String InsertSubjectC = "INSERT INTO " + TABLE_SUBJECT
				+ " VALUES (3, 'Hindi', 'TimeStamp')";
		String InsertSubjectD = "INSERT INTO " + TABLE_SUBJECT
				+ " VALUES (4, 'Punjabi', 'TimeStamp')";
		String InsertSubjectE = "INSERT INTO " + TABLE_SUBJECT
				+ " VALUES (5, 'Kannadha', 'TimeStamp')";
		String InsertSubjectF = "INSERT INTO " + TABLE_SUBJECT
				+ " VALUES (6, 'Telgu', 'TimeStamp')";
		String InsertSubjectG = "INSERT INTO " + TABLE_SUBJECT
				+ " VALUES (7, 'Keral', 'TimeStamp')";
		String InsertSubjectH = "INSERT INTO " + TABLE_SUBJECT
				+ " VALUES (8, 'Marathi', 'TimeStamp')";
		String InsertSubjectI = "INSERT INTO " + TABLE_SUBJECT
				+ " VALUES (9, 'Tamil', 'TimeStamp')";
		String InsertSubjectJ = "INSERT INTO " + TABLE_SUBJECT
				+ " VALUES (10, 'Haryanvi', 'TimeStamp')";
		String InsertSubjectK = "INSERT INTO " + TABLE_SUBJECT
				+ " VALUES (11, 'Bhojpuri', 'TimeStamp')";
		String InsertSubjectL = "INSERT INTO " + TABLE_SUBJECT
				+ " VALUES (12, 'Bihari', 'TimeStamp')";

		database.execSQL(InsertSubjectA);
		database.execSQL(InsertSubjectB);
		database.execSQL(InsertSubjectC);
		database.execSQL(InsertSubjectD);
		database.execSQL(InsertSubjectE);
		database.execSQL(InsertSubjectF);
		database.execSQL(InsertSubjectG);
		database.execSQL(InsertSubjectH);
		database.execSQL(InsertSubjectI);
		database.execSQL(InsertSubjectJ);
		database.execSQL(InsertSubjectK);
		database.execSQL(InsertSubjectL);
	}

	private static void dummyTestName(SQLiteDatabase database) {

		String Insert1 = "INSERT INTO " + TABLE_TESTNAME
				+ " VALUES ('1', 'UnitTest 1', 1, 'TimeStamp');";
		String Insert2 = "INSERT INTO " + TABLE_TESTNAME
				+ " VALUES ('2', 'UnitTest 2', 1, 'TimeStamp');";
		String Insert3 = "INSERT INTO " + TABLE_TESTNAME
				+ " VALUES ('3', 'FA 1', 1, 'TimeStamp');";
		String Insert4 = "INSERT INTO " + TABLE_TESTNAME
				+ " VALUES ('4', 'FA2 1', 2, 'TimeStamp');";
		String Insert5 = "INSERT INTO " + TABLE_TESTNAME
				+ " VALUES ('5', 'UnitTest 3', 2, 'TimeStamp');";

		database.execSQL(Insert1);
		database.execSQL(Insert2);
		database.execSQL(Insert3);
		database.execSQL(Insert4);
		database.execSQL(Insert5);
	}

	private static void dummyTest(SQLiteDatabase database) {
		// String Insert6 = "INSERT INTO "
		// + TABLE_TEST
		// +
		// " VALUES ('1', 1, 1, 'Term 1', 'Formative', 'UnitTest 1', 1, 'Debate','My name', NULL, 0, 100, 0, 10, 'TIMESTAMP');";
		// String Insert7 = "INSERT INTO "
		// + TABLE_TEST
		// +
		// " VALUES ('2', 2, 2, 'Term 1', 'Formative', 'UnitTest 2', 1, 'Declamation', 'My name', NULL, 0, 100, 0, 10, 'TIMESTAMP');";
		// String Insert8 = "INSERT INTO "
		// + TABLE_TEST
		// +
		// " VALUES ('3', 3, 3, 'Term 1', 'Formative', 'UnitTest 3', 1, 'Quiz','My name', NULL, 0, 100, 0, 10, 'TIMESTAMP');";
		// String Insert9 = "INSERT INTO "
		// + TABLE_TEST
		// +
		// " VALUES ('4', 4, 4, 'Term 1', 'Formative', 'UnitTest 4', 2, 'HEllo','My name', NULL, 0, 100, 0, 10, 'TIMESTAMP');";
		// String Insert10 = "INSERT INTO "
		// + TABLE_TEST
		// +
		// " VALUES ('5', 5, 5, 'Term 1', 'Formative', 'UnitTest 5', 3, 'HI', 'My name', NULL, 0, 100, 0, 10, 'TIMESTAMP');";
		// String Insert11 = "INSERT INTO "
		// + TABLE_TEST
		// +
		// " VALUES ('6', 6, 6, 'Term 1', 'Formative', 'UnitTest 6', 4, 'Bye', 'My name', NULL, 0, 100, 0, 10, 'TIMESTAMP');";
		//
		// database.execSQL(Insert6);
		// database.execSQL(Insert7);
		// database.execSQL(Insert8);
		// database.execSQL(Insert9);
		// database.execSQL(Insert10);
		// database.execSQL(Insert11);
		return;
	}

	private static void dummyParamter(SQLiteDatabase database) {

		String Insert1 = "INSERT INTO " + TABLE_PARAMETER
				+ " VALUES ('1', 'Debate', 'Confidence', 'TimeStamp');";
		String Insert2 = "INSERT INTO " + TABLE_PARAMETER
				+ " VALUES ('2', 'Debate', 'Attitude', 'TimeStamp');";
		String Insert3 = "INSERT INTO " + TABLE_PARAMETER
				+ " VALUES ('3', 'Debate', 'Performance' ,'TimeStamp');";
		String Insert4 = "INSERT INTO " + TABLE_PARAMETER
				+ " VALUES ('4', 'Debate', 'test', 'TimeStamp');";
		String Insert5 = "INSERT INTO " + TABLE_PARAMETER
				+ " VALUES ('5', 'Debate', 'test1', 'TimeStamp');";
		String Insert6 = "INSERT INTO " + TABLE_PARAMETER
				+ " VALUES ('6', 'Debate', 'test2', 'TimeStamp');";
		String Insert7 = "INSERT INTO " + TABLE_PARAMETER
				+ " VALUES ('7', 'Debate', 'test 4', 'TimeStamp');";
		String Insert8 = "INSERT INTO " + TABLE_PARAMETER
				+ " VALUES ('8', 'Debate', 'test 5', 'TimeStamp');";
		String Insert9 = "INSERT INTO " + TABLE_PARAMETER
				+ " VALUES ('9', 'Debate', 'test 6', 'TimeStamp');";
		String Insert10 = "INSERT INTO " + TABLE_PARAMETER
				+ " VALUES ('10', 'Debate', 'test 7', 'TimeStamp');";
		String Insert11 = "INSERT INTO " + TABLE_PARAMETER
				+ " VALUES ('11', 'Debate', 'test 8', 'TimeStamp');";
		String Insert12 = "INSERT INTO " + TABLE_PARAMETER
				+ " VALUES ('12', 'Debate', 'test 9', 'TimeStamp');";

		database.execSQL(Insert1);
		database.execSQL(Insert2);
		database.execSQL(Insert3);
		database.execSQL(Insert4);
		database.execSQL(Insert5);
		database.execSQL(Insert6);
		database.execSQL(Insert7);
		database.execSQL(Insert8);
		database.execSQL(Insert9);
		database.execSQL(Insert10);
		database.execSQL(Insert11);
		database.execSQL(Insert12);

	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHOOL);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_CLASS);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBJECT);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_TEACHER);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_TESTNAME);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_TEST);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_QUES);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_RESPONSE);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_PARAMETER);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUP);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_TOPIC);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBTOPIC);
		onCreate(database);
	}
}
