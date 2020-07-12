package com.infonuascape.osrshelper.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.infonuascape.osrshelper.BuildConfig;
import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.adapters.GrandExchangeDetailFragmentAdapter;
import com.infonuascape.osrshelper.db.DBController;
import com.infonuascape.osrshelper.listeners.GEDetailListener;
import com.infonuascape.osrshelper.models.grandexchange.GrandExchangeDetailInfo;
import com.infonuascape.osrshelper.tasks.GrandExchangeDetailPlotTask;
import com.jjoe64.graphview.series.DataPoint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class GrandExchangeDetailFragment extends OSRSFragment implements ViewPager.OnPageChangeListener, GEDetailListener, View.OnClickListener {
    private static final String TAG = "GrandExchangeDetailFragment";
    public final static String EXTRA_ITEM_ID = "EXTRA_ITEM_ID";

    private ViewPager viewPager;
    private GrandExchangeDetailFragmentAdapter adapter;
    private ImageView subscribeBtn;

    private String itemId;
    private DataPoint[] datapoints;
    private DataPoint[] averages;
    private GrandExchangeDetailInfo itemInfo;

    public static GrandExchangeDetailFragment newInstance(final String itemId) {
        GrandExchangeDetailFragment fragment = new GrandExchangeDetailFragment();
        Bundle b = new Bundle();
        b.putString(EXTRA_ITEM_ID, itemId);
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

        subscribeBtn = view.findViewById(R.id.item_subscribe);
        subscribeBtn.setOnClickListener(this);

        if(!BuildConfig.DEBUG) {
            subscribeBtn.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        asyncTask = new GrandExchangeDetailPlotTask(this, itemId);
        asyncTask.execute();
    }

    private void refreshItemInfo() {
        if(getView() != null) {
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
    public void onInfoFetched(DataPoint[] datapoints, DataPoint[] averages, GrandExchangeDetailInfo itemInfo) {
        if(datapoints != null && averages != null) {
            DBController.addGrandExchangeItem(getContext(), itemInfo);
            this.datapoints = datapoints;
            this.averages = averages;
            this.itemInfo = itemInfo;
            refreshItemInfo();
        } else {
            ((TextView) getView().findViewById(R.id.item_name)).setText(R.string.error_when_fetching);
        }
        ((GrandExchangePeriodFragment) adapter.getItem(viewPager.getCurrentItem())).onPageVisible(datapoints, averages);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.item_subscribe) {
            //Todo: magic
            Toast.makeText(getContext(), "Coming soon©", Toast.LENGTH_SHORT).show();
        }
    }
}