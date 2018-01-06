package com.example.hdw.news.activity.view.builder;

import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.example.hdw.news.R;

/**
 * Created by HDW on 2018/1/4.
 */

public class NewsViewBuilder extends ViewBuilder {
    private Toolbar mToolbar;
    private NestedScrollView mNestedScrollView;
    private LinearLayout mLinearLayout;
    private WebView mWebView;
    public NewsViewBuilder(Context context) {
        this(context,R.layout.news_acticity);
    }

    public NewsViewBuilder(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public void buildToolbar() {
        if (mToolbar == null){
            mToolbar = findViewById(R.id.toolbar);
            ((AppCompatActivity)getContext()).setSupportActionBar(mToolbar);
            ActionBar actionBar = ((AppCompatActivity)getContext()).getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(R.string.news_detail);
        }
    }

    @Override
    public void buildView() {
        mNestedScrollView = findViewById(R.id.nested_scroll_view);
        mLinearLayout = findViewById(R.id.news_view);
        mWebView = new WebView(getContext().getApplicationContext());

        mWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = mWebView.getSettings();

        webSettings.setBuiltInZoomControls(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setDomStorageEnabled(true);

        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLoadsImagesAutomatically(true);
        
        String newsUrl = ((Activity) getContext()).getIntent().getStringExtra("newsUrl");
        mLinearLayout.addView(mWebView);
        mWebView.loadUrl(newsUrl);
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    public NestedScrollView getNestedScrollView() {
        return mNestedScrollView;
    }

    public WebView getWebView() {
        return mWebView;
    }
}
