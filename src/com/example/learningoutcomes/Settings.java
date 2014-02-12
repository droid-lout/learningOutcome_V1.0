package com.example.learningoutcomes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.learningoutcomes.Formative.NothingSelectedSpinnerAdapter;
import com.example.learningoutcomes.database.LODatabaseHelper;

public class Settings extends Activity implements OnItemSelectedListener {

	/*
	 * The global settings are stored in usernameclass, usernameterm,
	 * usernametestname, usernamesubject in shared preferences
	 */
	/* this activity is yet to be tested */
	Spinner spinnerClass;
	Spinner spinnerSubject;
	Spinner spinnerTerm;
	Spinner spinnerTestName;

	ArrayAdapter<String> adapterClass;
	ArrayAdapter<String> adapterSubject;
	ArrayAdapter<String> adapterTerm;
	ArrayAdapter<String> adapterTestName;

	Map<String, String> listClass;
	Map<String, String> listSubject;
	ArrayList<String> listTerm;
	ArrayList<String> listTestName;
	String username;

	SQLiteDatabase database;
	LODatabaseHelper databaseHelper;

	String termSettings, classSettings, testNameSettings, subjectSettings;
	SharedPreferences prefs;
	Editor et;

	private int gallery;

	private int boolVal = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		spinnerClass = (Spinner) findViewById(R.id.spClass);
		spinnerTestName = (Spinner) findViewById(R.id.spTestName);
		spinnerTerm = (Spinner) findViewById(R.id.spTerm);
		spinnerSubject = (Spinner) findViewById(R.id.spSubject);

		listClass = new HashMap<String, String>();
		listSubject = new HashMap<String, String>();
		listTerm = new ArrayList<String>();
		listTestName = new ArrayList<String>();

		prefs = this.getSharedPreferences("global_settings",
				Context.MODE_PRIVATE);
		databaseHelper = new LODatabaseHelper(this);
		database = databaseHelper.getWritableDatabase();

		setUpSpinners();
		gallery = 0;
	}

	private void setUpSpinners() {
		// TODO Auto-generated method stub
		/* Get username from the Global settings */
		username = prefs.getString("username", "user");

		/*
		 * Get the value of Term from the teacher table and store in the list
		 * for term
		 */
		Cursor cursor = database.rawQuery(
				"Select distinct teacher_term from teacher where username = '"
						+ username + "'", null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			String term = cursor.getString(cursor
					.getColumnIndexOrThrow("teacher_term"));
			listTerm.add(term);
			cursor.moveToNext();
		}
		cursor.close();

		/* Get the subjects according to the term in the global settings */
		termSettings = prefs.getString(username + "term", "0");
		Log.e("Err", termSettings);
		/* Show the values in the spinner */
		adapterTerm = new ArrayAdapter<String>(this, -1, listTerm);
		spinnerTerm.setAdapter(new NothingSelectedSpinnerAdapter(adapterTerm,
				-1,
				// R.layout.contact_spinner_nothing_selected_dropdown, //
				// Optional
				this, "Select Term", "Term"));
		spinnerTerm.setSelection(listTerm.indexOf(termSettings) + 1);
		spinnerTerm.setOnItemSelectedListener(this);

		cursor = database.rawQuery(
				"Select subject_id from teacher where username = '" + username
						+ "' and teacher_term = '" + termSettings + "'", null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			int subject_id = cursor.getInt(cursor
					.getColumnIndexOrThrow("subject_id"));
			Cursor cursorSubject = database.rawQuery(
					"Select subject_name from subject where subject_id = '"
							+ subject_id + "'", null);
			cursorSubject.moveToFirst();
			if (!cursorSubject.isAfterLast()) {
				listSubject.put(cursorSubject.getString(cursorSubject
						.getColumnIndexOrThrow("subject_name")), cursor
						.getString(cursor.getColumnIndexOrThrow("subject_id")));
				cursorSubject.close();
			}
			/* Move the main cursor to next */
			cursor.moveToNext();
		}
		cursor.close();

		ArrayList<String> subjectVal = new ArrayList<String>(
				listSubject.keySet());
		adapterSubject = new ArrayAdapter<String>(this, -1, subjectVal);
		spinnerSubject.setAdapter(new NothingSelectedSpinnerAdapter(
				adapterSubject, -1,
				// R.layout.contact_spinner_nothing_selected_dropdown, //
				// Optional
				this, "Select Subject", "Subject"));
		subjectSettings = prefs.getString(username + "subject", "0");
		ArrayList<String> temp = new ArrayList<String>(listSubject.values());
		spinnerSubject.setOnItemSelectedListener(this);
		spinnerSubject.setSelection(temp.indexOf(subjectSettings) + 1);

		cursor = database.rawQuery(
				"Select class_id from teacher where username = '" + username
						+ "' and teacher_term = '" + termSettings
						+ "' and subject_id = '" + subjectSettings + "'", null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			int class_id = cursor.getInt(cursor
					.getColumnIndexOrThrow("class_id"));
			Cursor cursorClass = database.rawQuery(
					"Select class_name, section_name from class where class_id = '"
							+ class_id + "'", null);
			cursorClass.moveToFirst();
			if (!cursorClass.isAfterLast()) {
				listClass
						.put(cursorClass.getString(cursorClass
								.getColumnIndexOrThrow("class_name"))
								+ "-"
								+ cursorClass.getString(cursorClass
										.getColumnIndexOrThrow("section_name")),
								cursor.getString(cursor
										.getColumnIndexOrThrow("class_id")));
				cursorClass.close();
			}
			/* Move the main cursor to next */
			cursor.moveToNext();
		}
		cursor.close();

		ArrayList<String> classVal = new ArrayList<String>(listClass.keySet());
		adapterClass = new ArrayAdapter<String>(this, -1, classVal);
		spinnerClass.setAdapter(new NothingSelectedSpinnerAdapter(adapterClass,
				-1,
				// R.layout.contact_spinner_nothing_selected_dropdown, //
				// Optional
				this, "Select Class", "Class"));

		classSettings = prefs.getString(username + "class", "0");
		temp = new ArrayList<String>(listClass.values());
		spinnerClass.setOnItemSelectedListener(this);
		spinnerClass.setSelection(temp.indexOf(classSettings) + 1);

		/* Get the test name based on the school ID */
		int school_id = Integer.parseInt(prefs.getString(
				username + "school_id", "0"));
		cursor = database.rawQuery(
				"Select testname from testname where school_id = '" + school_id
						+ "'", null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			String testName = cursor.getString(cursor
					.getColumnIndexOrThrow("testname"));
			listTestName.add(testName);
			cursor.moveToNext();
		}
		cursor.close();

		adapterTestName = new ArrayAdapter<String>(this, -1, listTestName);
		spinnerTestName.setAdapter(new NothingSelectedSpinnerAdapter(
				adapterTestName, -1,
				// R.layout.contact_spinner_nothing_selected_dropdown, //
				// Optional
				this, "Select Test", "Test"));

		testNameSettings = prefs.getString(username + "testname", "0");

		spinnerTestName.setOnItemSelectedListener(this);
		spinnerTestName
				.setSelection(listTestName.indexOf(testNameSettings) + 1);

		/* Set on item selected listeners */

	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		if (gallery < 4) {
			gallery++;
			return;
		}
		switch (arg0.getId()) {
		case R.id.spTerm:

			et = prefs.edit();
			/* save the value of Term in global settings */
			et.putString(username + "term",
					adapterTerm.getItem((int) arg0.getItemIdAtPosition(arg2)));
			et.commit();
			/* Set class and subject Spinners */
			/* Get the term from global settings */
			Cursor cursor = database.rawQuery(
					"Select subject_id from teacher where username = '"
							+ username
							+ "' and teacher_term = '"
							+ adapterTerm.getItem((int) arg0
									.getItemIdAtPosition(arg2)) + "'", null);
			cursor.moveToFirst();
			listSubject = new HashMap<String, String>();
			while (!cursor.isAfterLast()) {
				int subject_id = cursor.getInt(cursor
						.getColumnIndexOrThrow("subject_id"));
				Cursor cursorSubject = database.rawQuery(
						"Select subject_name from subject where subject_id = '"
								+ subject_id + "'", null);
				cursorSubject.moveToFirst();

				if (!cursorSubject.isAfterLast()) {
					listSubject.put(cursorSubject.getString(cursorSubject
							.getColumnIndexOrThrow("subject_name")), cursor
							.getString(cursor
									.getColumnIndexOrThrow("subject_id")));
					cursorSubject.close();
				}
				/* Move the main cursor to next */
				cursor.moveToNext();
			}
			cursor.close();

			ArrayList<String> subjectVal = new ArrayList<String>(
					listSubject.keySet());
			adapterSubject = new ArrayAdapter<String>(this, -1, subjectVal);
			spinnerSubject.setAdapter(new NothingSelectedSpinnerAdapter(
					adapterSubject, -1,
					// R.layout.contact_spinner_nothing_selected_dropdown,
					// //
					// Optional
					this, "Select Subject", "Subject"));
			String temp = listSubject.get(subjectVal.get(0));
			et.putString(username + "subject", temp);
			spinnerSubject.setSelection(1);

			cursor = database.rawQuery(
					"Select class_id from teacher where username = '"
							+ username
							+ "' and teacher_term = '"
							+ adapterTerm.getItem((int) arg0
									.getItemIdAtPosition(arg2))
							+ "' and subject_id = '" + temp + "'", null);
			cursor.moveToFirst();
			listClass = new HashMap<String, String>();
			while (!cursor.isAfterLast()) {
				int class_id = cursor.getInt(cursor
						.getColumnIndexOrThrow("class_id"));
				Cursor cursorClass = database.rawQuery(
						"Select class_name,section_name from class where class_id = '"
								+ class_id + "'", null);
				cursorClass.moveToFirst();
				if (!cursorClass.isAfterLast()) {
					listClass
							.put(cursorClass.getString(cursorClass
									.getColumnIndexOrThrow("class_name"))
									+ "-"
									+ cursorClass
											.getString(cursorClass
													.getColumnIndexOrThrow("section_name")),
									cursor.getString(cursor
											.getColumnIndexOrThrow("class_id")));
					cursorClass.close();
				}
				/* Move the main cursor to next */
				cursor.moveToNext();
			}
			cursor.close();
			ArrayList<String> classVal = new ArrayList<String>(
					listClass.keySet());
			adapterClass = new ArrayAdapter<String>(this, -1, classVal);
			spinnerClass.setAdapter(new NothingSelectedSpinnerAdapter(
					adapterClass, -1,
					// R.layout.contact_spinner_nothing_selected_dropdown,
					// //
					// Optional
					this, "Select Class", "Class"));
			temp = listClass.get(classVal.get(0));
			et.putString(username + "class", temp);
			spinnerClass.setSelection(1);
			et.commit();
			break;

		case R.id.spSubject:
			/* save the value of subject_id in global settings */
			et = prefs.edit();
			String subjectId = listSubject.get(adapterSubject
					.getItem((int) arg0.getItemIdAtPosition(arg2)));
			et.putString(username + "subject", subjectId);

			/* Get the term from shared preference values */
			termSettings = prefs.getString(username + "term", "0");
			cursor = database.rawQuery(
					"Select class_id from teacher where username = '"
							+ username
							+ "' and teacher_term = '"
							+ termSettings
							+ "' and subject_id = '"
							+ listSubject.get(adapterSubject.getItem((int) arg0
									.getItemIdAtPosition(arg2))) + "'", null);
			cursor.moveToFirst();
			listClass = new HashMap<String, String>();

			while (!cursor.isAfterLast()) {
				int class_id = cursor.getInt(cursor
						.getColumnIndexOrThrow("class_id"));
				Cursor cursorClass = database.rawQuery(
						"Select class_name, section_name from class where class_id = '"
								+ class_id + "'", null);
				cursorClass.moveToFirst();
				if (!cursorClass.isAfterLast()) {
					listClass
							.put(cursorClass.getString(cursorClass
									.getColumnIndexOrThrow("class_name"))
									+ "-"
									+ cursorClass
											.getString(cursorClass
													.getColumnIndexOrThrow("section_name")),
									cursor.getString(cursor
											.getColumnIndexOrThrow("class_id")));
					cursorClass.close();
				}
				/* Move the main cursor to next */
				cursor.moveToNext();
			}
			cursor.close();

			classVal = new ArrayList<String>(listClass.keySet());
			adapterClass = new ArrayAdapter<String>(this, -1, classVal);
			spinnerClass.setAdapter(new NothingSelectedSpinnerAdapter(
					adapterClass, -1,
					// R.layout.contact_spinner_nothing_selected_dropdown,
					// //
					// Optional
					this, "Select Class", "Class"));

			temp = listClass.get(classVal.get(0));
			et.putString(username + "class", temp);
			spinnerClass.setSelection(1);
			et.commit();
			break;

		case R.id.spClass:
			et = prefs.edit();
			/* save the value of class_id in global settings */
			String classId = listClass.get(adapterClass.getItem((int) arg0
					.getItemIdAtPosition(arg2)));
			et.putString(username + "class", classId);
			et.commit();
			break;

		case R.id.spTestName:
			et = prefs.edit();
			/* save the value of testname in global settings */
			et.putString(username + "testname", adapterTestName
					.getItem((int) arg0.getItemIdAtPosition(arg2)));
			et.commit();
			break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

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

}
