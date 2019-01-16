package com.freehand.sample;

import com.freehand.base_component.core.activity.BaseActivity;
import com.freehand.base_component.core.navigate.NavigateManager;
import com.freehand.base_component.core.navigate.Navigator;
import com.freehand.logger.Logger;

import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class MainActivity extends BaseActivity {

    private int count = -1;

    @Override
    protected int getFragmentContainerResId() {
        return R.id.fm_container;
    }

    @Override
    protected void initView() {
        Logger.log().trace("MainActivity");
        NavigateManager.navigate(Navigator.make().fragment(FrgOption.class).enableBack());
        Logger.log().d("MainActivity", "debug");
        testOb();
    }


    private void testOb() {
        PublishSubject<Object> observable = PublishSubject.create();
        observable.observeOn(Schedulers.computation())
//                .toFlowable(BackpressureStrategy.LATEST)
                .subscribe(i -> Logger.log().d("minh", ": " + i)
                        , throwable -> Logger.log().e("error", throwable));

        for (int i = 0; i < 1000000; i++) {
            observable.onNext(i);
        }
    }

    @Override
    protected int defineLayout() {
        return R.layout.activity_main;
    }
}
