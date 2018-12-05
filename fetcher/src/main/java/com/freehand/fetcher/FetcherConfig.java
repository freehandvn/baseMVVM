package com.freehand.fetcher;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by minhpham on 12/2/18.
 * Purpose: .
 * Copyright © 2018 Pham Duy Minh. All rights reserved.
 */
public class FetcherConfig {
    private Set<IResponseInterceptor> interceptorList;
    private static final FetcherConfig ourInstance = new FetcherConfig();

    public static FetcherConfig getInstance() {
        return ourInstance;
    }

    private FetcherConfig() {
        interceptorList = new HashSet<>();
    }

    public void addDefaultReponseInterceptor(IResponseInterceptor interceptor){
        interceptorList.add(interceptor);
    }

    public Set<IResponseInterceptor> getInterceptorList() {
        return interceptorList;
    }
}
