package com.infonuascape.osrshelper.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.utils.Logger;

import androidx.annotation.Nullable;

/**
 * Created by marc_ on 2017-09-11.
 */

public class WebViewFragment extends OSRSFragment {
    private static final String TAG = "WebViewFragment";
    private final static String EXTRA_URL = "EXTRA_URL";
    private final static String EXTRA_IS_NEWS = "EXTRA_IS_NEWS";

    private String url;
    private WebView webView;
    private ProgressBar progressBar;
    private ViewGroup webViewContainer;

    public static WebViewFragment newInstance(final String url) {
        return newInstance(url, false);
    }

    public static WebViewFragment newInstance(final Bundle bundle) {
        WebViewFragment fragment = new WebViewFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static WebViewFragment newInstance(final String url, final boolean isNews) {
        WebViewFragment fragment = new WebViewFragment();
        Bundle b = getBundle(url, isNews);
        fragment.setArguments(b);
        return fragment;
    }

    public static Bundle getBundle(final String url, final boolean isNews) {
        Bundle b = new Bundle();
        b.putString(EXTRA_URL, url);
        b.putBoolean(EXTRA_IS_NEWS, isNews);
        return b;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.web_view, null);

        url = getArguments().getString(EXTRA_URL);

        progressBar = view.findViewById(R.id.progress_bar);

        webViewContainer = view.findViewById(R.id.container);
        webView = view.findViewById(R.id.web_view);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
                progressBar.setVisibility(View.GONE);
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setBackgroundColor(Color.TRANSPARENT);

        if (getArguments().getBoolean(EXTRA_IS_NEWS, false)) {
            webView.getSettings().setMinimumFontSize(32);
        }

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyWebView();
    }

    public void destroyWebView() {
        Logger.add(TAG, ": destroyWebView");
        if (webViewContainer != null && webView != null) {
            webViewContainer.removeAllViews();
            webView.clearHistory();
            webView.clearCache(true);
            webView.loadUrl("about:blank");
            webView.onPause();
            webView.removeAllViews();
            webView.destroyDrawingCache();
            webView.pauseTimers();
            webView.destroy();
            webView = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        webView.loadUrl(url);
    }

    @Override
    public boolean onBackPressed() {
        if (!url.equalsIgnoreCase(webView.getOriginalUrl())) {
            webView.goBack();
            return true;
        }

        return false;
    }
}
