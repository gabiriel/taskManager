package com.esgi.taskmanager.model;

import java.util.Comparator;

import android.os.Parcel;
import android.os.Parcelable;

public class Task implements Parcelable{

	public static long HIGH_PRIORITY = 1;
	public static long MID_PRIORITY = 2;
	public static long LOW_PRIORITY = 3;

	private long id;
	private String title;
	private String description;
	private String date;
	private String hour;
	private String category;
	private long priority;
	private long status;

	public Task(){
		id = 0;
		title = "";
		description = "";
		date = "XX-XX-XXXX";
		hour = "XX:XX";
		category = "";
		priority = LOW_PRIORITY;
		status = 0;
	}

	public Task(Parcel source){
		id = source.readLong();
		title = source.readString();
		description = source.readString();
		date = source.readString();
		hour = source.readString();
		category = source.readString();
		priority = source.readLong();
		status = source.readLong();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(this.id);
		dest.writeString(this.title);
		dest.writeString(this.description);
		dest.writeString(this.date);
		dest.writeString(this.hour);
		dest.writeString(this.category);
		dest.writeLong(this.priority);
		dest.writeLong(this.status);
	}

	public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {
		@Override
		public Task createFromParcel(Parcel source) {
			return new Task(source);
		}

		@Override
		public Task[] newArray(int size) {
			return new Task[size];
		}
	};


	// Comparator
	public static class CompareTitle implements Comparator<Task> {
		@Override
		public int compare(Task t1, Task t2) {
			return (int) (t1.title.compareToIgnoreCase(t2.title));
		}
	}

	// Comparator
	public static class ComparePriority implements Comparator<Task> {
		@Override
		public int compare(Task t1, Task t2) {
			return (int) (t2.priority - t1.priority);
		}
	}

	// Comparator
	public static class CompareStatus implements Comparator<Task> {
		@Override
		public int compare(Task t1, Task t2) {
			return (int) (t1.status - t2.status);
		}
	}


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getHour() {
		return hour;
	}

	public void setHour(String hour) {
		this.hour = hour;
	}

	public long getStatus() {
		return status;
	}

	public void setStatus(long status) {
		this.status = status;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public long getPriority() {
		return priority;
	}

	public void setPriority(long priority) {
		this.priority = priority;
	}
}
