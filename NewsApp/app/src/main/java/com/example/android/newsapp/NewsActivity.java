package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    public static String LOG_TAG = NewsActivity.class.getName();

    private static final String NEWS_URL = "http://content.guardianapis.com/search";

    private NewsAdapter adapter;

    private TextView emptyStateTextView;

    private ProgressBar progressBar;

    private RecyclerView recyclerView;

    private RecyclerView.LayoutManager recyclerManager;


    // INFLATING THE MENU XML
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    // HANDLING CLICK EVENTS WITH STARTING INTENTS
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id){
            // STARTING NEW ACTIVITY TO HANDLE SETTINGS OPTIONS
            case R.id.action_settings: Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;

            // STARTING NEWS ACTIVITY AGAIN TO REFRESH
            case R.id.reload: Intent newsIntent = new Intent(this, NewsActivity.class);
                startActivity(newsIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        // FINDING VIEWS
        emptyStateTextView = (TextView) findViewById(R.id.empty_view);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // SETTING LAYOUT MANAGER ON A RECYCLER VIEW
        recyclerManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerManager);

        // CHECKING THE STATE OF NETWORK CONNECTIVITY WITH MANAGER
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // GETTING THE INFO OF THE DATA NETWORK
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();

        // TO GET THE DATA IF THERE'S CONNECTION WITH INITIALIZING THE LOADER
        if (netInfo != null && netInfo.isConnected()){
            emptyStateTextView.setVisibility(View.GONE);
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(1 ,null, this);
            Log.v(LOG_TAG, "Start loader");
        } else {
            progressBar = (ProgressBar) findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.GONE);

            // TO SHOW IF THERE'S NO CONNECTION
            emptyStateTextView.setText(R.string.no_internet_connection);
        }

        // CREATING AND SETTING THE ADAPTER ON A RECYCLER VIEW
        adapter = new NewsAdapter(this, new ArrayList<News>());
        recyclerView.setAdapter(adapter);

    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {

        // GETTING THE PREFERENCES
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // GETTING THE KEY PREFERENCE FROM SETTINGS
        String qSearch = sharedPrefs.getString(getString(R.string.q_by_key), getString(R.string.default_search_value));

        // PARSING THE MAIN URI
        Uri baseUri = Uri.parse(NEWS_URL);

        // BUILDING A FINAL URI ON A MAIN URI
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("section", "technology");
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("order_by", "relevance");
        uriBuilder.appendQueryParameter("q", qSearch);
        uriBuilder.appendQueryParameter("api-key", "test");

        // JUST TO KNOW IF EVERYTHING WORKS
        Log.e(LOG_TAG, "Query results: " + uriBuilder.toString());

        // CREATING A NEW LOADER TO RETURN
        return new NewsLoader(this, uriBuilder.toString());

    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> newsList) {

        if (newsList != null && !newsList.isEmpty()){
            // CREATING AND SETTING THE ADAPTER ON A RECYCLER VIEW WITH NEWS ITEMS LIST
            adapter = new NewsAdapter(this, newsList);
            recyclerView.setAdapter(adapter);
        }

        // SETTING THE TEXT ON A VIEW IF THERE'S NO RESULTS AND REMOVING THE PROGRESS BAR
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        emptyStateTextView.setText(R.string.no_news_found);

    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
    }



}
