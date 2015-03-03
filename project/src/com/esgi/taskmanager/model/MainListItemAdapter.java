package com.esgi.taskmanager.model;

import java.util.List;

import com.esgi.taskmanager.R;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainListItemAdapter extends BaseAdapter {
	private Context context;
	private List<Task> tasks;
	private static LayoutInflater inflater = null;
	private SparseBooleanArray selectedTasks;

	public MainListItemAdapter(Context context, List<Task> tasks) {
		this.context = context;
		this.tasks = tasks;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		selectedTasks = new SparseBooleanArray();
	}

	@Override
	public int getCount() {
		return tasks.size();
	}

	@Override
	public Object getItem(int position) {
		return tasks.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (vi == null)
			vi = inflater.inflate(R.layout.list_item, null);
		TextView text = (TextView) vi.findViewById(R.id.title);
		text.setText(tasks.get(position).getTitle());
		TextView description = (TextView) vi.findViewById(R.id.description);
		description.setText(tasks.get(position).getDescription());

		TextView circle = (TextView) vi.findViewById(R.id.status);
		circle.setText(String.valueOf(tasks.get(position).getStatus()));
		int res_background;
		String letter_in_circle;
		if(tasks.get(position).getStatus()==0)
			res_background = R.drawable.round_button_to_do;
		else if (tasks.get(position).getStatus()==1)
			res_background =R.drawable.round_button_in_progress;
		else
			res_background =R.drawable.round_button_done;

		if(tasks.get(position).getPriority()==0)
			letter_in_circle = "";
		else if (tasks.get(position).getPriority()==1)
			letter_in_circle = "!";
		else
			letter_in_circle = "!!!";

		
		if(selectedTasks.get(position)){
			letter_in_circle = "X";
			res_background = R.drawable.round_button_delete;
		}
		circle.setBackgroundResource(res_background);
		circle.setText(letter_in_circle);

		return vi;
	}

	public void toggleSelection(int position) {
		selectView(position, !selectedTasks.get(position));
	}
	
	public SparseBooleanArray getSelectedTasks(){
		return selectedTasks;
	}

	public void removeSelection() {
		selectedTasks = new SparseBooleanArray();
		notifyDataSetChanged();
	}

	private void selectView(int position, boolean value) {
		if (value)
			selectedTasks.put(position, value);
		else
			selectedTasks.delete(position);
		notifyDataSetChanged();
	}

}
