package com.freehand.fetcher;

/**
 * Created by minhpham on 12/1/18.
 * Purpose: .
 * Copyright © 2018 Pham Duy Minh. All rights reserved.
 */
public class FetcherResult<T> {
    private T result;
    private Throwable error;

    public FetcherResult(T result) {
        this.result = result;
    }

    public FetcherResult(Throwable error) {
        this.error = error;
    }
}
