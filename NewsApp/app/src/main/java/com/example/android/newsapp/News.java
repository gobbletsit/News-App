package com.example.android.newsapp;

import static android.R.attr.thumbnail;

/**
 * Created by gobov on 5/31/2017.
 */

public class News {

    private String mTitle;

    private String mAuthor;

    private String mDate;

    private String mUrl;

    private String mSection;



    public News (String title, String author, String date, String section, String url){

        mTitle = title;

        mAuthor = author;

        mDate = date;

        mUrl = url;

        mSection = section;

    }

    public String getTitle (){
        return mTitle;
    }

    public String getAuthor () {return mAuthor;}

    public String getDate (){
        return mDate;
    }

    public String getUrl (){
        return mUrl;
    }

    public String getSection(){
        return mSection;
    }

}
