package com.freehand.sample;

import android.os.Handler;
import android.util.Log;

import com.freehand.base_component.core.activity.BaseActivity;
import com.freehand.base_component.core.navigate.NavigateManager;
import com.freehand.base_component.core.navigate.Navigator;
import com.freehand.logger.Logger;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

public class MainActivity extends BaseActivity {

    private int count = 0;
    PublishSubject<Object> observable;

    @Override
    protected int getFragmentContainerResId() {
        return R.id.fm_container;
    }

    @Override
    protected void initView() {
        Logger.log().trace("MainActivity");
        NavigateManager.navigate(Navigator.make().fragment(FrgOption.class).enableBack());
        Logger.log().d("MainActivity", "debug");
        observable = PublishSubject.create();
        Disposable dis1 = getOut().subscribe();
        Disposable dis2 = getOut().subscribe();
        Disposable dis3 = getOut().subscribe();
        dis1.dispose();
        dis2.dispose();
        new Handler().postDelayed(() -> dis3.dispose(), 2000);
    }


    private Observable getOut() {
        return observable
                .doOnSubscribe(disposable -> count++)
                .doOnDispose(() -> {
                    count--;
                    if (count <= 0) {
                        Log.d("minh", "testOb: come " +count);
                    }
                }).cache();
    }

    @Override
    protected int defineLayout() {
        return R.layout.activity_main;
    }
}
