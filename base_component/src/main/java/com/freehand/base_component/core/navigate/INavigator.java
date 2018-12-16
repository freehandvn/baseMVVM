package com.freehand.base_component.core.navigate;

import android.support.v4.app.FragmentTransaction;

import com.freehand.base_component.core.fragment.BaseFragment;

/**
 * Created by minhpham on 12/16/18.
 * Purpose: define navigator which contain information for navigate to another fragment
 * Copyright © 2018 Pham Duy Minh. All rights reserved.
 */
public interface INavigator {

    BaseFragment getFragment();

    Navigate.Strategy getStrategy();

    boolean shouldAddToBackStack();

    void setCustomAnimation(FragmentTransaction ft);
}
