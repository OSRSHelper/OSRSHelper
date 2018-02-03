package com.infonuascape.osrshelper.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.adapters.GrandExchangeDetailFragmentAdapter;
import com.infonuascape.osrshelper.db.DBController;
import com.infonuascape.osrshelper.listeners.GEDetailListener;
import com.infonuascape.osrshelper.models.grandexchange.GEItemInfo;
import com.infonuascape.osrshelper.tasks.GEDetailPlotTask;
import com.jjoe64.graphview.series.DataPoint;

public class GrandExchangeDetailFragment extends OSRSFragment implements ViewPager.OnPageChangeListener, GEDetailListener {
    private static final String TAG = "GrandExchangeDetailFragment";
    public final static String EXTRA_ITEM_ID = "EXTRA_ITEM_ID";

    private ViewPager viewPager;
    private GrandExchangeDetailFragmentAdapter adapter;

    private String itemId;
    private DataPoint[] datapoints;
    private DataPoint[] averages;
    private GEItemInfo itemInfo;

    public static GrandExchangeDetailFragment newInstance(String itemId) {
        GrandExchangeDetailFragment fragment = new GrandExchangeDetailFragment();
        Bundle b = new Bundle();
        b.putSerializable(EXTRA_ITEM_ID, itemId);
        fragment.setArguments(b);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.ge_detail, null);

        itemId = getArguments().getString(EXTRA_ITEM_ID);

        viewPager = view.findViewById(R.id.viewpager);
        adapter = new GrandExchangeDetailFragmentAdapter(getChildFragmentManager(), getContext());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);
        viewPager.setOffscreenPageLimit(4);
        TabLayout tabLayout = view.findViewById(R.id.sliding_tabs);
        tabLayout.setSelectedTabIndicatorColor(getContext().getResources().getColor(R.color.colorPrimaryDark));
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        asyncTask = new GEDetailPlotTask(getContext(), this, itemId);
        asyncTask.execute();
    }

    private void refreshItemInfo() {
        ((TextView) getView().findViewById(R.id.item_name)).setText(itemInfo.name);
        ((TextView) getView().findViewById(R.id.item_desc)).setText(itemInfo.description);
        ((TextView) getView().findViewById(R.id.item_price)).setText(itemInfo.current.value);
        ((TextView) getView().findViewById(R.id.item_today_change_price)).setText(itemInfo.today.value);

        ((TextView) getView().findViewById(R.id.item_one_month_price_variation)).setText(itemInfo.day30.change);
        ((TextView) getView().findViewById(R.id.item_three_month_price_variation)).setText(itemInfo.day90.change);
        ((TextView) getView().findViewById(R.id.item_six_month_price_variation)).setText(itemInfo.day180.change);

        getView().findViewById(R.id.item_member).setVisibility(itemInfo.members ? View.VISIBLE : View.GONE);
        getView().findViewById(R.id.item_image).setVisibility(View.VISIBLE);
        Glide.with(getContext()).asBitmap().load(itemInfo.iconLarge).into((ImageView) getView().findViewById(R.id.item_image));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        ((GrandExchangePeriodFragment) adapter.getItem(position)).onPageVisible(datapoints, averages);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onInfoFetched(DataPoint[] datapoints, DataPoint[] averages, GEItemInfo itemInfo) {
        DBController.addGrandExchangeItem(getContext(), itemInfo);
        this.datapoints = datapoints;
        this.averages = averages;
        this.itemInfo = itemInfo;
        refreshItemInfo();
        ((GrandExchangePeriodFragment) adapter.getItem(viewPager.getCurrentItem())).onPageVisible(datapoints, averages);
    }
}