package com.test.aktivocoresdksample_android;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class WebActivity extends AppCompatActivity {

    private static final String TAG = "WebActivity";
    private static final String FITBIT_SUCCESS = "/fitbit/success";
    private static final String GARMIN_SUCCESS = "/garmin/success";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        String url = getIntent().getStringExtra("url");
        Log.e(TAG, "URL: "+url);

        WebView webView = findViewById(R.id.webView);
        WebSettings weviewWebSettings = webView.getSettings();
        weviewWebSettings.setSaveFormData(false);
        weviewWebSettings.setBuiltInZoomControls(true);
        weviewWebSettings.setDisplayZoomControls(false);
        weviewWebSettings.setDomStorageEnabled(true);
        weviewWebSettings.setSupportZoom(false);
        weviewWebSettings.setJavaScriptEnabled(true);
        weviewWebSettings.setLoadsImagesAutomatically(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                if (url.contains(FITBIT_SUCCESS) || url.contains(GARMIN_SUCCESS)) {
                    if (view.getUrl().contains(FITBIT_SUCCESS) || view.getUrl().contains(GARMIN_SUCCESS)) {
                        Toast.makeText(WebActivity.this, "Connected successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        });
        webView.getSettings().setAppCacheEnabled(false);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.setHapticFeedbackEnabled(false);

        if (!TextUtils.isEmpty(url)) {
            webView.loadUrl(url);
        }
    }
}
