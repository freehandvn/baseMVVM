package com.freehand.realmprovider;

import javax.annotation.Nullable;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.realm.ObjectChangeSet;
import io.realm.RealmModel;
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
    private RealmObjectChangeListener<T> listener = (t,changeSet)-> channel.onNext(changeSet);

    public RealmObjectListener(T realmObject) {
        this.realObject = realmObject;
        realmObject.addChangeListener(listener);
    }

    @Override
    public Observable getOuput() {
        return channel;
    }

    @Override
    public void destroy() {
        realObject.removeChangeListener(listener);
        realObject = null;
        channel.onComplete();
        channel = null;
        listener = null;
    }
}
