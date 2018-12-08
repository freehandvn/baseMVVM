package com.freehand.fetcher;


import java.util.HashSet;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import retrofit2.Response;

/**
 * Created by minhpham on 11/29/18.
 * Purpose: just call api and store local
 * Copyright © 2018 Pham Duy Minh. All rights reserved.
 */
public class Fetcher<T> implements IFetcher<T> {
    private Set<IResponseInterceptor> interceptors;
    protected PublishSubject<FetcherResult<T>> output;
    private Scheduler subscribeOnScheduler = Schedulers.io();
    private Scheduler observeOnScheduler = AndroidSchedulers.mainThread();
    private Disposable disposal;

    public Fetcher() {
        interceptors = new HashSet<>(FetcherConfig.getInstance().getInterceptorList());
        output = PublishSubject.create();
    }


    /**
     * [optional] add callback process with response like store update ...
     *
     * @param responseInterceptor
     * @return
     */
    @Override
    public IFetcher<T> addResponseInterceptor(IResponseInterceptor responseInterceptor) {
        if (responseInterceptor != null) {
            interceptors.add(responseInterceptor);
        }
        return this;
    }

    /**
     * [optional] define what thread fetcher run
     *
     * @param subscribeOnScheduler
     * @return
     */
    @Override
    public IFetcher<T> subscribeOn(Scheduler subscribeOnScheduler) {
        this.subscribeOnScheduler = subscribeOnScheduler;
        return this;
    }

    /**
     * [optional] define what thread fetcher return
     *
     * @param observableOnScheduler
     * @return
     */
    @Override
    public IFetcher<T> observeOn(Scheduler observableOnScheduler) {
        this.observeOnScheduler = observableOnScheduler;
        return this;
    }

    /**
     * [optional] @return
     */
    @Override
    public Observable<FetcherResult<T>> getOutput() {
        return output;
    }

    /**
     * [main] call api and execute all fetcher logic
     */
    @Override
    public void fetch(Observable<T> api) {
        Observable<T> fetcherObs = getExecuteObservable(api);
        if (fetcherObs == null) return;
        this.disposal = fetcherObs.subscribeOn(subscribeOnScheduler).observeOn(observeOnScheduler).subscribe();
    }

    /**
     * [optional] cancel fetcher
     */
    @Override
    public void cancel() {
        if (disposal == null) return;
        if (disposal.isDisposed()) return;
        disposal.dispose();
        disposal = null;
    }

    /**
     * destroy fetcher
     */
    @Override
    public void destroy() {
        cancel();
        output.onComplete();
        output = null;
        interceptors.clear();
        interceptors = null;
    }

    private Observable<T> getExecuteObservable(Observable<T> inputApi) {
        Observable<T> api = inputApi;
        if (api == null) return Observable.empty();

        if (interceptors.size() > 0) {
            for (IResponseInterceptor interceptor : interceptors) {
                try {
                    api = interceptor.callback(api);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return api.doOnNext(t -> {
            if (shouldReturnResponse())
                output.onNext(new FetcherResult<>(t));
            if (FetcherConfig.getInstance().getErrorCallback() == null) return;
            if (!(t instanceof Response)) return;
            if (((Response) t).isSuccessful()) return;
            FetcherConfig.getInstance().getErrorCallback().onError(((Response) t).code(), ((Response) t).message());
        })
                .doOnError(throwable -> {
                    output.onNext(new FetcherResult<>(throwable));
                    if (FetcherConfig.getInstance().getErrorCallback() == null) return;
                    FetcherConfig.getInstance().getErrorCallback().onError(throwable);
                });
    }

    protected boolean shouldReturnResponse() {
        return true;
    }
}
