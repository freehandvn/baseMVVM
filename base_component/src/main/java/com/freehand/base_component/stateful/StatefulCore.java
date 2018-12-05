package com.freehand.base_component.stateful;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.freehand.base_component.core.view_model.BaseViewModel;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * Created by minhpham on 11/27/18.
 * Purpose: .
 * Copyright © 2018 Pham Duy Minh. All rights reserved.
 */
public class StatefulCore {

    private Map<Integer, Pair<ViewDataBinding, ViewGroup>> states = new HashMap<>();


    public void addState(ViewGroup group, BaseViewModel vm, int layoutSrc, int variableID, Integer parent, int state, boolean needshow) {
        // inflate view base on vm
        LayoutInflater inflater = LayoutInflater.from(group.getContext());
        ViewGroup viewParent;
        if (parent == null) {
            viewParent = group;
        } else {
            viewParent = group.findViewById(parent);
            if (viewParent == null) viewParent = group;
        }
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, layoutSrc, viewParent, true);
        // binding view and viewModel
        binding.setVariable(variableID, vm);
        binding.getRoot().setVisibility(needshow ? View.VISIBLE : View.GONE);
        // save state and view to handle
        states.put(state, new Pair<>(binding, viewParent));
    }

    public void addState(ViewGroup group,BaseViewModel vm, int layoutSrc, int variableID, Integer parent) {
        //inflate view base on vm
        LayoutInflater inflater = LayoutInflater.from(group.getContext());
        ViewGroup viewParent;
        if (parent == null) {
            viewParent = group;
        } else {
            viewParent = group.findViewById(parent);
            if (viewParent == null) viewParent = group;
        }
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, layoutSrc, viewParent, true);
        //binding view and viewModel
        binding.setVariable(variableID, vm);
        binding.getRoot().setVisibility(View.VISIBLE);
        //save state and view to handle
        int state = binding.getRoot().getId();
        states.put(state, new Pair(binding, viewParent));
    }

    public void removeState(int state) {
        if (!states.containsKey(state)) return ;
        states.get(state).second.removeView(states.get(state).first.getRoot());
    }

    public void showState(int state) {
        if (!states.containsKey(state)) return;
        View view = states.get(state).first.getRoot();
        // visible view and bring it to front
        view.setVisibility(View.VISIBLE);
        states.get(state).second.bringChildToFront(view);
    }

    public void hideState(int state) {
        if (!states.containsKey(state)) return;
        View view = states.get(state).first.getRoot();
        view.setVisibility(View.GONE);
    }

    public void reset() {
        if (states.size() == 0) return;
        for (Pair<ViewDataBinding, ViewGroup> binding : states.values()) {
            View temp = binding.first.getRoot();
            if (temp.getVisibility() != View.GONE) {
                temp.setVisibility(View.GONE);
            }
        }
        return ;
    }

    public void showState(Observable<Integer> observable) {
        if (observable == null) return ;
        observable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                showState(integer);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                throwable.printStackTrace();
            }
        });
    }

    public void hideState(Observable<Integer> observable) {
        if (observable == null) return;
        observable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                hideState(integer);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                throwable.printStackTrace();
            }
        });
    }

    public void setBinding(final ViewGroup group, Observable<Object[]> binding) {
        if (binding == null) return ;
        binding.observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<Object[]>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Object[] objects) throws Exception {
                        addState(group,(BaseViewModel) objects[0], (int) objects[1], (int) objects[2], (Integer) objects[3], (int) objects[4], (boolean) objects[5]);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    public void addStateVM(ViewGroup group,IStateVM vm) {
        if (vm == null) return;
        showState(vm.showStateObservable());
        hideState(vm.hideStateObservable());
        setBinding(group,vm.bindingObservable());
    }

    public void destroy(){
        states.clear();
        states = null;
    }
}
