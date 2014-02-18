package com.example.learningoutcomes.Formative;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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
import com.example.learningoutcomes.database.LODatabaseHelper;

public class FaEditTask extends Activity implements OnItemSelectedListener,
		OnClickListener {

	SQLiteDatabase database;
	LODatabaseHelper databaseHelper;

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

	String testId = null;

	long id;
	ListView paramListView;
	CheckBox cbObservations, cbGroupTask;

	Animation anim;
	Button save;
	List<ScoreClass> prevScore = null;

	List<prevParam> prevParm = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fa_create);

		checkVal = 0;
		/** Initialise database variables */
		databaseHelper = new LODatabaseHelper(this);
		database = databaseHelper.getWritableDatabase();

		spTaskName = (Spinner) findViewById(R.id.spTaskName);
		spTaskTopic = (Spinner) findViewById(R.id.spTaskTopic);
		ivAddParameter = (ImageView) findViewById(R.id.suggestionAdd);
		save = (Button) findViewById(R.id.btCreateTask);

		ivAddParameter.setOnClickListener(this);
		save.setOnClickListener(this);
		totalScore = (TextView) findViewById(R.id.tvTotalParamMarks);
		scoreName = (TextView) findViewById(R.id.tvTotalParamMarksText);

		tvAddParameter = (TextView) findViewById(R.id.tvAddParameter);
		tvAddParameter.setOnClickListener(this);
		listView = (ListView) findViewById(R.id.lvParameters);

		cbObservations = (CheckBox) findViewById(R.id.cbObservations);
		cbGroupTask = (CheckBox) findViewById(R.id.cbGroupTask);

		procedure = (EditText) findViewById(R.id.etProcedure);

		setTitle("");
		/* start the actual create task here */
		test = new testObj();

		/*
		 * Get the current task parameters from the database from the test id
		 * that has been passed from the previous screen
		 */
		testId = getIntent().getExtras().getString("test_id");

		Cursor cursor = database.rawQuery(
				"Select * from test where test_id = '" + testId + "'", null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			/*
			 * Store all the values of the required test in the object of test
			 * class
			 */
			/*
			 * These values need to be displayed onces the task edit screen
			 * comes up
			 */
			test.taskName = cursor.getString(cursor
					.getColumnIndexOrThrow("task_name"));
			test.taskTopic = cursor.getString(cursor
					.getColumnIndexOrThrow("task_topic"));
			test.procedure = cursor.getString(cursor
					.getColumnIndexOrThrow("procedure"));
			test.comment = cursor.getInt(cursor
					.getColumnIndexOrThrow("comment"));
			test.totalMarks = cursor.getInt(cursor
					.getColumnIndexOrThrow("total_marks"));
			test.groupTask = cursor.getInt(cursor
					.getColumnIndexOrThrow("group_task"));
			cursor.moveToNext();
		}
		cursor.close();

		cursor = database.rawQuery(
				"Select ques_id, ques_max_score, parameter_1 from ques where test_id = '"
						+ testId + "'", null);

		prevParm = new ArrayList<FaEditTask.prevParam>();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			/* These are all the parameters which are available */
			/*
			 * We need to store these parameters in a list and display these
			 * straight away in the listview which displays the parameter score
			 */
			/* We need the parameter score, parameter name from this */
			prevParam temp = new prevParam(cursor.getString(cursor
					.getColumnIndexOrThrow("ques_id")), cursor.getString(cursor
					.getColumnIndexOrThrow("parameter_1")),
					cursor.getInt(cursor
							.getColumnIndexOrThrow("ques_max_score")));

			/*
			 * This list contains the parameters which are already there in the
			 * task and will always remain
			 */
			prevParm.add(temp);
			cursor.moveToNext();
		}
		cursor.close();

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
		cursor = database.rawQuery(
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
		spTaskName.setSelection(taskName.indexOf(test.taskName) + 1);

		setupTaskTopicSpinner();
		spTaskTopic.setOnItemSelectedListener(this);
		spTaskTopic.setSelection(taskTopic.indexOf(test.taskTopic) + 1);

		/* Set the value of the edittext for procedure */
		if (!test.procedure.contentEquals("null"))
			procedure.setText(test.procedure);

		/* Check/Uncheck the check boxes */
		if (1 == test.groupTask) {
			cbGroupTask.setChecked(true);
		}
		if (1 == test.comment) {
			cbObservations.setChecked(true);
		}

		/* Setup the initial parameters in the list view */
		prevScore = new ArrayList<ScoreClass>();
		for (int i = 0; i < prevParm.size(); i++) {
			prevScore.add(get(prevParm.get(i).parameter_1,
					prevParm.get(i).maxMarks, 0));
			save.setEnabled(true);
		}
		adapter = new ScoreArrayAdapter(FaEditTask.this, prevScore, totalScore,
				null, false);
		totalScore.setText("" + test.totalMarks);
		listView.setAdapter(adapter);
		setAnimationValue();
		// setupActionBar();
	}

	// private void setupActionBar() {
	// final ActionBar actionBar = getActionBar();
	// actionBar.setDisplayShowCustomEnabled(true);
	// actionBar.setCustomView(R.layout.action_bar_text);
	// }

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
		int groupTask;
	};

	public class prevParam {
		String quesId;
		String parameter_1;
		int maxMarks;

		public prevParam(String quesId, String parameter_1, int maxMarks) {
			this.quesId = quesId;
			this.parameter_1 = parameter_1;
			this.maxMarks = maxMarks;
		}
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
			adapterModel = null;
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
										FaEditTask.this, "Select Task Name",
										"Task Name"));
						checkVal = 1;
						ivAddParameter.setEnabled(false);
						save.setEnabled(false);
						test.taskTopic = null;
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
						Cursor cursor = database
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
						database.execSQL(sql);
						dialog.dismiss();
						save.setEnabled(false);
						setupTaskTopicSpinner();
						test.taskTopic = null;
					}
				});
				dialog.show();
			} else {
				save.setEnabled(false);
				setupTaskTopicSpinner();
				test.taskTopic = null;
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
										FaEditTask.this, "Select Topic Name",
										"Topic Name"));
						save.setEnabled(false);
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
						Cursor cursor = database
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
						database.execSQL(sql);
						dialog.dismiss();
						if (prevScore.size() != 0)
							save.setEnabled(true);
						/* Set the adapter model to null */
					}
				});

				dialog.show();
			} else {
				if (prevScore.size() != 0)
					save.setEnabled(true);
				/* Set the adapter model to null */
			}
			break;
		}

	}

	private void setupTaskTopicSpinner() {
		spTaskTopic.setClickable(true);
		taskTopic = new ArrayList<String>();
		Cursor cursor = database.rawQuery(
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
					final Dialog innerDialog = new Dialog(FaEditTask.this,
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
									FaEditTask.this, list);
							paramListView.setAdapter(adapterModel);
							/* Insert data into table */
							Cursor cursor = database
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
							database.execSQL(sql);
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
						if (list.get(i).isSelected() & (test.taskTopic != null))
							/* Disable save button in case task topic is not set */
							save.setEnabled(true);
						if (list.get(i).isSelected()) {
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
					adapter = new ScoreArrayAdapter(FaEditTask.this, nextScore,
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
			/* This is where the task is finally updated */
			List<ScoreClass> listScores = new ArrayList<ScoreClass>();
			listScores = ((ScoreArrayAdapter) adapter).getData();

			for (int i = 0; i < listScores.size(); i++) {
				if (listScores.get(i).getScore() == 0) {
					Toast.makeText(this, "Parameter Score cannot be zero",
							Toast.LENGTH_SHORT).show();
					return;
				}
			}
			/* Update the task table with new values */

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

			String sql = "UPDATE test SET task_name = '" + test.taskName
					+ "' , task_topic = '" + test.taskTopic + "', comment ="
					+ comment + ", group_task = " + groupTask
					+ ", total_marks = " + totalMarks + " ,procedure = '"
					+ proc + "' where test_id = '" + testId + "'";

			database.execSQL(sql);

			/*
			 * Task has been updated, now update the parameters in the parameter
			 * table
			 */

			/* The two lists to be compared are prevParm and listScores */

			/* Add questions/parameters into the ques table */

			List<Integer> studentId = new ArrayList<Integer>();

			Cursor cursor = database.rawQuery(
					"Select student_id from student where class_id = "
							+ test.classId, null);
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {

				studentId.add(cursor.getInt(cursor
						.getColumnIndexOrThrow("student_id")));
				cursor.moveToNext();
			}
			cursor.close();

			int i = 0,
			j = 0,
			tempLength = 0;
			for (i = 0; i < prevParm.size(); i++) {
				tempLength = 0;
				for (j = 0; j < listScores.size(); j++) {
					int maxMarks = listScores.get(j).getMaxScore();
					int score = listScores.get(j).getScore();
					String name = listScores.get(j).getName();
					if (prevParm.get(i).parameter_1.contentEquals(name)) {
						/*
						 * Update the marks of this parameter and update marks
						 * of all the students
						 */
						if (score == prevParm.get(i).maxMarks) {
							/* No action is required in this case */
							listScores.remove(j);
						} else {
							/*
							 * Scaling is required and score needs to be updated
							 * for this parameter in the database
							 */
							sql = "UPDATE ques SET ques_max_score = " + score
									+ " where ques_id = '"
									+ prevParm.get(i).quesId + "'";
							database.execSQL(sql);

							int scalefactor = score / prevParm.get(i).maxMarks;

							/* remove this from the list scores */
							cursor = database.rawQuery(
									"Select student_id, response_id, response from response where ques_id = '"
											+ prevParm.get(i).quesId + "'",
									null);
							cursor.moveToFirst();
							while (!cursor.isAfterLast()) {
								/*
								 * Multiply marks of all the students with the
								 * scaling factor
								 */
								sql = "UPDATE response set response = "
										+ cursor.getInt(cursor
												.getColumnIndexOrThrow("response"))
										* scalefactor
										+ " where response_id = '"
										+ cursor.getString(cursor
												.getColumnIndexOrThrow("response_id"))
										+ "'";
								database.execSQL(sql);
								cursor.moveToNext();
							}
							cursor.close();
							listScores.remove(j);
						}
						tempLength = 1;
						break;
					}
				}
				if (0 == tempLength) {
					/*
					 * this parameter needs to be deleted from the database and
					 * from the student scores as well
					 */
					Log.e("Helllo", "Deleted " + prevParm.get(i).quesId);
					tempLength = 0;
					sql = "DELETE from ques where ques_id = '"
							+ prevParm.get(i).quesId + "'";
					database.execSQL(sql);
					/* Delete marks for each student for this ques id */
					sql = "DELETE from response where ques_id = '"
							+ prevParm.get(i).quesId + "'";
					database.execSQL(sql);
					/* Marks for each student for this has been deleted */
				}
			}

			cursor = database.rawQuery(
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

			Cursor cursorResponseId = database.rawQuery(
					"Select max(CAST(response_id as Int), 0) from response",
					null);

			int responseId = 0;
			cursorResponseId.moveToFirst();
			while (!cursorResponseId.isAfterLast()) {

				responseId = Integer.parseInt(cursorResponseId.getString(0));
				responseId += 1;
				cursorResponseId.moveToNext();
			}
			cursorResponseId.close();

			/* TODO - Ques number has to be decided on how to stored */
			int quesNum = 0;

			/* This is used to store all the previous id's */
			List<String> paramId = new ArrayList<String>();
			for (int l = 0; l < prevParm.size(); l++) {
				paramId.add(prevParm.get(l).quesId);
			}
			String[] stockArr = new String[paramId.size()];
			stockArr = paramId.toArray(stockArr);

			/* Add questions/parameters into the ques table */
			for (i = 0; i < listScores.size(); i++) {
				sql = "INSERT INTO ques VALUES ('"
						+ String.valueOf(quesId)
						+ "', '"
						+ testId
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

				database.execSQL(sql);
				/* Increement question ID by 1 */
				quesId += 1;
				quesNum += 1;

				/*
				 * In case marks of student have been added previously for this
				 * test, add new entries for this ques and make all marks zero
				 */
				for (int k = 0; k < studentId.size(); k++) {
					/*
					 * First check if the score has been entered for this
					 * student or not
					 */
					/*
					 * prevParm contains the id of all the parameters which were
					 * already there, if there is score for any one means that
					 * score needs to be added as 0
					 */

					/*
					 * Get the marks of the students if they have been saved
					 * earlier
					 */
					cursor = database.rawQuery(
							"Select response_id from response where student_id = '"
									+ studentId.get(k) + "' and ques_id in ("
									+ makePlaceholders(paramId.size()) + ")",
							stockArr);

					cursor.moveToFirst();
					if (!cursor.isAfterLast()) {
						/*
						 * This means some data is there and marks of this
						 * question need to be made 0 for this student
						 */
						sql = "INSERT INTO response VALUES ('"
								+ String.valueOf(responseId) + "', '"
								+ username + "', " + studentId.get(k) + ", '"
								+ quesId + "', " + 0 + ", '" + "" + "', '"
								+ "Present" + "', 'TimeStamp');";
						database.execSQL(sql);
						responseId += 1;

					}
					cursor.close();
				}
			}

			Toast.makeText(this, "Task Saved!", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(this, FaTaskList.class);
			finish();
			startActivity(intent);
			break;
		}
	}

	private String makePlaceholders(int size) {
		if (size < 1) {
			// It will lead to an invalid query anyway ..
			throw new RuntimeException("No placeholders");
		} else {
			StringBuilder sb = new StringBuilder(size * 2 - 1);
			sb.append("?");
			for (int i = 1; i < size; i++) {
				sb.append(",?");
			}
			return sb.toString();
		}
	}

	public List<Model> getModel() {
		Set<Model> set = new LinkedHashSet<Model>();
		Cursor cursor = database.rawQuery(
				"select parameter_name from parameter where task_name ='"
						+ test.taskName + "'", null);

		int check = 0;
		for (int i = 0; i < prevParm.size(); i++) {
			set.add(get(prevParm.get(i).parameter_1));
		}

		List<ScoreClass> score = new ArrayList<ScoreClass>();
		score = ((ScoreArrayAdapter) adapter).getData();
		for (int i = 0; i < score.size(); i++) {
			if (set.add(get(score.get(i).getName())))
				check++;
		}

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			set.add(get(cursor.getString(cursor
					.getColumnIndexOrThrow("parameter_name"))));
			cursor.moveToNext();
		}
		cursor.close();

		List<Model> list = new ArrayList<Model>(set);
		for (int i = 0; i < prevParm.size(); i++) {
			list.get(i).setSelected(true);
		}
		for (int j = prevParm.size(); j < prevParm.size() + check; j++) {
			list.get(j).setSelected(true);
		}
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
