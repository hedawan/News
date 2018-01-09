package com.example.hdw.news.activity.view.builder;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hdw.news.R;
import com.example.hdw.news.data.news.Comment;
import com.example.hdw.news.data.save.CommentData;
import com.example.hdw.news.util.TimeParse;

import java.util.List;

/**
 * Created by HDW on 2018/1/9.
 */

public class CommentViewBuilder extends ViewBuilder {
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;

    public CommentViewBuilder(Context context) {
        this(context, R.layout.comment_activity);
    }

    public CommentViewBuilder(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public void buildToolbar() {
        if (mToolbar == null) {
            mToolbar = findViewById(R.id.toolbar);
            ((AppCompatActivity) getContext()).setSupportActionBar(mToolbar);
            ActionBar actionBar = ((AppCompatActivity) getContext()).getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(R.string.comment);
        }
    }

    @Override
    public void buildAdapter() {
        if (mRecyclerView == null) {
            mRecyclerView = findViewById(R.id.comment_list);
            LinearLayoutManager manager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(manager);
            mRecyclerView.setAdapter(new CommentListAdapter());
        }
    }

    class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.ViewHolder> {
        private List<Comment> mCommentList;

        public CommentListAdapter() {
            mCommentList = CommentData.getInstance().getComment(((AppCompatActivity) getContext()).getIntent().getStringExtra("newsUrl"));
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Comment comment = mCommentList.get(position);
            holder.commentTime.setText(TimeParse.millisecondToDateTime(comment.date));
            holder.commentContent.setText(comment.content);
        }

        @Override
        public int getItemCount() {
            return mCommentList == null ? 0 : mCommentList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView commentTime;
            TextView commentContent;

            public ViewHolder(View itemView) {
                super(itemView);
                commentTime = itemView.findViewById(R.id.comment_time);
                commentContent = itemView.findViewById(R.id.comment_content);
            }
        }
    }
}
