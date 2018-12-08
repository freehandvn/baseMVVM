package com.freehand.fetcher;

/**
 * Created by minhpham on 12/9/18.
 * Purpose: .
 * Copyright © 2018 Pham Duy Minh. All rights reserved.
 */
public interface FetcherErrorCallback {
    /**
     * handle exception when execute fetcher
     *
     * @param throwable
     */
    void onError(Throwable throwable);

    /**
     * handle error return from server
     *
     * @param errorCode
     * @param message
     */
    void onError(int errorCode, String message);
}
