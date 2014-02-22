package com.example.learningoutcomes.Formative;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learningoutcomes.R;
import com.example.learningoutcomes.Settings;
import com.example.learningoutcomes.database.LODatabaseHelper;
import com.example.learningoutcomes.database.LODatabaseUtility;

public class FaTask extends Activity implements OnItemSelectedListener,
		OnClickListener {
	private Settings m_settings; 
	private SQLiteDatabase m_database;
	private LODatabaseHelper m_databaseHelper;

	ListView listView;
	public ArrayAdapter<ScoreClass> adapter;
	public TextView totalScore;
	TextView scoreName;
	int total;

	testObj test;
	String username;

	Spinner spTaskName;
	Spinner spTaskTopic;
	int schoolId;

	ArrayAdapter<String> adapterTaskName;
	ArrayList<String> taskName;
	ArrayAdapter<Model> adapterModel;

	ArrayAdapter<String> adapterTaskTopic;
	ArrayList<String> taskTopic;

	private int checkVal;
	ImageView ivAddParameter;
	TextView tvAddParameter;
	EditText procedure;

	long id;
	ListView paramListView;
	CheckBox cbObservations, cbGroupTask;

	Animation anim;
	Button save;
	List<ScoreClass> prevScore = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fa_create);
		checkVal = 0;
		/** Initialise database variables */
		m_databaseHelper = new LODatabaseHelper(this);
		m_database = m_databaseHelper.getWritableDatabase();
//		LODatabaseUtility.getInstance().setDatabase(m_database);
		m_settings = new Settings(getApplicationContext(), 
				(Spinner) findViewById(R.id.spClass), (Spinner) findViewById(R.id.spTestName),
				(Spinner) findViewById(R.id.spTerm), (Spinner) findViewById(R.id.spSubject));
		m_settings.init();
		spTaskName = (Spinner) findViewById(R.id.spTaskName);
		spTaskTopic = (Spinner) findViewById(R.id.spTaskTopic);
		ivAddParameter = (ImageView) findViewById(R.id.suggestionAdd);
		ivAddParameter.setEnabled(false);
		save = (Button) findViewById(R.id.btCreateTask);
		save.setEnabled(false);

		ivAddParameter.setOnClickListener(this);
		save.setOnClickListener(this);
		totalScore = (TextView) findViewById(R.id.tvTotalParamMarks);
		scoreName = (TextView) findViewById(R.id.tvTotalParamMarksText);

		tvAddParameter = (TextView) findViewById(R.id.tvAddParameter);
		tvAddParameter.setOnClickListener(this);
		tvAddParameter.setEnabled(false);
		listView = (ListView) findViewById(R.id.lvParameters);

		cbObservations = (CheckBox) findViewById(R.id.cbObservations);
		cbGroupTask = (CheckBox) findViewById(R.id.cbGroupTask);

		procedure = (EditText) findViewById(R.id.etProcedure);

		scoreName.setText("");
		totalScore.setText("");
		listView.setAdapter(null);
		setTitle("");
		/* start the actual create task here */
		test = new testObj();
		/* TODO - get this value from previous screen */
		test.testType = "Formative";
		/* Get the value of the global settings for this user */
		SharedPreferences shPref = getSharedPreferences("global_settings",
				MODE_PRIVATE);
		username = shPref.getString("username", "");
		test.classId = Integer.parseInt(shPref
				.getString(username + "class", ""));
		test.subjectId = Integer.parseInt(shPref.getString(
				username + "subject", ""));
		test.teacher_term = shPref.getString(username + "term", "");
		test.testName = shPref.getString(username + "testname", "");
		schoolId = Integer.parseInt(shPref.getString(username + "school_id",
				"0"));

		/* set up the spinner for this task name */
		taskName = new ArrayList<String>();
		Cursor cursor = m_database.rawQuery(
				"Select topic_name from topic where subject_id = '"
						+ test.subjectId + "'", null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			taskName.add(cursor.getString(cursor
					.getColumnIndexOrThrow("topic_name")));
			cursor.moveToNext();
		}
		taskName.add("Add +");
		cursor.close();
		adapterTaskName = new ArrayAdapter<String>(this, -1, taskName);
		spTaskName.setAdapter(new NothingSelectedSpinnerAdapter(
				adapterTaskName, -1,
				// R.layout.contact_spinner_nothing_selected_dropdown, //
				// Optional
				this, "Select Task", "Task"));
		spTaskName.setOnItemSelectedListener(this);

		spTaskTopic.setAdapter(new NothingSelectedSpinnerAdapter(
				adapterTaskName, -1,
				// R.layout.contact_spinner_nothing_selected_dropdown, //
				// Optional
				this, "Select Task First", "Task"));
		spTaskTopic.setOnItemSelectedListener(this);
		spTaskTopic.setClickable(false);

		setAnimationValue();
		// setupActionBar();
	}

	// private void setupActionBar() {
	// final ActionBar actionBar = getActionBar();
	// actionBar.setDisplayShowCustomEnabled(true);
	// actionBar.setCustomView(R.layout.action_bar_text);
	// }

	private List<ScoreClass> getScore() {
		List<ScoreClass> list = new ArrayList<ScoreClass>();
		list.add(get("Linux", 0, 0));
		list.add(get("Windows7", 0, 0));
		list.add(get("Suse", 0, 0));
		list.add(get("Eclipse", 0, 0));
		list.add(get("Ubuntu", 0, 0));
		list.add(get("Solaris", 0, 0));
		list.add(get("Android", 0, 0));
		list.add(get("iPhone", 0, 0));

		return list;
	}

	private ScoreClass get(String name, int score, int maxScore) {
		return new ScoreClass(name, score, maxScore);
	}

	private class testObj {
		int subjectId;
		int classId;
		String teacher_term;
		String testType;
		String testName;
		int taskNumber;
		String taskName;
		String taskTopic;
		String procedure;
		int comment;
		int totalMarks;
		int weightageFactor;
		String timeStamp;
	};

	@Override
	protected void onPause() {
		m_databaseHelper.close();
		super.onPause();
	}

	@Override
	protected void onResume() {
		m_database = m_databaseHelper.getWritableDatabase();
		super.onResume();
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		if (checkVal < 2) {
			checkVal++;
			return;
		}
		switch (arg0.getId()) {
		case R.id.spTaskName:
			/*
			 * In case user selects add new Give him option to add a new task
			 */
			if (arg0.getItemIdAtPosition(arg2) + 1 != adapterTaskName
					.getCount())
				test.taskName = adapterTaskName.getItem((int) arg0
						.getItemIdAtPosition(arg2));

			id = arg0.getItemIdAtPosition(arg2);
			if (arg0.getItemIdAtPosition(arg2) + 1 == adapterTaskName
					.getCount()) {
				/* Create the new dialog box */
				final Dialog dialog = new Dialog(this, R.style.DialogSlideAnim);
				dialog.setContentView(R.layout.dialog_view);
				dialog.setTitle("Add New Task");

				// set the custom dialog components - text, image and button
				final TextView tvName = (TextView) dialog
						.findViewById(R.id.tvDialog);
				tvName.setText("Task Name");
				final EditText etName = (EditText) dialog
						.findViewById(R.id.etDialog);
				etName.setHint("Enter Task Name");

				Button dialogButton = (Button) dialog
						.findViewById(R.id.btDialog);
				dialogButton.setText("Add Test");

				dialog.setOnCancelListener(new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						// TODO Auto-generated method stub
						spTaskName
								.setAdapter(new NothingSelectedSpinnerAdapter(
										adapterTaskName,
										-1,
										// R.layout.contact_spinner_nothing_selected_dropdown,
										// //
										// Optional
										FaTask.this, "Select Task Name",
										"Task Name"));
						checkVal = 1;
						ivAddParameter.setEnabled(false);
						scoreName.setText("");
						totalScore.setText("");
						listView.setAdapter(null);
						tvAddParameter.setEnabled(false);
						save.setEnabled(false);
					}
				});
				// if button is clicked, close the custom dialog
				dialogButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						String name = etName.getText().toString();
						int topicNameId = 0;
						taskName.add((int) id, name);
						adapterTaskName.notifyDataSetChanged();
						spTaskName.setSelection((int) id + 1);
						test.taskName = name;
						/* Insert data into table */
						Cursor cursor = m_database
								.rawQuery(
										"Select ifnull(max(CAST(topic_id as Int)), 0) from topic",
										null);

						cursor.moveToFirst();
						while (!cursor.isAfterLast()) {

							topicNameId = Integer.parseInt(cursor.getString(0));
							topicNameId += 1;
							cursor.moveToNext();
						}
						cursor.close();
						String sql = "INSERT INTO topic VALUES ('"
								+ String.valueOf(topicNameId) + "', '" + name
								+ "'," + test.subjectId + ", 'TimeStamp');";
						m_database.execSQL(sql);
						dialog.dismiss();
						ivAddParameter.setEnabled(false);
						save.setEnabled(false);
						scoreName.setText("");
						totalScore.setText("");
						listView.setAdapter(null);
						tvAddParameter.setEnabled(false);
						setupTaskTopicSpinner();
					}
				});
				dialog.show();
			} else {
				ivAddParameter.setEnabled(false);
				save.setEnabled(false);
				scoreName.setText("");
				totalScore.setText("");
				listView.setAdapter(null);
				tvAddParameter.setEnabled(false);
				setupTaskTopicSpinner();
			}
			break;
		case R.id.spTaskTopic:
			if (arg0.getItemIdAtPosition(arg2) + 1 != adapterTaskTopic
					.getCount())
				test.taskTopic = adapterTaskTopic.getItem((int) arg0
						.getItemIdAtPosition(arg2));
			if (arg0.getItemIdAtPosition(arg2) + 1 == adapterTaskTopic
					.getCount()) {
				/* Create the new dialog box */
				id = arg0.getItemIdAtPosition(arg2);
				final Dialog dialog = new Dialog(this, R.style.DialogSlideAnim);

				dialog.setContentView(R.layout.dialog_view);
				dialog.setTitle("Add Topic Name");

				// set the custom dialog components - text, image and button
				final TextView tvName = (TextView) dialog
						.findViewById(R.id.tvDialog);
				tvName.setText("Topic Name");
				final EditText etName = (EditText) dialog
						.findViewById(R.id.etDialog);
				etName.setHint("Enter Topic Name");

				Button dialogButton = (Button) dialog
						.findViewById(R.id.btDialog);
				dialogButton.setText("Add New Topic");
				dialog.setOnCancelListener(new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						// TODO Auto-generated method stub
						spTaskTopic
								.setAdapter(new NothingSelectedSpinnerAdapter(
										adapterTaskTopic,
										-1,
										// R.layout.contact_spinner_nothing_selected_dropdown,
										// //
										// Optional
										FaTask.this, "Select Topic Name",
										"Topic Name"));
						ivAddParameter.setEnabled(false);
						save.setEnabled(false);
						scoreName.setText("");
						totalScore.setText("");
						listView.setAdapter(null);
						tvAddParameter.setEnabled(false);
						checkVal = 1;

					}
				});
				// if button is clicked, close the custom dialog
				dialogButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						String name = etName.getText().toString();
						int subtopicNameId = 1;
						taskTopic.add((int) id, name);
						adapterTaskTopic.notifyDataSetChanged();
						spTaskTopic.setSelection((int) id + 1);
						test.taskTopic = name;

						/* Insert data into table */
						Cursor cursor = m_database
								.rawQuery(
										"Select max(CAST(subtopic_id as Int)) from subtopic",
										null);

						cursor.moveToFirst();
						while (!cursor.isAfterLast()) {

							subtopicNameId = Integer.parseInt(cursor
									.getString(0));
							subtopicNameId += 1;
							cursor.moveToNext();
						}
						cursor.close();
						String sql = "INSERT INTO subtopic VALUES ('"
								+ String.valueOf(subtopicNameId) + "', '"
								+ test.taskName + "', '" + name
								+ "', 'TimeStamp');";
						m_database.execSQL(sql);
						dialog.dismiss();
						ivAddParameter.setEnabled(true);
						save.setEnabled(false);
						scoreName.setText("Total Marks");
						totalScore.setText("" + 0);
						listView.setAdapter(null);
						tvAddParameter.setEnabled(true);
						/* Set the adapter model to null */
						adapterModel = null;
						prevScore = null;
						adapter = new ScoreArrayAdapter(FaTask.this, prevScore,
								totalScore, null, false);

					}
				});

				dialog.show();
			} else {
				/* Do something here! */
				ivAddParameter.setEnabled(true);
				save.setEnabled(false);
				scoreName.setText("Total Marks");
				totalScore.setText("" + 0);
				listView.setAdapter(null);
				tvAddParameter.setEnabled(true);
				/* Set the adapter model to null */
				adapterModel = null;
				prevScore = null;
				adapter = new ScoreArrayAdapter(FaTask.this, prevScore,
						totalScore, null, false);

			}
			break;
		}

	}

	private void setupTaskTopicSpinner() {
		spTaskTopic.setClickable(true);
		taskTopic = new ArrayList<String>();
		Cursor cursor = m_database.rawQuery(
				"Select subtopic_name from subtopic where topic_name = '"
						+ test.taskName + "'", null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			taskTopic.add(cursor.getString(cursor
					.getColumnIndexOrThrow("subtopic_name")));
			cursor.moveToNext();
		}
		taskTopic.add("Add +");
		// make sure to close the cursor
		cursor.close();

		adapterTaskTopic = new ArrayAdapter<String>(this, -1, taskTopic);
		spTaskTopic.setAdapter(new NothingSelectedSpinnerAdapter(
				adapterTaskTopic, -1,
				// R.layout.contact_spinner_nothing_selected_dropdown,
				// //
				// Optional
				this, "Select Topic", "Topic Name"));
		checkVal = 1;
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tvAddParameter:
		case R.id.suggestionAdd:
			/* Add new parameters to the list */
			final Dialog dialog = new Dialog(this, R.style.DialogSlideAnim);

			dialog.setContentView(R.layout.dialog_listview_view);
			dialog.setTitle("Add Parameters");
			// set the custom dialog components - text, image and button
			Button btAddNewParam = (Button) dialog
					.findViewById(R.id.addNewParam);
			Button btCheckoutParam = (Button) dialog
					.findViewById(R.id.checkoutParam);
			dialog.setCanceledOnTouchOutside(false);
			btAddNewParam.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					/** Logic to add a new Parameter */
					final Dialog innerDialog = new Dialog(FaTask.this,
							R.style.DialogSlideAnim);

					innerDialog.setContentView(R.layout.dialog_view);
					innerDialog.setTitle("Add Parameter Name");

					// set the custom dialog components - text, image and button
					final TextView tvName = (TextView) innerDialog
							.findViewById(R.id.tvDialog);
					tvName.setText("Parameter Name");
					final EditText etName = (EditText) innerDialog
							.findViewById(R.id.etDialog);
					etName.setHint("Enter Parameter Name");

					Button dialogButton = (Button) innerDialog
							.findViewById(R.id.btDialog);
					dialogButton.setText("Add New Parameter");
					// if button is clicked, close the custom dialog

					dialogButton.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							String name = etName.getText().toString();
							int parameterNameId = 1;

							List<Model> list = new ArrayList<Model>();
							list = ((InteractiveArrayAdapter) adapterModel)
									.getData();
							list.add(get(name));
							list.get(list.size() - 1).setSelected(true);
							adapterModel = new InteractiveArrayAdapter(
									FaTask.this, list);
							paramListView.setAdapter(adapterModel);
							/* Insert data into table */
							Cursor cursor = m_database
									.rawQuery(
											"Select max(CAST(parameter_id as Int)) from parameter",
											null);

							cursor.moveToFirst();
							while (!cursor.isAfterLast()) {

								parameterNameId = Integer.parseInt(cursor
										.getString(0));
								parameterNameId += 1;
								cursor.moveToNext();
							}
							cursor.close();
							String sql = "INSERT INTO parameter VALUES ('"
									+ String.valueOf(parameterNameId) + "', '"
									+ test.taskName + "', '" + name
									+ "', 'TimeStamp');";
							m_database.execSQL(sql);
							innerDialog.dismiss();
						}
					});

					innerDialog.show();

				}
			});
			btCheckoutParam.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					save.setEnabled(false);
					// TODO Auto-generated method stub
					List<Model> list = new ArrayList<Model>();
					list = ((InteractiveArrayAdapter) adapterModel).getData();
					List<ScoreClass> nextScore = new ArrayList<ScoreClass>();
					int totalMarks = 0;
					for (int i = 0; i < list.size(); i++) {
						if (list.get(i).isSelected()) {
							save.setEnabled(true);
							if (prevScore == null) {
								nextScore.add(get(list.get(i).getName(), 0, 0));
							} else {
								int j = 0;
								/* Check logic here */
								// l.add(get(list.get(i).getName(), 0, 0));
								for (j = 0; j < prevScore.size(); j++) {
									String name = prevScore.get(j).getName();
									if (name.contentEquals(list.get(i)
											.getName())) {
										/* Element already exists */
										break;
									}
								}
								if (j != prevScore.size()) {
									nextScore.add(get(list.get(i).getName(),
											prevScore.get(j).getScore(), 0));
									totalMarks += prevScore.get(j).getScore();
								} else {
									nextScore.add(get(list.get(i).getName(), 0,
											0));
								}
							}
						}
					}
					prevScore = nextScore;
					adapter = new ScoreArrayAdapter(FaTask.this, nextScore,
							totalScore, null, false);
					totalScore.setText("" + totalMarks);
					listView.setAdapter(adapter);
					dialog.dismiss();
				}
			});
			paramListView = (ListView) dialog.findViewById(R.id.listViewParam);
			List<Model> list = new ArrayList<Model>();
			if (null != adapterModel)
				list = ((InteractiveArrayAdapter) adapterModel).getData();
			else
				list = getModel();
			adapterModel = new InteractiveArrayAdapter(this, list);
			paramListView.setAdapter(adapterModel);
			dialog.show();

			break;
		case R.id.btCreateTask:
			/* Save the task here */
			List<ScoreClass> listScores = new ArrayList<ScoreClass>();
			listScores = ((ScoreArrayAdapter) adapter).getData();

			for (int i = 0; i < listScores.size(); i++) {
				if (listScores.get(i).getScore() == 0) {
					Toast.makeText(this, "Parameter Score cannot be zero",
							Toast.LENGTH_SHORT).show();
					return;
				}
			}
			/* Logic for adding new test and new tasks */

			/* Get the maximum test id from test table */

			Cursor cursor = m_database.rawQuery(
					"Select ifnull(max(CAST(test_id as Int)), 0) from test",
					null);
			int testId = 0;
			int taskNum = 0;
			cursor.moveToFirst();

			while (!cursor.isAfterLast()) {
				testId = Integer.parseInt(cursor.getString(0));
				testId += 1;
				cursor.moveToNext();
			}
			cursor = m_database.rawQuery(
					"Select ifnull(max(task_number),0) from test where class_id = "
							+ test.classId + " and subject_id = "
							+ test.subjectId + " and teacher_term = '"
							+ test.teacher_term + "' and testname = '"
							+ test.testName + "' and test_type =  'Formative"
							+ "' and username = '" + username + "'", null);

			cursor.moveToFirst();

			while (!cursor.isAfterLast()) {
				taskNum = Integer.parseInt(cursor.getString(0));
				taskNum += 1;
				cursor.moveToNext();
			}
			cursor.close();
			String proc = null;
			int totalMarks = Integer.parseInt(totalScore.getText().toString());
			int comment = 0;
			if (cbObservations.isChecked())
				comment = 1;
			if (procedure.getText().toString().length() != 0) {
				proc = procedure.getText().toString();
			}
			int groupTask = 0;
			if (cbGroupTask.isChecked())
				groupTask = 1;
			/* Setting task num as zero for now as it has no significance here */
			String sql = "INSERT INTO test VALUES ('" + String.valueOf(testId)
					+ "', '" + test.subjectId + "'," + test.classId + ", '"
					+ test.teacher_term + "', '" + test.testType + "', '"
					+ test.testName + "', '" + taskNum + "', '" + test.taskName
					+ "', '" + test.taskTopic + "', '" + proc + "', '"
					+ comment + "', " + totalMarks + ", " + groupTask + ", "
					+ 0 + ", '" + username + "', " + "'TimeStamp');";

			m_database.execSQL(sql);

			cursor = m_database.rawQuery(
					"Select ifnull(max(CAST(ques_id as Int)), 0) from ques",
					null);
			int quesId = 0;
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {

				quesId = Integer.parseInt(cursor.getString(0));
				quesId += 1;
				cursor.moveToNext();
			}
			cursor.close();
			int quesNum = 0;
			/* Add questions/parameters into the ques table */
			for (int i = 0; i < listScores.size(); i++) {
				sql = "INSERT INTO ques VALUES ('"
						+ String.valueOf(quesId)
						+ "', '"
						+ String.valueOf(testId)
						+ "',"
						+ quesNum
						+ ", "
						+ null
						+ ", '"
						+ listScores.get(i).getScore()
						+ "',null, null,null, null, null, null, null, null, null, null, '"
						+ listScores.get(i).getName()
						+ "',null, null, null, null,null, null,0,0, null, '"
						+ username + "', " + "'TimeStamp');";

				m_database.execSQL(sql);
				/* Increement question ID by 1 */
				quesId += 1;
				quesNum += 1;
			}
			Toast.makeText(this, "Task Saved!", Toast.LENGTH_SHORT).show();
			Log.e("Webmail", "Harman");
			Intent i = new Intent(this, FaTaskList.class);
			finish();
			startActivity(i);
			break;
		}
	}

	public List<Model> getModel() {
		List<Model> list = new ArrayList<Model>();
		Cursor cursor = m_database.rawQuery(
				"select parameter_name from parameter where task_name ='"
						+ test.taskName + "'", null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			list.add(get(cursor.getString(cursor
					.getColumnIndexOrThrow("parameter_name"))));
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return list;
	}

	private Model get(String s) {
		return new Model(s);
	}

	private void setAnimationValue() {
		anim = new AlphaAnimation(0.0f, 1.0f);
		anim.setDuration(100); // You can manage the time of the blink with
								// this parameter
		anim.setStartOffset(20);
		anim.setRepeatMode(Animation.REVERSE);
	}
}
