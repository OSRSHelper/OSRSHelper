package com.infonuascape.osrshelper.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.adapters.AccountTypeAdapter;
import com.infonuascape.osrshelper.db.DBController;
import com.infonuascape.osrshelper.enums.AccountType;
import com.infonuascape.osrshelper.models.Account;
import com.infonuascape.osrshelper.utils.Utils;

/**
 * Created by marc_ on 2018-01-20.
 */

public class ProfileFragment extends OSRSFragment implements View.OnClickListener {
    private static final String TAG = "ProfileFragment";
    private static final String EXTRA_USERNAME = "EXTRA_USERNAME";
    private Account account;
    private ProfileHeaderFragment profileHeaderFragment;

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

        profileHeaderFragment = (ProfileHeaderFragment) getChildFragmentManager().findFragmentById(R.id.profile_header);
        profileHeaderFragment.forceShowQuickActions();

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
        if(getMainActivity() != null) {
            getMainActivity().refreshProfileAccount();
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
        DBController.addOrUpdateAccount(getContext(), account);
        profileHeaderFragment.refreshProfile(account);
        refreshScreen();
    }

    private void switchAccountType() {
        final AccountTypeAdapter adapter = new AccountTypeAdapter(getActivity());
        new AlertDialog.Builder(getActivity()).setTitle(R.string.select_account_type)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item ) {
                        AccountType accountType = adapter.getItem(item);
                        account.type = accountType;
                        DBController.addOrUpdateAccount(getContext(), account);
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
}
