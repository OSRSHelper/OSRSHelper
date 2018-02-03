package com.infonuascape.osrshelper.fragments;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.controllers.MainFragmentController;
import com.infonuascape.osrshelper.models.Account;
import com.infonuascape.osrshelper.utils.Utils;

/**
 * Created by marc_ on 2018-01-21.
 */

public class ProfileHeaderFragment extends OSRSFragment implements View.OnClickListener {
    private static final String TAG = "ProfileHeaderFragment";
    private static final float QUICK_ACTIONS_HEIGHT = 62f;

    private FrameLayout quickActionsContainer;
    private boolean isAnimating;
    private Account account;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.profile_header, null);

        view.findViewById(R.id.profile_header).setOnClickListener(this);

        quickActionsContainer = view.findViewById(R.id.quick_actions_container);

        return view;
    }

    public void refreshProfile(final Account account) {
        this.account = account;
        ((TextView) getView().findViewById(R.id.account_username)).setText(account.username);
        ((ImageView) getView().findViewById(R.id.account_icon)).setImageResource(Utils.getAccountTypeResource(account.type));
        ((AccountQuickActionsFragment) getChildFragmentManager().findFragmentById(R.id.osrs_quick_actions)).setAccount(account);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.profile_header) {
            showProfile();
        }
    }

    private void showProfile() {
        MainFragmentController.getInstance().showFragment(ProfileFragment.newInstance(account.username));
    }

    public void setTitle(int textResId) {
        ((TextView) getView().findViewById(R.id.page_name)).setText(textResId);
    }
}
