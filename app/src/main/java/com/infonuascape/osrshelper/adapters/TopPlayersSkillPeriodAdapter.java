package com.infonuascape.osrshelper.adapters;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.controllers.MainFragmentController;
import com.infonuascape.osrshelper.db.OSRSDatabaseFacade;
import com.infonuascape.osrshelper.enums.TrackerTime;
import com.infonuascape.osrshelper.fragments.DataPointsFragment;
import com.infonuascape.osrshelper.fragments.HighScoreFragment;
import com.infonuascape.osrshelper.fragments.OSRSFragment;
import com.infonuascape.osrshelper.fragments.XPTrackerFragment;
import com.infonuascape.osrshelper.models.Account;
import com.infonuascape.osrshelper.models.players.PlayerExp;
import com.infonuascape.osrshelper.utils.Logger;
import com.infonuascape.osrshelper.utils.Utils;

import java.text.NumberFormat;
import java.util.List;

/**
 * Created by marc_ on 2018-01-21.
 */

public class TopPlayersSkillPeriodAdapter extends RecyclerView.Adapter<TopPlayersSkillPeriodAdapter.TopSkillViewHolder> {
    private static final String TAG = "TopPlayersSkillPeriodAdapter";

    private OSRSFragment fragment;
    private List<PlayerExp> playerExps;
    private TrackerTime period;

    public TopPlayersSkillPeriodAdapter(final OSRSFragment fragment, final List<PlayerExp> playerExps, final TrackerTime period) {
        this.fragment = fragment;
        this.playerExps = playerExps;
        this.period = period;
    }

    @Override
    public TopSkillViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = View.inflate(fragment.getContext(), R.layout.top_players_skill_list_item, null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(lp);
        return new TopSkillViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(TopSkillViewHolder holder, int position) {
        PlayerExp playerExp = playerExps.get(position);
        holder.accountNumber.setText(String.valueOf(position + 1));
        holder.accountName.setText(playerExp.displayName);
        holder.accountExp.setText(NumberFormat.getInstance().format(playerExp.experience) + " xp");
    }

    @Override
    public int getItemCount() {
        return playerExps.size();
    }

    class TopSkillViewHolder extends RecyclerView.ViewHolder {
        private static final float QUICK_ACTIONS_HEIGHT = 66f;
        private View container;
        private View quickActionsContainer;
        private ValueAnimator slideInQuickActions;
        private ValueAnimator slideOutQuickActions;
        private boolean isAnimating;
        private boolean isQuickActionsShown;

        private TextView accountNumber;
        private TextView accountName;
        private TextView accountExp;

        public TopSkillViewHolder(View itemView) {
            super(itemView);

            accountNumber = itemView.findViewById(R.id.account_number);
            accountName = itemView.findViewById(R.id.account_username);
            accountExp = itemView.findViewById(R.id.player_exp);

            quickActionsContainer = itemView.findViewById(R.id.quick_actions_container);
            container = itemView.findViewById(R.id.top_list_container);

            initAnimations();

            container.setOnClickListener(view -> {
                if(!isAnimating) {
                    if(isQuickActionsShown) {
                        slideOutQuickActions.start();
                    } else {
                        slideInQuickActions.start();
                    }
                }
            });

            itemView.findViewById(R.id.quick_action_hiscore).setOnClickListener(view -> MainFragmentController.getInstance().showFragment(HighScoreFragment.newInstance(getAccount(getAdapterPosition()))));
            itemView.findViewById(R.id.quick_action_tracker).setOnClickListener(view -> MainFragmentController.getInstance().showFragment(XPTrackerFragment.newInstance(getAccount(getAdapterPosition()), period)));
            itemView.findViewById(R.id.quick_action_data_points).setOnClickListener(view -> MainFragmentController.getInstance().showFragment(DataPointsFragment.newInstance(getAccount(getAdapterPosition()))));
        }

        private void initAnimations() {
            slideInQuickActions = ValueAnimator.ofFloat(0f, Utils.convertDpToPixel(QUICK_ACTIONS_HEIGHT, fragment.getContext()));
            slideInQuickActions.setDuration(300);
            slideInQuickActions.addUpdateListener(valueAnimator -> {
                float value = (float) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams params = quickActionsContainer.getLayoutParams();
                params.height = (int) value;
                quickActionsContainer.setLayoutParams(params);
            });
            slideInQuickActions.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    Logger.add(TAG, ":  onAnimationStart: position=" + getAdapterPosition());
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


            slideOutQuickActions = ValueAnimator.ofFloat(Utils.convertDpToPixel(QUICK_ACTIONS_HEIGHT, fragment.getContext()), 0f);
            slideOutQuickActions.setDuration(300);
            slideOutQuickActions.addUpdateListener(valueAnimator -> {
                float value = (float) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams params = quickActionsContainer.getLayoutParams();
                params.height = (int) value;
                quickActionsContainer.setLayoutParams(params);
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
    }

    private Account getAccount(final int position) {
        final PlayerExp playerExp = playerExps.get(position);
        Account account = OSRSApp.getInstance().getDatabaseFacade().getAccountByUsername(fragment.getContext(), playerExp.username);
        if(account == null) {
            account = new Account(playerExp.username, playerExp.displayName, playerExp.accountType);
        }

        return account;
    }
}
