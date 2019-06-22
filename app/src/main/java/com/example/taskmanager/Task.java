package com.example.taskmanager;

import android.os.Parcel;
import android.os.Parcelable;

public class Task implements Parcelable {
    private String assign;
    private String typeOfTask;
    private String title;
    private String date;
    private String time;
    private String period;
    private String reminder;
    private String detail;
    private String id;

    public Task(String assign, String typeOfTask, String title){
        this.assign = assign;
        this.typeOfTask = typeOfTask;
        this.title = title;
    }
    public Task(Parcel in) {
        this.assign = in.readString();
        this.typeOfTask = in.readString();
        this.title = in.readString();
        this.date = in.readString();
        this.time = in.readString();
        this.period = in.readString();
        this.reminder = in.readString();
        this.detail = in.readString();
    }

    public String getAssign() {
        return assign;
    }

    public String getDetail() {
        if (detail == null)
            return "";
        return detail;
    }

    public String getPeriod() {
        return period;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getReminder() {
        return reminder;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        if (title.isEmpty())
            return "title";
        return title;
    }

    public String getTypeOfTask() {
        return typeOfTask;
    }

    public void setAssign(String assign) {
        this.assign = assign;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public void setReminder(String remaining) {
        this.reminder = remaining;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTypeOfTask(String typeOfTask) {
        this.typeOfTask = typeOfTask;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(assign);
        dest.writeString(typeOfTask);
        dest.writeString(title);
        dest.writeString(date);
        dest.writeString(time);
        dest.writeString(period);
        dest.writeString(reminder);
        dest.writeString(detail);
    }
    /**
     * This field is needed for Android to be able to
     * create new objects, individually or as arrays
     *
     * If you don’t do that, Android framework will through exception
     * Parcelable protocol requires a Parcelable.Creator object called CREATOR
     */
    public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {

        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        public Task[] newArray(int size) {
            return new Task[size];
        }
    };
}
