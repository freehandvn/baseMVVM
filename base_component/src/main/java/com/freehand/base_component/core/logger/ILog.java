package com.freehand.base_component.core.logger;

/**
 * Created by minhpham on 12/19/18.
 * Purpose: .
 * Copyright © 2018 Pham Duy Minh. All rights reserved.
 */
public interface ILog {
    /**
     * d normal message
     * @param tag
     * @param msg
     */
    void d(String tag, String msg);

    /**
     * d Exception
     * @param tag
     * @param throwable
     */
    void e(String tag, Throwable throwable);

    /**
     * thread trace
     * @param tag
     */
    void trace(String tag);
}
