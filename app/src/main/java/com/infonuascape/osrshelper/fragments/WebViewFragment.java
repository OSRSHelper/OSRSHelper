package com.infonuascape.osrshelper.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.infonuascape.osrshelper.R;

/**
 * Created by marc_ on 2017-09-11.
 */

public class WebViewFragment extends OSRSFragment {
    private final static String EXTRA_URL = "EXTRA_URL";

    private String url;
    private WebView webView;

    public static WebViewFragment newInstance(final String url) {
        WebViewFragment fragment = new WebViewFragment();
        Bundle b = new Bundle();
        b.putString(EXTRA_URL, url);
        fragment.setArguments(b);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.web_view, null);

        url = getArguments().getString(EXTRA_URL);

        webView = view.findViewById(R.id.web_view);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);

        return view;
    }

    @Override
    public boolean onBackPressed() {
        if(!url.equalsIgnoreCase(webView.getOriginalUrl())) {
            webView.goBack();
            return true;
        }

        return true;
    }
}
