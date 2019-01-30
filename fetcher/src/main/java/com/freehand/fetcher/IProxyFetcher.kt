package com.freehand.fetcher

import io.reactivex.Observable

interface IProxyFetcher<I,O> {
    fun getOutput(): Observable<FetcherResult<O>>
    fun fetch(input : I)
    fun destroy()
    fun cancel()
}