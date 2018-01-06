package com.example.hdw.news.activity;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.hdw.news.R;
import com.example.hdw.news.activity.update.UpdateUI;
import com.example.hdw.news.activity.update.UpdateUIListener;
import com.example.hdw.news.activity.view.builder.MainViewBuilder;
import com.example.hdw.news.activity.view.director.MainViewDirector;
import com.example.hdw.news.data.get.ConnectionFinishEvent;
import com.example.hdw.news.data.get.ConnectionFinishListener;
import com.example.hdw.news.data.get.GetNetworkData;
import com.example.hdw.news.data.parse.AdapterFinishEvent;
import com.example.hdw.news.data.parse.AdapterFinishListener;
import com.example.hdw.news.data.parse.TencentNewsAdapter;
import com.example.hdw.news.data.save.SettingData;
import com.facebook.drawee.backends.pipeline.Fresco;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class MainActivity extends AppCompatActivity implements ConnectionFinishListener, AdapterFinishListener {
    private static final String TAG = "MainActivity";
    private UpdateUI mUpdateUI;
    private View mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainViewBuilder mainViewBuilder = new MainViewBuilder(this);
        MainViewDirector mainViewDirector = new MainViewDirector(mainViewBuilder);
        mView = mainViewDirector.construct();
        setContentView(mView);
        mUpdateUI = UpdateUI.getInstance();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(SettingData.getInstance().getNewsUrl())
                .build();
        GetNetworkData getNetworkData = new GetNetworkData(client, request);
        getNetworkData.addConnectionFinishListener(this);
        getNetworkData.connection();
    }

    @Override
    public void adapterFinish(final AdapterFinishEvent event) {
        Message message = new Message();
        message.obj = new UpdateUIListener() {
            @Override
            public void update() {
                RecyclerView recyclerView = mView.findViewById(R.id.news_list);
                ((MainViewBuilder.NewsListAdapter) recyclerView.getAdapter()).setTencentNews(event.getTencentNews());
                Log.d(TAG, "update: "+event.getTencentNews().getTencentNewsList().get(0));
                recyclerView.getAdapter().notifyDataSetChanged();
                Log.d(TAG, "update: update finish");
            }
        };
        Log.d(TAG, "adapterFinish: adapter finish");
        UpdateUI.getInstance().sendMessage(message);
    }

    @Override
    public void connectionFinish(ConnectionFinishEvent event) {
        TencentNewsAdapter adapter = new TencentNewsAdapter(event.getResponse());
        adapter.addAdapterFinishListener(this);
        adapter.parse();
        Log.d(TAG, "connectionFinish: connection finish");
    }
}
