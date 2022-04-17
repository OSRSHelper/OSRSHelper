package com.infonuascape.osrshelper.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.adapters.NewsAdapter;
import com.infonuascape.osrshelper.controllers.MainFragmentController;
import com.infonuascape.osrshelper.db.PreferencesController;
import com.infonuascape.osrshelper.listeners.EndlessRecyclerOnScrollListener;
import com.infonuascape.osrshelper.listeners.NewsFetcherListener;
import com.infonuascape.osrshelper.listeners.RecyclerItemClickListener;
import com.infonuascape.osrshelper.models.News;
import com.infonuascape.osrshelper.tasks.OSRSNewsTask;
import com.infonuascape.osrshelper.utils.Logger;
import com.infonuascape.osrshelper.utils.Utils;

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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                loadNews(currentPage);
            }
        });
        newsAdapter = new NewsAdapter(getContext(), this);
        recyclerView.setAdapter(newsAdapter);
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
        Logger.add(TAG, ": refreshSubscribeBtn");
        final boolean isSubscribedToNews = PreferencesController.getBooleanPreference(getContext(), PreferencesController.USER_PREF_IS_SUBSCRIBED_TO_NEWS, false);
        subscribeBtn.setText(isSubscribedToNews ? R.string.unsubscribe_news : R.string.subscribe_news);
        subscribeBtn.setSelected(isSubscribedToNews);
    }

    @Override
    public void onStart() {
        super.onStart();
        killAsyncTaskIfStillRunning();
        loadNews(1);
    }

    private void loadNews(int pageNum) {
        Logger.add(TAG, ": loadNews: pageNum=", pageNum);
        progressBar.setVisibility(View.VISIBLE);
        asyncTask = new OSRSNewsTask(this, pageNum);
        asyncTask.execute();
    }

    @Override
    public void onNewsFetchingError() {
        Logger.add(TAG, ": onNewsFetchingError");
        progressBar.setVisibility(View.GONE);
        emptyText.setVisibility(View.VISIBLE);
    }

    @Override
    public void onNewsFetched(List<News> news) {
        Logger.add(TAG, ": onNewsFetched: news=", news);
        if (getView() != null) {
            progressBar.setVisibility(View.GONE);
            emptyText.setVisibility(View.GONE);
            newsAdapter.addNews(news);
        }
    }

    @Override
    public void onItemClicked(int position) {
        Logger.add(TAG, ": onItemClicked: position=", position);
        News news = newsAdapter.getItem(position);
        MainFragmentController.getInstance().showFragment(WebViewFragment.newInstance(news.url, true));
    }

    @Override
    public void onItemLongClicked(int position) {
        //Listener not set
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.subscribe_btn) {
            final boolean isSubscribedToNews = PreferencesController.getBooleanPreference(getContext(), PreferencesController.USER_PREF_IS_SUBSCRIBED_TO_NEWS, false);
            Utils.subscribeToNews(getContext(), !isSubscribedToNews);
            refreshSubscribeBtn();
        }
    }
}
