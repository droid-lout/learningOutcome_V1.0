package com.example.learningoutcomes.Formative;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learningoutcomes.R;
import com.example.learningoutcomes.database.LODatabaseHelper;

public class FaScore extends Activity implements OnItemSelectedListener,
		OnCheckedChangeListener, OnClickListener {
	ListView listView;
	public ArrayAdapter<ScoreClass> adapter;
	public TextView totalScore;
	int total;

	String username, teacher_term, testName;
	int classId, subjectId;

	SQLiteDatabase database;
	LODatabaseHelper databaseHelper;

	List<String> studentName;

	ArrayAdapter<String> adapterStudentName;
	Spinner spStudent;

	List<String> parameterName;
	List<Integer> parameterMaxScore;
	List<String> parameterId;

	private int check = 0;
	String testId = null;
	int currentStudent;

	EditText comments;
	RadioGroup statusGroup;
	Button saveScore;
	String status = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fa_score);

		// /* Set the test id from intent as passed from the previous screen */
		testId = getIntent().getExtras().getString("test_id");
		status = "Present";
		databaseHelper = new LODatabaseHelper(this);
		database = databaseHelper.getWritableDatabase();

		parameterName = new ArrayList<String>();
		parameterMaxScore = new ArrayList<Integer>();
		parameterId = new ArrayList<String>();

		/* Get global values from global settings */
		SharedPreferences shPref = getSharedPreferences("global_settings",
				MODE_PRIVATE);
		username = shPref.getString("username", "");
		classId = Integer.parseInt(shPref.getString(username + "class", ""));
		subjectId = Integer
				.parseInt(shPref.getString(username + "subject", ""));
		teacher_term = shPref.getString(username + "term", "");
		testName = shPref.getString(username + "testname", "");

		spStudent = (Spinner) findViewById(R.id.spStudentName);

		comments = (EditText) findViewById(R.id.etComments);
		statusGroup = (RadioGroup) findViewById(R.id.rgStatus);
		statusGroup.setOnCheckedChangeListener(this);
		saveScore = (Button) findViewById(R.id.btSaveScore);
		saveScore.setOnClickListener(this);

		/* Setup Initial Data */
		SetupScreen();
		setupActionBar();
	}

	private void SetupScreen() {
		// TODO Auto-generated method stub
		/* Setup the student spinner */
		/* set up the spinner for this task name */
		studentName = new ArrayList<String>();
		Cursor cursor = database.rawQuery(
				"Select student_id, name from student where class_id = "
						+ classId, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			studentName.add(cursor.getString(cursor
					.getColumnIndexOrThrow("student_id"))
					+ "-"
					+ cursor.getString(cursor.getColumnIndexOrThrow("name")));
			cursor.moveToNext();
		}
		cursor.close();
		adapterStudentName = new ArrayAdapter<String>(this, -1, studentName);
		spStudent.setAdapter(new NothingSelectedSpinnerAdapter(
				adapterStudentName, -1,
				// R.layout.contact_spinner_nothing_selected_dropdown, //
				// Optional
				this, "Select Student", "Student"));
		spStudent.setOnItemSelectedListener(this);
		spStudent.setSelection(1);
		currentStudent = 0;

		/* Get the parameters from the question tale for this task */
		cursor = database.rawQuery(
				"Select ques_id, ques_max_score, parameter_1 from ques where test_id = '"
						+ testId + "'", null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			parameterName.add(cursor.getString(cursor
					.getColumnIndexOrThrow("parameter_1")));
			parameterId.add(cursor.getString(cursor
					.getColumnIndexOrThrow("ques_id")));
			parameterMaxScore.add(Integer.parseInt(cursor.getString(cursor
					.getColumnIndexOrThrow("ques_max_score"))));
			cursor.moveToNext();
		}
		cursor.close();

		totalScore = (TextView) findViewById(R.id.tvStudentScore);
		totalScore.setText("" + 0);
		listView = (ListView) findViewById(R.id.lvStudentScore);
		adapter = new ScoreArrayAdapter(this, getScore(), totalScore, null,
				true);
		listView.setAdapter(adapter);
		listView.post(new Runnable() {
			public void run() {
				checkStatusStudent();
			}
		});

	}

	private void setupActionBar() {
		// TODO Auto-generated method stub
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setCustomView(R.layout.action_bar_text);
	}

	private List<ScoreClass> getScore() {
		/* Variable to update the total marks */
		int totalMarks = 0;
		String[] stockArr = new String[parameterId.size()];
		stockArr = parameterId.toArray(stockArr);
		/* Get the marks of the students if they have been saved earlier */
		Cursor cursor = database.rawQuery(
				"Select response, comment, status from response where student_id = '"
						+ studentName.get(currentStudent).split("-")[0]
						+ "' and ques_id in ("
						+ makePlaceholders(parameterId.size()) + ")", stockArr);
		List<ScoreClass> list = new ArrayList<ScoreClass>();

		cursor.moveToFirst();
		if (cursor.isAfterLast()) {
			/* No response stored for this student before */
			for (int i = 0; i < parameterId.size(); i++) {
				list.add(get(parameterName.get(i), 0, parameterMaxScore.get(i)));
				/*
				 * By default, status is present and edit text does not contain
				 * anything
				 */
				totalMarks = 0;
			}

			status = "Present";
			comments.setText("");
		}

		int i = 0;
		while (!cursor.isAfterLast()) {
			list.add(get(parameterName.get(i), Integer.parseInt(cursor
					.getString(cursor.getColumnIndexOrThrow("response"))),
					parameterMaxScore.get(i)));
			status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
			comments.setText(cursor.getString(cursor
					.getColumnIndexOrThrow("comment")));

			totalMarks += Integer.parseInt(cursor.getString(cursor
					.getColumnIndexOrThrow("response")));
			cursor.moveToNext();
			i++;
		}

		/* Update the total marks textview */
		totalScore.setText("" + totalMarks);

		/* If the student is absent or on leave, diable the listview */
		cursor.close();
		return list;
	}

	private String makePlaceholders(int len) {
		if (len < 1) {
			// It will lead to an invalid query anyway ..
			throw new RuntimeException("No placeholders");
		} else {
			StringBuilder sb = new StringBuilder(len * 2 - 1);
			sb.append("?");
			for (int i = 1; i < len; i++) {
				sb.append(",?");
			}
			return sb.toString();
		}
	}

	private ScoreClass get(String name, int score, int maxScore) {
		return new ScoreClass(name, score, maxScore);
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		if (check == 0) {
			/* Do not run the spinner on first run */
			check = 1;
			return;
		}
		switch (arg0.getId()) {
		case R.id.spStudentName:
			currentStudent = arg2 - 1;
			adapter = new ScoreArrayAdapter(this, getScore(), totalScore, null,
					true);
			listView.setAdapter(adapter);
			listView.post(new Runnable() {
				public void run() {
					checkStatusStudent();
				}
			});
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

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub

		switch (checkedId) {
		case R.id.rbPresent:
			for (int i = 0; i < parameterName.size(); i++) {
				status = "Present";
				View view = listView.getChildAt(i);
				if (i >= listView.getFirstVisiblePosition()
						&& i <= listView.getLastVisiblePosition()) {
					TextView textScore = (TextView) view
							.findViewById(R.id.tvScore);
					ImageView downArrow = (ImageView) view
							.findViewById(R.id.ibDownArrow);
					ImageView upArrow = (ImageView) view
							.findViewById(R.id.ibUpArrow);
					textScore.setClickable(true);
					downArrow.setClickable(true);
					upArrow.setClickable(true);

				}
			}
			listView.setEnabled(true);
			comments.setEnabled(true);
			break;
		case R.id.rbAbsent:
			status = "Absent";
			for (int i = 0; i < parameterName.size(); i++) {
				View view = listView.getChildAt(i);
				if (i >= listView.getFirstVisiblePosition()
						&& i <= listView.getLastVisiblePosition()) {
					TextView textScore = (TextView) view
							.findViewById(R.id.tvScore);
					ImageView downArrow = (ImageView) view
							.findViewById(R.id.ibDownArrow);
					ImageView upArrow = (ImageView) view
							.findViewById(R.id.ibUpArrow);
					textScore.setClickable(false);
					downArrow.setClickable(false);
					upArrow.setClickable(false);

				}
			}
			listView.setEnabled(false);
			comments.setEnabled(false);
			break;
		case R.id.rbLeave:
			status = "Leave";
			for (int i = 0; i < parameterName.size(); i++) {
				View view = listView.getChildAt(i);
				if (i >= listView.getFirstVisiblePosition()
						&& i <= listView.getLastVisiblePosition()) {

					TextView textScore = (TextView) view
							.findViewById(R.id.tvScore);
					ImageView downArrow = (ImageView) view
							.findViewById(R.id.ibDownArrow);
					ImageView upArrow = (ImageView) view
							.findViewById(R.id.ibUpArrow);
					textScore.setClickable(false);
					downArrow.setClickable(false);
					upArrow.setClickable(false);

				}
			}
			listView.setEnabled(false);
			comments.setEnabled(false);
			break;

		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btSaveScore:
			Toast.makeText(
					this,
					"Score of " + studentName.get(currentStudent)
							+ " has been updated", Toast.LENGTH_SHORT).show();
			/* save the score for this student and display marks of next student */
			/* these values need to be saved in the response table */
			/*
			 * Also need to make sure whether this is first time when marks of
			 * this student are getting updated or not
			 */

			String[] stockArr = new String[parameterId.size()];
			stockArr = parameterId.toArray(stockArr);
			/* Get the marks of the students if they have been saved earlier */
			Cursor cursor = database.rawQuery(
					"Select response_id from response where student_id = '"
							+ studentName.get(currentStudent).split("-")[0]
							+ "' and ques_id in ("
							+ makePlaceholders(parameterId.size()) + ")",
					stockArr);

			cursor.moveToFirst();

			String comment = "";
			if (comments.getText().length() != 0) {
				comment = comments.getText().toString();
			}
			List<ScoreClass> listScores = new ArrayList<ScoreClass>();
			listScores = ((ScoreArrayAdapter) adapter).getData();

			if (cursor.isAfterLast()) {
				/* this is the first time these marks are getting entered */
				Cursor cursorResponseId = database
						.rawQuery(
								"Select max(CAST(response_id as Int), 0) from response",
								null);

				int responseId = 0;
				cursorResponseId.moveToFirst();
				while (!cursorResponseId.isAfterLast()) {

					responseId = Integer
							.parseInt(cursorResponseId.getString(0));
					responseId += 1;
					cursorResponseId.moveToNext();
				}
				cursorResponseId.close();

				for (int i = 0; i < parameterId.size(); i++) {
					/* Insert parameters into response table */
					/* Get the list of scores */

					String sql = "INSERT INTO response VALUES ('"
							+ String.valueOf(responseId) + "', '" + username
							+ "', "
							+ studentName.get(currentStudent).split("-")[0]
							+ ", '" + parameterId.get(i) + "', "
							+ listScores.get(i).getScore() + ", '" + comment
							+ "', '" + status + "', 'TimeStamp');";
					database.execSQL(sql);
					responseId += 1;

				}

			}
			int i = 0;
			while (!cursor.isAfterLast()) {
				/*
				 * Here we need to update the scores of student as they have
				 * already been added once
				 */
				String responseId = cursor.getString(cursor
						.getColumnIndexOrThrow("response_id"));

				String sql = "UPDATE response SET response = "
						+ listScores.get(i++).getScore() + ", status = '"
						+ status + "', comment = '" + comment
						+ "' where response_id = '" + responseId + "'";
				database.execSQL(sql);
				cursor.moveToNext();
			}
			cursor.close();

			/*
			 * In case it is a last student, display marks of first student
			 * after this
			 */
			if (studentName.size() == currentStudent + 1) {
				currentStudent = 0;
			} else {
				currentStudent = currentStudent + 1;
			}
			spStudent.setSelection(currentStudent + 1);
			adapter = new ScoreArrayAdapter(this, getScore(), totalScore, null,
					true);
			listView.setAdapter(adapter);
			listView.post(new Runnable() {
				public void run() {
					checkStatusStudent();
				}
			});
			break;
		}
	}

	private void checkStatusStudent() {
		// TODO Auto-generated method stub
		RadioButton present, leave, absent;

		present = (RadioButton) findViewById(R.id.rbPresent);
		leave = (RadioButton) findViewById(R.id.rbLeave);
		absent = (RadioButton) findViewById(R.id.rbAbsent);

		if (status.contentEquals("Present")) {
			/* Present */
			present.setChecked(true);
			onCheckedChanged(statusGroup, R.id.rbPresent);

		} else if (status.contentEquals("Leave")) {
			/* Leave */
			leave.setChecked(true);
			onCheckedChanged(statusGroup, R.id.rbLeave);

		} else if (status.contentEquals("Absent")) {
			/* Absent */

			onCheckedChanged(statusGroup, R.id.rbAbsent);
			absent.setChecked(true);
		}

	}
}
