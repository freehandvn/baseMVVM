package com.freehand.realmprovider;

import io.realm.Realm;

/**
 * Created by minhpham on 5/15/17.
 * Purpose: define provide realm
 * Copyright © 2017 Pham Duy Minh. All rights reserved.
 */

public interface IRealmProvider {
    //general get realm
    Realm getRealm();
    // return realm on main thread
    Realm getRealmOnMain();
    //release resource
    void release();
}
