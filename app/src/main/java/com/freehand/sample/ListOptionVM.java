package com.freehand.sample;

import android.content.Context;
import android.view.View;

import com.freehand.base_component.core.activity.BaseActivity;
import com.freehand.realmprovider.RealmVM;

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
}
