package com.example.learningoutcomes.Formative;


public class TaskListRow {
	private int taskNumber;
	private int taskMaxMarks;
	private String taskName;
	private String taskDate;
	private String taskTopicName;
	private boolean group;

	public TaskListRow(int taskNum, String taskName, String date,
			String taskTopicName, boolean group, int taskMaxMarks) {
		this.taskNumber = taskNum;
		this.taskName = taskName;
		this.taskDate = date;
		this.taskTopicName = taskTopicName;
		this.group = group;
		this.taskMaxMarks = taskMaxMarks;
	}
	
	public int getTaskNumber() {
		return taskNumber;
	}
	
	public void setTaskNumber(int taskNumber) {
		this.taskNumber = taskNumber;
	}
	
	public int getTaskMaxMarks() {
		return taskMaxMarks;
	}
	
	public void setTaskMaxMarks(int taskMaxMarks) {
		this.taskMaxMarks = taskMaxMarks;
	}
	
	public String getTaskName() {
		return taskName;
	}
	
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	
	public String getTaskDate() {
		return taskDate;
	}
	
	public void setTaskDate(String taskDate) {
		this.taskDate = taskDate;
	}
	
	public String getTaskTopicName() {
		return taskTopicName;
	}
	
	public void setTaskTopicName(String taskTopicName) {
		this.taskTopicName = taskTopicName;
	}
	
	public boolean ifGroup() {
		return group;
	}
	
	public void isGroup(boolean group) {
		this.group = group;
	}

}
