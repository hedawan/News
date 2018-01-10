package com.example.hdw.news.activity.view.builder;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.hdw.news.R;
import com.example.hdw.news.util.APKVersionCodeUtils;

/**
 * Created by HDW on 2018/1/9.
 */

public class AboutViewBuilder extends ViewBuilder {
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;

    public AboutViewBuilder(Context context) {
        this(context, R.layout.about_view);
    }

    public AboutViewBuilder(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public void buildToolbar() {
        if (mToolbar == null) {
            mToolbar = findViewById(R.id.toolbar);
            ((AppCompatActivity) getContext()).setSupportActionBar(mToolbar);
        }
        mToolbar.setTitle(R.string.about);
    }


    @Override
    public void buildDrawerLayout() {
        if (mDrawerLayout == null) {
            mDrawerLayout = ((AppCompatActivity) getContext()).findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle((AppCompatActivity) getContext(), mDrawerLayout, mToolbar, R.string.open, R.string.close);
            drawerToggle.syncState();
            mDrawerLayout.addDrawerListener(drawerToggle);
        }
    }

    @Override
    public void buildView() {
        TextView version = findViewById(R.id.edition);
        version.setText(APKVersionCodeUtils.getVerName(getContext()));
    }
}
