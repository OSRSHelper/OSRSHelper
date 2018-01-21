package com.infonuascape.osrshelper.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
import com.infonuascape.osrshelper.models.Account;
import com.infonuascape.osrshelper.utils.Utils;

/**
 * Created by marc_ on 2018-01-21.
 */

public class ProfileHeaderFragment extends OSRSFragment implements View.OnClickListener {
    private static final String TAG = "ProfileHeaderFragment";
    private static final float QUICK_ACTIONS_HEIGHT = 120f;

    private FrameLayout quickActionsContainer;
    private ValueAnimator slideInQuickActions;
    private ValueAnimator slideOutQuickActions;
    private boolean isAnimating;
    private boolean isQuickActionsShown;
    private Account account;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.profile_header, null);

        view.findViewById(R.id.profile_header).setOnClickListener(this);
        view.findViewById(R.id.account_username).setOnClickListener(this);

        quickActionsContainer = view.findViewById(R.id.quick_actions_container);

        initAnimations();
        return view;
    }

    private void initAnimations() {
        slideInQuickActions = ValueAnimator.ofFloat(0f, Utils.convertDpToPixel(QUICK_ACTIONS_HEIGHT, getContext()));
        slideInQuickActions.setDuration(300);
        slideInQuickActions.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams params = quickActionsContainer.getLayoutParams();
                params.height = (int) value;
                quickActionsContainer.setLayoutParams(params);
            }
        });
        slideInQuickActions.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                isAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                isAnimating = false;
                isQuickActionsShown = true;
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });


        slideOutQuickActions = ValueAnimator.ofFloat(Utils.convertDpToPixel(QUICK_ACTIONS_HEIGHT, getContext()), 0f);
        slideOutQuickActions.setDuration(300);
        slideOutQuickActions.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams params = quickActionsContainer.getLayoutParams();
                params.height = (int) value;
                quickActionsContainer.setLayoutParams(params);
            }
        });
        slideOutQuickActions.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                isAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                isAnimating = false;
                isQuickActionsShown = false;
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    public void forceShowQuickActions() {
        isAnimating = true;
        isQuickActionsShown = true;
        ViewGroup.LayoutParams params = quickActionsContainer.getLayoutParams();
        params.height = (int) Utils.convertDpToPixel(QUICK_ACTIONS_HEIGHT, getContext());
        quickActionsContainer.setLayoutParams(params);
        getView().findViewById(R.id.profile_more_btn).setVisibility(View.GONE);
    }

    public void refreshProfile(final Account account) {
        this.account = account;
        ((TextView) getView().findViewById(R.id.account_username)).setText(account.username);
        ((ImageView) getView().findViewById(R.id.account_icon)).setImageResource(Utils.getAccountTypeResource(account.type));
        ((AccountQuickActionsFragment) getChildFragmentManager().findFragmentById(R.id.osrs_quick_actions)).setAccount(account);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.account_username) {
            animateQuickActions();
        } else if(view.getId() == R.id.profile_header) {
            showProfile();
        }
    }

    private void showProfile() {
        if(getMainActivity() != null && !(getMainActivity().getCurrentFragment() instanceof ProfileFragment)) {
            getMainActivity().showFragment(ProfileFragment.newInstance(account.username));
        }
    }

    private void animateQuickActions() {
        if(!isAnimating) {
            if(isQuickActionsShown) {
                slideOutQuickActions.start();
            } else {
                slideInQuickActions.start();
            }
        }
    }

    public void setTitle(int textResId) {
        ((TextView) getView().findViewById(R.id.page_name)).setText(textResId);
    }
}