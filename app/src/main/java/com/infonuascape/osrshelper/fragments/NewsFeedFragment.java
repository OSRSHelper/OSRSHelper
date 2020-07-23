package com.infonuascape.osrshelper.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.db.DBController;
import com.infonuascape.osrshelper.models.Account;
import com.infonuascape.osrshelper.utils.Logger;

import androidx.annotation.Nullable;

/**
 * Created by marc_ on 2018-01-20.
 */

public class NewsFeedFragment extends OSRSFragment {
    private static final String TAG = "NewsFeedFragment";

    public static NewsFeedFragment newInstance() {
        NewsFeedFragment fragment = new NewsFeedFragment();
        Bundle b = new Bundle();
        fragment.setArguments(b);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Logger.add(TAG, ": onCreateView");
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
        Logger.add(TAG, ": refreshHeader");
        final Account account = DBController.getProfileAccount(getContext());
        if (account == null) {
            getView().findViewById(R.id.profile_not_set).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.profile_header).setVisibility(View.GONE);
        } else {
            getView().findViewById(R.id.profile_not_set).setVisibility(View.GONE);
            getView().findViewById(R.id.profile_header).setVisibility(View.VISIBLE);
            ((ProfileHeaderFragment) getChildFragmentManager().findFragmentById(R.id.profile_header)).refreshProfile(account);
            ((ProfileHeaderFragment) getChildFragmentManager().findFragmentById(R.id.profile_header)).setTitle(R.string.click_here_profile);
        }
    }

}
