package com.infonuascape.osrshelper.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.adapters.AccountTypeAdapter;
import com.infonuascape.osrshelper.adapters.TopPlayersAdapter;
import com.infonuascape.osrshelper.controllers.MainFragmentController;
import com.infonuascape.osrshelper.enums.AccountType;
import com.infonuascape.osrshelper.listeners.RecyclerItemClickListener;
import com.infonuascape.osrshelper.models.Account;
import com.infonuascape.osrshelper.models.Skill;
import com.infonuascape.osrshelper.utils.Utils;

public class TopPlayersFragment extends OSRSFragment implements RecyclerItemClickListener, View.OnClickListener {
    private static final String TAG = "TopPlayersFragment";
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private TopPlayersAdapter adapter;
    private AccountType accountType = AccountType.REGULAR;
    private ImageView accountTypeIcon;
    private TextView accountTypeName;

    public static TopPlayersFragment newInstance() {
        TopPlayersFragment fragment = new TopPlayersFragment();
        Bundle b = new Bundle();
        fragment.setArguments(b);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.top_players, null);

        accountTypeIcon = view.findViewById(R.id.account_type_icon);
        accountTypeName = view.findViewById(R.id.account_type_name);

        view.findViewById(R.id.account_type_btn).setOnClickListener(this);

        recyclerView = view.findViewById(R.id.list_rs_view);
        gridLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter = new TopPlayersAdapter(getContext(), this);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void switchAccountType() {
        final AccountTypeAdapter adapter = new AccountTypeAdapter(getActivity());
        new AlertDialog.Builder(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_DARK).setTitle(R.string.select_account_type)
                .setAdapter(adapter, (dialog, item) -> {
                    accountType = adapter.getItem(item);
                    if (accountType != null) {
                        accountTypeIcon.setImageResource(Utils.getAccountTypeResource(accountType));
                        accountTypeName.setText(accountType.displayName);
                    }
                }).show();
    }

    @Override
    public void onItemClicked(int position) {
        Skill skill = adapter.getItem(position);
        MainFragmentController.getInstance().showFragment(TopPlayersSkillFragment.newInstance(accountType, skill.getSkillType()));
    }

    @Override
    public void onItemLongClicked(int position) {
        //Ignore
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.account_type_btn) {
            switchAccountType();
        }
    }
}
