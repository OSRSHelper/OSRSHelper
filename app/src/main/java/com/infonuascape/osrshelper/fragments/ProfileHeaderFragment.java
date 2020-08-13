package com.infonuascape.osrshelper.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.db.DBController;
import com.infonuascape.osrshelper.models.Account;
import com.infonuascape.osrshelper.utils.Logger;
import com.infonuascape.osrshelper.utils.Utils;

/**
 * Created by marc_ on 2018-01-21.
 */

public class ProfileHeaderFragment extends OSRSFragment {
    private static final String TAG = "ProfileHeaderFragment";

    private TextView combatText;
    private View progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.profile_header, null);

        progressBar = view.findViewById(R.id.progressbar);
        combatText = view.findViewById(R.id.account_combat);

        return view;
    }

    public void refreshProfile(final Account account) {
        Logger.add(TAG, ": refreshProfile: account=", account);
        if (account != null && getView() != null) {
            ((TextView) getView().findViewById(R.id.account_username)).setText(account.getDisplayName());
            ((ImageView) getView().findViewById(R.id.account_icon)).setImageResource(Utils.getAccountTypeResource(account.type));
            ((AccountQuickActionsFragment) getChildFragmentManager().findFragmentById(R.id.osrs_quick_actions)).setAccount(account);
            if (account.combatLvl != 0) {
                showCombatLvl(account.combatLvl);
            }
        }
    }

    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    public void setTitle(int textResId) {
        Logger.add(TAG, ": setTitle: textResId=", textResId);
        if (textResId > 0) {
            ((TextView) getView().findViewById(R.id.page_name)).setText(textResId);
        } else {
            ((TextView) getView().findViewById(R.id.page_name)).setText("");
        }
    }

    public void showCombatLvl(final int combatLvl) {
        Logger.add(TAG, ": showCombatLvl: combatLvl=", combatLvl);
        final Account myProfile = DBController.getProfileAccount(getContext());
        if (myProfile != null && myProfile.combatLvl != 0) {
            if (myProfile.combatLvl > combatLvl) {
                if (myProfile.combatLvl + 10 > combatLvl) {
                    combatText.setTextColor(getResources().getColor(R.color.combat_lvl_lower));
                } else {
                    combatText.setTextColor(getResources().getColor(R.color.combat_lvl_slightly_lower));
                }
            } else if (myProfile.combatLvl == combatLvl) {
                combatText.setTextColor(getResources().getColor(R.color.combat_lvl_equal));
            } else {
                if (myProfile.combatLvl - 10 < combatLvl) {
                    combatText.setTextColor(getResources().getColor(R.color.combat_lvl_over));
                } else {
                    combatText.setTextColor(getResources().getColor(R.color.combat_lvl_slightly_over));
                }
            }
        } else {
            combatText.setTextColor(getResources().getColor(R.color.white));
        }
        combatText.setText(getContext().getResources().getString(R.string.combat_lvl, combatLvl));
    }
}
