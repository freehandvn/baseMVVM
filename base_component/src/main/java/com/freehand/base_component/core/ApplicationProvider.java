package com.freehand.base_component.core;

import android.app.Application;

/**
 * Created by minhpham on 12/12/18.
 * Purpose: .
 * Copyright © 2018 Pham Duy Minh. All rights reserved.
 */
public class ApplicationProvider {
    private static BaseApplication application;

    public static void init(BaseApplication app) {
        application = app;
    }

    public static BaseApplication application() {
        return application;
    }
}
