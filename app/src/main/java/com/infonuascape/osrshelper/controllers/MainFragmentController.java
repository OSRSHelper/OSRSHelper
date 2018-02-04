package com.infonuascape.osrshelper.controllers;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.activities.MainActivity;
import com.infonuascape.osrshelper.fragments.NewsFeedFragment;
import com.infonuascape.osrshelper.fragments.OSRSFragment;
import com.infonuascape.osrshelper.utils.Utils;

/**
 * Created by marc_ on 2018-01-21.
 */

public class MainFragmentController {
    private static final String ROOT_FRAGMENT_TAG = "ROOT_FRAGMENT_TAG";
    private static final String TAG = "MainFragmentController";

    private MainActivity mainActivity;
    private NavigationView navigationView;

    private static MainFragmentController instance;

    private MainFragmentController(final MainActivity mainActivity, final NavigationView navigationView) {
        this.mainActivity = mainActivity;
        this.navigationView = navigationView;
    }

    public static void init(final MainActivity mainActivity, final NavigationView navigationView) {
        instance = new MainFragmentController(mainActivity, navigationView);
    }

    public static MainFragmentController getInstance() {
        return instance;
    }

    public void showRootFragment(final int menuId, final OSRSFragment fragment) {
        Log.i(TAG, "showRootFragment:");
        Utils.hideKeyboard(mainActivity);
        for (int i = 0; i < navigationView.getMenu().size(); i++) {
            MenuItem menuItem = navigationView.getMenu().getItem(i);
            menuItem.setChecked(menuItem.getItemId() == menuId);
        }

        if(fragment != null) {
            Answers.getInstance().logContentView(new ContentViewEvent().putContentName(getCleanName(fragment)));
            FragmentManager fragmentManager = mainActivity.getSupportFragmentManager();
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentManager.beginTransaction().replace(R.id.content, fragment, ROOT_FRAGMENT_TAG).commitAllowingStateLoss();
        }
    }

    public void showFragment(final OSRSFragment fragment) {
        Log.i(TAG, "showFragment:");
        Utils.hideKeyboard(mainActivity);
        if(fragment != null && !isAlreadyInBackStack(fragment)) {
            Answers.getInstance().logContentView(new ContentViewEvent().putContentName(getCleanName(fragment)));
            FragmentManager fragmentManager = mainActivity.getSupportFragmentManager();
            final String tag = getTag(fragment);
            fragmentManager.beginTransaction().replace(R.id.content, fragment, tag).addToBackStack(tag).commitAllowingStateLoss();
        }
    }

    private String getCleanName(OSRSFragment fragment) {
        //Removes Fragment and add a space between capital letters
        return fragment.getClass().getSimpleName().replace("Fragment", "")
                .replaceAll("(.)([A-Z])", "$1 $2");
    }

    private boolean isAlreadyInBackStack(OSRSFragment fragment) {
        final String currentFragmentTag = getTag(getCurrentFragment());
        final String fragmentTag = getTag(fragment);
        final boolean isAlreadyInBackStack = TextUtils.equals(currentFragmentTag, fragmentTag);

        Log.i(TAG, "isAlreadyInBackStack=" + isAlreadyInBackStack
                + " currentFragment=" + currentFragmentTag
                + " fragmentName=" + fragmentTag);
        return isAlreadyInBackStack;
    }

    private String getTag(final Fragment fragment) {
        return fragment != null ? fragment.getClass().getSimpleName() : null;
    }

    public OSRSFragment getCurrentFragment() {
        FragmentManager fragmentManager = mainActivity.getSupportFragmentManager();

        Fragment fragment = null;
        //Check the backstack first
        if(fragmentManager.getBackStackEntryCount() > 0) {
            final String tag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
            fragment = fragmentManager.findFragmentByTag(tag);
        }

        //Else, try to get the root element
        if(fragment == null) {
            fragment = mainActivity.getSupportFragmentManager().findFragmentByTag(ROOT_FRAGMENT_TAG);
        }

        //Only cast if not null
        if(fragment != null) {
            Log.i(TAG, "getCurrentFragment: name=" + fragment.getClass().getSimpleName());
            return (OSRSFragment) fragment;
        }

        return null;
    }

    public boolean onBackPressed() {
        Log.i(TAG, "onBackPressed:");
        FragmentManager fragmentManager = mainActivity.getSupportFragmentManager();

        if(fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStackImmediate();
            return true;
        } else if(fragmentManager.getBackStackEntryCount() == 0) {
            OSRSFragment fragment = getCurrentFragment();
            if (fragment != null) {
                if (!fragment.onBackPressed()) {
                    if (!(fragment instanceof NewsFeedFragment)) {
                        showRootFragment(R.id.nav_home, NewsFeedFragment.newInstance());
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
