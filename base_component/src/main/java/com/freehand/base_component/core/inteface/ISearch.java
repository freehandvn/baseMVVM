package com.freehand.base_component.core.inteface;

import android.widget.EditText;

import io.reactivex.Observable;

/**
 * Created by minhpham on 8/11/17.
 * Purpose:
 * Copyright © 2017 Pham Duy Minh. All rights reserved.
 */
public interface ISearch {
    /**
     * key search will return on search channel
     * @param searchChannel
     */
    void observerSearch(EditText editText, Observable<String> searchChannel);
}
