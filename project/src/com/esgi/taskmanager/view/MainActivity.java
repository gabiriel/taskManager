package com.esgi.taskmanager.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.esgi.taskmanager.R;
import com.esgi.taskmanager.model.DataBaseAdapter;
import com.esgi.taskmanager.model.MainListItemAdapter;
import com.esgi.taskmanager.model.Task;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ListActivity{

	final static String TAG = "MainActivity";
	private DataBaseAdapter dbAdapter;
	private List<Task> list;
	private ListView listView;

	// PopUp Window Filter
	private TextView filterDateStart, filterDateEnd;
	private EditText filterTitle;
	private Spinner spinnerStatus;
	private int year_start, month_start, day_start, year_end, month_end, day_end ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	protected void onStart() {
		super.onStart();
		listView = (ListView) findViewById(android.R.id.list);

		dbAdapter = new DataBaseAdapter(this);
		dbAdapter.open();
		list = dbAdapter.getTasks();
		dbAdapter.close();
		listView.setAdapter(new MainListItemAdapter(this, list));

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Task taskSelected = (Task) listView.getItemAtPosition(position);
				Log.i(TAG, "Task selected : " + taskSelected.getId());
				Intent editActivity = new Intent(view.getContext(), CreateActivity.class);
				editActivity.putExtra("task", taskSelected);
				startActivity(editActivity);
			}
		}); 
	}

	public void addTask(View view) {
		Log.i(TAG, "Click on Add Button");
		Intent createActivity = new Intent(this, CreateActivity.class);
		startActivity(createActivity);
	}

	public void deleteTask(View view) {
		Log.i(TAG, "Click on Delete Button");
	}

	public void filterTask(View view) {
		Log.i(TAG, "Click on Filter Button");
		showPopup(MainActivity.this);

	}

	public void sortTask(View view) {
		Log.i(TAG, "Click on Sort Button");
		showPopupSort(MainActivity.this);
	}

	private void showPopupSort(final Activity context) {
		int popupWidth = WindowManager.LayoutParams.WRAP_CONTENT;
		int popupHeight = WindowManager.LayoutParams.WRAP_CONTENT;

		LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup);
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = layoutInflater.inflate(R.layout.popup_sort, viewGroup);

		TextView sortTitle = (TextView) layout.findViewById(R.id.sortTitle);
		TextView sortPriority = (TextView) layout.findViewById(R.id.sortPriority);
		TextView sortStatus = (TextView) layout.findViewById(R.id.sortStatus);

		// Creating the PopupWindow
		final PopupWindow popup = new PopupWindow(context);
		popup.setContentView(layout);
		popup.setWidth(popupWidth);
		popup.setHeight(popupHeight);
		popup.setFocusable(true);

		int OFFSET_X = 30;
		int OFFSET_Y = 30;
		popup.setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.white)));
		popup.showAtLocation(layout, Gravity.AXIS_CLIP, OFFSET_X, OFFSET_Y);

		sortTitle.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Collections.sort(list, new Task.CompareTitle());
				Toast.makeText(context, "sort Title", Toast.LENGTH_LONG).show();
				listView.setAdapter(new MainListItemAdapter(context, list));
				return false;
			}
		});

		sortPriority.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Collections.sort(list, new Task.ComparePriority());
				Toast.makeText(context, "sort Priority", Toast.LENGTH_LONG).show();
				listView.setAdapter(new MainListItemAdapter(context, list));
				return false;
			}
		});

		sortStatus.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Collections.sort(list, new Task.CompareStatus());
				Toast.makeText(context, "sort Status", Toast.LENGTH_LONG).show();
				listView.setAdapter(new MainListItemAdapter(context, list));
				return false;
			}
		});

	}

	@Override
	protected void onResume() {
		dbAdapter.open();
		super.onResume();
	}

	@Override
	protected void onPause() {
		dbAdapter.close();
		super.onPause();
	}

	private void showPopup(final Activity context) {
		int popupWidth = WindowManager.LayoutParams.WRAP_CONTENT;
		int popupHeight = WindowManager.LayoutParams.WRAP_CONTENT;

		LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup);
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = layoutInflater.inflate(R.layout.popup_filter, viewGroup);

		filterTitle = (EditText) layout.findViewById(R.id.textTitle);
		filterDateStart = (TextView) layout.findViewById(R.id.dateStartValue);
		filterDateEnd = (TextView) layout.findViewById(R.id.dateEndValue);

		final Calendar c = Calendar.getInstance();
		year_start = c.get(Calendar.YEAR);
		month_start = c.get(Calendar.MONTH)+1;
		day_start = c.get(Calendar.DAY_OF_MONTH);
		year_end = year_start+1;
		month_end = month_start;
		day_end = day_start;
		filterDateStart.setText(day_start + "-" + month_start + "-" + year_start);
		filterDateEnd.setText(day_end + "-" + month_end + "-" + year_end);

		spinnerStatus = (Spinner) layout.findViewById(R.id.status_spinner_popup);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.status_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerStatus.setAdapter(adapter);

		// Creating the PopupWindow
		final PopupWindow popup = new PopupWindow(context);
		popup.setContentView(layout);
		popup.setWidth(popupWidth);
		popup.setHeight(popupHeight);
		popup.setFocusable(true);

		int OFFSET_X = 30;
		int OFFSET_Y = 30;
		popup.setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.white)));
		popup.showAtLocation(layout, Gravity.CENTER, OFFSET_X, OFFSET_Y);
		Button close = (Button) layout.findViewById(R.id.close);
		Button valid = (Button) layout.findViewById(R.id.valid);

		close.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				popup.dismiss();
				return false;
			}
		});

		valid.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				try {
					validFilters(v);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				popup.dismiss();

				return false;
			}
		});
	}

	private void validFilters(View v) throws ParseException{
		String title = filterTitle.getText().toString();
		String dateS = filterDateStart.getText().toString();
		String dateE = filterDateEnd.getText().toString();
		int priority = (int) spinnerStatus.getSelectedItemId();
		dbAdapter.open();
		list = dbAdapter.getTasks();
		dbAdapter.close();

		// Add checkbox for title
		// IF TITLECHECKBOX IS CHECKED
		List<Task> filteredList = new ArrayList<Task>();
		
		for(Task t : list){
			if(t.getTitle().contains(title)){
				filteredList.add(t);
			}
		}
		// FIN IF TITLE
		
		// Checkbox for date
		// IF DATECHECKBOX IS CHECKED
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		Date dateStart = format.parse(dateS);
		Date dateEnd = format.parse(dateE);
		Date dateTask;
		for(Task t : list){
				dateTask = format.parse(t.getDate());
			if(dateStart.compareTo(dateTask) <= 0 && dateTask.compareTo(dateEnd) <= 0)
				filteredList.add(t);
		}
		// FIN IF DATE
		
		// IF STATUS IS CHECKED
		//FIN IF STATUS
		
		// IF PRIORITY IS CHECKED
		//FIN IF PRIORITY

		listView.setAdapter(new MainListItemAdapter(getBaseContext(), filteredList));
	}

	public void showCalendarDialogOnStart(View view){
		Log.i(TAG, "Tap on DateStart");

		final Calendar c = Calendar.getInstance();
		year_start = c.get(Calendar.YEAR);
		month_start = c.get(Calendar.MONTH);
		day_start = c.get(Calendar.DAY_OF_MONTH);

		DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				filterDateStart.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
			}
		}, year_start, month_start, day_start);
		dpd.setTitle(R.string.selectStartDate);
		dpd.show();
	}

	public void showCalendarDialogOnEnd(View view){
		Log.i(TAG, "Tap on DateEnd");

		final Calendar c = Calendar.getInstance();
		year_end = c.get(Calendar.YEAR);
		month_end = c.get(Calendar.MONTH);
		day_end = c.get(Calendar.DAY_OF_MONTH);

		DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				filterDateEnd.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
			}
		}, year_end, month_end, day_end);
		dpd.setTitle(R.string.selectEndDate);
		dpd.show();
	}
}
