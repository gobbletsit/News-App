package com.example.android.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by gobov on 5/31/2017.
 */

public class NewsQueryUtils {

    private static final String LOG_TAG = NewsQueryUtils.class.getName();

    public NewsQueryUtils(){
    }

    public static List<News> fetchNews (String requestUrl){

        // CREATING AN URL OBJECT FOR MAKING A REQUEST
        URL url = createUrl(requestUrl);

        // MAKING A REQUEST
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e){
            Log.e(LOG_TAG, "fetchNews: ", e);
        }

        // PARSING THE JSON STRING INTO A CREATED LIST
        List<News> newsList = extractNews(jsonResponse);

        return newsList;
    }

    // TO EXTRACT THE BOOK DATA IN AN ARRAY LIST
    private static List<News> extractNews (String newsJson){

        // TO RETURN EARLY IF THERE'S NO DATA
        if (TextUtils.isEmpty(newsJson)){
            return null;
        }

        // CREATING AN EMPTY ARRAY LIST OT ADD DATA TO
        ArrayList<News> newsArrayList = new ArrayList<>();

        // TRYING THE PARSE THE JSON OBJECT AND THROWING AN EXCEPTION IF THERE'S A PROBLEM
        try {

            // CREATING A JSON OBJECT
            JSONObject baseJsonResponse = new JSONObject(newsJson);

            // GETTING A JSON OBJECT TO FROM A BASE OBJECT
            JSONObject resultJsonObject = baseJsonResponse.getJSONObject("response");

            // CREATING A LIST OF JSON OBJECTS
            JSONArray newsArray = resultJsonObject.getJSONArray("results");

            // GOING THROUGH AN ARRAY LIST OF OBJECTS TO GET THE NEEDED DATA USING KEYWORDS
            for (int i = 0; i < newsArray.length(); i++){
                JSONObject currentNewsObject = newsArray.getJSONObject(i);

                // GETTING THE STRINGS
                String title = currentNewsObject.getString("webTitle");
                String date = currentNewsObject.getString("webPublicationDate");
                String url = currentNewsObject.getString("webUrl");
                String section = currentNewsObject.getString("sectionName");

                // CREATING A JSON ARRAY OF OBJECTS
                JSONArray tagsArray = currentNewsObject.getJSONArray("tags");
                String author = "";

                // GOING THROUGH AN ARRAY LIST OF JSON OBJECTS TO GET THE NEEDED DATA (AUTHOR NAMES)
                if (tagsArray.length() == 0){
                    author = null;
                } else {
                    for (int a = 0; a < tagsArray.length(); a++){
                        JSONObject tagsObject = tagsArray.getJSONObject(a);
                        author = tagsObject.getString("webTitle");
                    }
                }

                // CREATING A NEWS OBJECT AND ADDING IT TO THE LIST
                newsArrayList.add(new News(title, author, date, section, url));
            }

        } catch (JSONException e){
            Log.e(LOG_TAG, "Error parsing JSON Response", e);
        }
        return newsArrayList;

    }

    // TO CREATE A URL OBJECT USING A STRING PARAM
    private static URL createUrl (String stringUrl) {
        URL url = null;

        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e){
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    // TO MAKE A REQUEST WITH A URL PARAM AND TO RECEIVE A STRING
    private static String makeHttpRequest (URL url) throws IOException {

        // TO RETURN EARLY
        String jsonResponse = null;

        if (url == null){
            return jsonResponse;
        }

        HttpURLConnection connection = null;
        InputStream inputStream = null;

        // TO TRY TO ESTABLISH A URL CONNECTION WITH A REQUEST KEYWORD AND CATCHING THE EXCEPTION IF THERE'S A PROBLEM WITH IT
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.connect();

            // IF CONNECTION IS FULFILLED READ FROM STREAM
            if (connection.getResponseCode() == 200){
                inputStream = connection.getInputStream();
                jsonResponse = readStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + connection.getResponseCode());
            }
        } catch (IOException e){
            Log.e(LOG_TAG, " Problem retrieving the earthquake JSON results.", e);

            // TO DISCONNECT AND CLOSE IF CONNECTION IS EXECUTED
        } finally {
            if ( connection != null){
                connection.disconnect();
            }
            if (inputStream != null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    // TO READ THE STREAM OF BYTES AND TRANSLATE IT TO A STRING
    private static String readStream (InputStream stream) throws IOException{
        StringBuilder output = new StringBuilder();
        if (stream != null){
            InputStreamReader reader = new InputStreamReader(stream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = bufferedReader.readLine();

            while (line != null){
                output.append(line);
                line = bufferedReader.readLine();
            }
        }

        return output.toString();
    }


}
