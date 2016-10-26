package com.zjut.runner.view.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.zjut.runner.R;
import com.zjut.runner.util.Constants;

/**
 * Created by Phuylai on 2016/10/23.
 */

public class WebNormalFragment extends BaseFragment{

    protected WebView webview = null;
    protected String url = null;
    protected String title = null;
    protected boolean hideMenu = true;
    protected ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseArgements();
        drawerindicatorEnabled = false;
    }

    private void parseArgements() {
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        url = bundle.getString(Constants.PARAM_URL);
        title = bundle.getString(Constants.PARAM_TITLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutId = R.layout.fragment_webview;
        return super.onCreateView(inflater,container,savedInstanceState);
    }

    @Override
    public boolean onBackPressed() {
        if (webview.canGoBack()) {
            webview.goBack();
            return true;
        }
        return super.onBackPressed();
    }

    @Override
    public void changeTitle() {
        activity.changeTitle(title);
    }

    @Override
    protected void findViews(View rootView) {
        webview = (WebView) rootView.findViewById(R.id.webview);
        progressBar = (ProgressBar) rootView.findViewById(R.id.pb_sending_post);
        progressBar.setVisibility(View.VISIBLE);
        webview.getSettings().setLightTouchEnabled(true);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webview.setWebChromeClient(new WebChromeClient());
        loadUrl();
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void loadUrl() {
        String finalUrl = url;
        if (!finalUrl.startsWith("http")) {
            finalUrl = "http://" + finalUrl;
        }
        webview.loadUrl(finalUrl);
    }

    @Override
    public void initMenu(Context context, Menu menu) {
        clearMenu(menu);
        ((Activity) context).getMenuInflater().inflate(R.menu.menu_webview, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_home:
                getFragmentManager().popBackStack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void setListener() {

    }

    @Override
    public void onSearchClose() {

    }

    @Override
    public void search(String searchString) {

    }
}

