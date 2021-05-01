package com.example.ca1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DB extends SQLiteOpenHelper {
    public DB(@Nullable Context context) {
        super(context, "Reminderdata.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create Table RemainderDetails( Remaind TEXT, Date TEXT, Time TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop Table if exists RemainderDetails");

    }
    public boolean insertdata(String Remaind,String date,String time){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Remaind",Remaind);
        contentValues.put("Date",date);
        contentValues.put("Time",time);
        long result = db.insert("RemainderDetails",null,contentValues);
        if(result==-1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    public boolean deletedata(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from RemainderDetails where Remaind=?",new String[]{id});
        if(cursor.getCount()>0){
            long result = db.delete("RemainderDetails","Remaind=?",new String[]{id});
            if(result==-1){
                return false;
            }
            else {
                return true;
            }
        }
        else {
            return false;
        }
    }
    public Cursor getdata ()
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from RemainderDetails", null);
        return cursor;

    }
}
