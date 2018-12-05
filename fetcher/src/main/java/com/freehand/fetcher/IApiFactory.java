package com.freehand.fetcher;

import io.reactivex.Observable;

/**
 * Created by minhpham on 11/30/18.
 * Purpose: define method show the way to get api
 * Copyright © 2018 Pham Duy Minh. All rights reserved.
 */
public interface IApiFactory<T> {
    /**
     *
     * @return something like retrofit + rx2
     */
    Observable<T> getApi();
}
