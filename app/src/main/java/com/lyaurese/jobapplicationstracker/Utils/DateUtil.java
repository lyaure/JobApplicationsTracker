package com.lyaurese.jobapplicationstracker.Utils;

import android.annotation.SuppressLint;

import java.util.Calendar;

public abstract class DateUtil {
    @SuppressLint("DefaultLocale")
    public static String getDate(Calendar calendar){
        String date = "%d/%d/%d";
        return String.format(date, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) == 12 ? 0 : calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.YEAR));
    }
}
