package com.freehand.base_component.core.view_model;

import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.BaseObservable;
import android.util.Log;

import com.freehand.base_component.core.inteface.IViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.disposables.Disposable;

/**
 * Created by minhpham on 4/25/17.
 * Purpose: base view model
 * Copyright © 2017 Pham Duy Minh. All rights reserved.
 */

public abstract class BaseViewModel extends BaseObservable implements IViewModel {

    protected final List<IViewModel> viewModels;
    private final String TAG = BaseViewModel.class.getSimpleName();
    protected boolean isDestroy = false;
    private Set<Disposable> disposables;
    private boolean isPause = false;
    private int layoutID;
    private int variableID;

    public BaseViewModel() {
        viewModels = new ArrayList<>();
        layoutID = defineLayoutDefault();
        variableID = defineVariableID();
    }

    @Override
    public List<IViewModel> getChildViewModel() {
        return viewModels;
    }

    @Override
    public void destroy() {
        Log.e(TAG, "destroy vm: " + getClass().getName());
        isDestroy = true;

        if (viewModels != null && viewModels.size() > 0) {
            for (IViewModel vm : viewModels) {
                vm.destroy();
            }
            viewModels.clear();
        }
        if (disposables != null) {
            for (Disposable disposable : disposables) {
                disposable.dispose();
            }
            disposables.clear();
            disposables = null;
        }
    }

    @Override
    public boolean isDestroy() {
        return isDestroy;
    }

    public <T extends BaseViewModel> T getViewModelByClass(Class<T> clazz) {
        if (clazz == null) return null;
        for (IViewModel temp : viewModels) {
            if (clazz.getSimpleName().equals(temp.getClass().getSimpleName()) || clazz.isAssignableFrom(temp.getClass()))
                return (T) temp;
        }
        return null;
    }

    public void addChildViewModel(IViewModel... viewModels) {
        if (viewModels == null || viewModels.length == 0) return;
        for (IViewModel vm : viewModels) {
            if (vm == null) continue;
            this.viewModels.add(vm);
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        if (viewModels == null || viewModels.size() == 0) return;
        for (IViewModel vm : viewModels) {
            try {
                vm.onConfigurationChanged(newConfig);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void addDisposable(Disposable disposable) {
        if (disposables == null) disposables = new HashSet<>();
        disposables.add(disposable);
    }

    public void init() {
        if (viewModels == null || viewModels.size() == 0) return;
        for (IViewModel vm : viewModels) {
            try {
                vm.init();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (viewModels == null || viewModels.size() == 0) return;
        for (IViewModel vm : viewModels) {
            try {
                vm.onActivityResult(requestCode, resultCode, data);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean isPause() {
        return isPause;
    }

    @Override
    public void pause() {
        isPause = true;
        if (viewModels == null || viewModels.size() == 0) return;
        for (IViewModel vm : viewModels) {
            try {
                vm.pause();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void unpause() {
        isPause = false;
        if (viewModels == null || viewModels.size() == 0) return;
        for (IViewModel vm : viewModels) {
            try {
                vm.unpause();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public int getLayoutID() {
        return layoutID;
    }

    public void setLayoutID(int layoutID) {
        this.layoutID = layoutID;
    }

    public abstract int defineLayoutDefault();

    public abstract int defineVariableID();

    public boolean handleBackPress() {
        return false;
    }

    public int getVariableID() {
        return variableID;
    }

    public void setVariableID(int variableID) {
        this.variableID = variableID;
    }
}
