package com.freehand.sample;

import com.freehand.base_component.core.activity.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected int getFragmentContainerResId() {
        return R.id.fm_container;
    }

    @Override
    protected void initView() {
        pushFragment(new FrgOption(), true);
    }

    @Override
    protected int defineLayout() {
        return R.layout.activity_main;
    }
}
