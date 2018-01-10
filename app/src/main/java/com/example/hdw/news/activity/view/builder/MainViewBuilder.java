package com.example.hdw.news.activity.view.builder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.hdw.news.R;
import com.example.hdw.news.activity.NewsActivity;
import com.example.hdw.news.activity.update.UpdateUI;
import com.example.hdw.news.activity.update.UpdateUIListener;
import com.example.hdw.news.activity.view.director.AboutViewDirector;
import com.example.hdw.news.activity.view.director.SettingViewDirector;
import com.example.hdw.news.activity.view.director.ViewBuildDirector;
import com.example.hdw.news.data.get.ConnectionFinishEvent;
import com.example.hdw.news.data.get.ConnectionFinishListener;
import com.example.hdw.news.data.get.GetNetworkData;
import com.example.hdw.news.data.news.TencentNews;
import com.example.hdw.news.data.parse.AdapterFinishEvent;
import com.example.hdw.news.data.parse.AdapterFinishListener;
import com.example.hdw.news.data.parse.TencentNewsAdapter;
import com.example.hdw.news.data.save.SettingChangeEvent;
import com.example.hdw.news.data.save.SettingChangeListener;
import com.example.hdw.news.data.save.SettingData;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by HDW on 2017/12/27.
 */

public class MainViewBuilder extends ViewBuilder {
    private static final String TAG = "MainViewBuilder";
    private Toolbar mToolbar;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private RecyclerView mRecyclerView;
    private NewsItemClickListener mNewsItemClickListener;
    private View mHomeView;
    private View mSettingView;
    private View mAboutView;
    private NewsListAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public MainViewBuilder(Context context) {
        super(context, R.layout.activity_main);
    }

    @Override
    public void buildView() {
        if (mHomeView == null) {
            mHomeView = getLayoutInflater().inflate(R.layout.home, null, false);
        }
        FrameLayout frameLayout = findViewById(R.id.main_view);
        frameLayout.addView(mHomeView);
    }

    @Override
    public void buildToolbar() {
        if (mToolbar == null) {
            mToolbar = findViewById(R.id.toolbar);
            mToolbar.setTitle(R.string.home);
            ((AppCompatActivity) getContext()).setSupportActionBar(mToolbar);
        }
    }

    @Override
    public void buildNavigation() {
        if (mNavigationView == null) {
            mNavigationView = findViewById(R.id.navigation);
            mNavigationView.setCheckedItem(R.id.news_home);
            mNavigationView.setNavigationItemSelectedListener(new NavigationListener());
        }
    }

    @Override
    public void buildDrawerLayout() {
        if (mDrawerLayout == null) {
            mDrawerLayout = findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle((AppCompatActivity) getContext(), mDrawerLayout, mToolbar, R.string.open, R.string.close);
            drawerToggle.syncState();
            mDrawerLayout.addDrawerListener(drawerToggle);
        }
    }

    @Override
    public void buildAdapter() {
        if (mRecyclerView == null) {
            mRecyclerView = findViewById(R.id.news_list);
            LinearLayoutManager manager = new LinearLayoutManager(getContext());
            mAdapter = new NewsListAdapter();
            mNewsItemClickListener = new NewsItemClickListener(getContext(), mAdapter);

            mRecyclerView.setLayoutManager(manager);
            mRecyclerView.setAdapter(mAdapter);

//            DefaultItemAnimator animator = new DefaultItemAnimator();
//
//            animator.setAddDuration(10000);
//            animator.setRemoveDuration(10000);
//            mRecyclerView.setItemAnimator(animator);

            mRecyclerView.addOnScrollListener(new NewsLoad());
            mAdapter.setOnItemClickListener(mNewsItemClickListener);

        }

        if (mSwipeRefreshLayout == null) {
            mSwipeRefreshLayout = findViewById(R.id.news_update_alert);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });
        }

        Log.d(TAG, "buildAdapter: ");
    }

    public void seedUpdateMessage() {
        Message message = new Message();
        message.obj = new UpdateUIListener() {
            @Override
            public void update() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        };
        UpdateUI.getInstance().sendMessage(message);
    }

    public class NewsUpdate implements Runnable, ConnectionFinishListener, AdapterFinishListener, SettingChangeListener {
        private long mUpdateTime;
        private boolean mUpdate;
        private GetNetworkData mGetNetworkData;
        private boolean mRequest;

        public NewsUpdate() {
            mUpdate = true;
            mUpdateTime = SettingData.getInstance().getNewsUpdateTime();
            mGetNetworkData = new GetNetworkData(new OkHttpClient.Builder().build(), null);
            mGetNetworkData.addConnectionFinishListener(this);
            SettingData.getInstance().addSettingChangeListener(this);
            mRequest = SettingData.getInstance().isNewsUpdate();
            startUpdate();
        }

        public void startUpdate() {
            if (mRequest) {
                Log.d(TAG, "startUpdate: 更新线程开启");
                new Thread(this).start();
            }
        }


        @Override
        public void run() {
            Log.d(TAG, "run: 进入线程");
            try {
                while (mUpdate) {
                    Thread.sleep(1000);
                    if (mUpdateTime <= 0 && mRequest) {
                        Log.d(TAG, "run: start running");
                        Request request = new Request.Builder()
                                .url(SettingData.getInstance().getNewsUrl())
                                .build();
                        mGetNetworkData.setRequest(request);
                        seedUpdateMessage();
                        mGetNetworkData.connection();
                        mRequest = false;
                    } else if (mUpdateTime > 0) {
                        mUpdateTime -= 1000;
                        Log.d(TAG, "run: update time = " + mUpdateTime);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void adapterFinish(final AdapterFinishEvent event) {
            Log.d(TAG, "run: adapter finish");
            Message message = new Message();
            message.obj = new UpdateUIListener() {
                @Override
                public void update() {
                    if (!SettingData.getInstance().isNewsUpdate()) return;
                    mAdapter.setTencentNews(event.getTencentNews());
                    mAdapter.notifyDataSetChanged();
                    mSwipeRefreshLayout.setRefreshing(false);
                    mRecyclerView.scrollToPosition(0);
                    if (SettingData.getInstance().isNewsUpdate()) {
                        mUpdateTime = SettingData.getInstance().getNewsUpdateTime();
                        mRequest = true;
                    }
                    Log.d(TAG, "run: update finish");
                }
            };
            UpdateUI.getInstance().sendMessage(message);
        }

        @Override
        public void connectionFinish(ConnectionFinishEvent event) {
            Log.d(TAG, "run: connection finish ");
            TencentNewsAdapter adapter = new TencentNewsAdapter(event.getResponse());
            adapter.addAdapterFinishListener(this);
            adapter.parse();
        }

        @Override
        public void settingChange(SettingChangeEvent event) {
            Log.d(TAG, "settingChange: setting change");
            if (event.getSettingData().isNewsUpdate()) {
                mUpdate = true;
                mRequest = true;
                mUpdateTime = event.getSettingData().getNewsUpdateTime();
                startUpdate();
                Log.d(TAG, "settingChange: 开启更新");
            } else {
                mUpdate = false;
                Log.d(TAG, "settingChange: 关闭更新");
            }
        }
    }

    public static class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ViewHolder> implements View.OnClickListener {
        private static final String TAG = "NewsListAdapter";
        private TencentNews mTencentNews;
        private OnItemClickListener mOnItemClickListener = null;

        public NewsListAdapter() {
        }

        public NewsListAdapter(TencentNews tencentNews) {
            mTencentNews = tencentNews;
        }

        public static void setControllerListener(final SimpleDraweeView simpleDraweeView, final String imagePath) {
            final ViewGroup.LayoutParams layoutParams = simpleDraweeView.getLayoutParams();
            ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
                @Override
                public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable animatable) {
                    super.onFinalImageSet(id, imageInfo, animatable);
                }
            };
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setControllerListener(controllerListener)
                    .setUri(Uri.parse(imagePath))
                    .build();
            simpleDraweeView.setController(controller);

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.news_item, parent, false);
            view.setOnClickListener(this);
            Log.d(TAG, "onCreateViewHolder: " + viewType);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            TencentNews.TencentNewsItem item = mTencentNews.getTencentNewsList().get(position);
            holder.newsTitle.setText(item.mTitle);
            Log.d(TAG, "onBindViewHolder: " + item.mTitle);
            setControllerListener(holder.newsImage, item.mNewsImageUrl.get(1));
            holder.itemView.setTag(position);
//            holder.newsImage.setImageURI(Uri.parse(item.mNewsImageUrl.get(1)));
        }

        @Override
        public int getItemCount() {
            return mTencentNews == null ? 0 : mTencentNews.getTencentNewsList().size();
        }

        public TencentNews getTencentNews() {
            return mTencentNews;
        }

        public void setTencentNews(TencentNews tencentNews) {
            mTencentNews = tencentNews;
        }

        public List<String> getTencentNewsIdList() {
            return mTencentNews == null ? null : mTencentNews.getIdList();
        }

        public List<TencentNews.TencentNewsItem> getTencentNewsList() {
            return mTencentNews == null ? null : mTencentNews.getTencentNewsList();
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                //注意这里使用getTag方法获取position
                mOnItemClickListener.onItemClick(v, (int) v.getTag());
            }
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.mOnItemClickListener = listener;
        }

        public static interface OnItemClickListener {
            void onItemClick(View view, int position);
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView newsTitle;
            SimpleDraweeView newsImage;

            public ViewHolder(View itemView) {
                super(itemView);
                newsTitle = itemView.findViewById(R.id.news_title);
                newsImage = itemView.findViewById(R.id.news_image);
            }
        }
    }

    public class NewsLoad extends RecyclerView.OnScrollListener implements ConnectionFinishListener, AdapterFinishListener {
        private OkHttpClient mClient;
        private GetNetworkData mGetNetworkData;

        public NewsLoad() {
            mClient = new OkHttpClient.Builder().build();
            mGetNetworkData = new GetNetworkData(mClient, null);
            mGetNetworkData.addConnectionFinishListener(this);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (!mRecyclerView.canScrollVertically(1)) {
                List<String> tencentNewsIdList = mAdapter.getTencentNewsIdList();
                List<TencentNews.TencentNewsItem> tencentNewsItemList = mAdapter.getTencentNewsList();
                int start = tencentNewsItemList.size();
                StringBuffer newsUrl = new StringBuffer("https://xw.qq.com/service/api/proxy?key=Xw@2017Mmd&charset=utf-8&url=http://openapi.inews.qq.com/getQQNewsNormalContent?ids=");
                Log.d(TAG, "onScrolled: now to recycler view bottom");
                if (start >= 200) return;

                for (int i = 0; i < 20; i++) {
                    newsUrl.append(tencentNewsIdList.get(start + i));
                    newsUrl.append(",");
                }

                newsUrl.deleteCharAt(newsUrl.length() - 1);
                newsUrl.append("&refer=mobilewwwqqcom&srcfrom=newsapp&otype=json&&extActionParam=Fimgurl33,Fimgurl32,Fimgurl30&extparam=src,desc");
                Log.d(TAG, "onScrolled: newsUrl=" + newsUrl);

                Request request = new Request.Builder()
                        .url(newsUrl.toString())
                        .build();
                mGetNetworkData.setRequest(request);
                mSwipeRefreshLayout.setRefreshing(true);
                mGetNetworkData.connection();
            }
        }

        @Override
        public void adapterFinish(AdapterFinishEvent event) {
            Log.d(TAG, "adapterFinish: adapter finish");

            Message message = new Message();
            message.obj = new UpdateUIListener() {
                @Override
                public void update() {
                    mAdapter.notifyDataSetChanged();
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            };
            Log.d(TAG, "adapterFinish: adapter finish");
            UpdateUI.getInstance().sendMessage(message);
        }

        @Override
        public void connectionFinish(ConnectionFinishEvent event) {
            TencentNewsAdapter adapter = new TencentNewsAdapter(TencentNewsAdapter.NEWS_PARSE, event.getResponse(), mAdapter.getTencentNews());
            adapter.addAdapterFinishListener(this);
            adapter.parse();
        }
    }

    public static class NewsItemClickListener implements NewsListAdapter.OnItemClickListener {
        public static final int NEWS_WEB_VIEW = 0;
        public static final int SYSTEM_BROWSER = 1;
        private Context mContext;
        private NewsListAdapter mAdapter;

        public NewsItemClickListener(Context context,NewsListAdapter adapter) {
            mContext = context;
            mAdapter = adapter;
        }

        @Override
        public void onItemClick(View view, int position) {
            String newsUrl = mAdapter.getTencentNewsList().get(position).mNewsUrl;
            switch (SettingData.getInstance().getReadNewsMode()) {
                case SettingData.WEB_VIEW:
                    NewsActivity.startNewsActivity(mContext, newsUrl);
                    break;
                case SettingData.SYSTEM_BROWSER:
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(newsUrl));
                    mContext.startActivity(intent);
                    break;
            }
        }
    }

    public class NavigationListener implements NavigationView.OnNavigationItemSelectedListener {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            boolean result = false;
            FrameLayout frameLayout = findViewById(R.id.main_view);
            frameLayout.removeAllViews();
            View view = null;
            switch (item.getItemId()) {
                case R.id.news_home:
                    Log.d(TAG, "onNavigationItemSelected: home");
                    view = mHomeView;
                    result = true;
                    break;
                case R.id.news_setting:
                    Log.d(TAG, "onNavigationItemSelected: setting");
                    if (mSettingView == null) {
                        ViewBuilder builder = new SettingViewBuilder(getContext());
                        ViewBuildDirector director = new SettingViewDirector(builder);
                        mSettingView = director.construct();
                    }
                    view = mSettingView;
                    result = true;
                    break;
                case R.id.news_about:
                    Log.d(TAG, "onNavigationItemSelected: about");
                    if (mAboutView == null) {
                        ViewBuilder builder = new AboutViewBuilder(getContext());
                        ViewBuildDirector director = new AboutViewDirector(builder);
                        mAboutView = director.construct();
                    }
                    view = mAboutView;
                    result = true;
                    break;
            }
            frameLayout.addView(view);
            ((AppCompatActivity) getContext()).invalidateOptionsMenu();
            mDrawerLayout.closeDrawers();
            return result;
        }
    }

    public View getHomeView() {
        return mHomeView;
    }

    public View getSettingView() {
        return mSettingView;
    }

    public View getAboutView() {
        return mAboutView;
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    public NavigationView getNavigationView() {
        return mNavigationView;
    }

    public DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public NewsItemClickListener getNewsItemClickListener() {
        return mNewsItemClickListener;
    }
}
