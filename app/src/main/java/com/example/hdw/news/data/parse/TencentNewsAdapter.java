package com.example.hdw.news.data.parse;

import com.example.hdw.news.data.news.TencentNews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

import okhttp3.Response;

/**
 * Created by HDW on 2018/1/2.
 */

public class TencentNewsAdapter extends NewsAdapter<TencentNews> {
    private Response mResponse;
    private TencentNews mTencentNews;


    public TencentNewsAdapter(Response response) {
        mResponse = response;
        mTencentNews = new TencentNews();
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
        try {
            JSONObject jsonObject = new JSONObject(mResponse.body().string());
            JSONArray jsonArray = jsonObject.getJSONArray("idlist");
            jsonObject = jsonArray.getJSONObject(0);
            JSONArray ids = jsonObject.getJSONArray("ids");
            JSONArray newList = jsonObject.getJSONArray("newList");
            List<String> idsList = mTencentNews.getIdList();
            for (int i = 0; i < ids.length(); i++) {
                jsonObject = ids.getJSONObject(i);
                idsList.add(jsonObject.getString("id"));
            }
            List<TencentNews.TencentNewsItem> newsItemList = mTencentNews.getTencentNewsList();
            for (int i = 0; i < newList.length(); i++) {
                jsonObject = newList.getJSONObject(i);
                TencentNews.TencentNewsItem item = mTencentNews.new TencentNewsItem();
                item.mNewsId = jsonObject.getString("id");
                item.mTitle = jsonObject.getString("title");
                item.mNewsUrl = jsonObject.getString("url");
                item.mNewsTime = jsonObject.getString("time");
                item.mNewsSource = jsonObject.getString("source");
                JSONArray temp = jsonObject.getJSONArray("thumbnails");
                item.mNewsImageUrl.add(temp.getString(0));
                temp = jsonObject.getJSONArray("ext_data");
                item.mNewsImageUrl.add(temp.getString(1));
                item.mNewsImageUrl.add(temp.getString(2));
                temp = jsonObject.getJSONArray("thumbnails_qqnews");
                item.mNewsImageUrl.add(temp.getString(0));
            }
            notifyAdapterFinishListener(mTencentNews);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Response getResponse() {
        return mResponse;
    }

    public void setResponse(Response response) {
        mResponse = response;
    }

    public TencentNews getTencentNews() {
        return mTencentNews;
    }
}
