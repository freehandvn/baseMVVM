package com.freehand.fetcher;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by minhpham on 11/29/18.
 * Purpose: just call api and store local
 * Copyright © 2018 Pham Duy Minh. All rights reserved.
 */
public class Fetcher<T> implements IFetcher<T> {
    private List<IApiFactory<T>> factories;
    private Set<IResponseInterceptor> interceptors;
    private PublishSubject<FetcherResult<T>> output;
    private Scheduler subscribeOnScheduler = Schedulers.io();
    private Scheduler observeOnScheduler = AndroidSchedulers.mainThread();
    private Disposable disposal;

    public Fetcher() {
        factories = new ArrayList<>();
        interceptors = new HashSet<>(FetcherConfig.getInstance().getInterceptorList());
        output = PublishSubject.create();
    }

    /**
     * [optional] add api factory, define the way get api
     *
     * @param factory
     * @return
     */
    @Override
    public IFetcher<T> addAPIFactory(IApiFactory<T> factory) {
        if (factory != null) {
            this.factories.add(factory);
        }
        return this;
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
        factories.clear();
        factories = null;
        interceptors.clear();
        interceptors = null;
    }

    private Observable<T> getExecuteObservable(Observable<T> inputApi) {
        Observable<T> api = inputApi;
        if (factories.size() > 0) {
            for (IApiFactory<T> factory : factories) {
                if (api == null) api = factory.getApi();
                else api = api.concatWith(factory.getApi());
            }
        }
        if(api == null) return Observable.empty();

        if (interceptors.size() > 0) {
            for (IResponseInterceptor interceptor : interceptors) {
                try {
                    api = interceptor.callback(api);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return api.doOnNext(new Consumer<T>() {
            @Override
            public void accept(T t) {
                output.onNext(new FetcherResult<>(t));
            }
        }).doOnError(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                output.onNext(new FetcherResult<T>(throwable));
            }
        });
    }
}
