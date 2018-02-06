package com.infonuascape.osrshelper.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import com.infonuascape.osrshelper.fragments.OSRSPagerFragment;

/**
 * Created by marc_ on 2018-02-03.
 */

public abstract class OSRSNestedViewPagerAdapter extends FragmentPagerAdapter {
    private final FragmentManager fragmentManager;
    private final Context context;
    private OSRSPagerFragment[] subFragments = new OSRSPagerFragment[getCount()];
    private FragmentTransaction cleanupTransaction;

    public OSRSNestedViewPagerAdapter(final FragmentManager fragmentManager, final Context context) {
        super(fragmentManager);
        this.fragmentManager = fragmentManager;
        this.context = context;
    }

    public abstract int getCount();

    public abstract OSRSPagerFragment createFragment(final int position);

    public abstract int getTitle(final int position);

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        final int textResId = getTitle(position);
        return context.getResources().getString(textResId);
    }

    @Override
    public OSRSPagerFragment getItem(int position) {
        return getSubFragmentAtPosition(position);
    }

    @Override
    public long getItemId(int position) {
        return getSubFragmentAtPosition(position).hashCode();
    }

    //The next three methods are needed to remove fragments no longer used from the fragment manager
    @Override
    public void startUpdate(ViewGroup container) {
        super.startUpdate(container);
        cleanupTransaction = fragmentManager.beginTransaction();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        cleanupTransaction.remove((OSRSPagerFragment) object);
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        super.finishUpdate(container);
        cleanupTransaction.commitAllowingStateLoss();
    }

    private OSRSPagerFragment getSubFragmentAtPosition(int position) {
        if (subFragments[position] == null) {
            subFragments[position] = createFragment(position);
        }
        return subFragments[position];
    }
}
