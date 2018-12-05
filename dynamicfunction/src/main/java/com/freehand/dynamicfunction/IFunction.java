package com.freehand.dynamicfunction;

import io.reactivex.Observable;

/**
 * Created by minhpham on 4/25/17.
 * Copyright © 2017 Pham Duy Minh. All rights reserved.
 */

public interface IFunction<I, O> {
    /**
     * @return name if function
     */
    String getName();

    /**
     * execute input and notify output
     *
     * @param input
     */
    void execute(I input);

    /**
     * @return
     */
    Observable<O> getOutput();

    /**
     * close channel, release all resource
     */
    void release();
}
