package com.freehand.oauth2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import im.delight.android.webview.AdvancedWebView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by minhpham on 4/20/17.
 * Purpose: webview dialog show authenticate site use in oauth2
 * Copyright © 2017 Pham Duy Minh. All rights reserved.
 */

public class OAuth2Dialog extends Fragment implements OnClickListener,AdvancedWebView.Listener {

    private static final String TAG = OAuth2Dialog.class.getSimpleName();
    public static final String ERROR_WEB_PAGE_NOT_AVAILABLE = "ERROR_WEB_PAGE_NOT_AVAILABLE";
    private static final String RESPONSE_TYPE_VALUE = "code";
    private static final String STATE_PARAM = "state";
    private ProgressBar pd;
    private AdvancedWebView webView;
    private OAuth2CallBack mCallback = null;
    private OAuth2Parameter authorization;
    private OAuth2Parameter accessToken;
    private Disposable dispose;

    public void initParams() {
        OAuth2Config config = OAuth2Manager.getInstance().getConfig();
        authorization = config.getAuthorizationParam();
        accessToken = config.getAccessTokenParam();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_oauth2, container, false);
        pd =  view.findViewById(R.id.process_bar_oauth2);
        webView =  view.findViewById(R.id.web_oauth2);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initWeb();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWeb() {
        webView.setListener(getActivity(),this);
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setJavaScriptEnabled(true);
//        clearCookie();
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Log.d(TAG, "onReceivedError() called with: view = [" + view + "], request = [" + request + "], error = [" + error + "]");
                if (mCallback != null) mCallback.callback(false, ERROR_WEB_PAGE_NOT_AVAILABLE);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                pd.setVisibility(View.GONE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String authorizationUrl) {
                //This method will be called when the Auth proccess redirect to our RedirectUri.
                //We will check the url looking for our RedirectUri.
                Log.i("Authorize", "current: " + authorizationUrl);
                if (authorizationUrl.startsWith(authorization.getRedirectUri())) {
                    Log.i("Authorize", "");
                    Uri uri = Uri.parse(authorizationUrl);
                    //We take from the url the authorizationToken and the state token. We have to check that the state token returned by the Service is the same we sent.
                    //If not, that means the request may be a result of CSRF and must be rejected.
                    String stateToken = uri.getQueryParameter(STATE_PARAM);
                    if (stateToken == null || !stateToken.equals(authorization.getState())) {
                        Log.e("Authorize", "State token doesn't match");
                        if (mCallback != null) {
                            mCallback.callback(false, "State token doesn't match.");
                        }
                        return true;
                    }

                    //If the user doesn't allow authorization to our application, the authorizationToken Will be null.
                    String authorizationToken = uri.getQueryParameter(RESPONSE_TYPE_VALUE);
                    if (authorizationToken == null) {
                        Log.i("Authorize", "The user doesn't allow authorization.");
                        if (mCallback != null) {
                            mCallback.callback(false, "The user doesn't allow authorization.");
                        }
                        return true;
                    }
                    Log.i("Authorize", "Auth token received: " + authorizationToken);
                    //Generate URL for requesting Access Token
                    accessToken.addParameter(RESPONSE_TYPE_VALUE, authorizationToken);
                    //We make the request in a AsyncTask
                    getAccessToken();

                } else {
                    //Default behaviour
//                    authorizationUrl = "https://ids.puredatacenter.com/identity/login?signin=490ac52f388b2e7033e72e31e0e19352";
                    Log.i("Authorize", "Redirecting to: " + authorizationUrl);
                    webView.loadUrl(authorizationUrl);
                }
                return true;
            }
        });

        //Get the authorization Url
        String authUrl = authorization.getAvaiableUrl();
        Log.i("Authorize", "Loading Auth Url: " + authUrl);
        //Load the authorization URL into the webView
        webView.loadUrl(authUrl);
    }

    private void clearCookie() {
//        CookieSyncManager.createInstance(getContext());
//        CookieManager cookieManager = CookieManager.getInstance();
//        cookieManager.setAcceptCookie(false);
    }

    @Override
    public void onDestroyView() {
        webView.clearFormData();
        webView.clearCache(true);
        webView.clearHistory();
        webView.clearSslPreferences();
        webView.clearMatches();
        webView.loadUrl("about:blank");
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        webView.destroy();
        super.onDetach();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

    }

    private void getAccessToken() {
        pd.setVisibility(View.VISIBLE);
        dispose = Oauth2Utils.makeRequest(accessToken, getContext())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) {
                        pd.setVisibility(View.GONE);
                        if (mCallback != null) {
                            mCallback.callback(aBoolean, "got access token finish");
                        }
                    }
                });
    }

    public void setCallback(OAuth2CallBack callBack) {
        this.mCallback = callBack;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        webView.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onPause() {
        webView.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        webView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        webView.onDestroy();
        super.onDestroy();
        if(dispose!=null){
            dispose.dispose();
        }
    }

    @Override
    public void onPageStarted(String s, Bitmap bitmap) {
    }

    @Override
    public void onPageFinished(String s) {
    }

    @Override
    public void onPageError(int i, String s, String s1) {
    }

    @Override
    public void onDownloadRequested(String s, String s1, String s2, long l, String s3, String s4) {
    }

    @Override
    public void onExternalPageRequest(String authorizationUrl) {
    }
}
