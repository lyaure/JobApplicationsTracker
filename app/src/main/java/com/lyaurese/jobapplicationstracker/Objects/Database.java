package com.lyaurese.jobapplicationstracker.Objects;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lyaurese.jobapplicationstracker.Utils.DateUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;


public class Database extends SQLiteOpenHelper {
    private static final String DB_NAME = "JobOrganizerDB";
    private static final int DB_VERSION = 2;
    public static Database instance;

    private static final String APPLICATIONS_TABLE_NAME = "Applications", TAGS_TABLE = "Tags", JUNCTION_TABLE = "Junction";
    private static final String ARCHIVE_APPLICATIONS_TABLE = "Applications_archive", ARCHIVE_TAGS_TABLE = "Tags_archive", ARCHIVE_JUNCTION_TABLE = "Junction_archive", ARCHIVE_TABLE_NAMES = "Archives_tables";

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
    private final int COMPANY = 0, LOCATION = 1, DATE = 3, TAGS = 2, ALL = -1, INACTIVE = 0, ACTIVE = 1;

    private final String[] MONTHS_NAMES = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private final String[] DAYS_OF_WEEK = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};


    public Database(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE IF NOT EXISTS " + APPLICATIONS_TABLE_NAME +
                "(appId INTEGER, " +
                "company TEXT, " +
                "jobPosition TEXT, " +
                "jobNumber TEXT, " +
                "location TEXT, " +
                "active INTEGER, " +
                "appliedDate INTEGER," +
                "interview INTEGER, " +
                "interviewDate INTEGER, " +
                "comments TEXT," +
                "archiveName TEXT)";
        db.execSQL(query);

        query = "CREATE TABLE IF NOT EXISTS " + TAGS_TABLE + "(tagId INTEGER, name TEXT, archiveName TEXT)";
        db.execSQL(query);

        query = "CREATE TABLE IF NOT EXISTS " + JUNCTION_TABLE + "(appId INTEGER, tagId INTEGER, archiveName TEXT)";
        db.execSQL(query);

        query = "CREATE TABLE IF NOT EXISTS " + ARCHIVE_APPLICATIONS_TABLE +
                "(appId INTEGER, " +
                "company TEXT, " +
                "jobPosition TEXT, " +
                "jobNumber TEXT, " +
                "location TEXT, " +
                "active INTEGER, " +
                "appliedDate INTEGER," +
                "interview INTEGER, " +
                "interviewDate INTEGER, " +
                "comments TEXT," +
                "archiveName TEXT)";
        db.execSQL(query);

        query = "CREATE TABLE IF NOT EXISTS " + ARCHIVE_TAGS_TABLE + "(tagId INTEGER, name TEXT, archiveName TEXT)";
        db.execSQL(query);

        query = "CREATE TABLE IF NOT EXISTS " + ARCHIVE_JUNCTION_TABLE + "(appId INTEGER, tagId INTEGER, archiveName TEXT)";
        db.execSQL(query);

        query = "CREATE TABLE IF NOT EXISTS " + ARCHIVE_TABLE_NAMES + "(tableName TEXT, count INTEGER)";
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
        String query = "CREATE TABLE IF NOT EXISTS " + ARCHIVE_TABLE_NAMES + "(tableName TEXT, count INTEGER)";
        db.execSQL(query);
    }

//    @Override
//    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//
//    }

    public void moveToArchive(String archiveName){
        int count = this.getApplicationsCount();

        if( count > 0){
            SQLiteDatabase db = getWritableDatabase();

            copyAndDelete(db, APPLICATIONS_TABLE_NAME, ARCHIVE_APPLICATIONS_TABLE, archiveName);
            copyAndDelete(db, JUNCTION_TABLE, ARCHIVE_JUNCTION_TABLE, archiveName);
            copyAndDelete(db, TAGS_TABLE, ARCHIVE_TAGS_TABLE, archiveName);

            ContentValues values = new ContentValues();
            values.put("tableName", archiveName);
            values.put("count", count);
            db.insert(ARCHIVE_TABLE_NAMES, null, values);

            db.close();
        }
    }

    private void copyAndDelete(SQLiteDatabase db, String fromTable, String toTable, String archiveName){
        String query = "UPDATE " + fromTable + " SET archiveName = '" + archiveName + "'";
        db.execSQL(query);

        query = "INSERT INTO " + toTable + " SELECT * FROM " + fromTable;
        db.execSQL(query);

        query = "DELETE FROM " + fromTable;
        db.execSQL(query);
    }

    public void restore(String tableName){
        SQLiteDatabase db = getWritableDatabase();

        restoreAndDelete(db, ARCHIVE_APPLICATIONS_TABLE, APPLICATIONS_TABLE_NAME, tableName);
        restoreAndDelete(db, ARCHIVE_JUNCTION_TABLE, JUNCTION_TABLE, tableName);
        restoreAndDelete(db, ARCHIVE_TAGS_TABLE, TAGS_TABLE, tableName);

        String query = "DELETE FROM " + ARCHIVE_TABLE_NAMES + " WHERE tableName = '" + tableName + "'";
        db.execSQL(query);

        db.close();
    }

    private void restoreAndDelete(SQLiteDatabase db, String fromTable, String toTable, String archiveName){
        String query = "INSERT INTO " + toTable + " SELECT * FROM " + fromTable + " WHERE archiveName = '" + archiveName + "'";
        db.execSQL(query);

        query = "UPDATE " + toTable + " SET archiveName = 'main'";
        db.execSQL(query);

        query = "DELETE FROM " + fromTable + " WHERE archiveName = '" + archiveName + "'";
        db.execSQL(query);
    }

    public long insertNewApplication(Application application) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("appId", application.getId());
        values.put("company", application.getCompanyName());
        values.put("jobPosition", application.getJobPosition());
        values.put("jobNumber", application.getJobNumber());
        values.put("location", application.getLocation());
        values.put("active", application.applied() ? 1 : 0);
        values.put("appliedDate", application.getAppliedDate());
        values.put("interview", 0);
        values.put("interviewDate", 0);
        values.put("comments", application.getComment());
        values.put("archiveName", "main");

        long appId;
        appId = db.insert(APPLICATIONS_TABLE_NAME, null, values);

        values.clear();
        values.put("appId", appId);

        db.update(APPLICATIONS_TABLE_NAME, values, "appId = -1", null);

        insertTags(db, appId, application.getTags());

        db.close();

        return appId;
    }

    public void editApplication(Application application) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        Cursor cursor = db.rawQuery("SELECT * FROM " + APPLICATIONS_TABLE_NAME + " WHERE appId = '" + application.getId() + "'", null);
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

        db.update(APPLICATIONS_TABLE_NAME, values, "appId = ?", new String[]{"" + application.getId()});

        editTags(db, application.getId(), application.getTags());

        cursor.close();
    }

    public void editTags(SQLiteDatabase db, long appId, ArrayList<String> tags) {
        ArrayList<String> oldTags = getAppTags(appId);

        ArrayList<String> delete = new ArrayList<>();
        delete.addAll(oldTags);
        delete.removeAll(tags);

        ArrayList<String> tagIds = new ArrayList<>();

        for(String tag : delete) {
            Cursor cursor = db.rawQuery("SELECT tagId FROM " + TAGS_TABLE + " WHERE name = '" + tag + "'", null);
            cursor.moveToFirst();

            do{
                tagIds.add(String.valueOf(cursor.getLong(0)));
            }while(cursor.moveToNext());
        }

        for(String id : tagIds)
            db.delete(JUNCTION_TABLE, "appId = " + appId + " AND tagId = " + id, null);

        deleteIfNeeded(db, delete);

        tags.removeAll(oldTags);

        insertTags(db, appId, tags);
    }

    private void insertTags(SQLiteDatabase db, long appId, ArrayList<String> tags) {
        if (tags.size() > 0) {
            ContentValues values = new ContentValues();

            long tagId;

            for (String tag : tags) {
                Cursor cursor = db.rawQuery("SELECT tagId FROM " + TAGS_TABLE + " WHERE name = '" + tag + "'", null);


                if (cursor.moveToFirst()) {
                    tagId = cursor.getLong(0);
                } else {
                    values.put("tagId", -1);
                    values.put("name", tag);
                    tagId = db.insert(TAGS_TABLE, null, values);

                    values.clear();
                    values.put("tagId", tagId);

                    db.update(TAGS_TABLE, values, "tagId = -1", null);
                }

                values.clear();
                values.put("appId", appId);
                values.put("tagId", tagId);

                db.insert(JUNCTION_TABLE, null, values);

                values.clear();
                cursor.close();
            }
        }
        db.close();
    }

    public long getApplicationId(String jobNumber){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT appId FROM " + APPLICATIONS_TABLE_NAME + " WHERE jobNumber = '" + jobNumber + "' COLLATE NOCASE", null);

        long id = -1;

        if(cursor.moveToFirst())
            id = cursor.getLong(0);

        cursor.close();
        db.close();

        return id;
    }

    public String getApplicationCompanyName(long id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT company FROM " + APPLICATIONS_TABLE_NAME + " WHERE appId = " + id, null);

        cursor.moveToFirst();
        String name = cursor.getString(0);

        cursor.close();
        db.close();

        return name;
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

    public ArrayList<ListObject> getTagsList(int filter){
        String query;

        if (filter == ALL)
            query = "SELECT " + TAGS_TABLE + ".name, COUNT(*) FROM " + TAGS_TABLE +
                    " JOIN " + JUNCTION_TABLE + " ON " + JUNCTION_TABLE + ".tagId = " + TAGS_TABLE + ".tagId" +
                    " JOIN " + APPLICATIONS_TABLE_NAME + " ON " + APPLICATIONS_TABLE_NAME + ".appId = " + JUNCTION_TABLE + ".appId" +
                    " GROUP BY " + TAGS_TABLE + ".name";
        else
            query = "SELECT " + TAGS_TABLE + ".name, COUNT(*) FROM " + TAGS_TABLE +
                    " JOIN " + JUNCTION_TABLE + " ON " + JUNCTION_TABLE + ".tagId = " + TAGS_TABLE + ".tagId" +
                    " JOIN " + APPLICATIONS_TABLE_NAME + " ON " + APPLICATIONS_TABLE_NAME + ".appId = " + JUNCTION_TABLE + ".appId" +
                    " WHERE " + APPLICATIONS_TABLE_NAME + ".active = " + filter +
                    " GROUP BY " + TAGS_TABLE + ".name";

        return getListObjects(TAGS, query);
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

    public ArrayList<Application> getApplicationsListByTag(String tagName, int filter){
        String query;

        if(filter ==  -1)
            query = "SELECT * FROM " + APPLICATIONS_TABLE_NAME +
                    " JOIN " + JUNCTION_TABLE + " ON " + APPLICATIONS_TABLE_NAME + ".appId = " + JUNCTION_TABLE + ".appId" +
                    " JOIN " + TAGS_TABLE + " ON " + JUNCTION_TABLE + ".tagId = " + TAGS_TABLE + ".tagId" +
                    " WHERE " + TAGS_TABLE + ".name = '" + tagName + "'";
        else
            query = "SELECT * FROM " + APPLICATIONS_TABLE_NAME +
                    " JOIN " + JUNCTION_TABLE + " ON " + APPLICATIONS_TABLE_NAME + ".appId = " + JUNCTION_TABLE + ".appId" +
                    " JOIN " + TAGS_TABLE + " ON " + JUNCTION_TABLE + ".tagId = " + TAGS_TABLE + ".tagId" +
                    " WHERE " + TAGS_TABLE + ".name = '" + tagName + "' AND " + APPLICATIONS_TABLE_NAME + ".active = " + filter;

        return getApplicationsList(query);
    }

    private ArrayList<Application> getApplicationsList(String query) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            ArrayList<Application> list = new ArrayList<>();
            do {
                Application application = new Application(cursor.getInt(ID_COL_NUM), cursor.getString(COMPANY_COL_NUM), cursor.getString(JOB_POSITION_COL_NUM), cursor.getString(JOB_NUMBER_COL),
                        cursor.getString(LOCATION_COL_NUM), cursor.getLong(APPLIED_DATE_COL_NUM), cursor.getString(COMMENTS_COL_NUM));

                if (cursor.getInt(INTERVIEW_COL_NUM) == 1)
                    application.setInterviewDate(cursor.getLong(INTERVIEW_DATE_COL_NUM));

                if (cursor.getInt(ACTIVE_COL_NUM) == 0)
                    application.setActive(false);

                application.setTags(getAppTags(application.getId()));

                list.add(application);
            } while (cursor.moveToNext());

            return list;
        } else {
            cursor.close();
            db.close();

            return null;
        }
    }

    public ArrayList<String> getAppTags(long appId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT tagId FROM " + JUNCTION_TABLE + " WHERE appId = " + appId, null);

        ArrayList<String> tags = new ArrayList<>();
        ArrayList<Long> tagsId = new ArrayList<>();

        if (cursor.moveToFirst())
            do{
                tagsId.add(cursor.getLong(0));
            }while(cursor.moveToNext());

        for (long id : tagsId) {
            cursor = db.rawQuery("SELECT name FROM " + TAGS_TABLE + " WHERE tagId = " + id, null);
            cursor.moveToFirst();
            tags.add(cursor.getString(0));
        }

        cursor.close();
//        db.close();

        return tags;
    }

    private void deleteIfNeeded(SQLiteDatabase db, ArrayList<String> tags){
        for(String tag : tags){
            Cursor cursor = db.rawQuery("SELECT * FROM " + TAGS_TABLE + " WHERE name = '" + tag + "'", null);
            cursor.moveToNext();
            long tagId = cursor.getLong(0);

            cursor = db.rawQuery("SELECT * FROM " + JUNCTION_TABLE + " WHERE tagId = " + tagId, null);
            if(cursor.getCount() == 0)
                db.delete(TAGS_TABLE,"name = '" + tag + "'", null);

            cursor.close();
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
        String query = "SELECT * FROM " + APPLICATIONS_TABLE_NAME;
        return getApplicationsCount(query);
    }

    public int getArchiveApplicationsCount(String tableName){
        String query = "SELECT * FROM " + ARCHIVE_APPLICATIONS_TABLE + " WHERE archiveName = '" + tableName + "'";
        return getApplicationsCount(query);
    }

    private int getApplicationsCount(String query){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        int count = cursor.getCount();

        cursor.close();
        db.close();

        return count;
    }

    public int getActiveApplicationsCount() {
        String query = "SELECT * FROM " + APPLICATIONS_TABLE_NAME + " WHERE active = 1";
        return getActiveCount(query);
    }

    public int getActiveArchiveCount(String tableName){
        String query = "SELECT * FROM " + ARCHIVE_APPLICATIONS_TABLE + " WHERE active = 1 AND archiveName = '" + tableName + "'";
        return getActiveCount(query);
    }

    private int getActiveCount(String query){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        int count = cursor.getCount();

        cursor.close();
        db.close();

        return count;
    }

    public int getInterviewApplicationsCount() {
        String query = "SELECT * FROM " + APPLICATIONS_TABLE_NAME + " WHERE interview = 1";
        return getInterviewCount(query);
    }

    public int getInterviewArchiveCount(String tableName){
        String query = "SELECT * FROM " + ARCHIVE_APPLICATIONS_TABLE + " WHERE interview = 1 AND archiveName = '" + tableName + "'";
        return getInterviewCount(query);
    }

    private int getInterviewCount(String query){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        int count = cursor.getCount();

        cursor.close();
        db.close();

        return count;
    }

    public void setActive(long appId, boolean active) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + APPLICATIONS_TABLE_NAME + " WHERE appId = '" + appId + "'", null);

        ContentValues values = new ContentValues();
        values.put("active", active ? 1 : 0);

        db.update(APPLICATIONS_TABLE_NAME, values, "appId = ?", new String[]{"" + appId});

        cursor.close();
        db.close();
    }

    public LinkedHashMap<String, Integer> getLastSevenDaysApplicationsCount() {
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

            do {
                dayOfWeek = DateUtil.getDayOfWeek(cursor.getLong(APPLIED_DATE_COL_NUM)) - 1;
                dayOfMonth = DateUtil.getDayOfMonth(cursor.getLong(APPLIED_DATE_COL_NUM));
                month = DateUtil.getMonth(cursor.getLong(APPLIED_DATE_COL_NUM)) + 1;

                String key = DAYS_OF_WEEK[dayOfWeek] + " " + String.format("%02d", month) + "/" + String.format("%02d", dayOfMonth);
                map.put(key, map.get(key) + 1);
            } while (cursor.moveToNext());

            cursor.close();
            db.close();

            return map;
        }

        cursor.close();
        db.close();

        return null;
    }

    public LinkedHashMap<String, Integer> getApplicationsCountByMonth() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + APPLICATIONS_TABLE_NAME + " ORDER BY appliedDate ASC", null);

        if (cursor.moveToFirst()) {
            LinkedHashMap<String, Integer> map = new LinkedHashMap<>();

            int lastMonth = DateUtil.getMonth(cursor.getLong(APPLIED_DATE_COL_NUM)), year = DateUtil.getYear(cursor.getLong(APPLIED_DATE_COL_NUM));

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
                    if (lastMonth == MONTHS_NAMES.length) {
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

    public LinkedHashMap<String, Integer> getApplicationsCountByCompany() {
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

    public LinkedHashMap<String, Integer> getApplicationsCountByTag(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + TAGS_TABLE + ".name, COUNT(*) FROM " + TAGS_TABLE +
                " INNER JOIN " + JUNCTION_TABLE + " ON " + TAGS_TABLE + ".tagId = " + JUNCTION_TABLE + ".tagId " +
                "GROUP BY " + TAGS_TABLE + ".name ORDER BY " + TAGS_TABLE + ".name ASC", null);

        if (cursor.moveToFirst()) {
            LinkedHashMap<String, Integer> map = new LinkedHashMap<>();

            do {
                String key = cursor.getString(0);
                map.put(key, cursor.getInt(1));
            }
            while (cursor.moveToNext());

            return map;
        }

        cursor.close();
        db.close();

        return null;
    }

    public ArrayList<Tag> getTagsListWithId(long id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM " + TAGS_TABLE, null);

        ArrayList<String> allTags = new ArrayList<>();

        if(cursor.moveToFirst()){
            do{
                allTags.add(cursor.getString(0));
            }while(cursor.moveToNext());
        }

        ArrayList<String> appTags = getAppTags(id);

        allTags.removeAll(appTags);

        ArrayList<Tag> tags = new ArrayList<>();

        for(String tag : appTags)
            tags.add(new Tag(tag, true));

        for(String tag : allTags)
            tags.add(new Tag(tag, false));

        cursor.close();
        db.close();

        return tags;
    }

    public ArrayList<Tag> getAllTags() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM " + TAGS_TABLE, null);

        ArrayList<Tag> tags = new ArrayList<>();

        if(cursor.moveToFirst()){
            do{
                tags.add(new Tag(cursor.getString(0), false));
            }while(cursor.moveToNext());
        }

        db.close();
        cursor.close();
        return tags;
    }

    public void removeApplication(Application application) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(APPLICATIONS_TABLE_NAME, "appId = " + application.getId(), null);

        db.close();
    }

    public ArrayList<ListObject> getArchiveTableNames(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + ARCHIVE_TABLE_NAMES, null);

        if(cursor.moveToFirst()){
            ArrayList<ListObject> names = new ArrayList<>();

            do{
                ListObject object = new ListObject(-1, cursor.getString(0), cursor.getInt(1));
                names.add(object);
            }while(cursor.moveToNext());

            return names;
        }

        return null;
    }
}
