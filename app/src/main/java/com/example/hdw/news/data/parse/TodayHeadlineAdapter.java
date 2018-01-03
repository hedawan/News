package com.example.hdw.news.data.parse;

import com.example.hdw.news.data.news.NewsItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

/**
 * Created by HDW on 2017/12/31.
 */

public class TodayHeadlineAdapter extends NewsAdapter<List<NewsItem>> {
    private Response mResponse;
    private List<NewsItem> mNewsItemsList;

    public TodayHeadlineAdapter(Response response) {
        mResponse = response;
        mNewsItemsList = new ArrayList<>();
    }

    public void parse(){
        try {
            String html = mResponse.body().string();
            Document document = Jsoup.parse(html);
            Elements element = document.getElementsByClass("list_content");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notifyAdapterFinishListener(List<NewsItem> list) {
        AdapterFinishEvent event = new AdapterFinishEvent();
        for (AdapterFinishListener listener : getListenerList()) {
            listener.adapterFinish(event);
        }
    }

}
