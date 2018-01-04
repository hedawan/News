package com.example.hdw.news.activity.view.builder;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hdw.news.R;
import com.example.hdw.news.activity.update.UpdateUI;
import com.example.hdw.news.activity.update.UpdateUIListener;
import com.example.hdw.news.data.get.ConnectionFinishEvent;
import com.example.hdw.news.data.get.ConnectionFinishListener;
import com.example.hdw.news.data.get.GetNetworkData;
import com.example.hdw.news.data.news.TencentNews;
import com.example.hdw.news.data.parse.AdapterFinishEvent;
import com.example.hdw.news.data.parse.AdapterFinishListener;
import com.example.hdw.news.data.parse.NewsAdapter;
import com.example.hdw.news.data.parse.TencentNewsAdapter;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.fresco.animation.bitmap.cache.FrescoFrameCache;
import com.facebook.imagepipeline.image.ImageInfo;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by HDW on 2017/12/27.
 */

public class MainViewBuilder extends ViewBuilder {
    private Toolbar mToolbar;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private RecyclerView mRecyclerView;
    private static final String TAG = "MainViewBuilder";

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
//        View view = LayoutInflater.from(getContext()).inflate(R.layout.home, null, false);
//        mRecyclerView.addView(view);
        mRecyclerView.addOnScrollListener(new NewsLoadListener());
        Log.d(TAG, "buildAdapter: ");
    }

    public static class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ViewHolder> {
        private TencentNews mTencentNews;
        private static final String TAG = "NewsListAdapter";
        public NewsListAdapter() {
        }

        public NewsListAdapter(TencentNews tencentNews) {
            mTencentNews = tencentNews;
            Log.d(TAG, "NewsListAdapter: start");
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
            Log.d(TAG, "onBindViewHolder: "+item.mTitle);
            setControllerListener(holder.newsImage,item.mNewsImageUrl.get(1));
//            holder.newsImage.setImageURI(Uri.parse(item.mNewsImageUrl.get(1)));
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

        public List<String> getTencentNewsIdList(){
            return mTencentNews == null?null:mTencentNews.getIdList();
        }
        public List<TencentNews.TencentNewsItem> getTencentNewsList(){
            return mTencentNews ==null?null:mTencentNews.getTencentNewsList();
        }
        class ViewHolder extends RecyclerView.ViewHolder{
            TextView newsTitle;
            SimpleDraweeView newsImage;
            public ViewHolder(View itemView) {
                super(itemView);
                newsTitle = itemView.findViewById(R.id.news_title);
                newsImage = itemView.findViewById(R.id.news_image);
            }
        }

        public static void setControllerListener(final SimpleDraweeView simpleDraweeView, final String imagePath) {
            final ViewGroup.LayoutParams layoutParams = simpleDraweeView.getLayoutParams();
            ControllerListener controllerListener = new BaseControllerListener<ImageInfo>(){
                @Override
                public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable animatable) {
                    super.onFinalImageSet(id, imageInfo, animatable);
                    Log.d(TAG, "onFinalImageSet: image widtd="+layoutParams.width);
                    Log.d(TAG, "onFinalImageSet: width="+imageInfo.getWidth());
                    Log.d(TAG, "onFinalImageSet: height="+imageInfo.getHeight());
                }
            };
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setControllerListener(controllerListener)
                    .setUri(Uri.parse(imagePath))
                    .build();
            simpleDraweeView.setController(controller);

        }
    }
    public static class NewsLoadListener extends RecyclerView.OnScrollListener implements ConnectionFinishListener,AdapterFinishListener{
        private NewsListAdapter mAdapter;
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (mAdapter == null) mAdapter = (NewsListAdapter) recyclerView.getAdapter();
            if (!recyclerView.canScrollVertically(1)) {
                List<String> tencentNewsIdList = mAdapter.getTencentNewsIdList();
                List<TencentNews.TencentNewsItem> tencentNewsItemList = mAdapter.getTencentNewsList();
                int start = tencentNewsItemList.size();
                if (start >= 200) return;
                Log.d(TAG, "onScrolled: recyclerview finish");
                StringBuffer newsUrl = new StringBuffer("http://openapi.inews.qq.com/getQQNewsNormalContent?ids=");
                for (int i = 0; i < 20; i++) {
                    newsUrl.append(tencentNewsIdList.get(start + i));
                    newsUrl.append(",");
                }
                newsUrl.deleteCharAt(newsUrl.length()-1);
                newsUrl.append("&refer=mobilewwwqqcom");
                Log.d(TAG, "onScrolled: newsUrl="+newsUrl);
                OkHttpClient client = new OkHttpClient.Builder().build();
                Request request = new Request.Builder()
                        .url(newsUrl.toString())
                        .build();
                GetNetworkData getNetworkData = new GetNetworkData(client, request);
                getNetworkData.addConnectionFinishListener(this);
                getNetworkData.connection();

            }
        }

        @Override
        public void adapterFinish(AdapterFinishEvent event) {
            Log.d(TAG, "adapterFinish: adapter finish");
            //mAdapter.notifyDataSetChanged();

            Message message = new Message();
            message.obj = new UpdateUIListener() {
                @Override
                public void update() {
                    mAdapter.notifyDataSetChanged();
                }
            };
            Log.d(TAG, "adapterFinish: adapter finish");
            UpdateUI.getInstance().sendMessage(message);
        }

        @Override
        public void connectionFinish(ConnectionFinishEvent event) {
            TencentNewsAdapter adapter = new TencentNewsAdapter(TencentNewsAdapter.NEWS_PARSE,event.getResponse(),mAdapter.getTencentNews());
            adapter.addAdapterFinishListener(this);
            adapter.parse();
        }
    }

}
