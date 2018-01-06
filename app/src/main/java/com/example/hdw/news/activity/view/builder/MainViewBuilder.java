package com.example.hdw.news.activity.view.builder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
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
import com.example.hdw.news.activity.NewsActivity;
import com.example.hdw.news.activity.update.UpdateUI;
import com.example.hdw.news.activity.update.UpdateUIListener;
import com.example.hdw.news.data.get.ConnectionFinishEvent;
import com.example.hdw.news.data.get.ConnectionFinishListener;
import com.example.hdw.news.data.get.GetNetworkData;
import com.example.hdw.news.data.news.TencentNews;
import com.example.hdw.news.data.parse.AdapterFinishEvent;
import com.example.hdw.news.data.parse.AdapterFinishListener;
import com.example.hdw.news.data.parse.TencentNewsAdapter;
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
    private NestedScrollView mNestedScrollView;
    private NewsItemClickListener mNewsItemClickListener;

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
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        final NewsListAdapter newsListAdapter = new NewsListAdapter();
        mNestedScrollView = findViewById(R.id.nested_scroll_view);
        mNewsItemClickListener = new NewsItemClickListener(getContext(),newsListAdapter);

        manager.setSmoothScrollbarEnabled(true);
        manager.setAutoMeasureEnabled(true);

        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(newsListAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        newsListAdapter.setOnItemClickListener(mNewsItemClickListener);

        mNestedScrollView.setOnScrollChangeListener(new NewsLoad(newsListAdapter));
        Log.d(TAG, "buildAdapter: ");
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

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView newsTitle;
            SimpleDraweeView newsImage;

            public ViewHolder(View itemView) {
                super(itemView);
                newsTitle = itemView.findViewById(R.id.news_title);
                newsImage = itemView.findViewById(R.id.news_image);
            }
        }
    }

    public static class NewsLoad implements NestedScrollView.OnScrollChangeListener, ConnectionFinishListener, AdapterFinishListener {
        private NewsListAdapter mAdapter;

        public NewsLoad(NewsListAdapter adapter) {
            mAdapter = adapter;
        }

        @Override
        public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
            if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                //底部加载
                List<String> tencentNewsIdList = mAdapter.getTencentNewsIdList();
                List<TencentNews.TencentNewsItem> tencentNewsItemList = mAdapter.getTencentNewsList();
                int start = tencentNewsItemList.size();
                StringBuffer newsUrl = new StringBuffer("http://openapi.inews.qq.com/getQQNewsNormalContent?ids=");


                Log.d(TAG, "onScrolled: now to recycler view bottom");
                if (start >= 200) return;

                for (int i = 0; i < 20; i++) {
                    newsUrl.append(tencentNewsIdList.get(start + i));
                    newsUrl.append(",");
                }

                newsUrl.deleteCharAt(newsUrl.length() - 1);
                newsUrl.append("&refer=mobilewwwqqcom");
                Log.d(TAG, "onScrolled: newsUrl=" + newsUrl);

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
            TencentNewsAdapter adapter = new TencentNewsAdapter(TencentNewsAdapter.NEWS_PARSE, event.getResponse(), mAdapter.getTencentNews());
            adapter.addAdapterFinishListener(this);
            adapter.parse();
        }
    }

    public static class NewsItemClickListener implements NewsListAdapter.OnItemClickListener {
        public static final int NEWS_WEB_VIEW = 0;
        public static final int SYSTEM_BROWSER = 1;
        private Context mContext;
        private int mSingleChoiceItem;
        private String mNewsUrl;
        private NewsListAdapter mAdapter;

        public NewsItemClickListener(Context context,NewsListAdapter adapter) {
            mContext = context;
            mSingleChoiceItem = 0;
            mAdapter = adapter;
        }

        @Override
        public void onItemClick(View view, int position) {
            mNewsUrl = mAdapter.getTencentNewsList().get(position).mNewsUrl;
            final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle(R.string.news_open_alert)
                    .setSingleChoiceItems(R.array.news_open_mode, 0, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(TAG, "onClick: singleChoice" + which);
                            mSingleChoiceItem = which;
                        }
                    })
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(TAG, "onClick: on dialog OK which=" + which);
                            switch (mSingleChoiceItem) {
                                case NEWS_WEB_VIEW:
                                    Log.d(TAG, "onClick: " + which);
                                    NewsActivity.startNewsActivity(mContext, mNewsUrl);
                                    break;
                                case SYSTEM_BROWSER:
                                    Log.d(TAG, "onClick: sy" + which);
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse(mNewsUrl));
                                    mContext.startActivity(intent);
                                    break;
                            }
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            builder.show();
        }
    }
}
