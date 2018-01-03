package com.example.hdw.news.activity.view.builder;

import android.content.Context;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hdw.news.R;
import com.example.hdw.news.data.news.TencentNews;

/**
 * Created by HDW on 2017/12/27.
 */

public class MainViewBuilder extends ViewBuilder {
    private Toolbar mToolbar;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private RecyclerView mRecyclerView;

    public MainViewBuilder(Context context) {
        super(context, R.layout.activity_main);
    }

    @Override
    public void buildToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.home);
        ((AppCompatActivity) getContext()).setSupportActionBar(mToolbar);
    }

    @Override
    public void buildNavigation() {
        mNavigationView = findViewById(R.id.navigation);
        mNavigationView.setCheckedItem(R.id.news_home);
    }

    @Override
    public void buildDrawerLayout() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle((AppCompatActivity) getContext(), mDrawerLayout, mToolbar, R.string.open, R.string.close);
        drawerToggle.syncState();
        mDrawerLayout.addDrawerListener(drawerToggle);
    }

    @Override
    public void buildAdapter() {
        mRecyclerView = findViewById(R.id.news_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new NewsListAdapter());
    }

    class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ViewHolder> {
        private TencentNews mTencentNews;

        public NewsListAdapter() {
        }

        public NewsListAdapter(TencentNews tencentNews) {
            mTencentNews = tencentNews;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.news_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            TencentNews.TencentNewsItem item = mTencentNews.getTencentNewsList().get(position);
            holder.newsTitle.setText(item.mTitle);
            holder.newsImage.setImageURI(Uri.parse(item.mNewsImageUrl.get(1)));
        }

        @Override
        public int getItemCount() {
            return mTencentNews == null?0:mTencentNews.getTencentNewsList().size();
        }

        public TencentNews getTencentNews() {
            return mTencentNews;
        }

        public void setTencentNews(TencentNews tencentNews) {
            mTencentNews = tencentNews;
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            TextView newsTitle;
            ImageView newsImage;
            public ViewHolder(View itemView) {
                super(itemView);
                newsTitle = itemView.findViewById(R.id.news_title);
                newsImage = itemView.findViewById(R.id.news_image);
            }
        }
    }

}
