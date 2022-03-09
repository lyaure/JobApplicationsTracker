package com.lyaurese.jobapplicationstracker.Utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public abstract class DateUtil {
    @SuppressLint("DefaultLocale")
    public static String getDate(Calendar calendar){
        String date = "%d/%d/%d";
        return String.format(date, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) == 12 ? 0 : calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.YEAR));
    }

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
}
