package com.freehand.sample.api;

import com.freehand.fetcher.IResponseInterceptor;
import com.freehand.realmprovider.RealmUtility;
import com.freehand.sample.db.MultipleResource;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.realm.RealmObject;

/**
 * Created by minhpham on 12/3/18.
 * Purpose: .
 * Copyright © 2018 Pham Duy Minh. All rights reserved.
 */
public class StoreInterceptor implements IResponseInterceptor {
    @Override
    public <T> Observable<T> callback(Observable<T> response) {
        return response.observeOn(Schedulers.io()).doOnNext(t -> {
            if (t instanceof MultipleResource) {
                List data = ((MultipleResource) t).data;
                if (data == null || data.size() == 0) return;
                if (!(data.get(0) instanceof RealmObject)) return;
                RealmUtility.saveSync(((MultipleResource) t).data);
            } else if (t instanceof RealmObject) {
                RealmUtility.saveSync((RealmObject) t);
            }
        });
    }
}
