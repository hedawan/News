package com.example.hdw.news.data.news;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HDW on 2018/1/2.
 */

public class TencentNews {
    private List<String> mIdList;
    private List<TencentNewsItem> mTencentNewsList;

    public TencentNews() {
        mIdList = new ArrayList<>();
        mTencentNewsList = new ArrayList<>();
    }

    public List<String> getIdList() {
        return mIdList;
    }

    public List<TencentNewsItem> getTencentNewsList() {
        return mTencentNewsList;
    }

    public class TencentNewsItem {
        public String mNewsId;
        public String mTitle;
        public String mNewsUrl;
        public String mNewsTime;
        public List<String> mNewsImageUrl;
        public String mNewsSource;

        public TencentNewsItem() {
            mNewsImageUrl = new ArrayList<>();
        }
    }
}
