package com.example.learningoutcomes.Formative;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.learningoutcomes.R;
import com.example.learningoutcomes.database.LODatabaseHelper;
import com.example.learningoutcomes.swipeListView.SwipeDismissListViewTouchListener;

@SuppressLint("NewApi")
public class FaTaskList extends Activity {

	private ListView lvTaskList;
	ArrayAdapter<TaskListRow> adapter;

	SQLiteDatabase database;
	LODatabaseHelper databaseHelper;


	String username, teacher_term, testName;
	int classId, subjectId, selectPosition;

	/* Use testId.get(i) */
	List<String> testId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.fa_task_list);
		// Function call to set the title of the Action Bar
		/** Initialise database variables */
		databaseHelper = new LODatabaseHelper(this);
		database = databaseHelper.getWritableDatabase();
		setTitle(setName());
		getActionBar().setDisplayHomeAsUpEnabled(true);

		/* Get global values from global settings */
		SharedPreferences shPref = getSharedPreferences("global_settings",
				MODE_PRIVATE);
		username = shPref.getString("username", "");
		classId = Integer.parseInt(shPref.getString(username + "class", ""));
		subjectId = Integer
				.parseInt(shPref.getString(username + "subject", ""));
		teacher_term = shPref.getString(username + "term", "");
		testName = shPref.getString(username + "testname", "");

		lvTaskList = (ListView) findViewById(R.id.lvTaskList);
		testId = new ArrayList<String>();
		adapter = new TaskListRowDataAdapter(this, getTaskList(),
				editTaskListener, scoreTaskListener, viewTaskListener);
		lvTaskList.setAdapter(adapter);

		final SwipeDismissListViewTouchListener touchListener = new SwipeDismissListViewTouchListener(
				lvTaskList,
				new SwipeDismissListViewTouchListener.DismissCallbacks() {
					@Override
					public boolean canDismiss(int position) {
						return true;
					}

					@Override
					public void onDismiss(ListView listView,
							int[] reverseSortedPositions) {
						for (int position : reverseSortedPositions) {
							adapter.remove(adapter.getItem(position));
						}
						adapter.notifyDataSetChanged();
					}
				});

		lvTaskList.setOnTouchListener(touchListener);
		// Setting this scroll listener is required to ensure that during
		// ListView scrolling,
		// we don't look for swipes.
		lvTaskList.setOnScrollListener(touchListener.makeScrollListener());
		lvTaskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long id) {
				selectPosition = position;
				touchListener.clickAnimation();

			}
		});
		Button createButton = (Button) findViewById(R.id.btFACreateTask);
		createButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						FaTask.class);
				finish();
				startActivity(intent);
			}
		});
	}

	private CharSequence setName() {
		CharSequence welcomeName = "Formative";
		// welcomeName = getIntent().getExtras().getString("type");
		return welcomeName;
	}

	private List<TaskListRow> getTaskList() {
		List<TaskListRow> list = new ArrayList<TaskListRow>();

		Cursor cursor = database.rawQuery(
				"select * from test where class_id = " + classId
				+ " and subject_id = " + subjectId
				+ " and teacher_term = '" + teacher_term
				+ "' and testname = '" + testName
				+ "' and test_type =  'Formative"
				+ "' and username = '" + username + "'", null);
		cursor.moveToFirst();
		/* Adding static date for now */
		while (!cursor.isAfterLast()) {
			int taskNum = Integer.parseInt(cursor.getString(cursor
					.getColumnIndexOrThrow("task_number")));
			String taskname = cursor.getString(cursor
					.getColumnIndexOrThrow("task_name"));
			String taskTopic = cursor.getString(cursor
					.getColumnIndexOrThrow("task_topic"));
			int maxMarks = Integer.parseInt(cursor.getString(cursor
					.getColumnIndexOrThrow("total_marks")));
			int groupTask = Integer.parseInt(cursor.getString(cursor
					.getColumnIndexOrThrow("group_task")));
			Boolean group = false;
			if (groupTask == 1)
				group = true;
			testId.add(cursor.getString(cursor.getColumnIndexOrThrow("test_id")));
			list.add(createTaskListRow(taskNum, taskname, "20/01/33",
					taskTopic, group, maxMarks));
			cursor.moveToNext();
		}
		cursor.close();

		return list;
	}

	private TaskListRow createTaskListRow(int taskNum, String taskName,
			String Date, String topicName, boolean group, int maxMarks) {
		return new TaskListRow(taskNum, taskName, Date, topicName, group,
				maxMarks);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.action_bar, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menu) {
		switch (menu.getItemId()) {
		case android.R.id.home:
			finish();
			return super.onOptionsItemSelected(menu);
		case R.id.action_add:
			createTask();
			Intent intent = new Intent(getApplicationContext(), FaTask.class);
			finish();
			startActivity(intent);
			return super.onOptionsItemSelected(menu);
		case R.id.action_logout:
			Toast toast = Toast.makeText(getApplicationContext(),
					"Under Construction", Toast.LENGTH_SHORT);
			toast.show();
			return super.onOptionsItemSelected(menu);
		default:
			return super.onOptionsItemSelected(menu);
		}
	}

	private void createTask() {
		// if(getIntent().getExtras().getString("type") == "Formative")
		// Log.d("Activity Type", "Formative");
		//
	}

	View.OnClickListener editTaskListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getApplicationContext(), FaTask.class);
			intent.putExtra("test_id", "" + testId.get(selectPosition));
			startActivity(intent);
		}
	};

	View.OnClickListener scoreTaskListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getApplicationContext(), FaScore.class);
			intent.putExtra("test_id", "" + testId.get(selectPosition));
			startActivity(intent);
		}
	};
	View.OnClickListener viewTaskListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Toast toast = Toast.makeText(getApplicationContext(),
					"Why so serious?", Toast.LENGTH_SHORT);
			toast.show();
		}
	};
}
