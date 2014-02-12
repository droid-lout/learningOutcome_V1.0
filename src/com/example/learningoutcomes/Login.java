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

import com.example.learningoutcomes.Formative.FaTask;
import com.example.learningoutcomes.database.LODatabaseHelper;

public class Login extends Activity implements View.OnClickListener {

	SQLiteDatabase database;
	LODatabaseHelper databaseHelper;

	TextView register;
	TextView forgotPassword;
	Button btLogin;
	Animation anim;

	EditText etUsername, etPassword;
	String username, password, role;
	TextView displayMessage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getActionBar().hide();
		SharedPreferences prefs = this.getSharedPreferences("global_settings",
				Context.MODE_PRIVATE);
		String username = prefs.getString("username", "");

		Log.e("User", username);
		if (username.contentEquals("")) {
			// do Nothing and continue further the user hasn't logged in yet.
		} else {

			Intent i = new Intent(getApplicationContext(), Home.class);
			finish();
			startActivity(i);
		}
		setContentView(R.layout.login);
		/** Initialise database variables */
		databaseHelper = new LODatabaseHelper(this);
		database = databaseHelper.getWritableDatabase();
		register = (TextView) findViewById(R.id.register);
		register.setOnClickListener(this);
		forgotPassword = (TextView) findViewById(R.id.forgotPassword);
		forgotPassword.setOnClickListener(this);
		btLogin = (Button) findViewById(R.id.btLogin);
		btLogin.setOnClickListener(this);

		displayMessage = (TextView) findViewById(R.id.tvDisplayMessage);
		etUsername = (EditText) findViewById(R.id.etUserName);
		etPassword = (EditText) findViewById(R.id.etPassword);
		setAnimationValue();

	}

	private void setAnimationValue() {
		anim = new AlphaAnimation(0.0f, 1.0f);
		anim.setDuration(100); // You can manage the time of the blink with
								// this parameter
		anim.setStartOffset(20);
		anim.setRepeatMode(Animation.REVERSE);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.register:
			register.startAnimation(anim);
			Toast.makeText(this, "Register - Under Construction",
					Toast.LENGTH_SHORT).show();
			break;
		case R.id.forgotPassword:
			forgotPassword.startAnimation(anim);
			Toast.makeText(this, "Forgot Password - Under Construction",
					Toast.LENGTH_SHORT).show();
			break;
		case R.id.btLogin:
			/* check the username and password */
			if (etUsername.getText().length() == 0) {
				displayMessage.setText("Enter User Name");
				if (etPassword.getText().length() == 0) {
					displayMessage.setText("Enter User Name and Password");
				}
				return;
			}
			if (etPassword.getText().length() == 0) {
				displayMessage.setText("Enter User Password");
				return;
			}
			username = etUsername.getText().toString();
			password = etPassword.getText().toString();
			Cursor cursor = database.rawQuery(
					"select * from login where username = '" + username
							+ "' and password = '" + password + "'", null);
			/* Create some shared preferences here for global settings */
			/* Move to an intermediate page here */

			cursor.moveToFirst();
			if (cursor.isAfterLast()) {
				displayMessage.setText("Invalid username password !");
				return;
			} else {
				username = cursor.getString(cursor
						.getColumnIndexOrThrow("username"));

				role = cursor.getString(cursor.getColumnIndexOrThrow("role"));
			}
			cursor.close();

			/* Put the role of the user and the username in shared preferences */
			SharedPreferences shPref = getSharedPreferences("global_settings",
					MODE_PRIVATE);
			Editor et = shPref.edit();
			et.putString("username", username);
			et.putString("role", role);
			String temp = shPref.getString(username + "term", "");
			if (temp.contentEquals(""))
				et.putString(username + "term", "Term 2");
			temp = shPref.getString(username + "subject", "");
			if (temp.contentEquals(""))
				et.putString(username + "subject", "6");
			temp = shPref.getString(username + "class", "");
			if (temp.contentEquals(""))
				et.putString(username + "class", "2");
			temp = shPref.getString(username + "testname", "");
			if (temp.contentEquals(""))
				et.putString(username + "testname", "UnitTest 3");
			et.putString(username + "school_id", "1");
			et.commit();
			Intent i = new Intent(this, Home.class);
			finish();
			startActivity(i);
			break;
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
}
