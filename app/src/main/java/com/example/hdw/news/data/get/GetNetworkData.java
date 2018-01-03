package com.example.hdw.news.data.get;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by HDW on 2017/12/29.
 */

public class GetNetworkData extends GetConnection<Response> implements Runnable{
    private OkHttpClient mOkHttpClient;
    private Request mRequest;
    private ConnectionFinishEvent mEvent;
    private Thread mThread;

    public GetNetworkData(OkHttpClient client, Request request) {
        mOkHttpClient = client;
        mRequest = request;
    }

    public void connection(){
        mThread = new Thread(this);;
        mThread.start();
    }

    @Override
    public void notifyConnectionFinishListener(Response response) {
        mEvent = new ConnectionFinishEvent(response);
        for (ConnectionFinishListener listener : getListeners()) {
            listener.connectionFinish(mEvent);
        }
    }

    @Override
    public void run() {
        Request request = mRequest;
        Response response = null;
        try {
            response = mOkHttpClient.newCall(request).execute();
            notifyConnectionFinishListener(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public void setOkHttpClient(OkHttpClient okHttpClient) {
        mOkHttpClient = okHttpClient;
    }

    public Request getRequest() {
        return mRequest;
    }

    public void setRequest(Request request) {
        mRequest = request;
    }
}
