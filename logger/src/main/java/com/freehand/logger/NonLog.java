package com.freehand.logger;

/**
 * Created by minhpham on 12/19/18.
 * Purpose: don't do any thing
 * Copyright © 2018 Pham Duy Minh. All rights reserved.
 */
public class NonLog implements ILog {
    /**
     * d normal message
     *
     * @param tag
     * @param msg
     */
    @Override
    public void d(String tag, String msg) {

    }

    /**
     * d Exception
     *
     * @param tag
     * @param throwable
     */
    @Override
    public void e(String tag, Throwable throwable) {

    }

    /**
     * thread trace
     *
     * @param tag
     */
    @Override
    public void trace(String tag) {

    }

    /**
     * export all log
     */
    @Override
    public void export() {

    }
}
