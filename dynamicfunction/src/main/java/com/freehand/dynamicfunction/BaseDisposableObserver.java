package com.freehand.dynamicfunction;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by minhpham on 4/28/17.
 * Purpose: .
 * Copyright © 2017 Pham Duy Minh. All rights reserved.
 */

public class BaseDisposableObserver<T> extends DisposableObserver<T> {

    private ISubscriber subscriber;
    private OnDispose disposeHandle;

    public BaseDisposableObserver(ISubscriber<T> subscriber) {
        this.subscriber = subscriber;
    }

    protected void setSubscriber(ISubscriber subscriber) {
        this.subscriber = subscriber;
    }

    public void setOnDispose(OnDispose dispose) {
        this.disposeHandle = dispose;
    }

    /**
     * Provides the Observer with a new item to observe.
     * <p>
     * The {@link Observable} may call this method 0 or more times.
     * <p>
     * The {@code Observable} will not call this method again after it calls either {@link
     * #onComplete} or {@link #onError}.
     *
     * @param function the item emitted by the Observable
     */
    @Override
    public void onNext(@NonNull T function) {
        if (subscriber == null) return;
        if (subscriber.accept(function)) {
            dispose();
            if (disposeHandle != null) {
                disposeHandle.dispose();
            }
        }
    }

    /**
     * Notifies the Observer that the {@link Observable} has experienced an error condition.
     * <p>
     * If the {@link Observable} calls this method, it will not thereafter call {@link #onNext} or
     * {@link #onComplete}.
     *
     * @param e the exception encountered by the Observable
     */
    @Override
    public void onError(@NonNull Throwable e) {

    }

    /**
     * Notifies the Observer that the {@link Observable} has finished sending push-based
     * notifications.
     * <p>
     * The {@link Observable} will not call this method if it calls {@link #onError}.
     */
    @Override
    public void onComplete() {

    }

    public interface OnDispose {
        void dispose();
    }

}
