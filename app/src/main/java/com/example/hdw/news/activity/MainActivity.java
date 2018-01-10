package com.example.hdw.news.activity;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class MainActivity extends BaseActivity implements ConnectionFinishListener, AdapterFinishListener {
    private static final String TAG = "MainActivity";
    MainViewBuilder mMainViewBuilder;
    private UpdateUI mUpdateUI;
    private View mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainViewBuilder = new MainViewBuilder(this);
        MainViewDirector mainViewDirector = new MainViewDirector(mMainViewBuilder);
        mView = mainViewDirector.construct();
        setContentView(mView);
        mUpdateUI = UpdateUI.getInstance();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(SettingData.getInstance().getNewsUrl())
                .build();
        GetNetworkData getNetworkData = new GetNetworkData(client, request);
        getNetworkData.addConnectionFinishListener(this);
        mMainViewBuilder.getSwipeRefreshLayout().setRefreshing(true);
        getNetworkData.connection();
    }

    @Override
    public void adapterFinish(final AdapterFinishEvent event) {
        Message message = new Message();
        message.obj = new UpdateUIListener() {
            @Override
            public void update() {
                RecyclerView recyclerView = mMainViewBuilder.getHomeView().findViewById(R.id.news_list);
                ((MainViewBuilder.NewsListAdapter) recyclerView.getAdapter()).setTencentNews(event.getTencentNews());
                recyclerView.getAdapter().notifyDataSetChanged();
                mMainViewBuilder.getSwipeRefreshLayout().setRefreshing(false);
                mMainViewBuilder.new NewsUpdate();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_view_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        ViewGroup viewGroup = findViewById(R.id.main_view);
        if (mMainViewBuilder.getHomeView() == viewGroup.getChildAt(0)) {
            menu.findItem(R.id.search).setVisible(true);
        } else if (mMainViewBuilder.getHomeView() != viewGroup.getChildAt(0)) {
            menu.findItem(R.id.search).setVisible(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        ViewGroup viewGroup = findViewById(R.id.main_view);
        if (viewGroup.getChildAt(0).getId() != R.id.news_show) {
            viewGroup.removeAllViews();
            viewGroup.addView(mMainViewBuilder.getHomeView());
            mMainViewBuilder.getNavigationView().setCheckedItem(R.id.news_home);
        } else {
            super.onBackPressed();
        }
    }
}
