package com.infonuascape.osrshelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by marc_ on 2017-09-11.
 */

public class WebViewActivity extends Activity {
    private final static String EXTRA_URL = "EXTRA_URL";

    private String url;
    private WebView webView;

    public static void show(final Context context, final String url) {
        Intent i = new Intent(context, WebViewActivity.class);
        i.putExtra(EXTRA_URL, url);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.web_view);

        url = getIntent().getStringExtra(EXTRA_URL);

        webView = (WebView) findViewById(R.id.web_view);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        if(!url.equalsIgnoreCase(webView.getOriginalUrl())) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
