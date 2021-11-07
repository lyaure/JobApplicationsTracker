package com.lyaurese.jobsorganizer.Objects;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Database extends SQLiteOpenHelper {
    private static final String DB_NAME = "JobOrganizerDB";
    private static final String APPLICATIONS_TABLE_NAME = "Applications";
    private static final String COMPANIES_TABLE_NAME = "Companies";
    private static final int DB_VERSION = 1;
    public static Database instance;

    public Database(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE IF NOT EXISTS " + APPLICATIONS_TABLE_NAME +
                "(company TEXT, jobTitle TEXT, jobNumber TEXT, applied INTEGER, day INTEGER, month INTEGER, year INTEGER, comments TEXT)";
        db.execSQL(query);

        query = "CREATE TABLE IF NOT EXISTS " + COMPANIES_TABLE_NAME + "(company TEXT, count INTEGER)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertNewApplication(Application application){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("company", application.getCompanyName());
        values.put("jobTitle", application.getJobTitle());
        values.put("jobNumber", application.getJobNumber());
        values.put("applied", application.applied() ? 1 : 0);
        values.put("day", application.getAppliedDate().get(Calendar.DAY_OF_MONTH));
        values.put("month", application.getAppliedDate().get(Calendar.MONTH));
        values.put("year", application.getAppliedDate().get(Calendar.YEAR));
        values.put("comments", application.getComment());

        db.insert(APPLICATIONS_TABLE_NAME, null, values);

        values.clear();
        values.put("company", application.getCompanyName());

        Cursor cursor = db.rawQuery("SELECT * FROM " + COMPANIES_TABLE_NAME + " WHERE company = '" + application.getCompanyName() + "'", null);
        if(!cursor.moveToFirst()){
            values.put("count", 1);
            db.insert(COMPANIES_TABLE_NAME, null, values);
        }
        else{
            values.put("count", cursor.getInt(1) + 1);
            db.update(COMPANIES_TABLE_NAME, values, "company = ?", new String[]{application.getCompanyName()});
        }

        cursor.close();
        db.close();
    }

    public ArrayList<Company> getCompanyList(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + COMPANIES_TABLE_NAME + " ORDER BY company ASC", null);

        if(cursor.moveToFirst()){
            ArrayList<Company> list = new ArrayList<>();
            do{
                list.add(new Company(cursor.getString(0), cursor.getInt(1)));
            }while(cursor.moveToNext());

            return list;
        }
        else{
            cursor.close();
            db.close();

            return null;
        }
    }

    public ArrayList<Application> getApplicationList(String companyName){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + APPLICATIONS_TABLE_NAME + " WHERE company = '" + companyName + "'", null);

        if(cursor.moveToFirst()){
            ArrayList<Application> list = new ArrayList<>();
            do{
                Calendar calendar = new GregorianCalendar();
                calendar.set(cursor.getInt(6), cursor.getInt(5),cursor.getInt(4));

                list.add(new Application(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3) == 1,
                        calendar, cursor.getString(7)));
            }while(cursor.moveToNext());

            return list;
        }
        else{
            cursor.close();
            db.close();

            return null;
        }
    }
}
