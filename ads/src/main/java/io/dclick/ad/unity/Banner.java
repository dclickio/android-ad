package io.dclick.ad.unity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.DisplayCutout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowInsets;
import android.widget.FrameLayout;

import io.dclick.ad.AdPosition;
import io.dclick.ad.AdSize;
import io.dclick.ad.AdView;
import io.dclick.ad.Utils;

public class Banner {
    private static class Insets {
        int top = 0;
        int bottom = 0;
        int left = 0;
        int right = 0;
    }

    private AdView mAdView;
    private Activity mUnityPlayerActivity;

    private int mAdPosition;
    private int mHorizontalOffset;
    private int mVerticalOffset;
    private AdSize mAdSize;

    private boolean mHidden;
    private UnityAdListener mUnityListener;
    private View.OnLayoutChangeListener mLayoutChangeListener;

    public Banner(Activity activity, UnityAdListener listener) {
        this.mUnityPlayerActivity = activity;
        this.mUnityListener = listener;
    }

    public void create(final String id, final int position, final int width, final int height) {
        mUnityPlayerActivity.runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        mAdPosition = position;
                        mAdSize = new AdSize(Utils.dpToPx(width), Utils.dpToPx(height));
                        createAdView(id);
                    }
                });
    }

    public void create(final String id, final int x, final int y, final int width, final int height) {
        mUnityPlayerActivity.runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        mAdPosition = AdPosition.CUSTOM;
                        mHorizontalOffset = x;
                        mVerticalOffset = y;
                        mAdSize = new AdSize(Utils.dpToPx(width), Utils.dpToPx(height));
                        createAdView(id);
                    }
                });
    }

    private void createAdView(final String id) {
        mAdView = new AdView(mUnityPlayerActivity);
        mAdView.setBackgroundColor(Color.TRANSPARENT);
        mAdView.setAdUnitId(id);
        mAdView.setAdSize(mAdSize);
        mUnityPlayerActivity.addContentView(mAdView, getLayoutParams());
        mLayoutChangeListener =
                new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(
                            View v,
                            int left,
                            int top,
                            int right,
                            int bottom,
                            int oldLeft,
                            int oldTop,
                            int oldRight,
                            int oldBottom) {
                        boolean viewBoundsChanged =
                                left != oldLeft || right != oldRight || bottom != oldBottom || top != oldTop;
                        if (!viewBoundsChanged) {
                            return;
                        }

                        if (!mHidden) {
                            updatePosition();
                        }
                    }
                };

        mUnityPlayerActivity
                .getWindow()
                .getDecorView()
                .getRootView()
                .addOnLayoutChangeListener(mLayoutChangeListener);
    }

    public void loadAd() {
        mUnityPlayerActivity.runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        mAdView.loadAd();
                    }
                });
    }

    public void show() {
        mUnityPlayerActivity.runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        mHidden = false;
                        mAdView.setVisibility(View.VISIBLE);
                        updatePosition();
                        mAdView.resume();
                    }
                });
    }

    public void hide() {
        mUnityPlayerActivity.runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        mHidden = true;
                        mAdView.setVisibility(View.GONE);
                        mAdView.pause();
                    }
                });
    }

    public void destroy() {
        mUnityPlayerActivity.runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        if (mAdView != null) {
                            mAdView.destroy();
                            ViewParent parentView = mAdView.getParent();
                            if (parentView instanceof ViewGroup) {
                                ((ViewGroup) parentView).removeView(mAdView);
                            }
                        }
                    }
                });

        mUnityPlayerActivity
                .getWindow()
                .getDecorView()
                .getRootView()
                .removeOnLayoutChangeListener(mLayoutChangeListener);
    }

    private FrameLayout.LayoutParams getLayoutParams() {
        final FrameLayout.LayoutParams adParams =
                new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        adParams.gravity = Gravity.CENTER_HORIZONTAL;

        Insets insets = getSafeInsets();
        int safeInsetLeft = insets.left;
        int safeInsetTop = insets.top;
        adParams.bottomMargin = insets.bottom;
        adParams.rightMargin = insets.right;

        if (mAdPosition == AdPosition.BOTTOM) {
            adParams.gravity = adParams.gravity | Gravity.BOTTOM;
        } else if (mAdPosition == AdPosition.TOP) {
            adParams.gravity = adParams.gravity | Gravity.TOP;
            adParams.topMargin = safeInsetTop;
        } else if (mAdPosition == AdPosition.CUSTOM){
            adParams.leftMargin = mHorizontalOffset;
            adParams.topMargin = mVerticalOffset;
        }

        return adParams;
    }

    private void updatePosition() {
        if (mAdView == null || mHidden) {
            return;
        }
        mUnityPlayerActivity.runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        FrameLayout.LayoutParams layoutParams = getLayoutParams();
                        mAdView.setLayoutParams(layoutParams);
                    }
                });
    }

    private Insets getSafeInsets() {
        Insets insets = new Insets();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            return insets;
        }
        Window window = mUnityPlayerActivity.getWindow();
        if (window == null) {
            return insets;
        }
        WindowInsets windowInsets = window.getDecorView().getRootWindowInsets();
        if (windowInsets == null) {
            return insets;
        }
        DisplayCutout displayCutout = windowInsets.getDisplayCutout();
        if (displayCutout == null) {
            return insets;
        }
        insets.top = displayCutout.getSafeInsetTop();
        insets.left = displayCutout.getSafeInsetLeft();
        insets.bottom = displayCutout.getSafeInsetBottom();
        insets.right = displayCutout.getSafeInsetRight();
        return insets;
    }
}