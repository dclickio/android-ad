package io.dclick;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import io.dclick.interstitial.InterstitialAd;
import io.dclick.listeners.InterstitialAdListener;

public class InterstitialAdsActivity extends Activity {
    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitial_ads);

        AdManager.loadInterstitialAd(this, new InterstitialAdListener() {
            @Override
            public void onAdSelected(InterstitialAd interstitialAd) {
                // Ad Selected
                InterstitialAdsActivity.this.interstitialAd = interstitialAd;
            }

            @Override
            public void onAdLoaded() {
                // Ad Load Success
            }

            @Override
            public void onAdFailed() {
                // Ad Load Failed
            }
        });

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (interstitialAd != null) interstitialAd.show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (interstitialAd != null) interstitialAd.onDestroy();
        super.onDestroy();
    }
}
