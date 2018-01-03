package com.example.hdw.news.data.get;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by HDW on 2017/12/29.
 */

public abstract class GetConnection<T> {
    private List<ConnectionFinishListener> mListeners;

    public GetConnection() {
        mListeners = new ArrayList<>();
    }

    public void addConnectionFinishListener(ConnectionFinishListener listener) {
        mListeners.add(listener);
    }

    public void removeConnectionFinishListener(ConnectionFinishListener listener){
        mListeners.remove(listener);
    }

    public List<ConnectionFinishListener> getListeners() {
        return mListeners;
    }

    public abstract void notifyConnectionFinishListener(T o);
}
