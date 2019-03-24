
package com.example.simplenewsreader;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import static com.example.simplenewsreader.MainActivity.USER_PREFRENCES;

public class PrefrenceActivity extends AppCompatActivity
{
    String selectedURL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Retrieve shared pref data
        SharedPreferences prefs = getSharedPreferences(USER_PREFRENCES, MODE_PRIVATE);
        String restoredText = prefs.getString("url", null);

        if(restoredText != null)
        {
            selectedURL = prefs.getString("url", "");
        }

        /*
        if (restoredText != null) {
            String name = prefs.getString("username", "No name defined");//"No name defined" is the default value.
            int idName = prefs.getInt("numItems", 10); //0 is the default value.
            selectedURL = prefs.getString("url", ""); // Retrives the pref URL
        }
        else{
            System.out.println("No restored text");
        } */

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prefrence);

        // creates a drop down list that lets the user select
        // the number of items to be displayed
        final Spinner dropDownNumItems = findViewById(R.id.item_spinner);
        String[] itemOptions = {"10","20","50", "100"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, itemOptions);
        dropDownNumItems.setAdapter(adapter);

        // Create spinner drop down for the
        // frequency of updates
        final Spinner frequencyOfUpdates = findViewById(R.id.freq_ups_drp);
        String[] freqOpts = {"10min", "60min", "once per day"};

        ArrayAdapter<String> freqAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, freqOpts);
        frequencyOfUpdates.setAdapter(freqAdapter);

        Button button1 = findViewById(R.id.store_pref);
        button1.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                System.out.println("User preferences stored");

                String text = dropDownNumItems.getSelectedItem().toString();
                int choice = Integer.parseInt(text);

                // get the selected string of ups freq.
                String upsFreq = frequencyOfUpdates.getSelectedItem().toString();

                // Get the URL entered from the user, that will be
                // used to get the RSS feed from
                EditText url_field = findViewById(R.id.enterUrl);
                String prefURL = url_field.getText().toString();
                System.out.println("Preferd URL IS " + prefURL);

                // Stores the selected preferences
                SharedPreferences.Editor editor = getSharedPreferences(USER_PREFRENCES, MODE_PRIVATE).edit();
                editor.putString("username", "bob");
                editor.putInt("numItems", choice);
                editor.putString("url", prefURL);
                editor.putString("freq", upsFreq);
                editor.apply();

                setResult(RESULT_OK, new Intent());
                finish();
            }
        });
        // Now the code is letting the user specify
        // the URL to retrive the RSS.
        System.out.println("Selected URL is: " + selectedURL);
    }

    /**
     *  Saves the user prefrences using shared prerneces
    public void savePrefrences(View view)
    {
        SharedPreferences sharedPreferences;
    }

     */
}
