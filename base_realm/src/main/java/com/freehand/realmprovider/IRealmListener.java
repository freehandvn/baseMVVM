package com.freehand.realmprovider;

import io.reactivex.Observable;

/**
 * Created by minhpham on 12/24/18.
 * Purpose: .
 * Copyright © 2018 Pham Duy Minh. All rights reserved.
 */
public interface IRealmListener<O> {
    Observable<O> getOuput();

    void destroy();

    void pause();

    void unpause();
}
