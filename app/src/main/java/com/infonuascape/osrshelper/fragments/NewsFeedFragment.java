package com.infonuascape.osrshelper.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.activities.MainActivity;
import com.infonuascape.osrshelper.activities.UsernameActivity;
import com.infonuascape.osrshelper.db.DBController;
import com.infonuascape.osrshelper.models.Account;
import com.infonuascape.osrshelper.utils.Logger;

import androidx.annotation.Nullable;

/**
 * Created by marc_ on 2018-01-20.
 */

public class NewsFeedFragment extends OSRSFragment implements View.OnClickListener {
    private static final String TAG = "NewsFeedFragment";

    private ProfileHeaderFragment profileHeaderFragment;
    private View profileHeaderContainer;
    private View profileNotSetContainer;

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

        profileHeaderFragment = ((ProfileHeaderFragment) getChildFragmentManager().findFragmentById(R.id.profile_header));
        profileHeaderContainer = view.findViewById(R.id.profile_header);
        profileNotSetContainer = view.findViewById(R.id.profile_not_set);
        view.findViewById(R.id.profile_not_set_button).setOnClickListener(this);

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
            profileNotSetContainer.setVisibility(View.VISIBLE);
            profileHeaderContainer.setVisibility(View.GONE);
        } else {
            profileNotSetContainer.setVisibility(View.GONE);
            profileHeaderContainer.setVisibility(View.VISIBLE);
            profileHeaderFragment.refreshProfile(account);
            profileHeaderFragment.setTitle(R.string.click_here_profile);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.profile_not_set_button) {
            UsernameActivity.showForProfileForResult(getActivity(), MainActivity.REQUEST_CODE_SET_PROFILE);
        }
    }
}
