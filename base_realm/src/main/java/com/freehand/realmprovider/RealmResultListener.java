package com.freehand.realmprovider;

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
    private RealmResults<T> result;
    private PublishSubject<OrderedCollectionChangeSet> channel = PublishSubject.create();
    private OrderedRealmCollectionChangeListener<RealmResults<T>> listener = (realmResults, changeSet) -> channel.onNext(changeSet);

    public RealmResultListener(RealmResults<T> realmResult) {
        this.result = realmResult;
        this.result.addChangeListener(listener);
    }

    @Override
    public Observable getOuput() {
        return channel;
    }

    @Override
    public void destroy() {
        this.result.removeChangeListener(listener);
        this.result = null;
        this.channel.onComplete();
        this.channel = null;
        listener = null;
    }
}
