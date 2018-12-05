package com.freehand.base_component.stateful;

import android.databinding.BindingAdapter;
import android.view.View;

import com.freehand.base_component.core.view_model.BaseViewModel;
import com.freehand.base_component.core.view_model.IViewModel;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.ReplaySubject;

/**
 * Created by minhpham on 8/14/17.
 * Purpose:
 * Copyright © 2017 Pham Duy Minh. All rights reserved.
 */
public abstract class BaseStateVM extends BaseViewModel implements IStateVM {
    private BehaviorSubject<Integer> showState;
    private BehaviorSubject<Integer> hideState;
    private ReplaySubject<Object[]> bindingChannel;

    public BaseStateVM(IViewModel... models) {
        super(models);
        showState = BehaviorSubject.create();
        hideState = BehaviorSubject.create();
        bindingChannel = ReplaySubject.create();
    }

    public BaseStateVM() {
        super();
        showState = BehaviorSubject.create();
        hideState = BehaviorSubject.create();
        bindingChannel = ReplaySubject.create();
    }

    @Override
    public Observable<Integer> showStateObservable() {
        return showState;
    }

    @Override
    public Observable<Integer> hideStateObservable() {
        return hideState;
    }

    @Override
    public Observable<Object[]> bindingObservable() {
        return bindingChannel;
    }

    @Override
    public void addState(int state) {
        if (showState != null)
            showState.onNext(state);
    }

    @Override
    public void removeState(int state) {
        if (hideState != null)
            hideState.onNext(state);
    }

    @Override
    public void resetState() {
        int[] states = getAllState();
        if (states == null || states.length == 0 || hideState == null) return;
        for (int state : states) {
            hideState.onNext(state);
        }
    }

    protected abstract int[] getAllState();

    @Override
    public final void addBinding(BaseViewModel vm, int layout, int variable, Integer parent, int state, boolean needShow) {
        viewModels.add(vm);
        bindingChannel.onNext(new Object[]{vm, layout, variable, parent, state, needShow});
    }

    public void addBinding(BaseViewModel vm, int layout, int variableID, Integer parent, boolean needShow) {
        if (vm == null) return;
        viewModels.add(vm);
        bindingChannel.onNext(new Object[]{vm, layout, variableID, parent, variableID, needShow});
    }

    public void addBinding(BaseViewModel vm, Integer parent, int layout, int varID, int state, boolean needShow) {
        if (vm == null) return;
        viewModels.add(vm);
        bindingChannel.onNext(new Object[]{vm, layout, varID, parent, state, needShow});
    }

    @Override
    public void destroy() {
        super.destroy();
        if (showState != null && !showState.hasComplete()) {
            showState.onComplete();
        }
        if (hideState != null && !hideState.hasComplete()) {
            hideState.onComplete();
        }
        if (bindingChannel != null && !bindingChannel.hasComplete()) {
            bindingChannel.onComplete();
        }
        bindingChannel = null;
        showState = null;
        hideState = null;
    }
}
