package com.esgi.taskmanager.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
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
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Switch;
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
	private Switch switchTitle, switchDate, switchStatus;

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
				if(((MainListItemAdapter) listView.getAdapter()).getSelectedTasks().get(position) == false){
					Log.i(TAG, "Task selected : " + taskSelected.getId());
					Intent editActivity = new Intent(view.getContext(), CreateActivity.class);
					editActivity.putExtra("task", taskSelected);
					startActivity(editActivity);
				}
			}
		}); 

		listView.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> adv, View v, int pos, long id) {
				((MainListItemAdapter) listView.getAdapter()).toggleSelection(pos);
				return true;
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
		deleteSelectedTasks(view);
	}

	private void deleteSelectedTasks(View view) {
		SparseBooleanArray sba = ((MainListItemAdapter) listView.getAdapter()).getSelectedTasks();
		dbAdapter.open();
		for(int i=0;i<sba.size();i++){
			Toast.makeText(getApplicationContext(), "delete task : "+sba.keyAt(i), Toast.LENGTH_SHORT).show();
			dbAdapter.deleteTask(list.get(sba.keyAt(i)));
		}

		list = dbAdapter.getTasks();
		dbAdapter.close();
		
		listView.setAdapter(new MainListItemAdapter(view.getContext(), list));
	}

	public void filterTask(View view) {
		Log.i(TAG, "Click on Filter Button");
		showPopupFilter(MainActivity.this);
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
		TextView sortNone = (TextView) layout.findViewById(R.id.sortNone);

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
				Toast.makeText(context, R.string.sortByTitle, Toast.LENGTH_SHORT).show();
				listView.setAdapter(new MainListItemAdapter(context, list));
				popup.dismiss();
				return false;
			}
		});

		sortPriority.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Collections.sort(list, new Task.ComparePriority());
				Toast.makeText(context, R.string.sortByPriority, Toast.LENGTH_SHORT).show();
				listView.setAdapter(new MainListItemAdapter(context, list));
				popup.dismiss();
				return false;
			}
		});

		sortStatus.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Collections.sort(list, new Task.CompareStatus());
				Toast.makeText(context, R.string.sortByStatus, Toast.LENGTH_SHORT).show();
				listView.setAdapter(new MainListItemAdapter(context, list));
				popup.dismiss();
				return false;
			}
		});
		
		sortNone.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Collections.sort(list, new Task.CompareId());
				listView.setAdapter(new MainListItemAdapter(context, list));
				popup.dismiss();
				return false;
			}
		});

	}

	private void showPopupFilter(final Activity context) {
		int popupWidth = WindowManager.LayoutParams.WRAP_CONTENT;
		int popupHeight = WindowManager.LayoutParams.WRAP_CONTENT;

		LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup);
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = layoutInflater.inflate(R.layout.popup_filter, viewGroup);

		filterTitle = (EditText) layout.findViewById(R.id.textTitle);
		filterDateStart = (TextView) layout.findViewById(R.id.dateStartValue);
		filterDateEnd = (TextView) layout.findViewById(R.id.dateEndValue);

		switchTitle = (Switch) layout.findViewById(R.id.switchTitle);
		switchDate = (Switch) layout.findViewById(R.id.switchDate);
		switchStatus = (Switch) layout.findViewById(R.id.switchStatus);

		final Calendar c = Calendar.getInstance();
		year_start = c.get(Calendar.YEAR)-1;
		month_start = c.get(Calendar.MONTH)+1;
		day_start = c.get(Calendar.DAY_OF_MONTH);
		year_end = year_start+2;
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

		List<Task> filteredList = new ArrayList<Task>();
		filteredList.addAll(list);

		if(switchTitle.isChecked()){
			for (Iterator<Task> it = filteredList.iterator(); it.hasNext(); ) {
				Task t = it.next();
				if( !(t.getTitle().contains(title)) )
					it.remove();
			}
		}

		// Checkbox for date
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		Date dateStart = format.parse(dateS);
		Date dateEnd = format.parse(dateE);
		Date dateTask;

		if(switchDate.isChecked()){
			for (Iterator<Task> it = filteredList.iterator(); it.hasNext(); ) {
				Task t = it.next();
				dateTask = format.parse(t.getDate());
				if( !(dateStart.compareTo(dateTask) <= 0 && dateTask.compareTo(dateEnd) <= 0) )
					it.remove();
			}
		}

		if(switchStatus.isChecked())
			for (Iterator<Task> it = filteredList.iterator(); it.hasNext(); ) {
				Task t = it.next();
				if(t.getStatus() != spinnerStatus.getSelectedItemId())
					it.remove();
			}

		if(switchTitle.isChecked() || switchDate.isChecked() || switchStatus.isChecked()){
			listView.setAdapter(new MainListItemAdapter(getBaseContext(), filteredList));
		}else{
			listView.setAdapter(new MainListItemAdapter(getBaseContext(), list));
		}

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
