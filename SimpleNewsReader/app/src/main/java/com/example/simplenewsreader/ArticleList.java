package com.example.simplenewsreader;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.rometools.rome.feed.synd.SyndEntry;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.simplenewsreader.MainActivity.USER_PREFRENCES;
import static com.example.simplenewsreader.MainActivity.entries;
import static com.example.simplenewsreader.MainActivity.visibleListsEntries;

public class ArticleList extends AppCompatActivity
{
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mDescription = new ArrayList<>();
    private ArrayList<String> imgURLS = new ArrayList<>();
    public static ArrayList<Integer> visibleEntries = new ArrayList<>();

    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;

    String regExp = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);
        initRecyltView();

        // Get the URL entered from the user, that will be
        // used to get the RSS feed from
        final EditText reg_field = findViewById(R.id.regex_field);
        regExp = reg_field.getText().toString();
        System.out.println("regEXP String is: " + regExp);

        reg_field.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                regExp = reg_field.getText().toString();
                System.out.println("regEXP String is: " + regExp);

                // Filter based on regex
                String title = "";

                Pattern pattern = Pattern.compile(regExp);
                Matcher matcher;
                // Lager en arrayList, som inneholder positions,
                // tømmer array Listen for hver gang det skjer
                // text change.

                SyndEntry e;
                visibleEntries.clear();

                for(int i = 0; i < entries.size(); i++)
                {
                    e = (SyndEntry)entries.get(i);
                    title = e.getTitle();
                    matcher = pattern.matcher(title);

                    if(matcher.find())
                    {
                        Log.i("Regex: " ,  "Matched " + title);
                        visibleEntries.add(i);
                        System.out.println("###############");
                        System.out.println(title);
                        System.out.println("###############");
                        continue;
                    }
                }
                updateRecyleView();
            }
        });

    }

public void initList()
{
    int numItems = 0;
    // Retrieve shared pref data
    SharedPreferences prefs = getSharedPreferences(USER_PREFRENCES, MODE_PRIVATE);
    String restoredText = prefs.getString("url", null);

    if(restoredText != null)
    {
        numItems = prefs.getInt("numItems", 10);
    }
    Log.i("PREF", "Num Items Pref is: " + numItems);

    // Here we have to limit the amount of articles that is displayed
    // according to what the user has stored as a preference.
    int numToIter = numItems;
    if(numItems > entries.size())
    {
        numToIter = entries.size();
    }

    SyndEntry e;
    for(int i = 0; i < numToIter; i++)
    {
        e = (SyndEntry)entries.get(i);
        mNames.add(e.getTitle());
        mDescription.add(e.getDescription().getValue());
    }
}

private void initRecyltView()
{
    recyclerView = findViewById(R.id.recyclerv_view);
    adapter = new RecyclerViewAdapter(this, mNames, mDescription);
    recyclerView.setAdapter(adapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    initList();
}

public void updateRecyleView()
{
    Log.i("NUM ENTRIES", "Number of entries is: " + visibleEntries.size());

    // Loop over all elements to check that no duplicates have been stored
    for(int i = 0; i < visibleEntries.size(); i++)
    {
        SyndEntry e = (SyndEntry) entries.get(visibleEntries.get(i));
        Log.i("ENTRY " + i, " is: " + e.getTitle());
    }

    // Filter for regex
    if(!visibleEntries.isEmpty())
    {

        for(int i = 0; i < entries.size(); i++)
        {
            if(visibleEntries.contains(i))
            {
                Log.i("RegEx", "Kept entry at pos " + i);
                visibleListsEntries.add(entries.get(i));
            }
            else
            {
                entries.remove(i);
                adapter.notifyItemRemoved(i);
                adapter.notifyDataSetChanged();
                // recyclerView.setVisibility(View.GONE);
            }
        }

    /*
        recyclerView.setAdapter(null);
        recyclerView.setLayoutManager(null);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); */
        // adapter.notifyDataSetChanged();
    }
    // new RSSFetcher(getBaseContext()).execute((Void) null);
}

}
