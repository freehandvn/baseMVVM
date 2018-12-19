package com.freehand.base_component.core.utils;

import android.support.v4.util.Pair;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by minhpham on 1/2/18.
 * Purpose: util support d time
 * Copyright © 2018 Pham Duy Minh. All rights reserved.
 */
public class TimeLog {
    private static final TimeLog ourInstance = new TimeLog();
    private static final String TAG = "TimeLog";
    private Map<String, Pair<Long, Long>> data;

    private TimeLog() {
        data = new HashMap<>();
    }

    public static TimeLog getLog() {
        return ourInstance;
    }

    public void startTime(String key) {
        startTime(key,"");
    }
    public void startTime(String key, String content) {
        long startTime = System.currentTimeMillis();
        long preCount = 0;
        data.put(key, new Pair<>(startTime, preCount));
        Log.d(TAG, key +" content: "+content+ " start d time at: " + startTime);
    }

    public void logTime(String key){
        logTime(key,"");
    }

    public void logTime(String key, String content) {
        long time = System.currentTimeMillis();
        Pair<Long, Long> temp = data.get(key);
        if(temp==null) return;
        Log.d(TAG, key +" content: "+content+ " d time at: " + time);
        Log.d(TAG, key +" content: "+content+  " time spent: " + (time - temp.first)
                + ((temp.second > 0) ? " after: " + (time - temp.first - temp.second) : ""));
        data.put(key, new Pair<>(temp.first, time - temp.first));
    }


}
