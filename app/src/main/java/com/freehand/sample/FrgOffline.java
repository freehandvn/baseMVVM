package com.freehand.sample;

import android.support.annotation.NonNull;

import com.freehand.base_component.core.fragment.BaseFragmentBinding;

/**
 * Created by minhpham on 12/3/18.
 * Purpose: .
 * Copyright © 2018 Pham Duy Minh. All rights reserved.
 */
public class FrgOffline extends BaseFragmentBinding<OfflineLogVM> {
    @Override
    protected int defineVariableID() {
        return BR.vm;
    }

    @NonNull
    @Override
    protected OfflineLogVM onCreateViewModel() {
        return new OfflineLogVM();
    }

    @Override
    protected int defineLayout() {
        return R.layout.frg_log;
    }
}
