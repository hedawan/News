package com.example.hdw.news.activity.view.director;

import android.view.View;

import com.example.hdw.news.activity.view.builder.ViewBuilder;

/**
 * Created by HDW on 2018/1/9.
 */

public class AboutViewDirector extends ViewBuildDirector {
    public AboutViewDirector(ViewBuilder viewBuilder) {
        super(viewBuilder);
    }

    @Override
    public View construct() {
        mViewBuilder.buildToolbar();
        mViewBuilder.buildDrawerLayout();
        mViewBuilder.buildView();
        return mViewBuilder.getResult();
    }

}
