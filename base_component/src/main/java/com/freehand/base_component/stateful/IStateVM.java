package com.freehand.base_component.stateful;

import com.freehand.base_component.core.view_model.BaseViewModel;

import io.reactivex.Observable;

/**
 * Created by minhpham on 8/14/17.
 * Purpose:
 * Copyright © 2017 Pham Duy Minh. All rights reserved.
 */
public interface IStateVM {
    /**
     * @return handle show state Observable
     */
    Observable<Integer> showStateObservable();

    /**
     * @return handle hide state Observable
     */
    Observable<Integer> hideStateObservable();

    Observable<Object[]> bindingObservable();

    void addBinding(BaseViewModel vm, int layout, int variable, Integer parent, int state, boolean needShow);

    /**
     * show state
     *
     * @param state
     */
    void addState(int state);

    /**
     * hide state
     *
     * @param state
     */
    void removeState(int state);

    /**
     * hide all
     */
    void resetState();
}
