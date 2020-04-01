package io.dclick.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import io.dclick.ads.AdManager;
import io.dclick.ads.interstitial.InterstitialAd;
import io.dclick.ads.listeners.InterstitialAdListener;

public class InterstitialAdsActivity extends Activity {
    private InterstitialAd interstitialAd;
    private InterstitialAdListener interstitialAdListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitial_ads);

        interstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onAdSelected(InterstitialAd interstitialAd) {
                // Ad Selected
                InterstitialAdsActivity.this.interstitialAd = interstitialAd;
            }

            @Override
            public void onAdLoaded() {
                // Ad Load Success
                toast("onAdLoaded");
            }

            @Override
            public void onAdClosed() {
                AdManager.loadInterstitialAd(InterstitialAdsActivity.this, interstitialAdListener);
            }

            @Override
            public void onAdFailed() {
                // Ad Load Failed
                toast("onAdFailed");
            }
        };

        AdManager.loadInterstitialAd(this, interstitialAdListener);

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

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
