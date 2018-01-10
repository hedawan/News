package com.example.hdw.news.data.save;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.hdw.news.activity.NewsApplication;
import com.example.hdw.news.data.get.ConnectionFinishEvent;
import com.example.hdw.news.data.get.ConnectionFinishListener;

/**
 * Created by HDW on 2018/1/5.
 */

public class SettingData extends Setting<SettingData> {
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
    private static final String TAG = "SettingData";

    private SettingData(Context context) {
        mContext = context;
        mEditor = mContext.getSharedPreferences("Setting", Context.MODE_PRIVATE).edit();
        mSharedPreferences = context.getSharedPreferences("Setting", Context.MODE_PRIVATE);
        mReadNewsMode = mSharedPreferences.getInt("ReadNewsMode", -1);
        mAlertSelectLookNewsMode = mSharedPreferences.getBoolean("AlertSelectLookNewsMode", OPEN);
        mNewsUpdate = mSharedPreferences.getBoolean("NewsUpdate", CLOSE);
        mNewsUpdateTime = mSharedPreferences.getLong("NewsUpdateTime", -1);
        mNewsUrl = mSharedPreferences.getString("NewsUrl", null);
        initSetting();
    }

    private void initSetting() {
        if (mReadNewsMode == -1) {
            mEditor.putInt("ReadNewsMode", WEB_VIEW);
            mReadNewsMode = WEB_VIEW;
        }
        Log.d(TAG, "initSetting: news update time=" + mNewsUpdateTime);
        if (mNewsUpdateTime == -1) {
            mEditor.putLong("NewsUpdateTime", 900000);
            mNewsUpdateTime = 1;
        }
        if (mNewsUrl == null) {
            String newsUrl = "https://xw.qq.com/service/api/proxy?key=Xw@2017Mmd&charset=utf-8&url=http://openapi.inews.qq.com/getQQNewsIndexAndItems?chlid=news_news_top&refer=mobilewwwqqcom&srcfrom=newsapp&otype=json&ext_action=Fimgurl33,Fimgurl32,Fimgurl30";
            mEditor.putString("NewsUrl", newsUrl);
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
        mEditor.putInt("ReadNewsMode", readNewsMode);
        mEditor.apply();
        notifySettingChangeListener(this);
    }

    public boolean isAlertSelectLookNewsMode() {
        return mAlertSelectLookNewsMode;
    }

    public void setAlertSelectLookNewsMode(boolean alertSelectLookNewsMode) {
        mAlertSelectLookNewsMode = alertSelectLookNewsMode;
        mEditor.putBoolean("AlertSelectLookNewsMode", alertSelectLookNewsMode);
        mEditor.apply();
        notifySettingChangeListener(this);
    }

    public boolean isNewsUpdate() {
        return mNewsUpdate;
    }

    public void setNewsUpdate(boolean newsUpdate) {
        mNewsUpdate = newsUpdate;
        mEditor.putBoolean("NewsUpdate", mNewsUpdate);
        mEditor.apply();
        notifySettingChangeListener(this);
    }

    public long getNewsUpdateTime() {
        return mNewsUpdateTime;
    }

    public void setNewsUpdateTime(long newsUpdateTime) {
        mNewsUpdateTime = newsUpdateTime;
        mEditor.putLong("NewsUpdateTime", newsUpdateTime);
        mEditor.apply();
        notifySettingChangeListener(this);
    }

    public String getNewsUrl() {
        return mNewsUrl;
    }

    public void setNewsUrl(String newsUrl) {
        mNewsUrl = newsUrl;
        mEditor.putString("NewsUrl", newsUrl);
        mEditor.apply();
        notifySettingChangeListener(this);
    }

    @Override
    public void notifySettingChangeListener(SettingData o) {
        SettingChangeEvent event = new SettingChangeEvent(o);
        for (SettingChangeListener listener : getListeners()) {
            listener.settingChange(event);
        }
    }
}
