package com.freehand.dynamicfunction;

/**
 * Created by minhpham on 4/28/17.
 * Purpose: define Subscriber use in base function and DynamicFunctionService
 * Copyright © 2017 Pham Duy Minh. All rights reserved.
 */

public interface ISubscriber<T> {
    /**
     * @param value
     * @return true: dispose subscribe,false keep subscribe
     */
    boolean accept(T value);
}
