package com.infonuascape.osrshelper.adapters;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.controllers.MainFragmentController;
import com.infonuascape.osrshelper.db.DBController;
import com.infonuascape.osrshelper.enums.AccountType;
import com.infonuascape.osrshelper.fragments.CmlXPTrackerFragment;
import com.infonuascape.osrshelper.fragments.CombatCalcFragment;
import com.infonuascape.osrshelper.fragments.HighScoreFragment;
import com.infonuascape.osrshelper.fragments.OSRSFragment;
import com.infonuascape.osrshelper.models.Account;
import com.infonuascape.osrshelper.models.players.PlayerExp;
import com.infonuascape.osrshelper.utils.Utils;

import java.text.NumberFormat;
import java.util.List;

/**
 * Created by marc_ on 2018-01-21.
 */

public class CmlTopSkillPeriodAdapter extends RecyclerView.Adapter<CmlTopSkillPeriodAdapter.TopSkillViewHolder> {
    private static final String TAG = "CmlTopSkillPeriodAdapte";

    private OSRSFragment fragment;
    private List<PlayerExp> playerExps;

    public CmlTopSkillPeriodAdapter(final OSRSFragment fragment, final List<PlayerExp> playerExps) {
        this.fragment = fragment;
        this.playerExps = playerExps;
    }

    @Override
    public TopSkillViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = View.inflate(fragment.getContext(), R.layout.cml_top_skill_list_item, null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(lp);
        return new TopSkillViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(TopSkillViewHolder holder, int position) {
        PlayerExp playerExp = playerExps.get(position);
        holder.accountNumber.setText(String.valueOf(position + 1));
        holder.accountName.setText(playerExp.playerName);
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
            container = itemView.findViewById(R.id.cml_top_list_container);

            initAnimations();

            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!isAnimating) {
                        if(isQuickActionsShown) {
                            slideOutQuickActions.start();
                        } else {
                            slideInQuickActions.start();
                        }
                    }
                }
            });

            itemView.findViewById(R.id.quick_action_hiscore).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainFragmentController.getInstance().showFragment(HighScoreFragment.newInstance(getAccount(getAdapterPosition())));
                }
            });

            itemView.findViewById(R.id.quick_action_tracker).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainFragmentController.getInstance().showFragment(CmlXPTrackerFragment.newInstance(getAccount(getAdapterPosition())));
                }
            });

            itemView.findViewById(R.id.quick_action_combat).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainFragmentController.getInstance().showFragment(CombatCalcFragment.newInstance(getAccount(getAdapterPosition())));
                }
            });
        }

        private void initAnimations() {
            slideInQuickActions = ValueAnimator.ofFloat(0f, Utils.convertDpToPixel(QUICK_ACTIONS_HEIGHT, fragment.getContext()));
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
                    Log.i(TAG, " onAnimationStart: position=" + getAdapterPosition());
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
    }

    private Account getAccount(final int position) {
        final String username = playerExps.get(position).playerName;
        Account account = DBController.getAccountByUsername(fragment.getContext(), username);
        if(account == null) {
            account = new Account(username, AccountType.REGULAR);
        }

        return account;
    }
}
