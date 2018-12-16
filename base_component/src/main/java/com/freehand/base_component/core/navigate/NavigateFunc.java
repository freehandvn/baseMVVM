package com.freehand.base_component.core.navigate;

import com.freehand.dynamicfunction.IFunction;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by minhpham on 12/16/18.
 * Purpose: .
 * Copyright © 2018 Pham Duy Minh. All rights reserved.
 */
class NavigateFunc implements IFunction<INavigator,INavigator> {
    private static final String TAG = NavigateFunc.class.getName();
    private PublishSubject<INavigator> channel = PublishSubject.create();
    /**
     * @return name if function
     */
    @Override
    public String getName() {
        return TAG;
    }

    /**
     * execute input and notify output
     *
     * @param input
     */
    @Override
    public void execute(INavigator input) {
        channel.onNext(input);
    }

    /**
     * @return
     */
    @Override
    public Observable<INavigator> getOutput() {
        return channel;
    }

    /**
     * close channel, release all resource
     */
    @Override
    public void release() {
        channel.onComplete();
        channel = null;
    }
}
