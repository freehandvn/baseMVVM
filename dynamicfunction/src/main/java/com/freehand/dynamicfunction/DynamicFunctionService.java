package com.freehand.dynamicfunction;

import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by minhpham on 4/26/17.
 * Purpose: .
 * Copyright © 2017 Pham Duy Minh. All rights reserved.
 */

public class DynamicFunctionService {
    private static final DynamicFunctionService ourInstance = new DynamicFunctionService();
    private PublishSubject<IFunction> service;
    private List<IFunction> buffer;

    private DynamicFunctionService() {
        buffer = new CopyOnWriteArrayList<>();
    }

    public static DynamicFunctionService getInstance() {
        return ourInstance;
    }

    public void start() {
        if (service != null && !service.hasComplete()) {
            service.onComplete();
        }
        service = PublishSubject.create();
    }


    public void notifyFunctionInit(IFunction function) {
        if (service == null || service.hasComplete()) {
            Log.e("DynamicFunctionService", "service has destroy, can not notify this functon");
            return;
        }
//        Log.e("DynamicFunctionService", "add func: "+ function.getName());
        this.buffer.add(function);
        service.onNext(function);
    }

    public void notifyFunctionDestroy(IFunction function) {
//        Log.e("DynamicFunctionService", "remove func: "+ function.getName());
        this.buffer.remove(function);
    }

    public IFunction getExistFuncByName(String name) {
        if (buffer == null || buffer.size() == 0) return null;
        int n = buffer.size();
        for (int i = n - 1; i >= 0; i--) {
            if (buffer.get(i).getName().equals(name)) return buffer.get(i);
        }
        return null;
    }

    //HashMap is thread-safe => no need synchronize on this method
    public void subscribe(ISubscriber<IFunction> consumer) {

        if (service == null || service.hasComplete() || consumer == null) return;
        Observer observer = new BaseDisposableObserver(consumer);
        if (buffer == null || buffer.size() == 0) {
            service.subscribeWith(observer);
            return;
        }
        Observable<IFunction> bufferEmit = Observable.fromIterable(buffer);
        Observable.mergeDelayError(bufferEmit, service).subscribeWith(observer);
    }

    public void stop() {
        if (service == null) return;
        service.onComplete();
        if (buffer != null && buffer.size() > 0) {
            for (IFunction func : buffer) {
                func.release();
            }
            buffer.clear();
        }
        service = null;
    }
}
