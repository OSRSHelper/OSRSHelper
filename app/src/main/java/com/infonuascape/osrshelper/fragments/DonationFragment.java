package com.infonuascape.osrshelper.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.infonuascape.osrshelper.R;

import java.util.List;

/**
 * Created by marc_ on 2017-09-11.
 */

public class DonationFragment extends OSRSFragment implements View.OnClickListener, PurchasesUpdatedListener, BillingClientStateListener {
    private BillingClient mBillingClient;
    private boolean isConnected;
    private boolean isTryAgainToBuy;

    public static DonationFragment newInstance() {
        DonationFragment fragment = new DonationFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.donation, null);

        view.findViewById(R.id.donate_btn).setOnClickListener(this);

        mBillingClient = BillingClient.newBuilder(getContext()).setListener(this).build();
        mBillingClient.startConnection(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.donate_btn) {
            buyUsBeer();
        }
    }

    private void buyUsBeer() {
        isTryAgainToBuy = false;
        if(isConnected) {
            BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                    .setSku("donation")
                    .setType(BillingClient.SkuType.INAPP)
                    .build();
            mBillingClient.launchBillingFlow(getActivity(), flowParams);
        } else {
            isTryAgainToBuy = true;
            mBillingClient.startConnection(this);
        }
    }

    @Override
    public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {

    }

    @Override
    public void onBillingSetupFinished(@BillingClient.BillingResponse int billingResponseCode) {
        if (billingResponseCode == BillingClient.BillingResponse.OK) {
            isConnected = true;
            if(isTryAgainToBuy) {
                buyUsBeer();
            }
        }
    }
    @Override
    public void onBillingServiceDisconnected() {
        isConnected = false;
    }
}
