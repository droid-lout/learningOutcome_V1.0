package com.example.learningoutcomes.Formative;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.learningoutcomes.R;
import com.example.learningoutcomes.database.LODatabaseHelper;

public class FaCreateGroup extends Activity implements OnClickListener,
		OnItemLongClickListener {

	SQLiteDatabase database;
	LODatabaseHelper databaseHelper;

	String username, teacher_term, testName;
	int classId, subjectId;

	String testId = null;
	Button addNewGroup, saveGroup;

	ListView lvGroups;

	/* This will contain the final list of students in each group */
	List<Group> studentGroup;
	ArrayAdapter<Group> studentGroupAdapter;

	List<Model> studentName;
	ArrayAdapter<Model> adapterStudentName;
	List<String> grp;

	/* Temporary adapter used in deleting students from a group */
	ArrayAdapter<Model> adapter;

	int groupNumber;

	int isFirstTime;

	/* Make sure the marks of each student remains same when groups are changed */
	/* This activity helps in making groups for a particular task */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		/** Initialise database variables */
		databaseHelper = new LODatabaseHelper(this);
		database = databaseHelper.getWritableDatabase();
		setContentView(R.layout.fa_create_group);

		/*
		 * This is to check if this is the first time group activity has been
		 * called for this test id
		 */
		isFirstTime = 1;

		/* Set the test id from intent as passed from the previous screen */
		testId = getIntent().getExtras().getString("test_id");

		/* Get global values from global settings */
		SharedPreferences shPref = getSharedPreferences("global_settings",
				MODE_PRIVATE);
		username = shPref.getString("username", "");
		classId = Integer.parseInt(shPref.getString(username + "class", ""));
		subjectId = Integer
				.parseInt(shPref.getString(username + "subject", ""));
		teacher_term = shPref.getString(username + "term", "");
		testName = shPref.getString(username + "testname", "");
		addNewGroup = (Button) findViewById(R.id.btAddNewGroup);
		addNewGroup.setOnClickListener(this);
		saveGroup = (Button) findViewById(R.id.btSaveGroup);
		lvGroups = (ListView) findViewById(R.id.lvStudentGroup);
		saveGroup.setOnClickListener(this);

		groupNumber = 1;
		studentName = new ArrayList<Model>();
		Cursor cursor = database.rawQuery(
				"Select student_id, name from student where class_id = "
						+ classId, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			studentName.add(get(cursor.getString(cursor
					.getColumnIndexOrThrow("student_id"))
					+ "-"
					+ cursor.getString(cursor.getColumnIndexOrThrow("name"))));
			cursor.moveToNext();
		}
		cursor.close();
		adapterStudentName = new InteractiveArrayAdapter(this, studentName);
		grp = new ArrayList<String>();
		studentGroup = new ArrayList<Group>();

		/*
		 * In case groups are already made for this task, fetch data from the
		 * database and display this data
		 */
		/*
		 * In case groups are already there, set isFirstTime = 1 Also Update the
		 * group number accordingly No student should be available to be added
		 * to a new group
		 */
		cursor = database.rawQuery(
				"Select DISTINCT group_name from groups where test_id = '"
						+ testId + "'", null);
		cursor.moveToFirst();
		if (!cursor.isAfterLast()) {
			/*
			 * Groups have already been made for this test and they are about to
			 * get edited
			 */
			isFirstTime = 0;
			/* Set all the students as unavailable */
			studentName = new ArrayList<Model>();
			adapterStudentName = new InteractiveArrayAdapter(this, studentName);
		}
		while (!cursor.isAfterLast()) {
			/* Increement the value of group number */
			groupNumber++;
			/* Read all the groups which are there for this test id */
			Cursor group = database.rawQuery(
					"Select student_id from groups where group_name = '"
							+ cursor.getString(cursor
									.getColumnIndexOrThrow("group_name"))
							+ "' and test_id = '" + testId + "'", null);
			group.moveToFirst();
			List<String> studentList = new ArrayList<String>();
			while (!group.isAfterLast()) {
				/* Get Student name from the student table */
				Cursor student = database.rawQuery(
						"Select name from student where student_id = "
								+ group.getInt(group
										.getColumnIndexOrThrow("student_id")),
						null);
				student.moveToFirst();
				/*
				 * There will be always only one entry for a particular student
				 * id
				 */
				studentList.add(group.getInt(group
						.getColumnIndexOrThrow("student_id"))
						+ "-"
						+ student.getString(student
								.getColumnIndexOrThrow("name")));
				group.moveToNext();
			}
			/* Add this to the group list */
			Group temp = new Group(cursor.getString(cursor
					.getColumnIndexOrThrow("group_name")), studentList);
			studentGroup.add(temp);
			group.close();

			cursor.moveToNext();
		}
		cursor.close();
		studentGroupAdapter = new StudentGroupAdapter(this, studentGroup);
		lvGroups.setAdapter(studentGroupAdapter);
		lvGroups.setOnItemLongClickListener(this);
	}

	@Override
	protected void onPause() {
		databaseHelper.close();
		super.onPause();
	}

	@Override
	protected void onResume() {
		database = databaseHelper.getWritableDatabase();
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btAddNewGroup:
			addNewGroup();
			break;
		case R.id.btSaveGroup:
			/*
			 * Save the group if all the students have been made a part of the
			 * group
			 */

			/* Check if all the students have been added to groups */
			List<Model> list = new ArrayList<Model>();
			list = ((InteractiveArrayAdapter) adapterStudentName).getData();
			if (0 != list.size()) {
				Toast.makeText(
						this,
						"Error !!Some students have still not been added to groups!",
						Toast.LENGTH_SHORT).show();
				return;
			}
			/* All the students have been added to a group */
			/* Save this group */
			if (isFirstTime == 0) {
				/* If this is not the first time this activity has been called */
				/* Delet all the entries for group for this test id */
				String sql = "DELETE from groups where test_id = '" + testId
						+ "';";
				database.execSQL(sql);
			}

			/* Get the maximum group ID available from the group table */
			int groupId = 0;
			Cursor cursor = database.rawQuery(
					"Select ifnull(max(CAST(group_id as Int)), 0) from groups",
					null);

			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {

				groupId = Integer.parseInt(cursor.getString(0));
				groupId += 1;
				cursor.moveToNext();
			}
			cursor.close();

			/* Insert groups for this class */
			List<Group> temp = ((StudentGroupAdapter) studentGroupAdapter)
					.getData();
			for (int i = 0; i < temp.size(); i++) {
				List<String> students = new ArrayList<String>();
				students = temp.get(i).studentname;
				for (int j = 0; j < students.size(); j++) {
					/* Insert each student and the corresponding group name */
					String sql = "INSERT into groups values ('" + groupId
							+ "', " + students.get(j).split("-")[0] + ", '"
							+ temp.get(i).groupName + "' , '" + testId + "' ,'"
							+ "Timestamp" + "');";
					database.execSQL(sql);
					groupId++;
				}
			}
			if (isFirstTime == 0) {
				Toast.makeText(this, "Groups Updated!", Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(this, "Groups Saved!", Toast.LENGTH_SHORT)
						.show();
			}
			Intent i = new Intent(this, FaTaskList.class);
			finish();
			startActivity(i);

			break;

		}
	}

	private void addNewGroup() {
		/* Add a new group into the groups list */
		/* Open a new dialog */

		/* Add new parameters to the list */
		final Dialog dialog = new Dialog(this, R.style.DialogSlideAnim);
		List<Model> list = new ArrayList<Model>();
		list = ((InteractiveArrayAdapter) adapterStudentName).getData();
		if (0 == list.size()) {
			Toast.makeText(this,
					"All students of class have been put into groups",
					Toast.LENGTH_SHORT).show();
			return;
		}
		dialog.setContentView(R.layout.dialog_listview_group_view);
		dialog.setTitle("Add Students to a Group");
		Button btAddNewGroup = (Button) dialog.findViewById(R.id.addNewGroup);

		ListView paramListView = (ListView) dialog
				.findViewById(R.id.lvStudentList);

		paramListView.setAdapter(adapterStudentName);
		dialog.show();
		btAddNewGroup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/* Add new parameter and add this to list */
				List<Model> list = new ArrayList<Model>();
				list = ((InteractiveArrayAdapter) adapterStudentName).getData();
				List<String> Name = new ArrayList<String>();
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).isSelected()) {
						/*
						 * In case the student has been selected, remove the
						 * student from the list
						 */
						String name = list.get(i).getName();
						list.remove(i);
						i--;
						Name.add(name);
					}
				}
				if (!(Name.size() == 0)) {
					adapterStudentName = new InteractiveArrayAdapter(
							FaCreateGroup.this, list);
					/* Here add a new group to the list */
					Group temp = new Group("Group - " + groupNumber, Name);
					groupNumber++;
					studentGroup.add(temp);
					studentGroupAdapter.notifyDataSetChanged();
				}
				dialog.dismiss();
			}
		});
	}

	private Model get(String s) {
		return new Model(s);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			final int position, long id) {
		// TODO Auto-generated method stub
		/*
		 * Open a dialog here to ask whether you want to add or remove from this
		 * group
		 */
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setMessage("Edit Group?")
				.setPositiveButton("Add more students",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								/* Add a new group into the groups list */
								/* Open a new dialog */

								/* Add new parameters to the list */
								final Dialog innerDialog = new Dialog(
										FaCreateGroup.this,
										R.style.DialogSlideAnim);
								List<Model> list = new ArrayList<Model>();
								list = ((InteractiveArrayAdapter) adapterStudentName)
										.getData();
								if (0 == list.size()) {
									Toast.makeText(
											FaCreateGroup.this,
											"All students of class have been put into groups",
											Toast.LENGTH_SHORT).show();
									return;
								}
								innerDialog
										.setContentView(R.layout.dialog_listview_group_view);
								innerDialog.setTitle("Add Students to a Group");
								// set the custom dialog components - text,
								// image and button
								Button btAddNewGroup = (Button) innerDialog
										.findViewById(R.id.addNewGroup);

								ListView paramListView = (ListView) innerDialog
										.findViewById(R.id.lvStudentList);

								paramListView.setAdapter(adapterStudentName);
								innerDialog.show();
								btAddNewGroup
										.setOnClickListener(new OnClickListener() {

											@Override
											public void onClick(View v) {
												// TODO Auto-generated method
												// stub
												/*
												 * Add new parameter and add
												 * this to list
												 */

												List<Model> list = new ArrayList<Model>();
												list = ((InteractiveArrayAdapter) adapterStudentName)
														.getData();
												List<Group> temp = ((StudentGroupAdapter) studentGroupAdapter)
														.getData();
												List<String> Names = temp
														.get(position).studentname;
												for (int i = 0; i < list.size(); i++) {
													if (list.get(i)
															.isSelected()) {
														/*
														 * In case the student
														 * has been selected,
														 * remove the student
														 * from the list
														 */
														String name = list.get(
																i).getName();
														list.remove(i);
														i--;
														Names.add(name);
													}
												}
												if (!(Names.size() == 0)) {
													adapterStudentName = new InteractiveArrayAdapter(
															FaCreateGroup.this,
															list);

													Group group = new Group(
															studentGroup
																	.get(position).groupName,
															Names);
													studentGroup
															.remove(position);
													studentGroup.add(position,
															group);

													studentGroupAdapter
															.notifyDataSetChanged();
												}
												innerDialog.dismiss();
											}
										});
							}
						})
				.setNegativeButton("Remove Students",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								/* Logic to delete from current group */
								List<String> student = new ArrayList<String>();
								List<Group> temp = ((StudentGroupAdapter) studentGroupAdapter)
										.getData();
								student = temp.get(position).studentname;
								List<Model> studentData = new ArrayList<Model>();
								for (int i = 0; i < student.size(); i++) {
									studentData.add(get(student.get(i)));
								}
								adapter = new InteractiveArrayAdapter(
										FaCreateGroup.this, studentData);
								/* Add new parameters to the list */
								final Dialog innerDialog = new Dialog(
										FaCreateGroup.this,
										R.style.DialogSlideAnim);
								innerDialog
										.setContentView(R.layout.dialog_listview_group_view);
								innerDialog
										.setTitle("Remove Students from group");
								// set the custom dialog components - text,
								// image and button
								Button btRemove = (Button) innerDialog
										.findViewById(R.id.addNewGroup);
								btRemove.setText("Remove Selected Students");

								ListView paramListView = (ListView) innerDialog
										.findViewById(R.id.lvStudentList);

								paramListView.setAdapter(adapter);
								innerDialog.show();
								btRemove.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										/*
										 * Add new parameter and add this to
										 * list
										 */

										List<Model> studentsCurrentGroup = new ArrayList<Model>();
										studentsCurrentGroup = ((InteractiveArrayAdapter) adapter)
												.getData();
										Log.e("studentgroup.s", ""
												+ studentsCurrentGroup.size());

										/*
										 * Ignore this if no student is selected
										 * or all students are selected
										 */
										int check = 0;
										for (int i = 0; i < studentsCurrentGroup
												.size(); i++) {
											if (studentsCurrentGroup.get(i)
													.isSelected()) {
												/*
												 * This student needs to be
												 * removed
												 */

											} else {
												/*
												 * This student need not be
												 * removed student
												 */
												check++;
											}
										}

										if (studentsCurrentGroup.size() == check) {
											Toast.makeText(
													FaCreateGroup.this,
													"Select Atleast one student to be removed",
													Toast.LENGTH_SHORT).show();
											return;
										}

										/* Now delete students from the group */
										List<Group> temp = ((StudentGroupAdapter) studentGroupAdapter)
												.getData();

										List<String> Names = temp.get(position).studentname;

										List<Model> restoredStudentNames = ((InteractiveArrayAdapter) adapterStudentName)
												.getData();
										for (int i = 0; i < studentsCurrentGroup
												.size(); i++) {
											/*
											 * If the student is selected, it
											 * has to be removed
											 */
											if (studentsCurrentGroup.get(i)
													.isSelected()) {

												/*
												 * Add this student to the list
												 * of available students
												 */

												restoredStudentNames
														.add(get(studentsCurrentGroup
																.get(i)
																.getName()));
												/*
												 * Remove this student from the
												 * group
												 */
												Names.remove(i);
												studentsCurrentGroup.remove(i);
												i--;
											}
										}
										if (check == 0) {
											/*
											 * All the students have been
											 * removed
											 */
											String groupName = studentGroup
													.get(position).groupName;

											int groupnum = Integer
													.valueOf(groupName
															.split("- ")[1]);
											for (int j = position + 1; j < studentGroup
													.size(); j++) {
												studentGroup.get(j).groupName = "Group - "
														+ groupnum;
												groupnum++;
											}
											groupNumber = groupnum;
											studentGroup.remove(position);
										}
										if (!(Names.size() == 0)) {

											String groupName = studentGroup
													.get(position).groupName;
											studentGroup.remove(position);
											Group group = new Group(groupName,
													Names);
											studentGroup.add(position, group);
										}
										studentGroupAdapter
												.notifyDataSetChanged();
										adapterStudentName = new InteractiveArrayAdapter(
												FaCreateGroup.this,
												restoredStudentNames);

										innerDialog.dismiss();
									}

								});
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
		return false;
	}
}
