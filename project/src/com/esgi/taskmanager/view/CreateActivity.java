package com.esgi.taskmanager.view;

import java.util.Calendar;
import com.esgi.taskmanager.R;
import com.esgi.taskmanager.model.DataBaseAdapter;
import com.esgi.taskmanager.model.Task;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class CreateActivity extends Activity {

	private static final String TAG = "CreateActivity";
	private int mYear, mMonth, mDay, mHour, mMinute;
	final Intent intent = getIntent();
	private TextView dateValue, timeValue;
	private EditText textTitle, textDescription;
	private DataBaseAdapter dbAdapter;
	private Task task;
	private Boolean editMode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create);
		editMode=false;
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		dateValue = (TextView) findViewById(R.id.dateValue);
		timeValue = (TextView)findViewById(R.id.timeValue);
		textTitle = (EditText)findViewById(R.id.textTitle);
		textDescription = (EditText)findViewById(R.id.textDescription);
		final Calendar c = Calendar.getInstance();

		dbAdapter = new DataBaseAdapter(this);
		task = new Task();

		if(getIntent().getExtras() != null){
			task = (Task) getIntent().getParcelableExtra("task");
			editMode=true;
		}

		if(editMode){
			textTitle.setText(task.getTitle());
			textDescription.setText(task.getDescription());
			dateValue.setText(task.getDate());
			timeValue.setText(task.getHour());
		}else{
			mYear = c.get(Calendar.YEAR);
			mMonth = c.get(Calendar.MONTH);
			mDay = c.get(Calendar.DAY_OF_MONTH);
			mHour = c.get(Calendar.HOUR_OF_DAY);
			mMinute = c.get(Calendar.MINUTE);
			dateValue.setText(mDay + "-" + (mMonth + 1) + "-" + mYear);
			timeValue.setText(mHour + ":" + mMinute);
		}
	}

	public void showCalendarDialog(View view){
		Log.i(TAG, "Tap on DateValue");

		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

		DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				dateValue.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
			}
		}, mYear, mMonth, mDay);
		dpd.show();
	}

	public void showTimeDialog(View view){
		Log.i(TAG, "Tap on timeValue");

		final Calendar c = Calendar.getInstance();
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);

		TimePickerDialog tpd = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker view, int hour, int minute) {
				timeValue.setText(hour + ":" + minute);
			}
		}, mHour, mMinute, false);
		tpd.show();
	}

	public void saveTask(View view){
		Log.i(TAG, "Tap on Save button");
		// is fields empty ?
		if(textTitle.getText().length() == 0 
				|| textDescription.getText().length() == 0){
			Toast.makeText(getApplicationContext(), R.string.pleaseFillTheFields, Toast.LENGTH_SHORT).show();
			return;
		}

		// Save Task object
		task.setDate(dateValue.getText().toString());
		task.setHour(timeValue.getText().toString());
		task.setTitle(textTitle.getText().toString());
		task.setDescription(textDescription.getText().toString());
		task.setCategory("VIDE");
		task.setStatus(1);
		task.setPriority(1);

		if(dbAdapter==null){
			Log.i(TAG, "DBAdapter == null");
			return;
		}

		dbAdapter.open();
		if(editMode){
			dbAdapter.editTask(task);
			Toast.makeText(getApplicationContext(), R.string.editedIntoDB, Toast.LENGTH_SHORT).show();
		}else{
			dbAdapter.insertTask(task);
			Toast.makeText(getApplicationContext(), R.string.savedIntoDB, Toast.LENGTH_SHORT).show();
		}
		dbAdapter.close();
		finish();
	}
	
	public void deleteTask(View view){
		if(editMode){
			dbAdapter.open();
			dbAdapter.deleteTask(task);
			dbAdapter.close();
			Toast.makeText(getApplicationContext(), R.string.deletedIntoDB, Toast.LENGTH_SHORT).show();
		}
		finish();
	}

}
