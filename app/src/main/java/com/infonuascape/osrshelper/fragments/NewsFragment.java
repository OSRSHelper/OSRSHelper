package com.infonuascape.osrshelper.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.activities.MainActivity;
import com.infonuascape.osrshelper.adapters.NewsAdapter;
import com.infonuascape.osrshelper.listeners.NewsFetcherListener;
import com.infonuascape.osrshelper.listeners.RecyclerItemClickListener;
import com.infonuascape.osrshelper.models.OSRSNews;
import com.infonuascape.osrshelper.tasks.OSRSNewsTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marc_ on 2018-01-20.
 * Fragment that shows only the news from OSRS website
 */

public class NewsFragment extends OSRSFragment implements NewsFetcherListener, RecyclerItemClickListener {
    private static final String TAG = "NewsFragment";
    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;
    private TextView emptyText;
    private ProgressBar progressBar;
    private LinearLayoutManager linearLayoutManager;

    public static NewsFragment newInstance() {
        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName("OSRS News"));
        NewsFragment fragment = new NewsFragment();
        Bundle b = new Bundle();
        fragment.setArguments(b);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.osrs_news, null);

        recyclerView = view.findViewById(R.id.news_list);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        progressBar = view.findViewById(R.id.progress_bar);
        emptyText = view.findViewById(R.id.empty_view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        new OSRSNewsTask(getContext(), this).execute();
    }

    @Override
    public void onNewsFetchingStarted() {
        if(getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    @Override
    public void onNewsFetchingError() {
        if(getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    emptyText.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    @Override
    public void onNewsFetched(List<OSRSNews> news) {
        if(getActivity() != null) {
            progressBar.setVisibility(View.GONE);
            if (news != null && news.size() > 0) {
                newsAdapter = new NewsAdapter(getContext(), news, this);
                recyclerView.setAdapter(newsAdapter);
            } else {
                emptyText.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onItemClicked(int position) {
        OSRSNews news = newsAdapter.getItem(position);
        if(getMainActivity() != null) {
            getMainActivity().showFragment(R.id.nav_news, WebViewFragment.newInstance(news.url, true));
        }
    }

    @Override
    public void onItemLongClicked(int position) {
        //Listener not set
    }
}
