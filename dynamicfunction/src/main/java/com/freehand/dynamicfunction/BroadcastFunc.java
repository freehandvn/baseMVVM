package com.freehand.dynamicfunction;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by minhpham on 11/27/18.
 * Purpose: .
 * Copyright © 2018 Pham Duy Minh. All rights reserved.
 */
public class BroadcastFunc extends BaseFunction {

    BehaviorSubject<Object> channel;
    public BroadcastFunc(String name) {
        super(name);
        channel = BehaviorSubject.create();
    }

    @Override
    protected Object process(Object input) {
        return input;
    }

    @Override
    protected Observable defineInputChannel() {
        return channel;
    }

    @Override
    protected void destroy() {
        channel.onComplete();
        channel = null;
    }

    /**
     * execute input and notify output
     *
     * @param input
     */
    @Override
    public void execute(Object input) {
        channel.onNext(input);
    }
}
