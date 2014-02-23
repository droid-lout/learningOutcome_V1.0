package com.example.learningoutcomes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learningoutcomes.database.LODatabaseHelper;
import com.example.learningoutcomes.database.LODatabaseUtility;

public class Login extends Activity implements View.OnClickListener {

	private SQLiteDatabase m_database;
	private LODatabaseHelper m_databaseHelper;

	private TextView m_register;
	private TextView m_forgotPassword;
	private Button m_btLogin;
	private Animation m_anim;

	private EditText m_etUsername, m_etPassword;
	private String m_username, m_password, m_role, m_schoolId;
	private TextView m_displayMessage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getActionBar().hide();
		SharedPreferences prefs = this.getSharedPreferences("global_settings",
				Context.MODE_PRIVATE);
		m_username = prefs.getString("username", "");

		if (m_username.contentEquals("")) {
			// do Nothing and continue further the user hasn't logged in yet.
		} else {

			Intent i = new Intent(getApplicationContext(), Home.class);
			finish();
			startActivity(i);
		}
		setContentView(R.layout.login);
		/** Initialise m_database variables */
		m_databaseHelper = new LODatabaseHelper(getApplicationContext());
		m_database = m_databaseHelper.getWritableDatabase();
		LODatabaseUtility.getInstance().setDatabase(m_database);
		m_register = (TextView) findViewById(R.id.register);
		m_register.setOnClickListener(this);
		m_forgotPassword = (TextView) findViewById(R.id.forgotPassword);
		m_forgotPassword.setOnClickListener(this);
		m_btLogin = (Button) findViewById(R.id.btLogin);
		m_btLogin.setOnClickListener(this);

		m_displayMessage = (TextView) findViewById(R.id.tvDisplayMessage);
		m_etUsername = (EditText) findViewById(R.id.etUserName);
		m_etPassword = (EditText) findViewById(R.id.etPassword);
		setAnimationValue();

	}

	private void setAnimationValue() {
		m_anim = new AlphaAnimation(0.0f, 1.0f);
		m_anim.setDuration(100); // You can manage the time of the blink with
		// this parameter
		m_anim.setStartOffset(20);
		m_anim.setRepeatMode(Animation.REVERSE);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.register:
			m_register.startAnimation(m_anim);
			Toast.makeText(this, "m_register - Under Construction",
					Toast.LENGTH_SHORT).show();
			break;
		case R.id.forgotPassword:
			m_forgotPassword.startAnimation(m_anim);
			Toast.makeText(this, "Forgot Password - Under Construction",
					Toast.LENGTH_SHORT).show();
			break;
		case R.id.btLogin:
			/* check the username and password */
			if (m_etUsername.getText().length() == 0) {
				m_displayMessage.setText("Enter User Name");
				if (m_etPassword.getText().length() == 0) {
					m_displayMessage.setText("Enter User Name and Password");
				}
				return;
			}
			if (m_etPassword.getText().length() == 0) {
				m_displayMessage.setText("Enter User Password");
				return;
			}
			m_username = m_etUsername.getText().toString();
			m_password = m_etPassword.getText().toString();
			Cursor cursor = LODatabaseUtility.getInstance().cursorFromQuery(
					"select * from login where username = '" + m_username
					+ "' and password = '" + m_password + "'");
			if (cursor.isAfterLast()) {
				m_displayMessage.setText("Invalid username password !");
				return;
			} else {
				m_username = LODatabaseUtility.getInstance().dataStringfromCursor(cursor, "username");
				m_role = LODatabaseUtility.getInstance().dataStringfromCursor(cursor, "role");
				m_schoolId = LODatabaseUtility.getInstance().dataStringfromCursor(cursor, "school_id");
				Log.e("Login", m_schoolId);
			}
			cursor.close();

			/* Put the role of the user and the username in shared preferences */
			SharedPreferences shPref = getSharedPreferences("global_settings",
					MODE_PRIVATE);
			Editor et = shPref.edit();
			et.putString("username", m_username);
			et.putString("role", m_role);
			String temp = shPref.getString(m_username + "term", "");
			if (temp.contentEquals(""))
				et.putString(m_username + "term", "Term 1");
			temp = shPref.getString(m_username + "subject", "");
			if (temp.contentEquals(""))
				et.putString(m_username + "subject", "1");
			temp = shPref.getString(m_username + "class", "");
			if (temp.contentEquals(""))
				et.putString(m_username + "class", "1");
			temp = shPref.getString(m_username + "testname", "");
			if (temp.contentEquals(""))
				et.putString(m_username + "testname", "UnitTest 3");
			et.putString(m_username + "school_id", m_schoolId);
			et.commit();
			Intent i = new Intent(this, Home.class);
			finish();
			startActivity(i);
			break;
		}
	}

	@Override
	protected void onPause() {
		m_databaseHelper.close();
		super.onPause();
	}

	@Override
	protected void onResume() {
		m_database = m_databaseHelper.getWritableDatabase();
		LODatabaseUtility.getInstance().setDatabase(m_database);
		super.onResume();
	}
}
