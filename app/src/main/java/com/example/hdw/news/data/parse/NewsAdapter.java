package com.example.hdw.news.data.parse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HDW on 2017/12/31.
 */

public abstract class NewsAdapter<T> {
    private List<AdapterFinishListener> mListenerList;

    public NewsAdapter() {
        mListenerList = new ArrayList<>();
    }

    public void addAdapterFinishListener(AdapterFinishListener listener) {
        mListenerList.add(listener);
    }

    public void removeAdapterFinishListener(AdapterFinishListener listener) {
        mListenerList.remove(listener);
    }

    public abstract void notifyAdapterFinishListener(T object);

    public List<AdapterFinishListener> getListenerList() {
        return mListenerList;
    }

    public abstract void parse();
}
