package com.freehand.base_component.core.inteface;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.freehand.base_component.core.recycle.BaseRecyclerAdapter;
import com.freehand.base_component.core.recycle.OnItemListener;

/**
 * Created by minhpham on 11/26/18.
 * Purpose: .
 * Copyright © 2018 Pham Duy Minh. All rights reserved.
 */
public interface IListVM<I,T extends BaseRecyclerAdapter<I,?>> {
    T getAdapter();

    OnItemListener<I> getOnItemListener();

    RecyclerView.LayoutManager getLayoutManager(Context context);
}
