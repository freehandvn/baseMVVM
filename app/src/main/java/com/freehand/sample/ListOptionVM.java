package com.freehand.sample;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.freehand.base_component.core.activity.BaseActivity;
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
        Context context = v.getContext();
        if(context instanceof BaseActivity){
            ((BaseActivity) context).pushFragment(new FrgOffline(),true);
        }
    }

    public void onFetcher(View v) {
        Context context = v.getContext();
        if(context instanceof BaseActivity){
            ((BaseActivity) context).pushFragment(new FrgFetcher(),true);
        }
    }

    public void onDialog(View v){
        SampleDialog dialog = new SampleDialog();
        dialog.show().subscribeWith(new SafeObserver<String>() {
            /**
             * @param value
             * @return true: dispose subscribe,false keep subscribe
             */
            @Override
            public boolean accept(String value) {
                Log.d("minh", "accept: "+value);
                return true;
            }
        });
    }
}
