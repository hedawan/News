package com.example.hdw.news.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import com.example.hdw.news.R;
import com.example.hdw.news.activity.view.builder.NewsViewBuilder;
import com.example.hdw.news.activity.view.director.NewsViewDirector;

/**
 * Created by HDW on 2018/1/4.
 */

public class NewsActivity extends BaseActivity {
    NewsViewBuilder mNewsViewBuilder;

    public static void startNewsActivity(Context context,String newsUrl){
        Intent intent = new Intent(context, NewsActivity.class);
        intent.putExtra("newsUrl", newsUrl);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNewsViewBuilder = new NewsViewBuilder(this);
        NewsViewDirector newsViewDirector = new NewsViewDirector(mNewsViewBuilder);
        setContentView(newsViewDirector.construct());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.news_view_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.comment:
                CommentActivity.startCommentActivity(this, getIntent().getStringExtra("newsUrl"));
                break;
            case R.id.share:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, getIntent().getStringExtra("newsUrl"));
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        mNewsViewBuilder.getNestedScrollView().removeAllViews();
        WebView webView = mNewsViewBuilder.getWebView();
        webView.stopLoading();
        webView.removeAllViews();
        webView.destroy();
        mNewsViewBuilder = null;
        super.onDestroy();

    }
}
