package com.example.taskmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

    String databaseName;

    public DataBaseHelper(Context context,String databaseName, int version) {
        super(context, databaseName, null, version);
        this.databaseName = databaseName;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + databaseName +
                " (ID INTEGER PRIMARY KEY AUTOINCREMENT, ASSIGN TEXT, TYPE_OF_TASK TEXT, TITLE TEXT, DATE TEXT, TIME TEXT, PERIOD TEXT, REMINDER TEXT, DETAIL TEXT)");
        //
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
        contentValues.put("DETAIL", task.getDetail());
        long id = db.insert(databaseName, null, contentValues);

        if (id == -1)
            return false;
        else {
            task.setId(String.valueOf(id));
            return true;
        }
    }
    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + databaseName, null);
        return res;
    }
    public void updateData(Task task){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID",task.getId());
        contentValues.put("ASSIGN",task.getAssign());
        contentValues.put("TYPE_OF_TASK",task.getTypeOfTask());
        contentValues.put("TITLE", task.getTitle());
        contentValues.put("DATE", task.getDate());
        contentValues.put("TIME", task.getTime());
        contentValues.put("PERIOD", task.getPeriod());
        contentValues.put("REMINDER", task.getReminder());
        contentValues.put("DETAIL", task.getDetail());
        db.update(databaseName, contentValues, "ID = ?", new String[]{task.getId()});
    }
    public Integer deleteData(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(databaseName,"ID = ?", new String[]{id});
    }
    public void deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ databaseName);
    }

}
