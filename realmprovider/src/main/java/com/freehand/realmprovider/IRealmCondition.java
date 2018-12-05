package com.freehand.realmprovider;

import io.realm.RealmObject;
import io.realm.RealmQuery;

/**
 * Created by minhpham on 5/15/17.
 * Purpose: export query to custom
 * Copyright © 2017 Pham Duy Minh. All rights reserved.
 */

public interface IRealmCondition<T extends RealmObject> {
    RealmQuery<T> condition(RealmQuery<T> query);
}
