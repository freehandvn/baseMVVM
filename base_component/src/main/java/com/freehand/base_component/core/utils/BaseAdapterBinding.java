package com.freehand.base_component.core.utils;

import android.databinding.BindingAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.freehand.base_component.R;
import com.freehand.base_component.core.inteface.IListVM;
import com.freehand.base_component.core.inteface.ISearch;
import com.freehand.base_component.core.recycle.BaseRecyclerAdapter;
import com.freehand.base_component.stateful.IStateVM;
import com.freehand.base_component.stateful.IStatefulView;

import io.reactivex.Observable;

/**
 * Created by minhpham on 11/25/18.
 * Purpose: define basically extension
 * Copyright © 2018 Pham Duy Minh. All rights reserved.
 */
public class BaseAdapterBinding {

    @BindingAdapter("initSearch")
    public static void initSearch(EditText editText, ISearch search) {
        Observable<String> observer = CodeUtils.createTextChangeObservable(editText);
        search.observerSearch(editText, observer);
    }

    @BindingAdapter("initList")
    public static void initListView(final RecyclerView list, IListVM vm) {
        ViewCompat.setNestedScrollingEnabled(list, false);
        BaseRecyclerAdapter adapter = vm.getAdapter();
        list.setAdapter(adapter);
        if(adapter!=null){
            adapter.setOnItemListener(vm.getOnItemListener());
        }
        list.setItemAnimator(null);
        list.setLayoutManager(vm.getLayoutManager(list.getContext()));
        list.getLayoutManager().setAutoMeasureEnabled(true);
        list.setHasFixedSize(false);
    }

    @BindingAdapter("executeScrollTo")
    public static void executeScrollTo(RecyclerView view, int index) {
        if (index > -1) {
            RecyclerView.LayoutManager lm = view.getLayoutManager();
            if (lm instanceof LinearLayoutManager) {
                LinearLayoutManager line = (LinearLayoutManager) lm;
                int start = line.findFirstVisibleItemPosition();
                int end = line.findLastVisibleItemPosition();
                if (start >= 0 && end >= start && index >= start && index <= end) return;
            }
            view.scrollToPosition(index);
        }
    }

    @BindingAdapter("setStateVM")
    public static void setStateVM(View view, IStateVM vm) {
        if (!(view instanceof IStatefulView)) return;
        IStatefulView statefulView = (IStatefulView) view;
        statefulView.addStateVM(vm);
//        statefulView.showState(vm.showStateObservable());
//        statefulView.hideState(vm.hideStateObservable());
//        statefulView.setBinding(vm.bindingObservable());
    }


    @BindingAdapter("addViewmodel")
    public static void addViewmodel(View view, IStateVM vm) {
        if (!(view instanceof IStatefulView)) return;
        IStatefulView statefulView = (IStatefulView) view;
        statefulView.showState(vm.showStateObservable());
        statefulView.hideState(vm.hideStateObservable());
    }
}
