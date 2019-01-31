package com.freehand.fetcher


import io.reactivex.Observable
import io.reactivex.Scheduler

/**
 * Created by minhpham on 11/30/18.
 * Purpose: define fetcher protocol
 * Copyright © 2018 Pham Duy Minh. All rights reserved.
 */
interface IFetcher<I,O> {

    /**
     * [optional] @return
     */
    val output: Observable<FetcherResult<O>>

    /**
     * [optional] define what thread fetcher run
     * @param subscribeOnScheduler
     * @return
     */
    fun subscribeOn(subscribeOnScheduler: Scheduler): IFetcher<I,O>

    /**
     * [optional] define what thread fetcher return
     * @param observableOnScheduler
     * @return
     */
    fun observeOn(observableOnScheduler: Scheduler): IFetcher<I,O>

    /**
     * [main] method call api and execute all fetcher logic
     */
    fun fetch(input : I)


    /**
     * cancel fetcher
     */
    fun cancel()

    /**
     * destroy fetcher
     */
    fun destroy()

}
