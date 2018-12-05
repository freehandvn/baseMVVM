package com.freehand.base_component.core.view_model;

import android.content.Intent;
import android.content.res.Configuration;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by minhpham on 4/25/17.
 * Purpose: define view model interface
 * Copyright © 2017 Pham Duy Minh. All rights reserved.
 */

public interface IViewModel extends IPausable {

    /**
     * @return view models
     */
    List<IViewModel> getChildViewModel();

    /**
     * destroy viewmodel , release all resource
     */
    void destroy();

    /**
     * call when activity or fragment init view
     */
    void init();

    /**
     *
     * @return true if viewmodel destroy
     */
    boolean isDestroy();

    /**
     * call when activity or fragment trigger
     * @param newConfig
     */
    void onConfigurationChanged(Configuration newConfig);

    /**
     * call when activity or fragment trigger
     * @param requestCode
     * @param resultCode
     * @param data
     */
    void onActivityResult(int requestCode, int resultCode, Intent data);
}
