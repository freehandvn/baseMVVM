package com.pureprojects.oauth2;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * Created by minhpham on 4/21/17.
 * Purpose: define base request and its params
 * Copyright © 2017 Pham Duy Minh. All rights reserved.
 */

public class OAuth2Parameter {

    private static final String QUESTION_MARK = "?";
    private static final String AMPERSAND = "&";
    private static final String EQUALS = "=";
    private static final String STATE_PARAM = "state";
    private static final String REDIRECT_URI_PARAM = "redirect_uri";

    private final String site;
    private final Map<String, String> mapParams;
    private String redirectUri;
    private String state;

    public OAuth2Parameter(String site) {
        this.site = site;
        this.mapParams = new HashMap<>();
    }

    public OAuth2Parameter addParameter(String key, String val) {
        mapParams.put(key, val);
        return this;
    }

    public String getRedirectUri() {
        if (!TextUtils.isEmpty(redirectUri)) return redirectUri;
        if (mapParams != null && mapParams.containsKey(REDIRECT_URI_PARAM))
            return mapParams.get(REDIRECT_URI_PARAM);
        return redirectUri;
    }

    public OAuth2Parameter setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
        return this;
    }

    public String getState() {
        if (!TextUtils.isEmpty(state)) return state;
        if (mapParams != null && mapParams.containsKey(STATE_PARAM))
            return mapParams.get(STATE_PARAM);
        return "";
    }

    public OAuth2Parameter setState(String state) {
        this.state = state;
        return this;
    }

    public String getSite() {
        return site;
    }

    public Map<String, String> getMapParams() {
        return mapParams;
    }

    public String getAvaiableUrl() {
        if (TextUtils.isEmpty(site)) return site;
        if (mapParams == null || mapParams.size() == 0) return site;
        String url = null;
        for (String key : mapParams.keySet()) {
            if (url == null) {
                url = site + QUESTION_MARK + key + EQUALS + mapParams.get(key);
            } else {
                url = url + AMPERSAND + key + EQUALS + mapParams.get(key);
            }
        }
        return url;
    }

    public RequestBody getRequestBody() {
        FormBody.Builder request = new FormBody.Builder();
//                .addFormDataPart("somParam", "someValue")
//
//         .build();
        if (TextUtils.isEmpty(site)) return null;
        if (mapParams == null || mapParams.size() == 0) return null;
        for (String key : mapParams.keySet()) {
            request.add(key, mapParams.get(key));
        }
        return request.build();
    }
}
