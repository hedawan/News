package com.example.hdw.news.data.get;

import javax.sql.ConnectionEvent;

/**
 * Created by HDW on 2017/12/29.
 */

public interface ConnectionFinishListener {
    void connectionFinish(ConnectionFinishEvent event);
}
