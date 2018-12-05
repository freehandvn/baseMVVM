package com.freehand.realmprovider;

import com.freehand.base_component.core.BaseApplication;

import io.realm.Realm;

/**
 * Created by minhpham on 11/29/18.
 * Purpose: .
 * Copyright © 2018 Pham Duy Minh. All rights reserved.
 */
public abstract class RealmApplication extends BaseApplication {
    @Override
    public void onCreate() {
        Realm.init(this);
        super.onCreate();

    }
}
