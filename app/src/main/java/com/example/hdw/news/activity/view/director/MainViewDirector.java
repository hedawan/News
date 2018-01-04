package com.example.hdw.news.activity.view.director;

import android.view.View;

import com.example.hdw.news.activity.view.builder.ViewBuilder;

/**
 * Created by HDW on 2017/12/27.
 */

public class MainViewDirector extends ViewBuildDirector {
    public MainViewDirector(ViewBuilder viewBuilder) {
        super(viewBuilder);
    }

    @Override
    public View construct() {
        mViewBuilder.buildToolbar();
        mViewBuilder.buildNavigation();
        mViewBuilder.buildDrawerLayout();
        mViewBuilder.buildAdapter();
        return mViewBuilder.getResult();
    }
}
