package com.lyaurese.jobsorganizer.Objects;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

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

    public void insertNewApplication(String company, String jobTitle, String jobNumber, int applied, int day, int month, int year, String comments){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("company", company);
        values.put("jobTitle", jobTitle);
        values.put("jobNumber", jobNumber);
        values.put("applied", applied);
        values.put("day", day);
        values.put("month", month);
        values.put("year", year);
        values.put("comments", comments);

        db.insert(APPLICATIONS_TABLE_NAME, null, values);

        values.clear();
        values.put("company", company);

        Cursor cursor = db.rawQuery("SELECT * FROM " + COMPANIES_TABLE_NAME + " WHERE company = '" + company + "'", null);
        if(!cursor.moveToFirst()){
            values.put("count", 1);
            db.insert(COMPANIES_TABLE_NAME, null, values);
        }
        else{
            values.put("count", cursor.getInt(1) + 1);
            db.update(COMPANIES_TABLE_NAME, values, "company = ?", new String[]{company});
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
}
