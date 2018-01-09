package com.example.hdw.news.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.hdw.news.R;
import com.example.hdw.news.activity.view.builder.CommentViewBuilder;
import com.example.hdw.news.activity.view.builder.ViewBuilder;
import com.example.hdw.news.activity.view.director.CommentViewDirector;
import com.example.hdw.news.activity.view.director.ViewBuildDirector;

public class CommentActivity extends AppCompatActivity {

    public static void startCommentActivity(Context context, String newsUrl) {
        Intent intent = new Intent(context, CommentActivity.class);
        intent.putExtra("newsUrl", newsUrl);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewBuilder viewBuilder = new CommentViewBuilder(this);
        ViewBuildDirector director = new CommentViewDirector(viewBuilder);
        setContentView(director.construct());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
