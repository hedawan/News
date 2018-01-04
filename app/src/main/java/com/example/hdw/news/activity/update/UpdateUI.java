package com.example.hdw.news.activity.update;

import android.os.Handler;
import android.os.Message;

/**
 * Created by HDW on 2018/1/3.
 */

public class UpdateUI extends Handler {
    private UpdateUIListener mListener;
    private static UpdateUI sUpdateUI = new UpdateUI();
    private UpdateUI(){}
    public static UpdateUI getInstance(){
        return sUpdateUI;
    }

    @Override
    public void handleMessage(Message msg) {
        mListener = (UpdateUIListener) msg.obj;
        mListener.update();
    }
}
