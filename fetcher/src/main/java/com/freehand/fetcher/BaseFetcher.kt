package com.freehand.fetcher

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

abstract class BaseFetcher<I,O> : IProxyFetcher<I,O> {

    var channel = PublishSubject.create<FetcherResult<O>>()
    var subscribeCount = 0

    override fun getOutput(): Observable<FetcherResult<O>> {
        return channel
                .doOnSubscribe { subscribeCount++ }
                .doOnDispose {subscribeCount--; if(subscribeCount<=0) cancel() }
    }

    private var disposal: Disposable? = null

    override fun fetch(input: I) {
        disposal = defineApi(input)
                .map { out ->  FetcherResult(out)}
                .subscribeOn(Schedulers.io())
                .subscribe({it -> channel.onNext(it)},{ it -> channel.onNext(FetcherResult(it))})
    }

    override fun destroy() {
        disposal = null
        channel.onComplete()
    }

    override fun cancel() {
        disposal?.let { it.dispose() }
    }

    abstract fun defineApi(input: I):Observable<O>
}