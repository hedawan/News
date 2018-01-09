package com.example.hdw.news.activity.view.director;

import android.view.View;

import com.example.hdw.news.activity.view.builder.ViewBuilder;

/**
 * Created by HDW on 2018/1/9.
 */

public class CommentViewDirector extends ViewBuildDirector {
    public CommentViewDirector(ViewBuilder viewBuilder) {
        super(viewBuilder);
    }

    @Override
    public View construct() {
        mViewBuilder.buildToolbar();
        mViewBuilder.buildAdapter();
        return mViewBuilder.getResult();
    }
}
