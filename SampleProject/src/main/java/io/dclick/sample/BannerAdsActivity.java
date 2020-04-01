package io.dclick.sample;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import io.dclick.ads.AdManager;
import io.dclick.ads.banner.BannerAd;
import io.dclick.ads.listeners.BannerAdListener;

public class BannerAdsActivity extends Activity {
    private BannerAd bannerAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);

        // Load banner with listener
        AdManager.loadBannerAd(this, R.id.linearLayout, new BannerAdListener() {
            @Override
            public void onAdSelected(BannerAd bannerAd) {
                // Banner selected
                BannerAdsActivity.this.bannerAd = bannerAd;
            }

            @Override
            public void onAdLoaded() {
                // Banner load success
            }

            @Override
            public void onAdFailed() {
                // Banner load fail
            }
        });

        // Load banner without listener
        AdManager.loadBannerAd(this, R.id.frameLayout);
    }

    @Override
    protected void onDestroy() {
        if (bannerAd != null) bannerAd.onDestroy();
        super.onDestroy();
    }
}
