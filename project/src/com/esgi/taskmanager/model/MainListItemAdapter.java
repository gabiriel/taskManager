package com.esgi.taskmanager.model;

import java.util.List;

import com.esgi.taskmanager.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MainListItemAdapter extends BaseAdapter {
	private Context context;
	private List<Task> tasks;
	private static LayoutInflater inflater = null;

	public MainListItemAdapter(Context context, List<Task> tasks) {
		this.context = context;
		this.tasks = tasks;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
		return vi;
	}
}
