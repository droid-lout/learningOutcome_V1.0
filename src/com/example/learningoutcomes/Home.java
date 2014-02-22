package com.example.learningoutcomes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.learningoutcomes.Formative.FaTask;
import com.example.learningoutcomes.Formative.FaTaskList;
import com.example.learningoutcomes.database.LODatabaseHelper;
import com.example.learningoutcomes.database.LODatabaseUtility;

@SuppressLint("NewApi")
public class Home extends Activity implements OnClickListener {
	private Settings m_settings;
	private SQLiteDatabase m_database;
	private LODatabaseHelper m_databaseHelper;
	private DrawerLayout m_drawerLayout;
	private Button m_btFaTask;
	private Button m_btSaTask, m_btCsaTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		m_databaseHelper = new LODatabaseHelper(getApplicationContext());
		m_database = m_databaseHelper.getWritableDatabase();
		LODatabaseUtility.getInstance().setDatabase(m_database);
		m_settings = new Settings(getApplicationContext(), 
				(Spinner) findViewById(R.id.spClass), (Spinner) findViewById(R.id.spTestName),
				(Spinner) findViewById(R.id.spTerm), (Spinner) findViewById(R.id.spSubject));
		m_settings.init();

		// Function call to set the title of the Action Bar
		setTitle(setName());
		m_drawerLayout = (DrawerLayout) findViewById(R.id.homeDrawerLayout);
		m_btFaTask = (Button) findViewById(R.id.btFaTask);
		m_btFaTask.setOnClickListener(this);
		m_btSaTask = (Button) findViewById(R.id.btSaTask);
		m_btSaTask.setOnClickListener(this);
		m_btCsaTask = (Button) findViewById(R.id.btCsaTask);
		m_btCsaTask.setOnClickListener(this);
		
	}

	private CharSequence setName() {
		CharSequence welcomeName = "";
		SharedPreferences prefs = this.getSharedPreferences("global_settings",
				Context.MODE_PRIVATE);
		String username = prefs.getString("username", "user");
		Cursor cursor = LODatabaseUtility.getInstance().cursorFromQuery(
				"select name from teacher where username = '" + username + "'");
		welcomeName = LODatabaseUtility.getInstance().dataStringfromCursor(cursor, "name");
		cursor.close();
		welcomeName = "Hi! " + welcomeName;
		return welcomeName;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btFaTask:
			Intent intent = new Intent(this, FaTaskList.class);
			intent.putExtra("type", "Formative");
			startActivity(intent);
			break;
		case R.id.btCsaTask:
			Toast.makeText(this, "This is SPARTA !", Toast.LENGTH_SHORT).show();
			break;
		case R.id.btSaTask:
			Toast.makeText(this, "Under Construnction !", Toast.LENGTH_SHORT)
			.show();
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.action_bar, menu);
		MenuItem add = menu.findItem(R.id.action_add);
		MenuItem search = menu.findItem(R.id.action_search);
		add.setVisible(false);
		search.setVisible(false);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menu) {
		switch (menu.getItemId()) {
		case R.id.action_add:
			Intent intent = new Intent(getApplicationContext(), FaTask.class);
			startActivity(intent);
			break;
		case R.id.action_logout:
			/* Clearing username for now for log out */
			SharedPreferences shPref = getSharedPreferences("global_settings",
					MODE_PRIVATE);
			Editor et = shPref.edit();
			et.putString("username", "");
			et.commit();
			Intent i = new Intent(this, Login.class);
			finish();
			startActivity(i);
			break;
		case R.id.action_settings:
			if(!m_drawerLayout.isDrawerOpen(Gravity.END))
				m_drawerLayout.openDrawer(Gravity.END);
			else
				m_drawerLayout.closeDrawer(Gravity.END);
			break;
		default:
			return super.onOptionsItemSelected(menu);
		}
		return super.onOptionsItemSelected(menu);
	}
}
