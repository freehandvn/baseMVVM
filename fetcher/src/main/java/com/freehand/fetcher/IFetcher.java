package com.freehand.fetcher;


import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Created by minhpham on 11/30/18.
 * Purpose: define fetcher protocol
 * Copyright © 2018 Pham Duy Minh. All rights reserved.
 */
public interface IFetcher<T> {

    /**
     * [optional] add callback process with response like store update ...
     *
     * @param responseInterceptor
     * @return
     */
    IFetcher<T> addResponseInterceptor(IResponseInterceptor responseInterceptor);

    /**
     * [optional] define what thread fetcher run
     * @param subscribeOnScheduler
     * @return
     */
    IFetcher<T> subscribeOn(Scheduler subscribeOnScheduler);

    /**
     * [optional] define what thread fetcher return
     * @param observableOnScheduler
     * @return
     */
    IFetcher<T> observeOn(Scheduler observableOnScheduler);

    /**
     * [optional] @return
     */
    Observable<FetcherResult<T>> getOutput();

    /**
     * [main] method call api and execute all fetcher logic
     */
    void fetch(Observable<T> api);


    /**
     * cancel fetcher
     */
    void cancel();

    /**
     * destroy fetcher
     */
    void destroy();

}
