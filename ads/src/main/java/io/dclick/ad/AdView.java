package io.dclick.ad;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class AdView extends LinearLayout {
    private static int ADVIEW_COUNT = 0;
    private static int ADVIEW_COUNT_LIMIT = 3;
    private static final String TAG = "DclickAdView";
    private WebView webView;
    private String adUnitId;

    public AdView(Context context) {
        super(context);
        initView();
    }

    public AdView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
        getAttrs(attrs);
    }

    public AdView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        getAttrs(attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AdView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    public void loadAd() {
        if (webView == null) {
            return;
        }
        webView.loadUrl("https://api.dclick.io/v2/aai/" + adUnitId);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initView() {
        ADVIEW_COUNT++;
        if (ADVIEW_COUNT > ADVIEW_COUNT_LIMIT) {
            return;
        }
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(infService);
        View view = inflater.inflate(R.layout.ad_layout, this, false);

        webView = view.findViewById(R.id.dclick_webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                if (error.getErrorCode() != ERROR_UNKNOWN) {
                    webView.setVisibility(View.GONE);
                }
            }

            @SuppressLint("NewApi")
            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
                webView.setVisibility(View.GONE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url != null) {
                    if (isRedirectableRequest(url)) {
                        view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                        return true;
                    }
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            @SuppressLint("NewApi")
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                try {
                    if (isRedirectableRequest(request.getUrl().getPath())) {
                        view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, request.getUrl()));
                        return true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return super.shouldOverrideUrlLoading(view, request);
            }

            private boolean isRedirectableRequest(String url) {
                return !url.contains("/aai/") && url.startsWith("http");
            }
        });


        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
        addView(view);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View view = this.getChildAt(0);
        if (view == null || view.getVisibility() == View.GONE) {
            return;
        }

        int width = view.getMeasuredWidth();
        int height = view.getMeasuredHeight();
        int leftTop = (r - l - width) / 2;
        int topLeft = (b - t - height) / 2;
        super.onLayout(changed, leftTop, topLeft, leftTop + width, topLeft + height);
    }

    public void setAdUnitId(String adUnitId) {
        this.adUnitId = adUnitId;
    }

    public String getAdUnitId() {
        return this.adUnitId;
    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.AdsAttrs);
        setTypeArray(typedArray);
    }


    private void getAttrs(AttributeSet attrs, int defStyle) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.AdsAttrs, defStyle, 0);
        setTypeArray(typedArray);
    }


    private void setTypeArray(TypedArray typedArray) {
        adUnitId = typedArray.getString(R.styleable.AdsAttrs_adUnitId);
        typedArray.recycle();

    }
}
