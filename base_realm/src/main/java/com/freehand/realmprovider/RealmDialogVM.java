package com.freehand.realmprovider;

import com.freehand.base_component.core.dialog.DialogVM;
import com.freehand.base_component.core.inteface.IViewModel;

import io.realm.Realm;

/**
 * Created by minhpham on 12/16/18.
 * Purpose: .
 * Copyright © 2018 Pham Duy Minh. All rights reserved.
 */
public abstract class RealmDialogVM<O> extends DialogVM<O> {
    private Realm mRealm;

    public Realm getRealm() {
        if (mRealm == null) {
            mRealm = RealmUtility.realmProvider.getRealmOnMain();
            mRealm.setAutoRefresh(true);
        }
        return mRealm;
    }
}
