package com.infonuascape.osrshelper.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.bubble.BubbleService;
import com.infonuascape.osrshelper.controllers.MainFragmentController;
import com.infonuascape.osrshelper.db.DBController;
import com.infonuascape.osrshelper.models.Account;
import com.infonuascape.osrshelper.utils.Logger;

import androidx.annotation.Nullable;

/**
 * Created by marc_ on 2018-01-20.
 */

public class NewsFeedFragment extends OSRSFragment implements View.OnClickListener {
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

        view.findViewById(R.id.world_map_button).setOnClickListener(this);
        view.findViewById(R.id.grand_exchange_button).setOnClickListener(this);
        view.findViewById(R.id.bubble_button).setOnClickListener(this);

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
            getView().findViewById(R.id.osrs_quick_actions_profile_container).setVisibility(View.GONE);
        } else {
            getView().findViewById(R.id.profile_not_set).setVisibility(View.GONE);
            getView().findViewById(R.id.osrs_quick_actions_profile_container).setVisibility(View.VISIBLE);
            ((TextView) getView().findViewById(R.id.osrs_quick_actions_profile_title)).setText(account.username);
            ((AccountQuickActionsFragment) getChildFragmentManager().findFragmentById(R.id.osrs_quick_profile_actions)).setAccount(account);
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.world_map_button) {
            MainFragmentController.getInstance().showRootFragment(R.id.nav_world_map, WorldMapFragment.newInstance());
        } else if(view.getId() == R.id.grand_exchange_button) {
            MainFragmentController.getInstance().showRootFragment(R.id.nav_grand_exchange, GrandExchangeSearchFragment.newInstance());
        } else if(view.getId() == R.id.bubble_button) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(getContext())) {
                Intent myIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                startActivity(myIntent);
            } else {
                startBubbleService();
            }
        }
    }

    private void startBubbleService() {
        Logger.add(TAG, ": startBubbleService");
        Intent i = new Intent(getContext(), BubbleService.class);
        i.setPackage(getActivity().getPackageName());
        getActivity().startService(i);
    }
}
