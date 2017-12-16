package com.infonuascape.osrshelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;

import java.util.List;

/**
 * Created by marc_ on 2017-09-11.
 */

public class DonationActivity extends Activity implements View.OnClickListener, PurchasesUpdatedListener, BillingClientStateListener {
    private BillingClient mBillingClient;
    private boolean isConnected;
    private boolean isTryAgainToBuy;

    public static void show(final Context context) {
        Intent i = new Intent(context, DonationActivity.class);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.donation);

        findViewById(R.id.donate_btn).setOnClickListener(this);

        mBillingClient = BillingClient.newBuilder(this).setListener(this).build();
        mBillingClient.startConnection(this);
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
            mBillingClient.launchBillingFlow(this, flowParams);
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
