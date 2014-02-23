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
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.learningoutcomes.R;
import com.example.learningoutcomes.Settings;
import com.example.learningoutcomes.database.LODatabaseHelper;
import com.example.learningoutcomes.database.LODatabaseUtility;
import com.example.learningoutcomes.swipeListView.SwipeDismissListViewTouchListener;

@SuppressLint("NewApi")
public class FaTaskList extends Activity {
	private Settings m_settings;
	private LODatabaseHelper m_databaseHelper;
	private SQLiteDatabase m_database;
	private DrawerLayout m_faTaskListDrawerLayout;
	private ListView lvTaskList;
	private ArrayAdapter<TaskListRow> m_adapter;
	private String username, teacher_term, testName;
	private int classId, subjectId, selectPosition;
	private boolean m_drawerOpen = false;

	/* Use testId.get(i) */
	List<String> testId;
	List<Integer> groupList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.fa_task_list);
		// Function call to set the title of the Action Bar
		/** Initialise database variables */
		m_databaseHelper = new LODatabaseHelper(this);
		m_database = m_databaseHelper.getWritableDatabase();
		LODatabaseUtility.getInstance().setDatabase(m_database);
		m_settings = new Settings(getApplicationContext(), 
				(Spinner) findViewById(R.id.spClass), (Spinner) findViewById(R.id.spTestName),
				(Spinner) findViewById(R.id.spTerm), (Spinner) findViewById(R.id.spSubject));
		m_settings.init();

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
		//drawer layout
		m_faTaskListDrawerLayout = (DrawerLayout) findViewById(R.id.faTaskListDrawerLayout);
		//Setting Swipe in action
		lvTaskList = (ListView) findViewById(R.id.lvTaskList);
		testId = new ArrayList<String>();
		groupList = new ArrayList<Integer>();
		m_adapter = new TaskListRowDataAdapter(this, getTaskList(),
				editTaskListener, scoreTaskListener, viewTaskListener);
		lvTaskList.setAdapter(m_adapter);

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
							m_adapter.remove(m_adapter.getItem(position));
						}
						m_adapter.notifyDataSetChanged();
					}
				});

		lvTaskList.setOnTouchListener(touchListener);
		// Setting this scroll listener is required to ensure that during
		// ListView scrolling,
		// we don't look for swipes.
		lvTaskList.setOnScrollListener(touchListener.makeScrollListener());
		lvTaskList
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

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

		Cursor cursor = m_database.rawQuery(
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
			groupList.add(groupTask);
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
		MenuItem add = menu.findItem(R.id.action_add);
		MenuItem search = menu.findItem(R.id.action_search);
		if(m_drawerOpen) {
			add.setVisible(false);
			search.setVisible(false);
		}
		else {
			add.setVisible(true);
			search.setVisible(true);
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menu) {
		switch (menu.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		case R.id.action_add:
			Intent intent = new Intent(getApplicationContext(), FaTask.class);
			finish();
			startActivity(intent);
			break;
		case R.id.action_logout:
			Toast toast = Toast.makeText(getApplicationContext(),
					"Under Construction", Toast.LENGTH_SHORT);
			toast.show();
			break;
		case R.id.action_settings:
			if(!m_faTaskListDrawerLayout.isDrawerOpen(Gravity.END)) {
				m_faTaskListDrawerLayout.openDrawer(Gravity.END);
				m_drawerOpen = true;
				invalidateOptionsMenu();
			}
			else {
				m_faTaskListDrawerLayout.closeDrawer(Gravity.END);
				m_drawerOpen = false;
				invalidateOptionsMenu();
			}
			break;
		default:
			return super.onOptionsItemSelected(menu);
		}
		return super.onOptionsItemSelected(menu);
	}

	View.OnClickListener editTaskListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getApplicationContext(),
					FaEditTask.class);
			intent.putExtra("test_id", "" + testId.get(selectPosition));
			startActivity(intent);
		}
	};

	View.OnClickListener scoreTaskListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent;
			if(groupList.get(selectPosition) == 1) {
				intent = new Intent(getApplicationContext(), FaCreateGroup.class);
			}
			else {
				intent = new Intent(getApplicationContext(), FaScore.class);
			}
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

//	@Override
//	protected void onPause() {
//		m_databaseHelper.close();
//		super.onPause();
//	}
//
//	@Override
//	protected void onResume() {
//		m_database = m_databaseHelper.getWritableDatabase();
//		LODatabaseUtility.getInstance().setDatabase(m_database);
//		super.onResume();
//	}

}
