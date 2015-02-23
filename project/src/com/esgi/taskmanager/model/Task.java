package com.esgi.taskmanager.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Task implements Parcelable{

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
		priority = 0;
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
