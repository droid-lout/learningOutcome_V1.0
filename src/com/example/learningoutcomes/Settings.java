package com.example.learningoutcomes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
	private Context m_context;
	private Spinner m_spinnerClass;
	private Spinner m_spinnerSubject;
	private Spinner m_spinnerTerm;
	private Spinner m_spinnerTestName;

	private ArrayAdapter<String> adapterClass;
	private ArrayAdapter<String> adapterSubject;
	private ArrayAdapter<String> adapterTerm;
	private ArrayAdapter<String> adapterTestName;

	private Map<String, String> m_listClass;
	private Map<String, String> m_listSubject;
	private ArrayList<String> m_listTerm;
	private ArrayList<String> m_listTestName;

	SQLiteDatabase database;
	LODatabaseHelper databaseHelper;

	private String m_username, m_termSettings, m_classSettings, m_testNameSettings, m_subjectSettings;
	private SharedPreferences m_prefs;
	private Editor m_editor;


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

		m_prefs = m_context.getSharedPreferences("global_settings",
				Context.MODE_PRIVATE);
		setUpSpinners();
	}

	private void setUpSpinners() {
		/* Get username from the Global settings */
		m_username = m_prefs.getString("username", "user");
		/*
		 * Get the value of Term from the teacher table and store in the list
		 * for term
		 */
		String query = "Select distinct teacher_term from teacher where username = '"
				+ m_username + "'";
		Cursor cursor = LODatabaseUtility.getInstance().cursorFromQuery(query);
		m_listTerm = LODatabaseUtility.getInstance().dataListfromCursor(cursor,
				"teacher_term");
		m_termSettings = m_prefs.getString(m_username + "term", "0");
		adapterTerm = setSpinnerAdapter(m_spinnerTerm, adapterTerm, m_listTerm, "Select Term", "Term");
		m_spinnerTerm.setOnItemSelectedListener(this);
		m_spinnerTerm.setSelection(m_listTerm.indexOf(m_termSettings) + 1);

		query = "Select subject_id from teacher where username = '" + m_username
				+ "' and teacher_term = '" + m_termSettings + "'";
		cursor = LODatabaseUtility.getInstance().cursorFromQuery(query);
		updateSubjectSpinner(cursor, query);

		m_subjectSettings = m_prefs.getString(m_username + "subject", "0");
		ArrayList<String> temp = new ArrayList<String>(m_listSubject.values());
		m_spinnerSubject.setOnItemSelectedListener(this);
		m_spinnerSubject.setSelection(temp.indexOf(m_subjectSettings) + 1);

		/*
		 * Get the value of class and section a teacher teaches for a particular
		 * term and a particular subject
		 */
		query = "Select class_id from teacher where username = '" + m_username
				+ "' and teacher_term = '" + m_termSettings
				+ "' and subject_id = '" + m_subjectSettings + "'";
		cursor = LODatabaseUtility.getInstance().cursorFromQuery(query);

		updateClassSpinner(cursor, query);
		m_classSettings = m_prefs.getString(m_username + "class", "0");
		temp = new ArrayList<String>(m_listClass.values());
		m_spinnerClass.setOnItemSelectedListener(this);
		m_spinnerClass.setSelection(temp.indexOf(m_classSettings) + 1);

		/* Get the test name based on the school ID */
		int school_id = Integer.parseInt(m_prefs.getString(
				m_username + "school_id", "0"));
		query = "Select testname from testname where school_id = '" + school_id
				+ "'";
		cursor = LODatabaseUtility.getInstance().cursorFromQuery(query);
		updateTestNameSpinner(cursor, query);
		m_testNameSettings = m_prefs.getString(m_username + "testname", "0");
		m_spinnerTestName.setOnItemSelectedListener(this);
		m_spinnerTestName.setSelection(m_listTestName.indexOf(m_testNameSettings) + 1);

	}

	@Override
	public void onItemSelected(AdapterView<?> adapterView, View view, int position,
			long id) {
		String query;
		Cursor cursor;
		int tempId;
		ArrayList<String> temp = new ArrayList<String>();

		switch (adapterView.getId()) {
		case R.id.spTerm:
			m_editor = m_prefs.edit();
			/* save the value of Term in global settings */
			m_editor.putString(m_username + "term", 
					adapterTerm.getItem((int) adapterView.getItemIdAtPosition(position)));
			m_editor.commit();
			/* Set class and subject Spinners */
			/* Get the term from global settings */
			query = "Select subject_id from teacher where username = '"
					+ m_username
					+ "' and teacher_term = '"
					+ adapterTerm.getItem((int) adapterView.getItemIdAtPosition(position))
					+ "'";
			cursor = LODatabaseUtility.getInstance().cursorFromQuery(query);
			updateSubjectSpinner(cursor, query);
			// Check whether the previous subject is already in the m_listSubject.keysSet
			temp.clear();
			temp = new ArrayList<String>(m_listSubject.values());
			m_subjectSettings = m_prefs.getString(m_username + "subject", "0");
			tempId = temp.indexOf(m_subjectSettings);
			if(tempId < 0)
				tempId = 0;
			m_editor.putString(m_username + "subject", "" + tempId);
			m_spinnerSubject.setSelection(tempId + 1);
			break;

		case R.id.spSubject:
			/* Get the term from shared preference values */
			m_termSettings = m_prefs.getString(m_username + "term", "0");
			/* save the value of subject_id in global settings */
			m_editor = m_prefs.edit();
			String subjectId = m_listSubject.get(adapterSubject
					.getItem((int) adapterView.getItemIdAtPosition(position)));
			m_editor.putString(m_username + "subject", subjectId);

			query = "Select class_id from teacher where username = '"
					+ m_username
					+ "' and teacher_term = '"
					+ m_termSettings
					+ "' and subject_id = '"
					+ m_listSubject.get(adapterSubject.getItem((int) adapterView.getItemIdAtPosition(position)))
					+ "'";
			cursor = LODatabaseUtility.getInstance().cursorFromQuery(query);
			updateClassSpinner(cursor, query);
			temp = new ArrayList<String>(m_listClass.values());
			// Check whether the previous subject is already in the m_listSubject.keysSet
			m_classSettings = m_prefs.getString(m_username + "class", "0");
			tempId = temp.indexOf(m_classSettings);
			if(tempId < 0)
				tempId = 0;
			m_editor.putString(m_username + "class", "" + tempId);
			m_spinnerClass.setSelection(tempId + 1);
			m_editor.commit();

			break;

		case R.id.spClass:
			m_editor = m_prefs.edit();
			/* save the value of class_id in global settings */
			String classId = m_listClass.get(adapterClass.getItem((int) adapterView
					.getItemIdAtPosition(position)));
			m_editor.putString(m_username + "class", classId);
			m_editor.commit();
			break;

		case R.id.spTestName:
			m_editor = m_prefs.edit();
			/* save the value of testname in global settings */
			m_editor.putString(m_username + "testname", adapterTestName
					.getItem((int) adapterView.getItemIdAtPosition(position)));
			m_editor.commit();
			break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> adapterView) {

	}

	private void updateSubjectSpinner(Cursor cursor, String query) {
		ArrayList<String> subjectId = new ArrayList<String>();
		ArrayList<String> subjectName = new ArrayList<String>();
		m_listSubject.clear();
		subjectId = LODatabaseUtility.getInstance().dataListfromCursor(cursor,"subject_id");
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

		ArrayList<String> subjectVal = new ArrayList<String>(m_listSubject.keySet());
		adapterSubject = setSpinnerAdapter(m_spinnerSubject, adapterSubject,
				subjectVal, "Select Subject", "Subject");
	}

	private void updateClassSpinner(Cursor cursor, String query) {
		ArrayList<String> classId = new ArrayList<String>();
		ArrayList<String> className = new ArrayList<String>();
		ArrayList<String> sectionName = new ArrayList<String>();
		m_listClass.clear();
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

		ArrayList<String> classVal = new ArrayList<String>(m_listClass.keySet());
		adapterClass = setSpinnerAdapter(m_spinnerClass, adapterClass,
				classVal, "Select Class", "Class");

	}

	private void updateTestNameSpinner(Cursor cursor, String query) {
		m_listTestName = LODatabaseUtility.getInstance().dataListfromCursor(
				cursor, "testname");
		adapterTestName = setSpinnerAdapter(m_spinnerTestName, adapterTestName,
				m_listTestName, "Select Test", "Test");
	}


	private ArrayAdapter<String> setSpinnerAdapter(Spinner spinner,
			ArrayAdapter<String> arrayAdapter, ArrayList<String> arrayList,
			String adapterMessage, String adapterScrollTitle) {
		arrayAdapter = new ArrayAdapter<String>(m_context, -1, arrayList);
		spinner.setAdapter(new NothingSelectedSpinnerAdapter(arrayAdapter, -1,
				// R.layout.contact_spinner_nothing_selected_dropdown, //
				// Optional
				m_context, adapterMessage, adapterScrollTitle));

		/* Get the subjects according to the term in the global settings */
		return arrayAdapter;
	}
}
