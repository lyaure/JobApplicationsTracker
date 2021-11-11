package com.lyaurese.jobsorganizer.Utils;

import android.annotation.SuppressLint;

import java.util.Calendar;

public abstract class DateUtil {
    @SuppressLint("DefaultLocale")
    public static String getDate(Calendar calendar){
        String date = "%d/%d/%d";
        return String.format(date, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) == 0 ? 12 : calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
    }
}
