package com.example.hdw.news.data.parse;

import com.example.hdw.news.data.news.TencentNews;

/**
 * Created by HDW on 2017/12/31.
 */

public class AdapterFinishEvent {
    private TencentNews mTencentNews;

    public AdapterFinishEvent() {
    }

    public AdapterFinishEvent(TencentNews tencentNews) {
        mTencentNews = tencentNews;
    }

    public TencentNews getTencentNews() {
        return mTencentNews;
    }

    public void setTencentNews(TencentNews tencentNews) {
        mTencentNews = tencentNews;
    }
}
