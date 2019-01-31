package com.freehand.fetcher

/**
 * Created by minhpham on 12/1/18.
 * Purpose: .
 * Copyright © 2018 Pham Duy Minh. All rights reserved.
 */
class FetcherResult<T> {
    val result: T?
    val error: Throwable?

    val isSuccess: Boolean
        get() = error == null

    constructor(result: T) {
        this.result = result
        this.error = null
    }

    constructor(error: Throwable) {
        this.error = error
        this.result = null
    }
}
