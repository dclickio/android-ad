package io.dclick.sample;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import io.dclick.ads.AdManager;
import io.dclick.ads.listeners.NativeAdListener;
import io.dclick.ads.nativ.NativeAd;
import io.dclick.ads.nativ.NativeAdData;
import io.dclick.ads.utils.Utils;

public class NativeAdsActivity extends Activity {
    private NativeAd nativeAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_ads);

        // NativeAd load start
        AdManager.loadNativeAd(this, new NativeAdListener() {
            @Override
            public void onAdSelected(NativeAd nativeAd) {
                // Native Ad Selected
                NativeAdsActivity.this.nativeAd = nativeAd;
            }

            @Override
            public void onAdLoaded(NativeAdData nativeAdData) {
                // Native Ad Load Success
                String title = nativeAdData.getTitle();
                String desc = nativeAdData.getDescription();
                String iconUrl = nativeAdData.getIcon().getUri();
                String imageUrl = nativeAdData.getImages().get(0).getUri();

                ((TextView) findViewById(R.id.title)).setText(title);
                ((TextView) findViewById(R.id.description)).setText(desc);

                Utils.showImage((ImageView) findViewById(R.id.icon), iconUrl);
                Utils.showImage((ImageView) findViewById(R.id.icon), imageUrl);
            }

            @Override
            public void onAdFailed() {
                // Native Ad Load Failed
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (nativeAd != null) nativeAd.onDestroy();
        super.onDestroy();
    }
}
