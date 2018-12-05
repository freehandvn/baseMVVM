package com.freehand.fetcher;

import io.reactivex.Observable;

/**
 * Created by minhpham on 11/30/18.
 * Purpose: define callback method support interceptor to process response
 * Copyright © 2018 Pham Duy Minh. All rights reserved.
 */
public interface IResponseInterceptor {
    <T> Observable<T> callback(Observable<T> response);
}
