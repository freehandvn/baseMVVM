package com.freehand.base_component.stateful;

import com.freehand.base_component.core.view_model.BaseViewModel;

import io.reactivex.Observable;

/**
 * Created by minhpham on 8/14/17.
 * Purpose:
 * Copyright © 2017 Pham Duy Minh. All rights reserved.
 */
public interface IStatefulView {

    /**
     * init a state, with id is @{state} and viewModel
     *
     * @param vm
     * @param state
     */
    IStatefulView addState(BaseViewModel vm, int layoutSrc, int variableID, Integer parent, int state, boolean needshow);

    /**
     * add viewModel and layout , statefull view will binding them and show
     *
     * @param vm
     * @param layoutSrc
     * @param variableID
     *
     * @return
     */
    IStatefulView addState(BaseViewModel vm, int layoutSrc, int variableID, Integer parent);

    /**
     * remove a state view by state id
     *
     * @param state
     */
    IStatefulView removeState(int state);

    /**
     * show a state view by id
     *
     * @param state
     */
    IStatefulView showState(int state);

    /**
     * hide a state by id
     *
     * @param state
     */
    IStatefulView hideState(int state);

    /**
     * reset all state visible, default hide
     */
    IStatefulView reset();

    /**
     * show state channel
     *
     * @param state
     *
     * @return
     */
    IStatefulView showState(Observable<Integer> state);

    /**
     * hide state channel
     *
     * @param state
     *
     * @return
     */
    IStatefulView hideState(Observable<Integer> state);

    IStatefulView setBinding(Observable<Object[]> binding);

    /**
     * @param vm
     *
     * @return
     */
    IStatefulView addStateVM(IStateVM vm);

    /**
     * release resource
     */
    void destroy();

}
