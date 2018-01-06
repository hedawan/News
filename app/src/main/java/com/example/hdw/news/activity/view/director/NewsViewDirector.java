package com.example.hdw.news.activity.view.director;

import android.view.View;

import com.example.hdw.news.activity.view.builder.ViewBuilder;

/**
 * Created by HDW on 2018/1/4.
 */

public class NewsViewDirector extends ViewBuildDirector {
    public NewsViewDirector(ViewBuilder viewBuilder) {
        super(viewBuilder);
    }

    @Override
    public View construct() {
        mViewBuilder.buildToolbar();
        mViewBuilder.buildView();
        return mViewBuilder.getResult();
    }


}
