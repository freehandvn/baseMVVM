package com.freehand.sample;

import com.freehand.base_component.core.activity.BaseActivity;
import com.freehand.base_component.core.navigate.NavigateManager;
import com.freehand.base_component.core.navigate.Navigator;

public class MainActivity extends BaseActivity {

    @Override
    protected int getFragmentContainerResId() {
        return R.id.fm_container;
    }

    @Override
    protected void initView() {
        NavigateManager.navigate(Navigator.make().fragment(FrgOption.class).enableBack());
    }

    @Override
    protected int defineLayout() {
        return R.layout.activity_main;
    }
}
