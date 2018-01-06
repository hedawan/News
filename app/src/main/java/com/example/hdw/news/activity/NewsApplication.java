package com.example.hdw.news.activity;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by HDW on 2018/1/5.
 */

public class NewsApplication extends Application {
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        Fresco.initialize(this);
    }

    public static Context getContext(){
        return sContext;
    }
}
