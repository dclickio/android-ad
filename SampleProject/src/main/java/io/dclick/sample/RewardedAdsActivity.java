package io.dclick.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import io.dclick.ads.AdManager;
import io.dclick.ads.listeners.RewardedAdListener;
import io.dclick.ads.rewarded.RewardedAd;

public class RewardedAdsActivity extends Activity {
    private RewardedAd rewardedAd;
    private RewardedAdListener listener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewarded_ads);

        listener = new RewardedAdListener() {
            @Override
            public void onAdSelected(RewardedAd rewardedAd) {
                // RewardedAd selected
                toast("RewardedAdsActivity onAdSelected");
                RewardedAdsActivity.this.rewardedAd = rewardedAd;
            }

            @Override
            public void onAdLoaded() {
                // RewardedAd loaded
                // Now you can show rewarded ad
                toast("RewardedAdsActivity onAdLoaded");
            }

            @Override
            public void onAdFailed() {
                // RewardedAd load failed
                toast("RewardedAdsActivity onAdFailed");
            }

            @Override
            public void onAdClosed() {
                // RewardedAd closed
                toast("RewardedAdsActivity onAdClosed");
                AdManager.loadRewardedAd(RewardedAdsActivity.this, listener);
            }

            @Override
            public void onRewardEarned() {
                // User earn reward
                toast("RewardedAdsActivity onRewardEarned");
            }
        };

        // RewardedAd load start
        AdManager.loadRewardedAd(this, listener);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rewardedAd != null) rewardedAd.show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (rewardedAd != null) rewardedAd.onDestroy();
        super.onDestroy();
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
