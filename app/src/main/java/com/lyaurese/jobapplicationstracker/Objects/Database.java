package com.lyaurese.jobapplicationstracker.Objects;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lyaurese.jobapplicationstracker.Utils.GraphUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;


public class Database extends SQLiteOpenHelper {
    private static final String DB_NAME = "JobOrganizerDB";
    private static final String APPLICATIONS_TABLE_NAME = "Applications";
    private static final String COMPANIES_TABLE_NAME = "Companies";
    private static final int DB_VERSION = 2;


    private final int COMPANY_COL_NUM = 0;
    private final int JOB_TITLE_COL_NUM = 1;
    private final int JOB_NUMBER_COL = 2;
    private final int APPLIED_COL_NUM = 3;
    private final int APPLIED_DAY_COL_NUM = 4;
    private final int APPLIED_MONTH_COL_NUM = 5;
    private final int APPLIED_YEAR_COL_NUM = 6;
    private final int INTERVIEW_COL_NUM = 7;
    private final int INTERVIEW_DAY_COL_NUM = 8;
    private final int INTERVIEW_MONTH_COL_NUM = 9;
    private final int INTERVIEW_YEAR_COL_NUM = 10;
    private final int COMMENTS_COL_NUM = 11;
    private final int ACTIVE_COL_NUM = 12;
    private final int LOCATION_COL_NUM = 13;

    private static final String[] MONTHS_NAMES = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec"};



    public Database(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE IF NOT EXISTS " + APPLICATIONS_TABLE_NAME +
                "(company TEXT, jobTitle TEXT, jobNumber TEXT, applied INTEGER, appliedDay INTEGER, appliedMonth INTEGER, appliedYear INTEGER, " +
                "interview INTEGER, interviewDay INTEGER, interviewMonth INTEGER, interviewYear INTEGER, comments TEXT, active INTEGER, location TEXT)";
        db.execSQL(query);

        query = "CREATE TABLE IF NOT EXISTS " + COMPANIES_TABLE_NAME + "(company TEXT, count INTEGER)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion > oldVersion){
            db.execSQL("ALTER TABLE " + APPLICATIONS_TABLE_NAME + " ADD COLUMN location TEXT DEFAULT NULL");
        }
    }

    public void insertNewApplication(Application application){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("company", application.getCompanyName());
        values.put("jobTitle", application.getJobTitle());
        values.put("jobNumber", application.getJobNumber());
        values.put("applied", application.applied() ? 1 : 0);
        if(application.getAppliedDate() != null) {
            values.put("appliedDay", application.getAppliedDate().get(Calendar.DAY_OF_MONTH));
            values.put("appliedMonth", application.getAppliedDate().get(Calendar.MONTH) + 1);
            values.put("appliedYear", application.getAppliedDate().get(Calendar.YEAR));
        }
        values.put("interview", 0);
        values.put("comments", application.getComment());
        values.put("active", application.applied() ? 1 : 0);
        values.put("location", application.getLocation());

        db.insert(APPLICATIONS_TABLE_NAME, null, values);

        updateCompany(application.getCompanyName(), 1);

        db.close();
    }

    public void editApplication(Application application, String jobNumber){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        String oldCompanyName;

        Cursor cursor = db.rawQuery("SELECT * FROM " + APPLICATIONS_TABLE_NAME + " WHERE jobNumber = '" + jobNumber + "'", null);
        cursor.moveToFirst();

        oldCompanyName = cursor.getString(COMPANY_COL_NUM);

        values.put("company", application.getCompanyName());
        values.put("jobTitle", application.getJobTitle());
        values.put("jobNumber", application.getJobNumber());

        if(application.applied() && application.getAppliedDate() != null) {
            values.put("applied",1);
            values.put("appliedDay", application.getAppliedDate().get(Calendar.DAY_OF_MONTH));
            values.put("appliedMonth", application.getAppliedDate().get(Calendar.MONTH) + 1);
            values.put("appliedYear", application.getAppliedDate().get(Calendar.YEAR));
        }
        else {
            values.put("applied",0);
            values.putNull("appliedDay");
            values.putNull("appliedMonth");
            values.putNull("appliedYear");
        }

        if(application.interview() && application.getInterviewDate() != null) {
            values.put("interview", 1);
            values.put("interviewDay", application.getInterviewDate().get(Calendar.DAY_OF_MONTH));
            values.put("interviewMonth", application.getInterviewDate().get(Calendar.MONTH) + 1);
            values.put("interviewYear", application.getInterviewDate().get(Calendar.YEAR));
        }
        else {
            values.put("interview", 0);
            values.putNull("interviewDay");
            values.putNull("interviewMonth");
            values.putNull("interviewYear");
        }
        values.put("comments", application.getComment());
        values.put("active", application.isActive() ? 1 : 0);
        values.put("location", application.getLocation());

        db.update(APPLICATIONS_TABLE_NAME, values, "jobNumber = ?", new String[]{jobNumber});

        if(!application.getCompanyName().equals(oldCompanyName)){
            updateCompany(cursor.getString(COMPANY_COL_NUM), -1);
            updateCompany(application.getCompanyName(), 1);
        }

        cursor.close();
        db.close();
    }


    public ArrayList<Company> getCompaniesListAll(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + COMPANIES_TABLE_NAME + " ORDER BY company ASC", null);

        if(cursor.moveToFirst()){
            ArrayList<Company> list = new ArrayList<>();
            do{
                list.add(new Company(cursor.getString(COMPANY_COL_NUM), cursor.getInt(JOB_TITLE_COL_NUM)));
            }while(cursor.moveToNext());

            return list;
        }
        else{
            cursor.close();
            db.close();

            return null;
        }
    }


    public ArrayList<Company> getCompaniesListWithFilter(int filter){
        if(filter == -1)
            return getCompaniesListAll();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT company, COUNT(*) FROM " + APPLICATIONS_TABLE_NAME + " WHERE active = " + filter + " GROUP BY company ORDER BY company ASC" , null);
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

        if(cursor.moveToLast()){
            ArrayList<Application> list = new ArrayList<>();
            do{
                Calendar appliedCalendar = null;

                if(cursor.getInt(APPLIED_COL_NUM) == 1){
                    appliedCalendar = Calendar.getInstance();
                    appliedCalendar.set(cursor.getInt(APPLIED_YEAR_COL_NUM), cursor.getInt(APPLIED_MONTH_COL_NUM) - 1,cursor.getInt(APPLIED_DAY_COL_NUM));
                }

                Application application = new Application(cursor.getString(COMPANY_COL_NUM), cursor.getString(JOB_TITLE_COL_NUM), cursor.getString(JOB_NUMBER_COL), cursor.getString(LOCATION_COL_NUM), cursor.getInt(APPLIED_COL_NUM) == 1,
                        appliedCalendar, cursor.getString(COMMENTS_COL_NUM));

                if(cursor.getInt(INTERVIEW_COL_NUM) == 1){
                    Calendar interviewCalendar = Calendar.getInstance();
                    interviewCalendar.set(cursor.getInt(INTERVIEW_YEAR_COL_NUM), cursor.getInt(INTERVIEW_MONTH_COL_NUM) - 1, cursor.getInt(INTERVIEW_DAY_COL_NUM));
                    application.setInterviewDate(interviewCalendar);
                }

                if(cursor.getInt(ACTIVE_COL_NUM) == 0)
                    application.setActive(false);

                list.add(application);
            }while(cursor.moveToPrevious());

            return list;
        }
        else{
            cursor.close();
            db.close();

            return null;
        }
    }

    public boolean isJobExists(String jobNumber, String company){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + APPLICATIONS_TABLE_NAME + " WHERE jobNumber = '" + jobNumber + "' AND company = '" + company + "'", null);

        boolean exists = cursor.moveToFirst();
        db.close();
        cursor.close();

        return exists;
    }

    private void updateCompany(String name, int update){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        Cursor cursor = db.rawQuery("SELECT * FROM " + COMPANIES_TABLE_NAME + " WHERE company = '" + name + "'", null);

        if(!cursor.moveToFirst()){
            values.put("company", name);
            values.put("count", 1);
            db.insert(COMPANIES_TABLE_NAME, null, values);
        }
        else{
            int count = cursor.getInt(JOB_TITLE_COL_NUM) + update;
            if(count == 0){
                db.delete(COMPANIES_TABLE_NAME, "company = ?", new String[]{name});
            }
            else{
                values.put("company", name);
                values.put("count", count);
                db.update(COMPANIES_TABLE_NAME, values, "company = ?", new String[]{name});
            }
        }
        cursor.close();
        db.close();
    }

    public int getApplicationsCount(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + APPLICATIONS_TABLE_NAME, null);

        int count = cursor.getCount();

        cursor.close();
        db.close();

        return count;
    }

    public int getActiveCount(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + APPLICATIONS_TABLE_NAME + " WHERE active = 1 AND applied = 1", null);

        int count = cursor.getCount();

        cursor.close();
        db.close();

        return count;
    }

    public int getNoResponseApplications(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + APPLICATIONS_TABLE_NAME + " WHERE interview = 0", null);

        int count = cursor.getCount();

        cursor.close();
        db.close();

        return count;
    }

    public int getInterviewApplications(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + APPLICATIONS_TABLE_NAME + " WHERE interview = 1", null);

        int count = cursor.getCount();

        cursor.close();
        db.close();

        return count;
    }

    public LinkedHashMap<String, Integer> getApplicationsByMonth(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + APPLICATIONS_TABLE_NAME + " WHERE applied = 1 ORDER BY appliedYear ASC, appliedMonth ASC ", null);

        if(cursor.moveToFirst()){
            LinkedHashMap<String, Integer> map = new LinkedHashMap<>();

            int lastMonth = cursor.getInt(APPLIED_MONTH_COL_NUM), year = cursor.getInt(APPLIED_YEAR_COL_NUM) - 2000;

            do{
                if(lastMonth == cursor.getInt(APPLIED_MONTH_COL_NUM)){
                    String key = MONTHS_NAMES[cursor.getInt(APPLIED_MONTH_COL_NUM) - 1] + " " + (cursor.getInt(APPLIED_YEAR_COL_NUM) - 2000);
                    if(!map.containsKey(key))
                        map.put(key, 1);
                    else
                        map.put(key, map.get(key)+1);
                }
                else{
                    cursor.moveToPrevious();
                    lastMonth++;
                    if(lastMonth == 13){
                        lastMonth = 1;
                        year ++;
                    }

                    String key = MONTHS_NAMES[lastMonth-1] + " " + year;
                    map.put(key, 0);
                }
            }while(cursor.moveToNext());

            cursor.close();
            db.close();

            return map;
        }

        cursor.close();
        db.close();

        return null;
    }

    public void setActive(String jobNumber, boolean active){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + APPLICATIONS_TABLE_NAME + " WHERE jobNumber = '" + jobNumber + "'", null);

        ContentValues values = new ContentValues();
        values.put("active", active ? 1 : 0);

        db.update(APPLICATIONS_TABLE_NAME, values, "jobNumber = ?", new String[]{jobNumber});

        cursor.close();
        db.close();
    }

    public LinkedHashMap<String,Integer> getApplicationsByCompany(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + APPLICATIONS_TABLE_NAME + " WHERE applied = 1", null);

        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();

        if(cursor.moveToFirst()){
            do{
                String key = cursor.getString(COMPANY_COL_NUM);
                if(!map.containsKey(key))
                    map.put(key, 1);
                else
                    map.put(key, map.get(key)+1);
            }
            while(cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return map;
    }

    public void removeApplication(Application application){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(APPLICATIONS_TABLE_NAME, "company = ? AND jobTitle = ?", new String[]{application.getCompanyName(), application.getJobTitle()});

        updateCompany(application.getCompanyName(), -1);

        db.close();
    }
}
