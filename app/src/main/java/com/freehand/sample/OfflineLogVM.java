package com.freehand.sample;

import android.databinding.Bindable;

import com.freehand.realmprovider.RealmUtility;
import com.freehand.realmprovider.RealmVM;
import com.freehand.sample.db.Doom;

import io.realm.RealmResults;

/**
 * Created by minhpham on 12/3/18.
 * Purpose: .
 * Copyright © 2018 Pham Duy Minh. All rights reserved.
 */
public class OfflineLogVM extends RealmVM {

    private RealmResults<Doom> data;

    @Override
    public void init() {
        super.init();
        data = RealmUtility.findAllBinding(getRealm(), Doom.class, null);
        notifyPropertyChanged(BR.log);
    }

    @Override
    public int defineLayoutDefault() {
        return R.layout.frg_log;
    }

    @Override
    public int defineVariableID() {
        return BR.vm;
    }

    @Bindable
    public String getLog() {
        if(data.size() == 0) return "empty";
        StringBuilder result = new StringBuilder();
        for(Doom temp : data){
            result.append(temp.name).append("::");
        }
        return result.toString();
    }
}
