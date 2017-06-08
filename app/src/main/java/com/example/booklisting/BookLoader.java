package com.example.booklisting;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;

import java.net.URL;
import java.util.List;

/**
 * Created by USER on 09/05/2017.
 */

class BookLoader extends AsyncTaskLoader<List<Book>> {

    private String mUrl;

    //Public constructor.
    public BookLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {
        // Don't perform the request if there are no URLs, or the first URL is null.
        if (mUrl == null) {
            return null;
        }

        // Perform the HTTP request for earthquake data and process the response.
        List<Book> result = QueryUtils.fetchBookData(mUrl);

        return result;
    }
}
