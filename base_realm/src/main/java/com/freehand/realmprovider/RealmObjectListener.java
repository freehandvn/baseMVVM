package com.freehand.realmprovider;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.realm.ObjectChangeSet;
import io.realm.RealmObject;
import io.realm.RealmObjectChangeListener;

/**
 * Created by minhpham on 12/24/18.
 * Purpose: .
 * Copyright © 2018 Pham Duy Minh. All rights reserved.
 */
public class RealmObjectListener<T extends RealmObject> implements IRealmListener {

    private T realObject;
    private PublishSubject<ObjectChangeSet> channel = PublishSubject.create();
    private Observable<ObjectChangeSet> output;
    private AtomicInteger obseverCount = new AtomicInteger(0);
    private AtomicBoolean pauseflag = new AtomicBoolean(false);
    private ObjectChangeSet lastedChange = null;
    private RealmObjectChangeListener<T> listener = (t, changeSet) -> {
        if(pauseflag.get()){
            lastedChange = changeSet;
            return;
        }
        channel.onNext(changeSet);
    };

    public RealmObjectListener(T realmObject) {
        this.realObject = realmObject;
        output = channel.doOnDispose(() -> {
            if (obseverCount.get() > 0) {
                int count = obseverCount.decrementAndGet();
                if (count == 0) {
                    realmObject.removeChangeListener(listener);
                }
            }
        }).doOnSubscribe(disposable -> {
            if (obseverCount.get() == 0) {
                //subscribe
                realmObject.addChangeListener(listener);
            } else {
                obseverCount.incrementAndGet();
            }
        }).doOnError(throwable -> realmObject.removeChangeListener(listener));
    }

    @Override
    public Observable getOuput() {
        return output;
    }

    @Override
    public void destroy() {
        realObject.removeChangeListener(listener);
        realObject = null;
        channel.onComplete();
        channel = null;
        listener = null;
        output = null;
    }

    @Override
    public void pause() {
        pauseflag.set(true);
    }

    @Override
    public void unpause() {
        pauseflag.set(false);
        if(channel!=null && lastedChange!=null){
            channel.onNext(lastedChange);
        }
    }
}
