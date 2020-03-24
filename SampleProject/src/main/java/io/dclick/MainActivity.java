package io.dclick;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // For Test
        AdManager.initialize(this);

        // For Production
//        AdConfig adConfig = new AdConfig();
//        adConfig.setApiKey(AdNetwork.Dclick, "DCLICK API KEY");
//        adConfig.setApiKey(AdNetwork.Google, "AdMob API KEY");
//
//        adConfig.setBannerUnitId(AdNetwork.Dclick, "DCLICK Banner Unit ID");
//        adConfig.setBannerUnitId(AdNetwork.Google, "AdMob Banner Unit ID");
//
//        AdManager.initialize(this, adConfig);

        findViewById(R.id.banner_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BannerAdsActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.interstitial_ad_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InterstitialAdsActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.rewarded_ad_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RewardedAdsActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.native_ad_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NativeAdsActivity.class);
                startActivity(intent);
            }
        });
    }
}
