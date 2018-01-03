package com.example.hdw.news.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.hdw.news.activity.view.builder.MainViewBuilder;
import com.example.hdw.news.activity.view.director.MainViewDirector;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainViewBuilder mainViewBuilder = new MainViewBuilder(this);
        MainViewDirector mainViewDirector = new MainViewDirector(mainViewBuilder);
        setContentView(mainViewDirector.construct());
    }
}
