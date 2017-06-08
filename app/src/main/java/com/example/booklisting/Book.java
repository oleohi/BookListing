package com.example.booklisting;

/**
 * Created by USER on 09/05/2017.
 */

public class Book {
    /*
     * Private class member variables
     */
    private String mTitle;
    private String mAuthor1;
    private String mUrl;

    /*
     * Public constructor for class private variables.
     */

    public Book(String title, String author1, String url) {
        mTitle = title;
        mAuthor1 = author1;
        mUrl = url;
    }

    /*
     * Public getters and setters for private class members.
     */

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }


    public String getmAuthor1() {
        return mAuthor1;
    }

    public void setmAuthor1(String mAuthor1) {
        this.mAuthor1 = mAuthor1;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }
}
