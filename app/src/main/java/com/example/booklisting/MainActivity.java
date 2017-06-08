package com.example.booklisting;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {


    private static final String BOOK_API_REQUEST ="https://www.googleapis.com/books/v1/volumes?q=";

    private static final int BOOK_LOADER_ID = 1;

    private TextView emptyText, searchInstruction;

    private RelativeLayout emptyContainer;

    BookAdapter mAdapter;
    ProgressBar progressBar;

    private EditText searchText;
    private Button searchButton;

    String searchString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set empty container since there are no books to display yet
        emptyText = (TextView) findViewById(R.id.tvEmptyView);
        emptyText.setText(R.string.empty_text);
        searchInstruction = (TextView) findViewById(R.id.tvSearchInstruction);
        searchInstruction.setText(R.string.search_instruction);

        progressBar = (ProgressBar) findViewById(R.id.loading_spinner);
        progressBar.setVisibility(View.GONE); // Hide progress bar

        final ListView booksList = (ListView) findViewById(R.id.list);

        emptyContainer = (RelativeLayout) findViewById(R.id.emptyContainer);

        //set the empty container (containing two text views) if no books are found
        booksList.setEmptyView(emptyContainer);

        mAdapter = new BookAdapter(this, 0, new ArrayList<Book>());

        // Get a reference to the ListView, and attach the adapter to the listView.
        booksList.setAdapter(mAdapter);

        //Set OnItemClick listeners on the list items
        booksList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Find the current book that was clicked on
                Book currentBook = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri url = Uri.parse(currentBook.getmUrl());
                Intent urlIntent = new Intent(Intent.ACTION_VIEW);
                urlIntent.setData(url);
                startActivity(urlIntent);

                // This says something like "Open with" or "Choose app..."
                String title = getString(R.string.open_with);
                // Create intent to show the chooser dialog
                urlIntent = Intent.createChooser(urlIntent, title);

                // Verify that the intent will resolve to an activity
                if (urlIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(urlIntent);
                }
            }
        });


        searchButton = (Button) findViewById(R.id.btnSearch);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar = (ProgressBar) findViewById(R.id.loading_spinner);
                progressBar.setVisibility(View.VISIBLE); // Hide progress bar
                //Hide whatever is on the screen while data fetch is attempted
                emptyContainer.setVisibility(View.GONE);
                booksList.setVisibility(View.GONE);

                // Check if there is internet connection
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null && activeNetwork.isConnected();

                if (isConnected) {

                    //Retrieve string from EditText
                    searchText = (EditText) findViewById(R.id.etSearch);
                    searchString = searchText.getText().toString().trim();
                    searchString = searchString.replaceAll(" +", "+");

                    // Get a reference to the LoaderManager, in order to interact with loaders.
                    LoaderManager loaderManager = getLoaderManager();

                    // Initialize the loader. Pass in the int ID constant defined above and pass in null for
                    // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
                    // because this activity implements the LoaderCallbacks interface).
                    loaderManager.restartLoader(BOOK_LOADER_ID, null, MainActivity.this);
                }
                else{
                    progressBar = (ProgressBar) findViewById(R.id.loading_spinner);
                    progressBar.setVisibility(View.GONE); // Hide progress bar
                    emptyText = (TextView) findViewById(R.id.tvEmptyView);
                    emptyText.setText(R.string.no_internet);
                    searchInstruction.setVisibility(View.GONE);
                }

            }
        });

    }

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        Uri baseUri = Uri.parse(BOOK_API_REQUEST + searchString);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        return new BookLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        progressBar = (ProgressBar) findViewById(R.id.loading_spinner);
        //Hide the progress bar
        progressBar.setVisibility(View.GONE);

        // Clear the adapter of previous news data
        mAdapter.clear();

        //Set empty container in case there is no books to display
        emptyText = (TextView) findViewById(R.id.tvEmptyView);
        emptyText.setText(R.string.no_books);
        searchInstruction = (TextView) findViewById(R.id.tvSearchInstruction);
        searchInstruction.setText(R.string.try_again);
        ListView booksList = (ListView) findViewById(R.id.list);
        booksList.setEmptyView(emptyContainer);

        // If there is a valid list of {@link Book}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
        emptyText.setText("");
        searchInstruction.setText("");
    }
}
