package com.freehand.fetcher


import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

/**
 * Created by minhpham on 11/29/18.
 * Purpose: just call api and store local
 * Copyright © 2018 Pham Duy Minh. All rights reserved.
 */
 abstract class Fetcher<I, O> : IFetcher<I, O> {
    override val output: Observable<FetcherResult<O>>
        get() = channel
    protected var channel: PublishSubject<FetcherResult<O>> = PublishSubject.create()
    private var subscribeOnScheduler = Schedulers.io()
    private var observeOnScheduler = AndroidSchedulers.mainThread()
    private var disposal: Disposable? = null


    /**
     * [optional] define what thread fetcher run
     *
     * @param subscribeOnScheduler
     * @return
     */
    override fun subscribeOn(subscribeOnScheduler: Scheduler): IFetcher<I, O> {
        this.subscribeOnScheduler = subscribeOnScheduler
        return this
    }

    /**
     * [optional] define what thread fetcher return
     *
     * @param observableOnScheduler
     * @return
     */
    override fun observeOn(observableOnScheduler: Scheduler): IFetcher<I, O> {
        this.observeOnScheduler = observableOnScheduler
        return this
    }

    /**
     * [main] call api and execute all fetcher logic
     */
    override fun fetch(input: I) {

        val fetcherObs = getExecuteObservable(input) ?: return
        this.disposal = fetcherObs.subscribeOn(subscribeOnScheduler)
                .observeOn(observeOnScheduler)
                .subscribe({ t ->
                    if (shouldReturnResponse())
                        channel.onNext(FetcherResult(t))
                },
                        { error -> channel.onNext(FetcherResult(error)) })
    }

    /**
     * [optional] cancel fetcher
     */
    override fun cancel() {
        if (disposal == null) return
        if (disposal!!.isDisposed) return
        disposal!!.dispose()
        disposal = null
    }

    /**
     * destroy fetcher
     */
    override fun destroy() {
        cancel()
        channel.onComplete()
    }

    private fun getExecuteObservable(input: I): Observable<O>? {

        val api = preProcessFetcher(input).concatMap { createApiObservable(it) } ?: return Observable.empty()

        return api
                .observeOn(Schedulers.io())
                .concatMap { t -> processAfterFetch(t, input) }
                .doOnError { throwable ->
                    FetcherConfig.errorCallback?.onError(throwable)
                }
    }

    /**
     * trigger before fetcher
     * @param input
     */
    protected open fun preProcessFetcher(input: I):Observable<I> {
        return Observable.just(input)
    }

    /**
     * trigger after fetched
     * @param output
     * @param input
     */
    protected open fun processAfterFetch(output: O, input: I):Observable<O> {
        return Observable.just(output)
    }

    /**
     * @param input
     * @return
     */
    protected abstract fun createApiObservable(input: I): Observable<O>?

    /**
     * @return true: return data after fetched
     */
    protected open fun shouldReturnResponse(): Boolean {
        return true
    }
}
