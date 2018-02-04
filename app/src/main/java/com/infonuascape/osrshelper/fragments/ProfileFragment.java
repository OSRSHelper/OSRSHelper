package com.infonuascape.osrshelper.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.infonuascape.osrshelper.BuildConfig;
import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.activities.MainActivity;
import com.infonuascape.osrshelper.adapters.AccountTypeAdapter;
import com.infonuascape.osrshelper.adapters.ProfileDeltasAdapter;
import com.infonuascape.osrshelper.db.DBController;
import com.infonuascape.osrshelper.enums.AccountType;
import com.infonuascape.osrshelper.listeners.ProfileInfoListener;
import com.infonuascape.osrshelper.models.Account;
import com.infonuascape.osrshelper.models.players.Delta;
import com.infonuascape.osrshelper.tasks.ProfileInfoFetcherTask;
import com.infonuascape.osrshelper.utils.Utils;

import java.util.ArrayList;

/**
 * Created by marc_ on 2018-01-20.
 */

public class ProfileFragment extends OSRSFragment implements View.OnClickListener, ProfileInfoListener {
    private static final String TAG = "ProfileFragment";
    private static final String EXTRA_USERNAME = "EXTRA_USERNAME";
    private Account account;
    private ProfileHeaderFragment profileHeaderFragment;
    private ArrayList<Delta> deltas;
    private ProfileDeltasAdapter adapter;
    private RecyclerView recyclerView;

    public static ProfileFragment newInstance(final String username) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle b = new Bundle();
        b.putString(EXTRA_USERNAME, username);
        fragment.setArguments(b);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.profile, null);

        view.findViewById(R.id.account_type_edit).setOnClickListener(this);
        view.findViewById(R.id.account_set_profile).setOnClickListener(this);
        view.findViewById(R.id.account_follow_profile).setOnClickListener(this);

        recyclerView = view.findViewById(R.id.profile_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        if(!BuildConfig.DEBUG) {
            view.findViewById(R.id.account_follow_profile).setVisibility(View.GONE);
        }

        profileHeaderFragment = (ProfileHeaderFragment) getChildFragmentManager().findFragmentById(R.id.profile_header);

        return view;
    }

    private void initProfile() {
        final String username = getArguments().getString(EXTRA_USERNAME);
        account = DBController.getAccountByUsername(getContext(), username);
        if(account == null) {
            account = new Account(username, AccountType.REGULAR);
            DBController.addOrUpdateAccount(getContext(), account);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        refreshScreen();
    }

    private void refreshScreen() {
        initProfile();
        ((TextView) getView().findViewById(R.id.account_type)).setText(Utils.getAccountTypeString(account.type));
        //Is profile
        getView().findViewById(R.id.account_set_profile).setVisibility(account.isProfile ? View.GONE : View.VISIBLE);
        //Is following
        if(account.isFollowing) {
            ((ImageView) getView().findViewById(R.id.account_follow_profile_image)).setImageResource(R.drawable.follow_full);
            ((TextView) getView().findViewById(R.id.account_follow_profile_text)).setText(R.string.following);
        } else {
            ((ImageView) getView().findViewById(R.id.account_follow_profile_image)).setImageResource(R.drawable.follow_empty);
            ((TextView) getView().findViewById(R.id.account_follow_profile_text)).setText(R.string.follow);
        }
        profileHeaderFragment.setTitle(R.string.profile);
        profileHeaderFragment.refreshProfile(account);
        loadDeltas();
        if(getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).refreshProfileAccount();
        }
    }

    private void loadDeltas() {
        Log.i(TAG, "loadDeltas");
        if(deltas == null) {
            killAsyncTaskIfStillRunning();
            asyncTask = new ProfileInfoFetcherTask(getContext(), this, account);
            asyncTask.execute();
        } else {
            adapter = new ProfileDeltasAdapter(getContext(), deltas);
            recyclerView.setAdapter(adapter);
        }
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.account_type_edit) {
            switchAccountType();
        } else if(view.getId() == R.id.account_set_profile) {
            setUserAsMyProfile();
        } else if(view.getId() == R.id.account_follow_profile) {
            followThisAccount();
        }
    }

    private void followThisAccount() {
        account.isFollowing = !account.isFollowing;
        DBController.updateAccount(getContext(), account);
        profileHeaderFragment.refreshProfile(account);
        refreshScreen();
        Toast.makeText(getContext(), "Coming soon©", Toast.LENGTH_SHORT).show();
    }

    private void switchAccountType() {
        final AccountTypeAdapter adapter = new AccountTypeAdapter(getActivity());
        new AlertDialog.Builder(getActivity()).setTitle(R.string.select_account_type)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item ) {
                        AccountType accountType = adapter.getItem(item);
                        account.type = accountType;
                        DBController.updateAccount(getContext(), account);
                        refreshScreen();
                    }
                }).show();
    }

    private void setUserAsMyProfile() {
        new AlertDialog.Builder(getActivity()).setTitle(R.string.set_profile_title)
                .setMessage(R.string.set_profile_desc)
                .setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DBController.setProfileAccount(getContext(), account);
                        refreshScreen();
                    }
                })
                .setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();
    }

    @Override
    public void onProfileInfoLoaded(ArrayList<Delta> deltas) {
        this.deltas = deltas;
        asyncTask = null;
        adapter = new ProfileDeltasAdapter(getContext(), deltas);
        recyclerView.setAdapter(adapter);
    }
}
