package com.freehand.realmprovider;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by minhpham on 12/24/18.
 * Purpose: .
 * Copyright © 2018 Pham Duy Minh. All rights reserved.
 */
public class RealmResultListener<T extends RealmObject> implements IRealmListener {
    private Observable<OrderedCollectionChangeSet> output;
    private final AtomicInteger obseverCount = new AtomicInteger(0);
    private RealmResults<T> result;
    private PublishSubject<OrderedCollectionChangeSet> channel = PublishSubject.create();
    private AtomicBoolean pauseflag = new AtomicBoolean(false);
    private OrderedCollectionChangeSet lastedChange = null;
    private OrderedRealmCollectionChangeListener<RealmResults<T>> listener = (realmResults, changeSet) -> {
        if(pauseflag.get()){
            lastedChange = changeSet;
            return;
        }
        channel.onNext(changeSet);
    };

    public RealmResultListener(RealmResults<T> realmResult) {
        this.result = realmResult;
        output = channel.doOnDispose(() -> {
            if (obseverCount.get() > 0) {
                int count = obseverCount.decrementAndGet();
                if (count == 0) {
                    result.removeChangeListener(listener);
                }
            }
        }).doOnSubscribe(disposable -> {
            if (obseverCount.get() == 0) {
                //subscribe
                result.addChangeListener(listener);
            } else {
                obseverCount.incrementAndGet();
            }
        }).doOnError(throwable -> result.removeChangeListener(listener));
    }

    @Override
    public Observable getOuput() {
        return output;
    }

    @Override
    public void destroy() {
        this.result.removeChangeListener(listener);
        this.result = null;
        this.channel.onComplete();
        this.channel = null;
        output = null;
        listener = null;
    }

    @Override
    public void pause() {
        pauseflag.set(true);
    }

    @Override
    public void unpause() {
        pauseflag.set(false);
        if (channel != null && lastedChange!=null) {
            channel.onNext(lastedChange);
        }
    }
}
