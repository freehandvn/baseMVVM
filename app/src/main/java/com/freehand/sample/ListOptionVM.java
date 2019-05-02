package com.freehand.sample;

import android.util.Log;
import android.view.View;

import com.freehand.base_component.core.navigate.NavigateManager;
import com.freehand.base_component.core.navigate.Navigator;
import com.freehand.dynamicfunction.SafeObserver;
import com.freehand.realmprovider.RealmVM;
import com.freehand.sample.dialog.SampleDialog;
import com.freehand.sample.fetcher.FrgFetcher;

/**
 * Created by minhpham on 12/3/18.
 * Purpose: .
 * Copyright © 2018 Pham Duy Minh. All rights reserved.
 */
public class ListOptionVM extends RealmVM {

    public void onOffline(View v) {
        NavigateManager.navigate(Navigator.make().fragment(FrgOffline.class).enableBack());
    }

    public void onFetcher(View v) {
        NavigateManager.navigate(Navigator.make().fragment(FrgFetcher.class).enableBack());
    }

    public void onDialog(View v) {
        SampleDialog dialog = new SampleDialog();
        dialog.show().subscribeWith(new SafeObserver<String>() {
            /**
             * @param value
             * @return true: dispose subscribe,false keep subscribe
             */
            @Override
            public boolean accept(String value) {
                Log.d("minh", "accept: " + value);
                return true;
            }
        });
    }

    @Override
    public int defineLayoutDefault() {
        return R.layout.frg_list_option;
    }

    @Override
    public int defineVariableID() {
        return BR.vm;
    }
}
