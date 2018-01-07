package com.example.hdw.news.activity.view.director;

import android.view.View;

import com.example.hdw.news.activity.view.builder.ViewBuilder;

/**
 * Created by HDW on 2018/1/7.
 */

public class SettingViewDirector extends ViewBuildDirector {
    public SettingViewDirector(ViewBuilder viewBuilder) {
        super(viewBuilder);
    }

    @Override
    public View construct() {
        mViewBuilder.buildView();
        mViewBuilder.buildToolbar();
        mViewBuilder.buildDrawerLayout();
        mViewBuilder.buildAdapter();
        setView(mViewBuilder.getResult());
        return getView();
    }
}
