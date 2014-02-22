package com.example.learningoutcomes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.learningoutcomes.Formative.NothingSelectedSpinnerAdapter;
import com.example.learningoutcomes.database.LODatabaseHelper;
import com.example.learningoutcomes.database.LODatabaseUtility;

public class Settings implements OnItemSelectedListener {

	/*
	 * The global settings are stored in usernameclass, usernameterm,
	 * usernametestname, usernamesubject in shared preferences
	 */
	/* this activity is yet to be tested */
	Context m_context;
	Spinner m_spinnerClass;
	Spinner m_spinnerSubject;
	Spinner m_spinnerTerm;
	Spinner m_spinnerTestName;

	ArrayAdapter<String> adapterClass;
	ArrayAdapter<String> adapterSubject;
	ArrayAdapter<String> adapterTerm;
	ArrayAdapter<String> adapterTestName;

	Map<String, String> m_listClass;
	Map<String, String> m_listSubject;
	ArrayList<String> m_listTerm;
	ArrayList<String> m_listTestName;
	String username;

	SQLiteDatabase database;
	LODatabaseHelper databaseHelper;

	String termSettings, classSettings, testNameSettings, subjectSettings;
	SharedPreferences prefs;
	Editor et;

	private int gallery;

	public Settings(Context context, Spinner spinnerClass,
			Spinner spinnnerTestName, Spinner spinnerTerm,
			Spinner spinnerSubject) {
		m_context = context;
		m_spinnerClass = spinnerClass;
		m_spinnerTestName = spinnnerTestName;
		m_spinnerTerm = spinnerTerm;
		m_spinnerSubject = spinnerSubject;
	}

	public void init() {

		m_listClass = new HashMap<String, String>();
		m_listSubject = new HashMap<String, String>();
		m_listTerm = new ArrayList<String>();
		m_listTestName = new ArrayList<String>();

		prefs = m_context.getSharedPreferences("global_settings",
				Context.MODE_PRIVATE);
//		databaseHelper = new LODatabaseHelper(m_context);
//		database = databaseHelper.getWritableDatabase();
//		LODatabaseUtility.getInstance().setDatabase(database);
		setUpSpinners();
		gallery = 0;
	}

	private void setUpSpinners() {
		/* Get username from the Global settings */
		username = prefs.getString("username", "user");
		Log.e("Username", username);
		/*
		 * Get the value of Term from the teacher table and store in the list
		 * for term
		 */
		String query = "Select distinct teacher_term from teacher where username = '"
				+ username + "'";
		Cursor cursor = LODatabaseUtility.getInstance().cursorFromQuery(query);
		m_listTerm = LODatabaseUtility.getInstance().dataListfromCursor(cursor,
				"teacher_term");
		termSettings = prefs.getString(username + "term", "0");
		adapterTerm = setSpinnerAdapter(m_spinnerTerm, adapterTerm, m_listTerm, termSettings, "Select Term", "Term");
		m_spinnerTerm.setOnItemSelectedListener(this);
		m_spinnerTerm.setSelection(m_listTerm.indexOf(termSettings) + 1);
		
		query = "Select subject_id from teacher where username = '" + username
				+ "' and teacher_term = '" + termSettings + "'";
		cursor = LODatabaseUtility.getInstance().cursorFromQuery(query);
		ArrayList<String> subjectId = new ArrayList<String>();
		ArrayList<String> subjectName = new ArrayList<String>();

		subjectId = LODatabaseUtility.getInstance().dataListfromCursor(cursor,
				"subject_id");
		for (int i = 0; i < subjectId.size(); ++i) {
			query = "Select subject_name from subject where subject_id = '"
					+ subjectId.get(i) + "'";
			cursor = LODatabaseUtility.getInstance().cursorFromQuery(query);
			subjectName = LODatabaseUtility.getInstance().dataListfromCursor(
					cursor, "subject_name");
			for (int j = 0; j < subjectName.size(); ++j) {
				m_listSubject.put(subjectName.get(j), subjectId.get(i));
			}
		}

		subjectSettings = prefs.getString(username + "subject", "0");
		ArrayList<String> subjectVal = new ArrayList<String>(m_listSubject.keySet());
		adapterSubject = setSpinnerAdapter(m_spinnerSubject, adapterSubject,
				subjectVal, termSettings, "Select Subject", "Subject");

		ArrayList<String> temp = new ArrayList<String>(m_listSubject.values());
		m_spinnerSubject.setOnItemSelectedListener(this);
		m_spinnerSubject.setSelection(temp.indexOf(subjectSettings) + 1);

		/*
		 * Get the value of class and section a teacher teaches for a particular
		 * term and a particular subject
		 */
		query = "Select class_id from teacher where username = '" + username
				+ "' and teacher_term = '" + termSettings
				+ "' and subject_id = '" + subjectSettings + "'";
		cursor = LODatabaseUtility.getInstance().cursorFromQuery(query);
		ArrayList<String> classId = new ArrayList<String>();
		ArrayList<String> className = new ArrayList<String>();
		ArrayList<String> sectionName = new ArrayList<String>();

		classId = LODatabaseUtility.getInstance().dataListfromCursor(cursor,
				"class_id");
		for (int i = 0; i < classId.size(); ++i) {
			// Get the class name(s) associated with the class IDs
			query = "Select class_name from class where class_id = '"
					+ classId.get(i) + "'";
			cursor = LODatabaseUtility.getInstance().cursorFromQuery(query);
			className = LODatabaseUtility.getInstance().dataListfromCursor(
					cursor, "class_name");
			// Get the section name(s) associated with the class IDs
			query = "Select section_name from class where class_id = '"
					+ classId.get(i) + "'";
			cursor = LODatabaseUtility.getInstance().cursorFromQuery(query);
			sectionName = LODatabaseUtility.getInstance().dataListfromCursor(
					cursor, "section_name");
			for (int j = 0; j < className.size(); ++j) {
				// we can also use section name .size as well cause both size
				// are the same.
				// For every corresponding class there is a section
				m_listClass.put(className.get(j) + "-" + sectionName.get(j),
						classId.get(i));
			}
		}

		classSettings = prefs.getString(username + "class", "0");

		ArrayList<String> classVal = new ArrayList<String>(m_listClass.keySet());
		adapterClass = setSpinnerAdapter(m_spinnerClass, adapterClass,
				classVal, classSettings, "Select Class", "Class");

		temp = new ArrayList<String>(m_listClass.values());
		m_spinnerClass.setOnItemSelectedListener(this);
		m_spinnerClass.setSelection(temp.indexOf(classSettings) + 1);

		/* Get the test name based on the school ID */
		int school_id = Integer.parseInt(prefs.getString(
				username + "school_id", "0"));
		query = "Select testname from testname where school_id = '" + school_id
				+ "'";
		cursor = LODatabaseUtility.getInstance().cursorFromQuery(query);
		m_listTestName = LODatabaseUtility.getInstance().dataListfromCursor(
				cursor, "testname");

		testNameSettings = prefs.getString(username + "testname", "0");
		adapterTestName = setSpinnerAdapter(m_spinnerTestName, adapterTestName,
				m_listTestName, testNameSettings, "Select Test", "Test");
		m_spinnerTestName.setSelection(m_listTestName.indexOf(testNameSettings) + 1);

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
			m_listSubject = new HashMap<String, String>();
			while (!cursor.isAfterLast()) {
				int subject_id = cursor.getInt(cursor
						.getColumnIndexOrThrow("subject_id"));
				Cursor cursorSubject = database.rawQuery(
						"Select subject_name from subject where subject_id = '"
								+ subject_id + "'", null);
				cursorSubject.moveToFirst();

				if (!cursorSubject.isAfterLast()) {
					m_listSubject.put(cursorSubject.getString(cursorSubject
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
					m_listSubject.keySet());
			adapterSubject = new ArrayAdapter<String>(m_context, -1, subjectVal);
			m_spinnerSubject.setAdapter(new NothingSelectedSpinnerAdapter(
					adapterSubject, -1,
					// R.layout.contact_spinner_nothing_selected_dropdown,
					// //
					// Optional
					m_context, "Select Subject", "Subject"));
			String temp = m_listSubject.get(subjectVal.get(0));
			et.putString(username + "subject", temp);
			m_spinnerSubject.setSelection(1);

			cursor = database.rawQuery(
					"Select class_id from teacher where username = '"
							+ username
							+ "' and teacher_term = '"
							+ adapterTerm.getItem((int) arg0
									.getItemIdAtPosition(arg2))
							+ "' and subject_id = '" + temp + "'", null);
			cursor.moveToFirst();
			m_listClass = new HashMap<String, String>();
			while (!cursor.isAfterLast()) {
				int class_id = cursor.getInt(cursor
						.getColumnIndexOrThrow("class_id"));
				Cursor cursorClass = database.rawQuery(
						"Select class_name,section_name from class where class_id = '"
								+ class_id + "'", null);
				cursorClass.moveToFirst();
				if (!cursorClass.isAfterLast()) {
					m_listClass
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
					m_listClass.keySet());
			adapterClass = new ArrayAdapter<String>(m_context, -1, classVal);
			m_spinnerClass.setAdapter(new NothingSelectedSpinnerAdapter(
					adapterClass, -1,
					// R.layout.contact_spinner_nothing_selected_dropdown,
					// //
					// Optional
					m_context, "Select Class", "Class"));
			temp = m_listClass.get(classVal.get(0));
			et.putString(username + "class", temp);
			m_spinnerClass.setSelection(1);
			et.commit();
			break;

		case R.id.spSubject:
			/* save the value of subject_id in global settings */
			et = prefs.edit();
			String subjectId = m_listSubject.get(adapterSubject
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
							+ m_listSubject.get(adapterSubject
									.getItem((int) arg0
											.getItemIdAtPosition(arg2))) + "'",
					null);
			cursor.moveToFirst();
			m_listClass = new HashMap<String, String>();

			while (!cursor.isAfterLast()) {
				int class_id = cursor.getInt(cursor
						.getColumnIndexOrThrow("class_id"));
				Cursor cursorClass = database.rawQuery(
						"Select class_name, section_name from class where class_id = '"
								+ class_id + "'", null);
				cursorClass.moveToFirst();
				if (!cursorClass.isAfterLast()) {
					m_listClass
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

			classVal = new ArrayList<String>(m_listClass.keySet());
			adapterClass = new ArrayAdapter<String>(m_context, -1, classVal);
			m_spinnerClass.setAdapter(new NothingSelectedSpinnerAdapter(
					adapterClass, -1,
					// R.layout.contact_spinner_nothing_selected_dropdown,
					// //
					// Optional
					m_context, "Select Class", "Class"));

			temp = m_listClass.get(classVal.get(0));
			et.putString(username + "class", temp);
			m_spinnerClass.setSelection(1);
			et.commit();
			break;

		case R.id.spClass:
			et = prefs.edit();
			/* save the value of class_id in global settings */
			String classId = m_listClass.get(adapterClass.getItem((int) arg0
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

	private ArrayAdapter<String> setSpinnerAdapter(Spinner spinner,
			ArrayAdapter<String> arrayAdapter, ArrayList<String> arrayList,
			String selection, String adapterMessage, String adapterScrollTitle) {
		arrayAdapter = new ArrayAdapter<String>(m_context, -1, arrayList);
		spinner.setAdapter(new NothingSelectedSpinnerAdapter(arrayAdapter, -1,
		// R.layout.contact_spinner_nothing_selected_dropdown, //
		// Optional
				m_context, adapterMessage, adapterScrollTitle));

		/* Get the subjects according to the term in the global settings */
		spinner.setSelection(arrayList.indexOf(selection) + 1);
		spinner.setOnItemSelectedListener(this);
		return arrayAdapter;
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}
}
