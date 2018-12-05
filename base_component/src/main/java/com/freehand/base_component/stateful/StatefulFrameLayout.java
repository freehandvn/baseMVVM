package com.freehand.base_component.stateful;

import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.freehand.base_component.core.view_model.BaseViewModel;

import io.reactivex.Observable;

/**
 * Created by minhpham on 8/14/17.
 * Purpose:
 * Copyright © 2017 Pham Duy Minh. All rights reserved.
 */
public class StatefulFrameLayout extends FrameLayout implements IStatefulView {

    StatefulCore core = new StatefulCore();

    public StatefulFrameLayout(@NonNull Context context) {
        super(context);
    }

    public StatefulFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StatefulFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public StatefulFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public IStatefulView addState(BaseViewModel vm, int layoutSrc, int variableID, Integer parent, int state, boolean needshow) {
        core.addState(this, vm, layoutSrc, variableID, parent, state, needshow);
        return this;
    }

    @Override
    public IStatefulView addState(BaseViewModel vm, int layoutSrc, int variableID, Integer parent) {
        core.addState(this, vm, layoutSrc, variableID, parent);
        return this;
    }

    @Override
    public IStatefulView removeState(int state) {
        core.removeState(state);
        return this;
    }

    @Override
    public IStatefulView showState(int state) {
        core.showState(state);
        return this;
    }

    @Override
    public IStatefulView hideState(int state) {
        core.hideState(state);
        return this;
    }

    @Override
    public IStatefulView reset() {
        core.reset();
        return this;
    }

    @Override
    public IStatefulView showState(Observable<Integer> observable) {
        core.showState(observable);
        return this;
    }

    @Override
    public IStatefulView hideState(Observable<Integer> observable) {
        core.hideState(observable);
        return this;
    }

    @Override
    public IStatefulView setBinding(Observable<Object[]> binding) {
        core.setBinding(this, binding);
        return this;
    }

    @Override
    public IStatefulView addStateVM(IStateVM vm) {
        core.addStateVM(this, vm);
        return this;
    }

    @Override
    protected void detachAllViewsFromParent() {
        super.detachAllViewsFromParent();
        destroy();
    }

    @Override
    public void destroy() {
        core.destroy();
        core = null;
    }
}
