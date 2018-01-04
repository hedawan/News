package com.example.hdw.news.data.parse;

import android.util.Log;

import com.example.hdw.news.data.news.TencentNews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Response;

/**
 * Created by HDW on 2018/1/2.
 */

public class TencentNewsAdapter extends NewsAdapter<TencentNews> {
    public static final int ID_PARSE = 1;
    public static final int NEWS_PARSE = 2;
    public static final int ALL_PARSE = 3;
    private int mParse;
    private static final String TAG = "TencentNewsAdapter";
    private Response mResponse;
    private TencentNews mTencentNews;
    private String mJson;

    public TencentNewsAdapter(Response response) {
        this(ALL_PARSE,response,new TencentNews());
    }

    public TencentNewsAdapter(int parse, Response response, TencentNews tencentNews) {
        mParse = parse;
        mResponse = response;
        mTencentNews = tencentNews;
        try {
            mJson = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notifyAdapterFinishListener(TencentNews object) {
        AdapterFinishEvent event = new AdapterFinishEvent(object);
        for (AdapterFinishListener listener : getListenerList()) {
            listener.adapterFinish(event);
        }
    }

    @Override
    public void parse() {
        JSONObject jsonObject = null;
        JSONArray jsonArray = null;
        try {
            jsonObject = new JSONObject(mJson);
            switch (mParse) {
                case ID_PARSE:
                    jsonArray = jsonObject.getJSONArray("idlist");
                    jsonObject = jsonArray.getJSONObject(0);
                    idParse(jsonObject.getJSONArray("ids"), mTencentNews.getIdList());
                    break;
                case NEWS_PARSE:
                    if (!jsonObject.isNull("idlist")) {
                        jsonArray = jsonObject.getJSONArray("idlist");
                        jsonObject = jsonArray.getJSONObject(0);
                    }
                    newsParse(jsonObject.getJSONArray("newslist"), mTencentNews.getTencentNewsList());
                    break;
                case ALL_PARSE:
                    jsonArray = jsonObject.getJSONArray("idlist");
                    jsonObject = jsonArray.getJSONObject(0);
                    idParse(jsonObject.getJSONArray("ids"), mTencentNews.getIdList());
                    newsParse(jsonObject.getJSONArray("newslist"), mTencentNews.getTencentNewsList());
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        notifyAdapterFinishListener(mTencentNews);
    }

    public void idParse(JSONArray idArray, List<String> idList) throws JSONException {
        JSONObject jsonObject = null;
        for (int i = 0; i < idArray.length(); i++) {
            jsonObject = idArray.getJSONObject(i);
            idList.add(jsonObject.getString("id"));
        }
    }

    public void newsParse(JSONArray newsArray, List<TencentNews.TencentNewsItem> newsItemList) throws JSONException {
        TencentNews.TencentNewsItem item;
        JSONObject jsonObject;
        for (int i = 0; i < newsArray.length(); i++) {
            Log.d(TAG, "parse: position" + i);
            jsonObject = newsArray.getJSONObject(i);
            item = mTencentNews.new TencentNewsItem();
            item.mNewsId = jsonObject.getString("id");
            item.mTitle = jsonObject.getString("title");
            item.mNewsUrl = jsonObject.getString("url");
            item.mNewsTime = jsonObject.getString("time");
            item.mNewsSource = jsonObject.getString("source");
            item.mNewsImageUrl.add(jsonObject.getJSONArray("thumbnails").getString(0));
//                Log.d(TAG, "parse: ext_data ="+jsonObject.isNull("ext_data"));
//                JSONObject tempObj = jsonObject.getJSONObject("ext_data");
//                item.mNewsImageUrl.add(tempObj.getString("Fimgurl32"));
//                item.mNewsImageUrl.add(tempObj.getString("Fimgurl30"));
            item.mNewsImageUrl.add(jsonObject.getJSONArray("thumbnails_qqnews").getString(0));
            newsItemList.add(item);
        }
    }

    public Response getResponse() {
        return mResponse;
    }

    public void setResponse(Response response) {
        mResponse = response;
    }

    public void setParse(int parse) {
        mParse = parse;
    }

    public void setTencentNews(TencentNews tencentNews) {
        mTencentNews = tencentNews;
    }

    public TencentNews getTencentNews() {
        return mTencentNews;
    }
}
