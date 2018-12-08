package com.freehand.fetcher;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by minhpham on 12/2/18.
 * Purpose: .
 * Copyright © 2018 Pham Duy Minh. All rights reserved.
 */
public class FetcherConfig {
    private static final FetcherConfig ourInstance = new FetcherConfig();
    private Set<IResponseInterceptor> interceptorList;
    private FetcherErrorCallback errorCallback;

    private FetcherConfig() {
        interceptorList = new HashSet<>();
    }

    public static FetcherConfig getInstance() {
        return ourInstance;
    }

    public void addDefaultReponseInterceptor(IResponseInterceptor interceptor) {
        interceptorList.add(interceptor);
    }

    public Set<IResponseInterceptor> getInterceptorList() {
        return interceptorList;
    }

    public void addErrorHandle(FetcherErrorCallback callback) {
        this.errorCallback = callback;
    }

    public FetcherErrorCallback getErrorCallback() {
        return errorCallback;
    }
}
