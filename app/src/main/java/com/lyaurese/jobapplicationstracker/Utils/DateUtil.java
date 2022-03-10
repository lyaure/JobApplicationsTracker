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

    public static int getMonth(long timeInMillis){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);

        return calendar.get(Calendar.MONTH);
    }

    public static int getYear(long timeInMillis){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);

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

    private static void setToMidnight(Calendar calendar){
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }
}
