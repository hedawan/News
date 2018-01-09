package com.example.hdw.news.data.save;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.hdw.news.activity.NewsApplication;
import com.example.hdw.news.data.news.Comment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HDW on 2018/1/8.
 */

public class CommentData {
    private static CommentData sCommentData = new CommentData();
    private NewsSQLiteOpenHelper mNewsSQLiteOpenHelper;
    private static final String TAG = "CommentData";
    private String mCreateNewsUrlTable = "create table NewsUrl ("
            + "id integer primary key autoincrement,"
            + "url text)";
    private String mCreateCommentTable = "create table NewsComment ("
            + "id integer,"
            + "content text,"
            + "date integer)";

    private CommentData() {
        mNewsSQLiteOpenHelper = new NewsSQLiteOpenHelper(NewsApplication.getContext(), "Comment.db", null, 1);
    }

    public static CommentData getInstance() {
        return sCommentData;
    }

    public void addComment(String url, String content, long date) {
        String select = "select id from NewsUrl where url = \'" + url + "\';";
        Log.d(TAG, "addComment: select = " + select);
        SQLiteDatabase readDatabase = mNewsSQLiteOpenHelper.getReadableDatabase();
        SQLiteDatabase writeDatabase = mNewsSQLiteOpenHelper.getWritableDatabase();
        Cursor cursor = readDatabase.rawQuery(select, null);
        int id;
        String addComment;
        //记录新闻URL
        if (!cursor.moveToFirst()) {
            String addUrl = "insert into NewsUrl (url) values (?);";
            Log.d(TAG, "addComment: addUrl = " + addUrl);
            writeDatabase.execSQL(addUrl, new String[]{url});
            cursor.close();
            cursor = readDatabase.rawQuery(select, null);
        }
        //记录新闻评论和评论时间
        if (cursor.moveToFirst()) {
            id = cursor.getInt(cursor.getColumnIndex("id"));
            addComment = "insert into NewsComment (id,content,date) values (?,?,?);";
            writeDatabase.execSQL(addComment, new String[]{"" + id, content, "" + date});
        }
        Log.d(TAG, "addComment: cursor = " + cursor.getCount());
        cursor.close();
        readDatabase.close();
        writeDatabase.close();
    }

    public List<Comment> getComment(String url) {
        List<Comment> commentList = null;
        SQLiteDatabase readDatabase = mNewsSQLiteOpenHelper.getReadableDatabase();
        String select = "select id from NewsUrl where url = \'" + url + "\'";
        Cursor cursor = readDatabase.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            cursor.close();
            String selectComment = "select content,date from NewsComment where id = " + id + " order by date desc";
            cursor = readDatabase.rawQuery(selectComment, null);
            commentList = new ArrayList<>();
            Comment comment;
            if (cursor.moveToFirst()) {
                do {
                    comment = new Comment();
                    comment.content = cursor.getString(cursor.getColumnIndex("content"));
                    Log.d(TAG, "getComment: comment content=" + comment.content);
                    comment.date = cursor.getLong(cursor.getColumnIndex("date"));
                    commentList.add(comment);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        readDatabase.close();
        return commentList;
    }

    class NewsSQLiteOpenHelper extends SQLiteOpenHelper {

        public NewsSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(mCreateNewsUrlTable);
            db.execSQL(mCreateCommentTable);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
