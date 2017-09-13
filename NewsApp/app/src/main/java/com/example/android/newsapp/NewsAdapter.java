package com.example.android.newsapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.R.attr.start;
import static android.os.Build.VERSION_CODES.M;

/**
 * Created by gobov on 5/31/2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private static final String LOG_TAG = NewsAdapter.class.getName();

    private List<News> newsList;

    private Context mContext;

    public NewsAdapter (Context context, List<News> newsList){
        this.newsList = newsList;
        this.mContext = context;
    }

    // VIEW HOLDER CLASS FOR RECYCLER VIEW IMPLEMENTATION
    public static class ViewHolder extends RecyclerView.ViewHolder {

        protected TextView title;

        protected TextView author;

        protected TextView date;

        protected TextView section;

        protected View rootView;


        public ViewHolder (View newsItem){
            super(newsItem);

            // FINDING VIEWS FOR EACH ITEM
            title = (TextView) newsItem.findViewById(R.id.title_text_view);
            author = (TextView) newsItem.findViewById(R.id.auth_text_view);
            date = (TextView) newsItem.findViewById(R.id.date_text_view);
            section = (TextView) newsItem.findViewById(R.id.sectionTextView);
            rootView = (View) newsItem.findViewById(R.id.root_view);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // INFLATING THE LAYOUT TO BE USED AS AN  EACH ITEM VIEW AND PLACING IT IN A HOLDER
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, null);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NewsAdapter.ViewHolder holder, int position) {
        // GETTING THE POSITION OF EACH ITEM NEWS
        final News currentNews = newsList.get(position);

        // GETTING THE URL TO USE FOR AN ITEM CLICK
        final Uri newsUri = Uri.parse(currentNews.getUrl());

        // USING THE FORMAT DATE METHOD
        String formattedDate = formatDate(currentNews.getDate());

        // CHECKING IF THERE IS DATA AND SETTING TEXT ON EACH TEXT VIEW
        if (currentNews.getTitle() != null){
            holder.title.setText(currentNews.getTitle());
        } else {
            holder.title.setText(R.string.unknown);
        }

        if (currentNews.getAuthor() != null){
            holder.author.setText(currentNews.getAuthor());
        } else {
            holder.author.setText(R.string.unknown);
        }

        if (currentNews.getSection() != null){
            holder.section.setText(currentNews.getSection());
        } else {
            holder.section.setText(R.string.unknown);
        }

        if (currentNews.getDate() != null){
            holder.date.setText(formattedDate);
        } else {
            holder.date.setText(R.string.unknown);
        }

        // SETTING ON CLICK LISTENER ON ITEM VIEW HOLDER TO REDIRECT TO WEB PAGE
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentNews.getUrl() != null){
                    // IF THERE IS URL DATA, START WEB INTENT
                    Intent urlIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                    mContext.startActivity(urlIntent);
                } else {
                    Log.v(LOG_TAG, "No web url to use");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    // TO PARSE THE DATE STRING IN A DATE OBJECT AND TO REFORMAT IT TO A SPECIFIC DATE SYNTAX
    private static String formatDate (String dateString) {

        // CREATING A FORMAT TO USE FOR PARSING A DATE STRING FROM JSON
        SimpleDateFormat rawDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.UK);
        try {
            // CREATING A DATE OBJECT FROM A GIVEN DATE STRING
            Date parsedDate = rawDateFormat.parse(dateString);
            // CREATING A NEW FORMAT FOR THE CREATED DATE OBJECT
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyy", Locale.UK);
            return dateFormat.format(parsedDate);
        } catch (ParseException e){
            Log.e(LOG_TAG, "Error date ", e);
            return null;
        }
    }
}
