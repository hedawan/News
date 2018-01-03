package com.example.hdw.news.activity.view.director;

import android.view.View;

import com.example.hdw.news.activity.view.builder.ViewBuilder;

/**
 * Created by HDW on 2017/12/27.
 */

public abstract class ViewBuildDirector implements Director<View> {
    protected ViewBuilder mViewBuilder;

    public ViewBuildDirector(ViewBuilder viewBuilder) {
        mViewBuilder = viewBuilder;
    }

    public ViewBuilder getViewBuilder() {
        return mViewBuilder;
    }

    public void setViewBuilder(ViewBuilder viewBuilder) {
        mViewBuilder = viewBuilder;
    }
}
