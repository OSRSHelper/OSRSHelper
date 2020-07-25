package com.infonuascape.osrshelper.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.adapters.AccountQuickActionsAdapter;
import com.infonuascape.osrshelper.controllers.MainFragmentController;
import com.infonuascape.osrshelper.enums.QuickAction;
import com.infonuascape.osrshelper.listeners.RecyclerItemClickListener;
import com.infonuascape.osrshelper.models.Account;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * Created by marc_ on 2018-01-21.
 */

public class AccountQuickActionsFragment extends OSRSFragment implements RecyclerItemClickListener {
    private static final String TAG = "AccountQuickActionsFrag";

    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private RecyclerView recyclerView;
    private AccountQuickActionsAdapter adapter;

    private Account account;

    public static AccountQuickActionsFragment newInstance() {
        AccountQuickActionsFragment fragment = new AccountQuickActionsFragment();
        Bundle b = new Bundle();
        fragment.setArguments(b);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.quick_actions, null);

        recyclerView = view.findViewById(R.id.quick_actions_list);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<QuickAction> quickActions = new ArrayList<>();
        quickActions.add(QuickAction.HISCORES);
        quickActions.add(QuickAction.XP_TRACKER);
        quickActions.add(QuickAction.COMBAT_CALC);
        adapter = new AccountQuickActionsAdapter(getContext(), quickActions, this);
        recyclerView.setAdapter(adapter);
    }

    public void setAccount(final Account account) {
        this.account = account;
    }

    @Override
    public void onItemClicked(int position) {
        if(account != null) {
            QuickAction quickAction = adapter.getItem(position);
            if(quickAction == QuickAction.HISCORES) {
                MainFragmentController.getInstance().showRootFragment(account.isProfile ? R.id.nav_hiscores : -1, HighScoreFragment.newInstance(account));
            } else if(quickAction == QuickAction.XP_TRACKER) {
                MainFragmentController.getInstance().showRootFragment(account.isProfile ? R.id.nav_cml_tracker : -1, XPTrackerFragment.newInstance(account));
            } else if(quickAction == QuickAction.COMBAT_CALC) {
                MainFragmentController.getInstance().showRootFragment(account.isProfile ? R.id.nav_combat_lvl : -1, CombatCalcFragment.newInstance(account));
            }
        }
    }

    @Override
    public void onItemLongClicked(int position) {
        //Listener not set
    }
}
