package com.example.learningoutcomes.database;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class LODatabaseUtility {
	private static LODatabaseUtility m_instance = null;
	private SQLiteDatabase m_database;
	private LODatabaseUtility() {}
	
	public static synchronized LODatabaseUtility getInstance() {
		if(m_instance == null) {
			m_instance = new LODatabaseUtility();
		}
		return m_instance;
	}
	
	/*
	 * Returns the cursor from the query provided by @param
	 * @param query: Query we need to process 
	 */
	public Cursor cursorFromQuery(String query) {
		Cursor cursor = m_database.rawQuery(query, null);
		return cursor;
		
	}
	
	/*
	 * populate the data we need to from the database in a list
	 * @param cursor: the cursor which you will use
	 * @param column: the column you want to search
	 */
	public ArrayList<String> dataListfromCursor(Cursor cursor, String column) {
		ArrayList<String> list = new ArrayList<String>();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			String term = cursor.getString(cursor
					.getColumnIndexOrThrow(column));
			list.add(term);
			cursor.moveToNext();
		}
		cursor.close();
		return list;
	}
	
	/*
	 * set database
	 */
	public void setDatabse(SQLiteDatabase database) {
		m_database = database;
	}
}
