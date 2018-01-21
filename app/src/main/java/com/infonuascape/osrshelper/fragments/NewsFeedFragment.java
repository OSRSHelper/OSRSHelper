package com.infonuascape.osrshelper.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.db.DBController;
import com.infonuascape.osrshelper.models.Account;

/**
 * Created by marc_ on 2018-01-20.
 */

public class NewsFeedFragment extends OSRSFragment {
    private static final String TAG = "NewsFeedFragment";

    public static NewsFeedFragment newInstance() {
        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName("News feed"));
        NewsFeedFragment fragment = new NewsFeedFragment();
        Bundle b = new Bundle();
        fragment.setArguments(b);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.osrs_news_feed, null);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshHeader();
    }

    private void refreshHeader() {
        Log.i(TAG, "refreshHeader");
        final Account account = DBController.getProfileAccount(getContext());
        if (account == null) {
            getView().findViewById(R.id.profile_not_set).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.osrs_quick_actions).setVisibility(View.GONE);
        } else {
            getView().findViewById(R.id.profile_not_set).setVisibility(View.GONE);
            getView().findViewById(R.id.osrs_quick_actions).setVisibility(View.VISIBLE);
            ((AccountQuickActionsFragment) getChildFragmentManager().findFragmentById(R.id.osrs_quick_actions)).setAccount(account);
        }
    }

}
