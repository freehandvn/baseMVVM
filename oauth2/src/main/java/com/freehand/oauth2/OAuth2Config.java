package com.freehand.oauth2;

import io.reactivex.annotations.NonNull;

/**
 * Created by minhpham on 4/21/17.
 * Purpose: .
 * Copyright © 2017 Pham Duy Minh. All rights reserved.
 */

public class OAuth2Config {
    private static final String REDIRECT_URI = "redirect_uri";
    private final OAuth2Parameter authorizationParam;
    private final OAuth2Parameter accessTokenParam;
    private final OAuth2Parameter refreshTokenParam;


    public OAuth2Config(@NonNull OAuth2Parameter authorizationParam, @NonNull OAuth2Parameter accessTokenParam, OAuth2Parameter refreshTokenParam) {
        this.authorizationParam = authorizationParam;
        this.accessTokenParam = accessTokenParam;
        this.refreshTokenParam = refreshTokenParam;
    }

    public OAuth2Parameter getAuthorizationParam() {
        return authorizationParam;
    }

    public OAuth2Parameter getAccessTokenParam() {
        return accessTokenParam;
    }

    public OAuth2Parameter getRefreshTokenParam() {
        return refreshTokenParam;
    }
}
