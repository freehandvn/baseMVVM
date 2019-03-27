package com.freehand.sample

import com.freehand.base_component.core.activity.BaseActivity
import com.freehand.base_component.core.navigate.NavigateManager
import com.freehand.base_component.core.navigate.Navigator
import com.freehand.logger.trace

class MainActivity : BaseActivity() {

    private var count = 0

    override fun getFragmentContainerResId(): Int {
        return R.id.fm_container
    }

    override fun initView() {
        trace("minh")
        NavigateManager.navigate(Navigator.make().fragment(FrgOption::class.java).enableBack())
    }

    override fun defineLayout(): Int {
        return R.layout.activity_main
    }
}
