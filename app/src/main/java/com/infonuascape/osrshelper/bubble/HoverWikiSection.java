package com.infonuascape.osrshelper.bubble;

import android.content.Context;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;

import com.infonuascape.osrshelper.utils.Logger;

import io.mattcarroll.hover.Content;

public class HoverWikiSection implements Content {
    private static final String TAG = "HoverWikiSection";

    private Context context;
    private WebView webView;

    public HoverWikiSection(final Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public View getView() {
        if (webView == null) {
            webView = new WebView(context);
            webView.setWebChromeClient(new WebChromeClient());
            webView.setWebViewClient(new WebViewClient() {

                @Override
                public void onPageCommitVisible(WebView view, String url) {
                    super.onPageCommitVisible(view, url);
                }
            });
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setUseWideViewPort(true);
            webView.loadUrl("https://oldschool.runescape.wiki/");
        }
        return webView;
    }

    @Override
    public boolean isFullscreen() {
        return true;
    }

    @Override
    public void onShown() {
        Logger.add(TAG, ": onShown");
    }

    @Override
    public void onHidden() {
        Logger.add(TAG, ": onHidden");
    }
}
