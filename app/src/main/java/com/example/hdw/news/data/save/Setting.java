package com.example.hdw.news.data.save;

import com.example.hdw.news.data.get.ConnectionFinishListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HDW on 2018/1/10.
 */

public abstract class Setting<T> {
    private List<SettingChangeListener> mListeners;

    public Setting() {
        mListeners = new ArrayList<>();
    }

    public void addSettingChangeListener(SettingChangeListener listener) {
        mListeners.add(listener);
    }

    public void removeSettingChangeListener(SettingChangeListener listener) {
        mListeners.remove(listener);
    }

    public List<SettingChangeListener> getListeners() {
        return mListeners;
    }

    public abstract void notifySettingChangeListener(T o);
}
