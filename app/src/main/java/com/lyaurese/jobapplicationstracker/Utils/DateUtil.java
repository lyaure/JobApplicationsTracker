package com.lyaurese.jobapplicationstracker.Utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;

public abstract class DateUtil {
    private static final String[] MONTHS_NAMES = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    public static String getDate(long timeInMillis){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(new Date(timeInMillis));
    }

    public static int getDayOfWeek(long dateInMillis){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateInMillis);

        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public static int getDayOfMonth(long dateInMillis){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateInMillis);

        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int getMonth(long dateInMillis){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateInMillis);

        return calendar.get(Calendar.MONTH);
    }

    public static int getYear(long dateInMillis){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateInMillis);

        return calendar.get(Calendar.YEAR);
    }

    public static long[] getMonthIntervalsInMillis(String date){
        long[] intervals = new long[2];
        Calendar calendar = Calendar.getInstance();
        int month = 0, year = 2000;

        String monthName = date.substring(0, 3);

        for(int i=0; i<MONTHS_NAMES.length; i++){
            if(MONTHS_NAMES[i].equals(monthName)) {
                month = i;
                break;
            }
        }

        year += Integer.parseInt(date.substring(4, date.length()));

        calendar.set(year, month, 1);
        setToMidnight(calendar);

        intervals[0] = calendar.getTimeInMillis();

        int numOfDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        calendar.add(Calendar.DAY_OF_MONTH, numOfDaysInMonth - 1);
        setToMidnight(calendar);

        intervals[1] = calendar.getTimeInMillis();

        return intervals;
    }

    public static long[] getDayIntervals(String date){
        long[] intervals = new long[2];
        int month = Integer.parseInt(date.substring(4, 6)) - 1;
        int day = Integer.parseInt(date.substring(7, date.length()));

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int year = calendar.get(Calendar.YEAR);

        if(calendar.get(Calendar.MONTH) == 0 && month > calendar.get(Calendar.MONTH))
            year = calendar.get(Calendar.YEAR) - 1;

        calendar.set(year, month, day);
        setToMidnight(calendar);
        intervals[0] = calendar.getTimeInMillis();

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        intervals[1] = calendar.getTimeInMillis();

        return intervals;
    }

    public static long[] getLastSevenDaysIntervalsInMillis(){
        long[] intervals = new long[2];
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        setToMidnight(calendar);
        calendar.add(Calendar.DAY_OF_MONTH, 1);

        intervals[1] = calendar.getTimeInMillis();

        calendar.add(Calendar.DAY_OF_MONTH, -7);

        intervals[0] = calendar.getTimeInMillis();

        return intervals;
    }

    public static int getNumOfDaysInMonth(long dateInMillis){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateInMillis);

        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    private static void setToMidnight(Calendar calendar){
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }
}
