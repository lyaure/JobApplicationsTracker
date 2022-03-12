package com.lyaurese.jobapplicationstracker.Objects;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lyaurese.jobapplicationstracker.Utils.DateUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;


public class Database extends SQLiteOpenHelper {
    private static final String DB_NAME = "JobOrganizerDB";
    private static final String APPLICATIONS_TABLE_NAME = "Applications";
    private static final int DB_VERSION = 1;
    public static Database instance;


    private final int ID_COL_NUM = 0;
    private final int COMPANY_COL_NUM = 1;
    private final int JOB_POSITION_COL_NUM = 2;
    private final int JOB_NUMBER_COL = 3;
    private final int LOCATION_COL_NUM = 4;
    private final int ACTIVE_COL_NUM = 5;
    private final int APPLIED_DATE_COL_NUM = 6;
    private final int INTERVIEW_COL_NUM = 7;
    private final int INTERVIEW_DATE_COL_NUM = 8;
    private final int COMMENTS_COL_NUM = 9;

    private final int COMPANY = 0, LOCATION = 1, DATE = 2, ALL = -1, INACTIVE = 0, ACTIVE = 1;

    private final String[] MONTHS_NAMES = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private final String[] DAYS_OF_WEEK = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};


    public Database(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE IF NOT EXISTS " + APPLICATIONS_TABLE_NAME +
                "(id INTEGER, " +
                "company TEXT, " +
                "jobPosition TEXT, " +
                "jobNumber TEXT, " +
                "location TEXT, " +
                "active INTEGER, " +
                "appliedDate INTEGER," +
                "interview INTEGER, " +
                "interviewDate, INTEGER, " +
                "comments TEXT)";
        db.execSQL(query);

    }

    // ensures that only one Database will ever exist at any given time
    public static synchronized Database getInstance(Context context) {
        if (instance == null)
            instance = new Database(context.getApplicationContext());

        return instance;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertNewApplication(Application application) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("id", application.getId());
        values.put("company", application.getCompanyName());
        values.put("jobPosition", application.getJobPosition());
        values.put("jobNumber", application.getJobNumber());
        values.put("location", application.getLocation());
        values.put("active", application.applied() ? 1 : 0);
        values.put("appliedDate", application.getAppliedDate());
        values.put("interview", 0);
        values.put("interviewDate", 0);
        values.put("comments", application.getComment());

        db.insert(APPLICATIONS_TABLE_NAME, null, values);

        db.close();
    }

    public void editApplication(Application application) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        Cursor cursor = db.rawQuery("SELECT * FROM " + APPLICATIONS_TABLE_NAME + " WHERE id = '" + application.getId() + "'", null);
        cursor.moveToFirst();

        values.put("company", application.getCompanyName());
        values.put("jobPosition", application.getJobPosition());
        values.put("jobNumber", application.getJobNumber());
        values.put("location", application.getLocation());
        values.put("active", application.isActive() ? 1 : 0);
        values.put("appliedDate", application.getAppliedDate());

        if (application.interview() && application.getInterviewDate() != 0) {
            values.put("interview", 1);
            values.put("interviewDate", application.getInterviewDate());
        } else {
            values.put("interview", 0);
            values.put("interviewDate", 0);
        }
        values.put("comments", application.getComment());

        db.update(APPLICATIONS_TABLE_NAME, values, "id = ?", new String[]{"" + application.getId()});

        cursor.close();
        db.close();
    }

    public ArrayList<ListObject> getCompaniesList(int filter) {
        String query;

        if (filter == ALL)
            query = "SELECT company, COUNT(*) FROM " + APPLICATIONS_TABLE_NAME + " GROUP BY company ORDER BY company ASC";
        else
            query = "SELECT company, COUNT(*) FROM " + APPLICATIONS_TABLE_NAME + " WHERE active = " + filter + " GROUP BY company ORDER BY company ASC";

        return getListObjects(COMPANY, query);
    }

    public ArrayList<ListObject> getLocationsList(int filter) {
        String query;

        if (filter == ALL)
            query = "SELECT location, COUNT(*) FROM " + APPLICATIONS_TABLE_NAME + " GROUP BY location ORDER BY location ASC";
        else
            query = "SELECT location, COUNT(*) FROM " + APPLICATIONS_TABLE_NAME + " WHERE active = " + filter + " GROUP BY location ORDER BY location ASC";

        return getListObjects(LOCATION, query);
    }


    private ArrayList<ListObject> getListObjects(int type, String query) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<ListObject> list = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                list.add(new ListObject(type, cursor.getString(0), cursor.getInt(1)));
            } while (cursor.moveToNext());

        } else {
            cursor.close();
            db.close();
        }

        return list;
    }

    public ArrayList<Application> getApplicationsListSortByCompany(String companyName, int filter) {
        String query;

        if (filter == -1)
            query = "SELECT * FROM " + APPLICATIONS_TABLE_NAME + " WHERE company = '" + companyName + "' ORDER BY appliedDate DESC";
        else
            query = "SELECT * FROM " + APPLICATIONS_TABLE_NAME + " WHERE company = '" + companyName + "' AND active = " + filter + " ORDER BY appliedDate DESC";

        return getApplicationsList(query);
    }

    public ArrayList<Application> getApplicationsListSortByLocation(String locationName, int filter) {
        String query;

        if (filter == -1)
            query = "SELECT * FROM " + APPLICATIONS_TABLE_NAME + " WHERE location = '" + locationName + "' ORDER BY appliedDate DESC";
        else
            query = "SELECT * FROM " + APPLICATIONS_TABLE_NAME + " WHERE location = '" + locationName + "' AND active = " + filter + " ORDER BY appliedDate DESC";

        return getApplicationsList(query);
    }

    public ArrayList<Application> getApplicationsListSortByMonths(String date, int filter) {
        long[] monthIntervalsInMillis = DateUtil.getMonthIntervalsInMillis(date);

        return getApplicationsListSortByDate(monthIntervalsInMillis, filter);
    }

    public ArrayList<Application> getApplicationsListSortByDay(String date, int filter) {
        long[] daysIntervalsInMillis = DateUtil.getDayIntervals(date);

        return getApplicationsListSortByDate(daysIntervalsInMillis, filter);
    }

    private ArrayList<Application> getApplicationsListSortByDate(long[] dateIntervalsInMillis, int filter) {
        String query;

        if (filter == -1)
            query = "SELECT * FROM " + APPLICATIONS_TABLE_NAME + " WHERE appliedDate >= '" + dateIntervalsInMillis[0] + "' AND appliedDate <= '" + dateIntervalsInMillis[1] + "' ORDER BY appliedDate ASC";
        else
            query = "SELECT * FROM " + APPLICATIONS_TABLE_NAME + " WHERE appliedDate >= '" + dateIntervalsInMillis[0] + "' AND appliedDate <= '" + dateIntervalsInMillis[1] + "' AND active = " + filter + " ORDER BY appliedDate ASC";

        return getApplicationsList(query);
    }

    private ArrayList<Application> getApplicationsList(String query) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            ArrayList<Application> list = new ArrayList<>();
            do {
                Application application = new Application(cursor.getInt(ID_COL_NUM), cursor.getString(COMPANY_COL_NUM), cursor.getString(JOB_POSITION_COL_NUM), cursor.getString(JOB_NUMBER_COL), cursor.getString(LOCATION_COL_NUM),
                        cursor.getLong(APPLIED_DATE_COL_NUM), cursor.getString(COMMENTS_COL_NUM));

                if (cursor.getInt(INTERVIEW_COL_NUM) == 1)
                    application.setInterviewDate(cursor.getLong(INTERVIEW_DATE_COL_NUM));


                if (cursor.getInt(ACTIVE_COL_NUM) == 0)
                    application.setActive(false);

                list.add(application);
            } while (cursor.moveToNext());

            return list;
        } else {
            cursor.close();
            db.close();

            return null;
        }
    }

    //----TODO----
    public boolean isJobExists(String jobNumber, String company) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + APPLICATIONS_TABLE_NAME + " WHERE jobNumber = '" + jobNumber + "' AND company = '" + company + "'", null);

        boolean exists = cursor.moveToFirst();
        db.close();
        cursor.close();

        return exists;
    }

    public int getApplicationsCount() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + APPLICATIONS_TABLE_NAME, null);

        int count = cursor.getCount();

        cursor.close();
        db.close();

        return count;
    }

    public int getActiveCount() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + APPLICATIONS_TABLE_NAME + " WHERE active = 1", null);

        int count = cursor.getCount();

        cursor.close();
        db.close();

        return count;
    }

    public int getInterviewApplicationsCount() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + APPLICATIONS_TABLE_NAME + " WHERE interview = 1", null);

        int count = cursor.getCount();

        cursor.close();
        db.close();

        return count;
    }

    public void setActive(int id, boolean active) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + APPLICATIONS_TABLE_NAME + " WHERE id = '" + id + "'", null);

        ContentValues values = new ContentValues();
        values.put("active", active ? 1 : 0);

        db.update(APPLICATIONS_TABLE_NAME, values, "id = ?", new String[]{"" + id});

        cursor.close();
        db.close();
    }

    public LinkedHashMap<String, Integer> getLastSevenDaysApplications() {
        SQLiteDatabase db = getReadableDatabase();
        long[] lastSevenDaysIntervalsInMillis = DateUtil.getLastSevenDaysIntervalsInMillis();

        Cursor cursor = db.rawQuery("SELECT * FROM " + APPLICATIONS_TABLE_NAME + " WHERE appliedDate >= " + lastSevenDaysIntervalsInMillis[0] + " AND appliedDate < " + lastSevenDaysIntervalsInMillis[1] + " ORDER BY appliedDate ASC", null);

        if (cursor.moveToFirst()) {
            LinkedHashMap<String, Integer> map = new LinkedHashMap<>();

            int dayOfWeek = DateUtil.getDayOfWeek(lastSevenDaysIntervalsInMillis[0]) - 1,
                    dayOfMonth = DateUtil.getDayOfMonth(lastSevenDaysIntervalsInMillis[0]),
                    month = DateUtil.getMonth(lastSevenDaysIntervalsInMillis[0]) + 1;

            if (dayOfWeek < 0)
                dayOfWeek = 0;

            for (int i = 0; i < 7; i++) {
                String key = DAYS_OF_WEEK[dayOfWeek] + " " + String.format("%02d", month) + "/" + String.format("%02d", dayOfMonth);
                map.put(key, 0);

                dayOfWeek++;
                if (dayOfWeek == DAYS_OF_WEEK.length)
                    dayOfWeek = 0;

                dayOfMonth++;
                if (dayOfMonth > DateUtil.getNumOfDaysInMonth(lastSevenDaysIntervalsInMillis[0])) {
                    dayOfMonth = 1;
                    month++;
                }
            }

            do{
                dayOfWeek = DateUtil.getDayOfWeek(cursor.getLong(APPLIED_DATE_COL_NUM)) - 1;
                dayOfMonth = DateUtil.getDayOfMonth(cursor.getLong(APPLIED_DATE_COL_NUM));
                month = DateUtil.getMonth(cursor.getLong(APPLIED_DATE_COL_NUM)) + 1;

                String key = DAYS_OF_WEEK[dayOfWeek] + " " + String.format("%02d", month) + "/" + String.format("%02d", dayOfMonth);
                map.put(key, map.get(key) + 1);
            }while (cursor.moveToNext());

            cursor.close();
            db.close();

            return map;
        }

        cursor.close();
        db.close();

        return null;
    }

    public LinkedHashMap<String, Integer> getApplicationsByMonth() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + APPLICATIONS_TABLE_NAME + " ORDER BY appliedDate ASC", null);

        if (cursor.moveToFirst()) {
            LinkedHashMap<String, Integer> map = new LinkedHashMap<>();

            int lastMonth = DateUtil.getMonth(cursor.getLong(APPLIED_DATE_COL_NUM)), year = DateUtil.getYear(cursor.getLong(APPLIED_DATE_COL_NUM)) - 2000;

            do {
                if (lastMonth == DateUtil.getMonth(cursor.getLong(APPLIED_DATE_COL_NUM))) {
                    String key = MONTHS_NAMES[lastMonth] + " " + year;
                    if (!map.containsKey(key))
                        map.put(key, 1);
                    else
                        map.put(key, map.get(key) + 1);
                } else {
                    cursor.moveToPrevious();
                    lastMonth++;
                    if (lastMonth == MONTHS_NAMES.length + 1) {
                        lastMonth = 0;
                        year++;
                    }

                    String key = MONTHS_NAMES[lastMonth] + " " + year;
                    map.put(key, 0);
                }
            } while (cursor.moveToNext());

            cursor.close();
            db.close();

            return map;
        }

        cursor.close();
        db.close();

        return null;
    }

    public LinkedHashMap<String, Integer> getApplicationsByCompany() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + APPLICATIONS_TABLE_NAME + " ORDER BY company ASC", null);

        if (cursor.moveToFirst()) {
            LinkedHashMap<String, Integer> map = new LinkedHashMap<>();

            do {
                String key = cursor.getString(COMPANY_COL_NUM);
                if (!map.containsKey(key))
                    map.put(key, 1);
                else
                    map.put(key, map.get(key) + 1);
            }
            while (cursor.moveToNext());

            return map;
        }

        cursor.close();
        db.close();

        return null;
    }

    public void removeApplication(Application application) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(APPLICATIONS_TABLE_NAME, "id = ?", new String[]{"" + application.getId()});

        db.close();
    }
}
