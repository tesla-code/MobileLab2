package com.example.simplenewsreader;

/**
 *      TO DO
 *      Use RecyclerView for that. On top of the UI, provide a simple EditText that will work
 *      like a filter to only show the articles that match the provided Regular Expression.
 *      If no text is given, all items should be visible, with the query string is provided,
 *      the articles should be filtered by the regular expression provided.
 *
 *      The app has JUnit Tests for testing the parsing, and the filtering functionality.
 *
 *     Tested adding a different rss feed with:
 *     https://www.technologyreview.com/topnews.rss witch worked =D
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    public static final String USER_PREFRENCES = "UserPref";
    public static List entries = new ArrayList();


    public static List visibleListsEntries = new ArrayList();

    public static final int REQUEST_CODE_TRANSFER = 101;
    Timer t = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences.Editor editor = getSharedPreferences(USER_PREFRENCES, MODE_PRIVATE).edit();
        editor.putString("username", "bob");
        editor.putString("url", "https://www.sciencedaily.com/rss/matter_energy/engineering.xml");
        editor.putInt("numItems", 10);
        editor.apply();

        /**
         *  Timer is used to fetch updates after x amount of time
         */
        t.scheduleAtFixedRate(
                new TimerTask()
                {
                    public void run()
                    {
                        // Clear entires so we dont get duplicates
                        entries.clear();

                        // Update the rss List based on userPref Time
                        new RSSFetcher(getBaseContext()).execute((Void) null);
                    }
                },
                0,      // run first occurrence imediatetly
                10 * 60000); // run once every 10 minute by default, can be changed by user prefs

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Fetch RSS from science daily
        new RSSFetcher(getBaseContext()).execute((Void) null);

        // Go to the User Preference activity
        Button button = findViewById(R.id.pref_button);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, PrefrenceActivity.class);
                //startActivity(intent);
                startActivityForResult(intent, REQUEST_CODE_TRANSFER);
            }
        });

        // Go to the Article List activity
        Button buttonList = findViewById(R.id.button_article_list);
        buttonList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentList = new Intent(MainActivity.this, ArticleList.class);
                startActivity(intentList);
            }
        });
    }

    /**
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_TRANSFER) {
            if (resultCode == RESULT_OK) {

                // update based on user pref's
               t.cancel();
                // Retrieve shared pref data
                SharedPreferences prefs = getSharedPreferences(USER_PREFRENCES, MODE_PRIVATE);
                String updateFreq = prefs.getString("freq", "10min");
                String subString = updateFreq.substring(0,2);

                // Get url from pref, and do a force update on the
                // entries.

                String urlPref = prefs.getString("url", "no arg");
                Log.i("URLPREF: ","URL: new url pref from user is: " + urlPref);

                // Empty old List
                entries.clear();
                Log.i("List", "Emptying entries list.");

                // Make a new list
                new RSSFetcher(getBaseContext()).execute((Void) null);

                int waitToUp = 0;
                try{
                    int num = Integer.parseInt(subString);
                    waitToUp = num;
                    // System.out.println("lulz" + num);
                } catch (NumberFormatException e) {
                    System.out.println("Is Day");
                    waitToUp = -1;
                }

                // Here the app is suppose to update once per day
                if(waitToUp == -1)
                {
                    waitToUp =  86400000;
                }

                if(waitToUp == 10) {
                    System.out.println("Wait to up is: " + waitToUp);
                    waitToUp *= 60000;
                    System.out.println("Wait to up is: " + waitToUp);
                }

                if (waitToUp == 60)
                {
                    System.out.println("Wait to up is: " + waitToUp);
                    waitToUp *= 60000;
                    System.out.println("Wait to up is: " + waitToUp);
                }

                t = new Timer();
                t.scheduleAtFixedRate(
                        new TimerTask()
                        {
                            public void run()
                            {
                                // Fetch RSS/Atom feed again
                                // new RSSFetcher().execute((Void) null);

                            }
                        },
                        0,      // run first occurrence immediately
                        waitToUp);   // how long to wait before it runs again
            }
        }
    }
}