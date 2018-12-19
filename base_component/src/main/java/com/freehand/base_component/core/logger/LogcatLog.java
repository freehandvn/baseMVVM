package com.freehand.base_component.core.logger;

import android.util.Log;

/**
 * Created by minhpham on 12/19/18.
 * Purpose: .
 * Copyright © 2018 Pham Duy Minh. All rights reserved.
 */
public class LogcatLog implements ILog {
    /**
     * d normal message
     *
     * @param tag
     * @param msg
     */
    @Override
    public void d(String tag, String msg) {
        Log.d(tag,msg);
    }

    /**
     * d Exception
     *
     * @param tag
     * @param throwable
     */
    @Override
    public void e(String tag, Throwable throwable) {
        if(throwable == null) return;
        Log.e(tag,throwable.getMessage());
        throwable.printStackTrace();
    }

    /**
     * thread trace
     *
     * @param tag
     */
    @Override
    public void trace(String tag) {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        if(trace.length>=4){
            Log.d(tag, "  \n============Begin trace=============\n"
                    + buildStackTraceString(trace)
                    + "============End trace============="
            );
        }
    }

    /**
     * export all log
     */
    @Override
    public void export() {

    }

    private static String buildStackTraceString(final StackTraceElement[] elements) {
        StringBuilder sb = new StringBuilder();
        if (elements != null && elements.length > 0) {
            int n = Math.min(7,elements.length);
            for (int i = 3;i<n;i++) {
                sb.append(elements[i].toString()).append("\n");
            }
        }
        return sb.toString();
    }
}
