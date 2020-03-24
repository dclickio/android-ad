package io.dclick;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import io.dclick.listeners.RewardedAdListener;
import io.dclick.rewarded.RewardedAd;

public class RewardedAdsActivity extends Activity {
    private RewardedAd rewardedAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewarded_ads);

        // RewardedAd load start
        AdManager.loadRewardedAd(this, new RewardedAdListener() {
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
            }

            @Override
            public void onRewardEarned() {
                // User earn reward
                toast("RewardedAdsActivity onRewardEarned");
            }
        });

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
