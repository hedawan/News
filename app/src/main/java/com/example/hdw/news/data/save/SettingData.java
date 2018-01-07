package com.example.hdw.news.data.save;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.hdw.news.activity.NewsApplication;

/**
 * Created by HDW on 2018/1/5.
 */

public class SettingData {
    public static final boolean OPEN = true;
    public static final boolean CLOSE = false;
    public static final int WEB_VIEW = 0;
    public static final int SYSTEM_BROWSER = 1;
    private int mReadNewsMode;
    private boolean mAlertSelectLookNewsMode;
    private boolean mNewsUpdate;
    private long mNewsUpdateTime;
    private String mNewsUrl;
    private Context mContext;
    public static final SettingData sSettingData = new SettingData(NewsApplication.getContext());

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    private SettingData(Context context) {
        mContext = context;
        mEditor = mContext.getSharedPreferences("Setting", Context.MODE_PRIVATE).edit();
        mSharedPreferences = context.getSharedPreferences("Setting", Context.MODE_PRIVATE);
        mReadNewsMode = mSharedPreferences.getInt("LookNewsMode", -1);
        mAlertSelectLookNewsMode = mSharedPreferences.getBoolean("AlertSelectLookNewsMode", OPEN);
        mNewsUpdate = mSharedPreferences.getBoolean("NewsUpdate", CLOSE);
        mNewsUpdateTime = mSharedPreferences.getInt("NewsUpdateTime", -1);
        mNewsUrl = mSharedPreferences.getString("NewsUrl", null);
        initSetting();
    }

    private void initSetting() {
        if (mReadNewsMode == -1) {
            mEditor.putInt("mReadNewsMode", WEB_VIEW);
            mReadNewsMode = WEB_VIEW;
        }
        if (mNewsUpdateTime == -1) {
            mEditor.putLong("mNewsUpdateTime", 1);
            mNewsUpdateTime = 1;
        }
        if (mNewsUrl == null) {
            String newsUrl= "http://openapi.inews.qq.com/getQQNewsIndexAndItems?chlid=news_news_top&refer=mobilewwwqqcom";
            mEditor.putString("mNewsUrl", newsUrl);
            mNewsUrl = newsUrl;
        }
        mEditor.putBoolean("AlertSelectLookNewsMode", mAlertSelectLookNewsMode);
        mEditor.putBoolean("NewsUpdate", mNewsUpdate);
        mEditor.apply();
    }

    public static SettingData getInstance(){
        return sSettingData;
    }

    public int getReadNewsMode() {
        return mReadNewsMode;
    }

    public void setReadNewsMode(int readNewsMode) {
        mReadNewsMode = readNewsMode;
        mEditor.putInt("mReadNewsMode", readNewsMode);
        mEditor.apply();
    }

    public boolean isAlertSelectLookNewsMode() {
        return mAlertSelectLookNewsMode;
    }

    public void setAlertSelectLookNewsMode(boolean alertSelectLookNewsMode) {
        mAlertSelectLookNewsMode = alertSelectLookNewsMode;
        mEditor.putBoolean("AlertSelectLookNewsMode", alertSelectLookNewsMode);
        mEditor.apply();
    }

    public boolean isNewsUpdate() {
        return mNewsUpdate;
    }

    public void setNewsUpdate(boolean newsUpdate) {
        mNewsUpdate = newsUpdate;
        mEditor.putBoolean("NewsUpdate", mNewsUpdate);
        mEditor.apply();
    }

    public long getNewsUpdateTime() {
        return mNewsUpdateTime;
    }

    public void setNewsUpdateTime(long newsUpdateTime) {
        mNewsUpdateTime = newsUpdateTime;
        mEditor.putLong("mNewsUpdateTime", newsUpdateTime);
        mEditor.apply();
    }

    public String getNewsUrl() {
        return mNewsUrl;
    }

    public void setNewsUrl(String newsUrl) {
        mNewsUrl = newsUrl;
        mEditor.putString("mNewsUrl", newsUrl);
        mEditor.apply();
    }
}
