package com.example.learningoutcomes.Formative;

import java.util.List;

public class Group {
	List<String> studentname;
	String groupName;

	Group(String groupName, List<String> studentName) {
		this.studentname = studentName;
		this.groupName = groupName;
	}

	List<String> getStudentNameList() {
		return studentname;
	}
}
