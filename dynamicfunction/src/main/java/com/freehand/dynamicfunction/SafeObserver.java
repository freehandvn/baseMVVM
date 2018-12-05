package com.freehand.dynamicfunction;

/**
 * Created by minhpham on 9/8/17.
 * Purpose:
 * Copyright © 2017 Pham Duy Minh. All rights reserved.
 */
public abstract class SafeObserver<T> extends BaseDisposableObserver<T> implements ISubscriber<T> {
    public SafeObserver() {
        super(null);
        setSubscriber(this);
    }
}
