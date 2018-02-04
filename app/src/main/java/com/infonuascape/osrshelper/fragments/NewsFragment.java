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

import com.google.firebase.messaging.FirebaseMessaging;
import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.adapters.NewsAdapter;
import com.infonuascape.osrshelper.controllers.MainFragmentController;
import com.infonuascape.osrshelper.db.PreferencesController;
import com.infonuascape.osrshelper.listeners.NewsFetcherListener;
import com.infonuascape.osrshelper.listeners.RecyclerItemClickListener;
import com.infonuascape.osrshelper.models.OSRSNews;
import com.infonuascape.osrshelper.tasks.OSRSNewsTask;

import java.util.List;

/**
 * Created by marc_ on 2018-01-20.
 * Fragment that shows only the news from OSRS website
 */

public class NewsFragment extends OSRSFragment implements NewsFetcherListener, RecyclerItemClickListener, View.OnClickListener {
    private static final String TAG = "NewsFragment";
    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;
    private TextView emptyText;
    private ProgressBar progressBar;
    private LinearLayoutManager linearLayoutManager;
    private List<OSRSNews> news;
    private TextView subscribeBtn;

    public static NewsFragment newInstance() {
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

        subscribeBtn = view.findViewById(R.id.subscribe_btn);
        subscribeBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshSubscribeBtn();
    }

    private void refreshSubscribeBtn() {
        final boolean isSubscribedToNews = PreferencesController.getBooleanPreference(getContext(), PreferencesController.USER_PREF_IS_SUBSCRIBED_TO_NEWS, false);
        subscribeBtn.setText(isSubscribedToNews ? R.string.unsubscribe_news: R.string.subscribe_news);
        subscribeBtn.setSelected(isSubscribedToNews);
    }

    @Override
    public void onStart() {
        super.onStart();
        killAsyncTaskIfStillRunning();
        if(news != null) {
            newsAdapter = new NewsAdapter(getContext(), news, this);
            recyclerView.setAdapter(newsAdapter);
        } else {
            asyncTask = new OSRSNewsTask(getContext(), this);
            asyncTask.execute();
        }
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
        if(getView() != null) {
            progressBar.setVisibility(View.GONE);
            if (news != null && news.size() > 0) {
                this.news = news;
                emptyText.setVisibility(View.GONE);
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
        MainFragmentController.getInstance().showFragment(WebViewFragment.newInstance(news.url, true));
    }

    @Override
    public void onItemLongClicked(int position) {
        //Listener not set
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.subscribe_btn) {
            final boolean isSubscribedToNews = PreferencesController.getBooleanPreference(getContext(), PreferencesController.USER_PREF_IS_SUBSCRIBED_TO_NEWS, false);
            PreferencesController.setPreference(getContext(), PreferencesController.USER_PREF_IS_SUBSCRIBED_TO_NEWS, !isSubscribedToNews);
            if (isSubscribedToNews) {
                FirebaseMessaging.getInstance().unsubscribeFromTopic("news");
            } else {
                FirebaseMessaging.getInstance().subscribeToTopic("news");
            }
            refreshSubscribeBtn();
        }
    }
}
