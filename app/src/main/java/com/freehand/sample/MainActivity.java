package com.freehand.sample;

import com.freehand.base_component.core.activity.BaseActivity;
import com.freehand.base_component.core.logger.Logger;
import com.freehand.base_component.core.navigate.NavigateManager;
import com.freehand.base_component.core.navigate.Navigator;

public class MainActivity extends BaseActivity {

    @Override
    protected int getFragmentContainerResId() {
        return R.id.fm_container;
    }

    @Override
    protected void initView() {
        Logger.log().trace("MainActivity");
        NavigateManager.navigate(Navigator.make().fragment(FrgOption.class).enableBack());
        Logger.log().d("MainActivity","debug");
    }

    @Override
    protected int defineLayout() {
        return R.layout.activity_main;
    }
}
