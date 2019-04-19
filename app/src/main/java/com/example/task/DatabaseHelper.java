package com.example.task;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;


public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="Preference.db";
    private static final String TABLE_NAME="preferences";

    private static final String name="name";
    private static final String value="value";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE if not exists "+TABLE_NAME+" (name TEXT,value TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void setPreferences_TABLE(String Name,String Value)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(name,Name);
        contentValues.put(value,Value);
        db.insert(TABLE_NAME,null,contentValues);
        db.close();
    }
    Cursor getLanguage()
    {
        SQLiteDatabase db=getReadableDatabase();
        return db.rawQuery("select value from "+TABLE_NAME+" where name='Language'" , null);
    }
    Cursor getCurrency()
    {
        SQLiteDatabase db=getReadableDatabase();
        return db.rawQuery("select value from "+TABLE_NAME +" where name='Currency'", null);
    }
    void deletePreferences()
    {
        SQLiteDatabase db=getWritableDatabase();
        db.delete(TABLE_NAME,null,null);
    }

}
