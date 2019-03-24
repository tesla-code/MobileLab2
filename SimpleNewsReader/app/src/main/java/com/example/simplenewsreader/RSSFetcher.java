package com.example.simplenewsreader;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import java.net.MalformedURLException;
import java.net.URL;
import java.io.*;

import static android.content.Context.MODE_PRIVATE;
import static com.example.simplenewsreader.ArticleList.visibleEntries;
import static com.example.simplenewsreader.MainActivity.USER_PREFRENCES;
import static com.example.simplenewsreader.MainActivity.entries;
import static com.example.simplenewsreader.MainActivity.visibleListsEntries;


/**
 * The com.example.simplenewsreader.RSSFetcher retrives rss feed from URL's
 *
 * @author Victor
 * @version 0.1.0
 *
 */
public class RSSFetcher extends AsyncTask<Void, Void, Boolean> {
    // private String m_url; x
    private String m_i_url = "https://www.sciencedaily.com/rss/matter_energy/engineering.xml";
    private Context mContext;

    public RSSFetcher(Context i_context) {
        mContext = i_context;
    }

    /**
     * runs before we doInbackground, can be a good place to
     * retrive URL input from the user in f.exs a text field.
     * <p>
     * urlLink = mEditText.getText().toString();
     */
    @Override
    protected void onPreExecute() {

    }

    /**
     * param is void, but would be nice if the URL string could be passed from
     * the main activiy to the fetcher.
     *
     * @param voids
     * @return
     */
    @Override
    protected Boolean doInBackground(Void... voids) {

        // m_i_url = ;
        SharedPreferences prefs = mContext.getSharedPreferences(USER_PREFRENCES, MODE_PRIVATE);
        m_i_url = prefs.getString("url", "");
        Log.i("PREF URL", "Preference url is : " + m_i_url);

        getFeed(m_i_url);
        return false;
    }

    /**
     * Get rss 2.0 or atom feed
     */
    public void getFeed(String i_url) {
        boolean ok = false;
        try {
            URL feedUrl = new URL(i_url);
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedUrl));

            System.out.println(feed);

            ok = true;
            entries.clear();

            for (SyndEntry entry : feed.getEntries()) {
                entries.add(entry);
            }

            Log.i("Entries", "Entries contains " + entries.size());

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR: " + e.getMessage());
        }

        if (!ok) {
            System.out.println();
            System.out.println("FeedReader reads and prints any RSS/Atom feed type.");
            System.out.println("The first parameter must be the URL of the feed to read.");
            System.out.println();
        }
    }
}