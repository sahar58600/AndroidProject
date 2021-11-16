package com.example.myproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class WebViewPage extends AppCompatActivity
{
    WebView webView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_page);

        webView = findViewById(R.id.webView);

        webView.loadUrl("https://www.touristisrael.com/events/");
    }
}