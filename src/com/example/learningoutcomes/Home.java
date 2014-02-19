package com.example.learningoutcomes;

import java.util.HashSet;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.learningoutcomes.Formative.FaTask;
import com.example.learningoutcomes.Formative.FaTaskList;
import com.example.learningoutcomes.Formative.Model;
import com.example.learningoutcomes.database.LODatabaseHelper;

@SuppressLint("NewApi")
public class Home extends Activity implements OnClickListener {
	Settings m_settings;
	SQLiteDatabase database;
	LODatabaseHelper databaseHelper;

	Button btFaTask;;
	Button btSaTask, btCsaTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		m_settings = new Settings(getApplicationContext(), 
				(Spinner) findViewById(R.id.spClass), (Spinner) findViewById(R.id.spTestName),
				(Spinner) findViewById(R.id.spTerm), (Spinner) findViewById(R.id.spSubject));
		databaseHelper = new LODatabaseHelper(this);
		database = databaseHelper.getWritableDatabase();
		
		// Function call to set the title of the Action Bar
		setTitle(setName());
		btFaTask = (Button) findViewById(R.id.btFaTask);
		btFaTask.setOnClickListener(this);
		btSaTask = (Button) findViewById(R.id.btSaTask);
		btSaTask.setOnClickListener(this);
		btCsaTask = (Button) findViewById(R.id.btCsaTask);
		btCsaTask.setOnClickListener(this);
	}

	private CharSequence setName() {
		CharSequence welcomeName = "";
		SharedPreferences prefs = this.getSharedPreferences("global_settings",
				Context.MODE_PRIVATE);
		String username = prefs.getString("username", "user");
		Cursor cursor = database.rawQuery(
				"select name from teacher where username = '" + username + "'",
				null);
		// System.out.println(cursor);
		cursor.moveToFirst();
		welcomeName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
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
			return super.onOptionsItemSelected(menu);
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

			return super.onOptionsItemSelected(menu);
		case R.id.action_settings:
/*			i = new Intent(this, Settings.class);
			startActivity(i);*/
			return super.onOptionsItemSelected(menu);
		default:
			return super.onOptionsItemSelected(menu);
		}
	}
}
