package com.example.simplenewsreader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class DisplayContent extends AppCompatActivity {
    WebView wb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_content);

        wb = findViewById(R.id.webView);
        wb.getSettings().setJavaScriptEnabled(true);

        // Get the data
        Intent intent = getIntent();
        String url = intent.getStringExtra("urlArg");
        wb.loadUrl(url);
    }
}