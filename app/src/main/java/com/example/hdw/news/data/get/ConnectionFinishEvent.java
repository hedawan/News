package com.example.hdw.news.data.get;

import okhttp3.Response;

/**
 * Created by HDW on 2017/12/29.
 */

public class ConnectionFinishEvent {
    private Response mResponse;

    public ConnectionFinishEvent(Response response) {
        mResponse = response;
    }

    public Response getResponse() {
        return mResponse;
    }
}
