package com.example.hdw.news.util;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by HDW on 2018/1/9.
 */

public class TimeParse {
    private TimeParse() {
    }

    public static String millisecondToDateTime(long millisecond) {
        Date date = new Date(millisecond);
        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        return dateFormat.format(date);
    }

    public static String getSystemDateTime() {
        Date date = new Date(System.currentTimeMillis());
        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        return dateFormat.format(date);
    }

    public static long minToMillisecond(long min) {
        return min * 60 * 1000;
    }

    public static long secondToMillisecond(long second) {
        return second * 1000;
    }
}
