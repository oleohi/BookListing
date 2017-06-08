package com.example.booklisting;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by USER on 09/05/2017.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(@NonNull MainActivity context, @LayoutRes int resource, ArrayList<Book> books) {
        super(context, resource, books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Get the Book object located at this position in the list
        Book book = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID tvTitle
        TextView title = (TextView) convertView.findViewById(R.id.tvTitle);
        title.setText(book.getmTitle());

        // Find the TextView in the news_list_item.xml layout with the ID tvAuthor
        TextView author = (TextView) convertView.findViewById(R.id.tvAuthor1);
        author.setText(book.getmAuthor1());

        //Return the view of all three views above.
        return convertView;
    }
}
