package com.freehand.fetcher

/**
 * Created by minhpham on 12/2/18.
 * Purpose: .
 * Copyright © 2018 Pham Duy Minh. All rights reserved.
 */
object FetcherConfig {
    var errorCallback: FetcherErrorCallback? = null
        private set

    fun addErrorHandle(callback: FetcherErrorCallback) {
        this.errorCallback = callback
    }
}
