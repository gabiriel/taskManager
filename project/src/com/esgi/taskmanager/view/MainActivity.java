package com.esgi.taskmanager.view;

import java.util.List;

import com.esgi.taskmanager.R;
import com.esgi.taskmanager.model.DataBaseAdapter;
import com.esgi.taskmanager.model.MainListItemAdapter;
import com.esgi.taskmanager.model.Task;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ListActivity{

	final static String TAG = "MainActivity";
	private DataBaseAdapter dbAdapter;
	private List<Task> list;
	private ListView listView;

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
				Log.i(TAG, "Task selected : "+taskSelected.getId());
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
	}

	public void sortTask(View view) {
		Log.i(TAG, "Click on Sort Button");
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
}
