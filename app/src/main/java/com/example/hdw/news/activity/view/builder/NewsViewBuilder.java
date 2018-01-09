package com.example.hdw.news.activity.view.builder;

import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hdw.news.R;
import com.example.hdw.news.data.save.CommentData;

/**
 * Created by HDW on 2018/1/4.
 */

public class NewsViewBuilder extends ViewBuilder {
    private Toolbar mToolbar;
    private NestedScrollView mNestedScrollView;
    private WebView mWebView;
    private String mNewsUrl;
    private static final String TAG = "NewsViewBuilder";
    public NewsViewBuilder(Context context) {
        this(context,R.layout.news_acticity);
    }

    public NewsViewBuilder(Context context, int resource) {
        super(context, resource);
        mNewsUrl = ((AppCompatActivity) context).getIntent().getStringExtra("newsUrl");
        Log.d(TAG, "NewsViewBuilder: url = " + mNewsUrl);
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
        mWebView = findViewById(R.id.web_view);

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

        mWebView.loadUrl(mNewsUrl);
    }

    @Override
    public void buildAdapter() {
        final EditText editText = findViewById(R.id.edit_comment);
        ImageView imageView = findViewById(R.id.yes_comment);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = editText.getText().toString();
                if ("" != comment) {
                    CommentData.getInstance().addComment(mNewsUrl, comment, System.currentTimeMillis());
                    editText.setText("");
                    Toast.makeText(getContext(), R.string.comment_success, Toast.LENGTH_SHORT).show();
                }
            }
        });
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
