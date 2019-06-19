package com.example.taskmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

    String databaseName;

    public DataBaseHelper(Context context,String name, int version) {
        super(context, name, null, version);
        databaseName = name;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + databaseName +
                "(ASSIGN TEXT, TYPE_OF_TASK TEXT, TITLE TEXT, DATE TEXT, TIME TEXT, PERIOD TEXT, REMINDER TEXT, DETAIL TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + databaseName);
        onCreate(db);
    }

    public boolean insertData(Task task){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ASSIGN",task.getAssign());
        contentValues.put("TYPE_OF_TASK",task.getTypeOfTask());
        contentValues.put("TITLE", task.getTitle());
        contentValues.put("DATE", task.getDate());
        contentValues.put("TIME", task.getTime());
        contentValues.put("PERIOD", task.getPeriod());
        contentValues.put("REMINDER", task.getReminder());
        contentValues.put("DERAIL", task.getDetail());
        long result = db.insert(databaseName, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }
    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from" + databaseName, null);
        return res;
    }
}
