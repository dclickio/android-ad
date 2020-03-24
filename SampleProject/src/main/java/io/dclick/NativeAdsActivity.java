package io.dclick;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.io.InputStream;

import io.dclick.listeners.NativeAdListener;
import io.dclick.nativ.NativeAd;
import io.dclick.nativ.NativeAdData;

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

                showImage((ImageView) findViewById(R.id.icon), iconUrl);
                showImage((ImageView) findViewById(R.id.icon), imageUrl);
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

    private void showImage(final ImageView imageView, final String urlStr) {
        new DownloadImageTask(imageView).execute(urlStr);
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            if (urldisplay == null) {
                return null;
            }

            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
