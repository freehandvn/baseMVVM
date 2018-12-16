package com.freehand.base_component.core;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.freehand.base_component.core.utils.DeviceUtils;
import com.freehand.dynamicfunction.DynamicFunctionService;

import java.io.IOException;

import io.reactivex.annotations.NonNull;
import io.reactivex.exceptions.UndeliverableException;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * Created by minhpham on 11/29/18.
 * Purpose: .
 * Copyright © 2018 Pham Duy Minh. All rights reserved.
 */
public abstract class BaseApplication extends Application implements Application.ActivityLifecycleCallbacks {

    private Activity activity;

    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationProvider.init(this);
        DeviceUtils.initialize();
        DynamicFunctionService.getInstance().start();
        initRxJava();
        registerActivityLifecycleCallbacks(this);
    }

    private void initRxJava() {
        RxJavaPlugins.setErrorHandler(e -> {
            e.printStackTrace();
            if (e instanceof UndeliverableException) {
                e = e.getCause();
            }
            if ((e instanceof IOException)) {
                // fine, irrelevant network problem or API that throws on cancellation
                return;
            }
            if (e instanceof InterruptedException) {
                // fine, some blocking code was interrupted by a dispose call
                return;
            }
            if ((e instanceof NullPointerException) || (e instanceof IllegalArgumentException)) {
                // that's likely a bug in the application
                Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                return;
            }
            if (e instanceof IllegalStateException) {
                // that's a bug in RxJava or in a custom operator
                Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                return;
            }
            Log.w("Undeliverable exception", e);
        });
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        DynamicFunctionService.getInstance().stop();
        unregisterActivityLifecycleCallbacks(this);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    public Activity getActivity() {
        return activity;
    }
}
