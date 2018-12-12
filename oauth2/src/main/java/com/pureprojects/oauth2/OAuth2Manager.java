package com.pureprojects.oauth2;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import javax.net.ssl.SSLContext;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by minhpham on 4/20/17.
 * Purpose: wrapper oauth2
 * Copyright © 2017 Pham Duy Minh. All rights reserved.
 */

public class OAuth2Manager {
    //key store
    public static final String USER_INFO = "user_info";
    public static final String EXPIRES = "expires";
    public static final String ACCESS_TOKEN = "accessToken";
    public static final String REFRESH_TOKEN = "refreshToken";
    public static final String TOKEN_TYPE = "tokenType";
    public static final String ERROR_CODE = "errorCode";
    public static final String PP_CONTEXT_ID = "PPContextID";
    public static final int INVALID_CODE = -696969;
    private static final String AMPERSAND = "&";
    private static final String EQUALS = "=";
    private static final OAuth2Manager ourInstance = new OAuth2Manager();
    private OAuth2Config mConfig;
    private SSLContext sslContext;

    private OAuth2Manager() {
    }

    public static OAuth2Manager getInstance() {
        return ourInstance;
    }

    public void config(OAuth2Config config) {
        this.mConfig = config;
    }

    /**
     * query stored token
     *
     * @param context
     *
     * @return
     */
    public String getToken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(USER_INFO, 0);
        String accessToken = preferences.getString(ACCESS_TOKEN, null);
        return accessToken;
    }

    public boolean hasAccessToken(Context context) {
        if(context == null) return false;
        SharedPreferences preferences = context.getSharedPreferences(USER_INFO, 0);
        String accessToken = preferences.getString(ACCESS_TOKEN, null);
        return !TextUtils.isEmpty(accessToken);
    }

    /**
     * query stored token
     *
     * @param context
     *
     * @return
     */
    public String getTokenType(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(USER_INFO, 0);
        String accessToken = preferences.getString(TOKEN_TYPE, null);
        return accessToken;
    }

    /**
     * query error code
     *
     * @param context
     *
     * @return
     */
    public int getErrorCode(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(USER_INFO, 0);
        int errorCode = preferences.getInt(ERROR_CODE, INVALID_CODE);
        return errorCode;
    }

    /**
     * check stored token has expired
     *
     * @param context
     *
     * @return
     */
    public boolean isTokenExpired(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(USER_INFO, 0);
        long expires = preferences.getLong(EXPIRES, 0);
        return expires < System.currentTimeMillis();
    }

    /**
     * request accept token from authenticate server
     *
     * @param callBack
     */
    public OAuth2Dialog requestToken(OAuth2CallBack callBack) {
        OAuth2Dialog dialog = new OAuth2Dialog();
        dialog.setCallback(callBack);
        dialog.initParams();
        return dialog;
    }

    public OAuth2Config getConfig() {
        return mConfig;
    }

    public void clearToken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(USER_INFO, 0);
        SharedPreferences.Editor edit = preferences.edit();
        edit.clear();
        edit.commit();
    }

    public void refreshToken(Context context, final OAuth2CallBack callBack) {
        if (mConfig.getRefreshTokenParam() == null) return;
        OAuth2Parameter refreshLink = mConfig.getRefreshTokenParam();
        SharedPreferences preferences = context.getSharedPreferences(USER_INFO, 0);
        String refreshToken = preferences.getString(REFRESH_TOKEN, null);
        if (TextUtils.isEmpty(refreshToken)) return;

        refreshLink = refreshLink.addParameter("refresh_token", refreshToken);
        Oauth2Utils.makeRequest(refreshLink, context)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) throws Exception {
                        if (callBack == null) return;
                        callBack.callback(aBoolean, "finish refresh your token");
                    }
                });
    }

    public Observable<Boolean> refreshToken(Context context) {
        if (mConfig.getRefreshTokenParam() == null) return Observable.just(false);
        OAuth2Parameter refreshLink = mConfig.getRefreshTokenParam();
        SharedPreferences preferences = context.getSharedPreferences(USER_INFO, 0);
        String refreshToken = preferences.getString(REFRESH_TOKEN, null);
        if (TextUtils.isEmpty(refreshToken)) return Observable.just(false);

        refreshLink = refreshLink.addParameter("refresh_token", refreshToken);
        return Oauth2Utils.makeRequest(refreshLink, context)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public void setSslContext(SSLContext sslContext) {
        this.sslContext = sslContext;
    }

    public SSLContext getSslContext() {
        return sslContext;
    }

    public void setPPContextId(Context context, String ppContextId) {
        if (TextUtils.isEmpty(ppContextId)) return;
        SharedPreferences preferences = context.getSharedPreferences(USER_INFO, 0);
        preferences.edit().putString(PP_CONTEXT_ID, ppContextId).apply();
    }

    public String getPPContextId(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(USER_INFO, 0);
        return preferences.getString(PP_CONTEXT_ID, "");
    }

}
