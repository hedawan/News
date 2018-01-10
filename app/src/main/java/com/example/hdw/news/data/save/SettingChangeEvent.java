package com.example.hdw.news.data.save;

/**
 * Created by HDW on 2018/1/10.
 */

public class SettingChangeEvent {
    private SettingData mSettingData;

    public SettingChangeEvent(SettingData settingData) {
        mSettingData = settingData;
    }

    public SettingData getSettingData() {
        return mSettingData;
    }
}
