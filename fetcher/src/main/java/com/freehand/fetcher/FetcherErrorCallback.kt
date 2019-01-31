package com.freehand.fetcher

/**
 * Created by minhpham on 12/9/18.
 * Purpose: .
 * Copyright © 2018 Pham Duy Minh. All rights reserved.
 */
interface FetcherErrorCallback {
    /**
     * handle exception when execute fetcher
     *
     * @param throwable
     */
    fun onError(throwable: Throwable)

    /**
     * handle error return from server
     *
     * @param errorCode
     * @param message
     */
    fun onError(errorCode: Int, message: String)
}
