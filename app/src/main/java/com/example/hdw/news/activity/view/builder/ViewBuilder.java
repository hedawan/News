package com.example.hdw.news.activity.view.builder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by HDW on 2017/12/27.
 */

public abstract class ViewBuilder implements Builder<View>{
    private Context mContext;
    private View mView;
    private LayoutInflater mLayoutInflater;

    public ViewBuilder(Context context) {
        mContext = context;
    }

    public ViewBuilder(Context context, int resource) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mView = mLayoutInflater.inflate(resource, null, false);
    }

    public void buildView(){};
    public void buildToolbar(){};
    public void buildNavigation(){};
    public void buildDrawerLayout(){}
    public void buildAdapter(){}

    @Override
    public View getResult() {
        return mView;
    }

    public final <T extends View> T findViewById(int id){
        return mView.findViewById(id);
    }

    public Context getContext() {
        return mContext;
    }

    public View getView() {
        return mView;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public LayoutInflater getLayoutInflater() {
        return mLayoutInflater;
    }
}
