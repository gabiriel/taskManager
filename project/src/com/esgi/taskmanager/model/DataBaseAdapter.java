package com.esgi.taskmanager.model;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DataBaseAdapter {
	public static final String TAG = "DataBaseAdapter";

	private DataBaseHandler databaseHandler;
	private SQLiteDatabase sqldb;
	private final Context context;

	public static final String DATABASE_NAME = "taskManager.db";
	public static final int DATABASE_VERSION = 1;

	// TASK TABLE
	public static final String TASK_TABLE_NAME = "_task";
	public static final String TASK_ID = "_id";
	public static final String TASK_TITLE = "_title";
	public static final String TASK_DESCRIPTION = "_description";
	public static final String TASK_DUE_DATE = "_due_date";
	public static final String TASK_DUE_HOUR = "_due_hour";
	public static final String TASK_PRIORITY = "_priority";
	public static final String TASK_STATUS = "_status";

	// SQL COMMAND
	public static final String SQL_CREATE_TABLE_TASK = 
			"CREATE TABLE " + TASK_TABLE_NAME + " (" +
					TASK_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					TASK_TITLE + " TEXT, " +
					TASK_DESCRIPTION + " TEXT, "+
					TASK_DUE_DATE + " TEXT, "+
					TASK_DUE_HOUR + " TEXT, "+
					TASK_PRIORITY + " REAL, "+
					TASK_STATUS + " REAL);";
	public static final String SQL_SELECT_ALL_TASK = 
			"SELECT * TABLE " + TASK_TABLE_NAME + ";";
	public static final String SQL_DROP_TABLE_TASK = 
			"DROP TABLE IF EXISTS " + TASK_TABLE_NAME + ";";

	public DataBaseAdapter(Context context){
		this.context = context;
	}

	public DataBaseAdapter open(){
		databaseHandler = new DataBaseHandler(context, DATABASE_NAME, null, DATABASE_VERSION);
		sqldb = databaseHandler.getWritableDatabase();
		return this;
	}

	public void close(){
		if(databaseHandler != null)
			databaseHandler.close();
	}

	private class DataBaseHandler extends SQLiteOpenHelper{
		public DataBaseHandler(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.i(TAG, "TABLE CREATE");
			db.execSQL(SQL_CREATE_TABLE_TASK);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL(SQL_DROP_TABLE_TASK);
			onCreate(db);
		}
	}

	public void insertTask(Task task){
		ContentValues initialValues = new ContentValues();
		initialValues.put(TASK_TITLE, task.getTitle());
		initialValues.put(TASK_DESCRIPTION, task.getDescription());
		initialValues.put(TASK_DUE_DATE, task.getDate());
		initialValues.put(TASK_DUE_HOUR, task.getHour());
		initialValues.put(TASK_PRIORITY, task.getPriority());
		initialValues.put(TASK_STATUS, task.getStatus());
		sqldb.insert(TASK_TABLE_NAME, null, initialValues);
		Log.i(TAG, "ADD TASK INTO TABLE");
	}

	public void editTask(Task task){
		ContentValues updateValues = new ContentValues();
		updateValues.put(TASK_TITLE, task.getTitle());
		updateValues.put(TASK_DESCRIPTION, task.getDescription());
		updateValues.put(TASK_DUE_DATE, task.getDate());
		updateValues.put(TASK_DUE_HOUR, task.getHour());
		updateValues.put(TASK_PRIORITY, task.getPriority());
		updateValues.put(TASK_STATUS, task.getStatus());
		sqldb.update(TASK_TABLE_NAME, updateValues, TASK_ID + " = '" + task.getId() + "'", null);
		Log.i(TAG, "UPDATE TASK INTO TABLE");
	}
	
	public void deleteTask(Task task){
		sqldb.delete(TASK_TABLE_NAME, TASK_ID + "= '"+ task.getId() +"'" , null);
		Log.i(TAG, "DELETE TASK ("+ task.getId() +") INTO TABLE");
	}

	private Task cursorToTask(Cursor cursor) {
		Task task = new Task();
		task.setId(cursor.getLong(0));
		task.setTitle(cursor.getString(1));
		task.setDescription(cursor.getString(2));
		task.setDate(cursor.getString(3));
		task.setHour(cursor.getString(4));
		task.setPriority(cursor.getInt(5));
		task.setStatus(cursor.getInt(6));
		return task;
	}

	public List<Task> getTasks(){
		List<Task> list = new ArrayList<Task>();
		String[] allColumns = {TASK_ID, TASK_TITLE, TASK_DESCRIPTION, TASK_DUE_DATE, TASK_DUE_HOUR, TASK_PRIORITY, TASK_STATUS };
		Cursor cursor = sqldb.query(TASK_TABLE_NAME, allColumns, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Task task = cursorToTask(cursor);
			list.add(task);
			cursor.moveToNext();
		}
		cursor.close();
		return list;
	}
}
