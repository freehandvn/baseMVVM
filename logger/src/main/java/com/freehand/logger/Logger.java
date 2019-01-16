package com.freehand.logger;

/**
 * Created by minhpham on 12/19/18.
 * Purpose: .
 * Copyright © 2018 Pham Duy Minh. All rights reserved.
 */
public class Logger {
    private static ILog defaultLog = new NonLog();

    public static void setDefaultLog(ILog log) {
        defaultLog = log;
    }

    public static ILog log() {
        return defaultLog;
    }
}
