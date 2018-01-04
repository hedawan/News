package com.example.hdw.news.activity;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HDW on 2018/1/3.
 */

public class ActivityManager {
    public static List<Activity> sActivityList = new ArrayList<>();

    public static void addActivity(Activity activity){
        sActivityList.add(activity);
    }

    public static void removeActivity(Activity activity) {
        sActivityList.remove(activity);
    }

    public void finishAll() {
        for (Activity activity : sActivityList) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
            sActivityList.clear();
        }
    }
}
