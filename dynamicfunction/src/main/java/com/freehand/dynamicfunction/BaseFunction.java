package com.freehand.dynamicfunction;

import android.text.TextUtils;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * Created by minhpham on 4/25/17.
 * Purpose: an abstract class implement {@link IFunction}
 * Copyright © 2017 Pham Duy Minh. All rights reserved.
 */

public abstract class BaseFunction<I, O> implements IFunction<I, O> {
    protected String name;
    protected Observable<O> output;

    public BaseFunction(String name) {
        // notify to service this function created
        this.name = TextUtils.isEmpty(name) ? getClass().getName() : name;
        DynamicFunctionService.getInstance().notifyFunctionInit(this);
        output = defineInputChannel().map(new Function<I, O>() {
            @Override
            public O apply(I input) throws Exception {
                return process(input);
            }
        });
    }

    @Override
    public String getName() {
        return name;
    }

    protected abstract O process(I input);

    protected abstract Observable<I> defineInputChannel();

    @Override
    public Observable<O> getOutput() {
        return output;
    }

    protected abstract void destroy();

    @Override
    public void release() {
        destroy();
        output = null;
        //check has release before
        DynamicFunctionService.getInstance().notifyFunctionDestroy(this);

    }

}
