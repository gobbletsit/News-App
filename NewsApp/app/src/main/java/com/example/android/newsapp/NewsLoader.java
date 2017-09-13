package com.example.android.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by gobov on 5/31/2017.
 */

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    private String mUrl;

    // CONSTRUCTOR WITH THE CLASS CONTEXT AND A STRING URL TO LOAD DATA
    public NewsLoader (Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        if (mUrl == null){
            return null;
        }

        // GETTING THE BOOKS (URL) FROM QUERY UTILS
        List<News> result = NewsQueryUtils.fetchNews(mUrl);

        return result;
    }
}
