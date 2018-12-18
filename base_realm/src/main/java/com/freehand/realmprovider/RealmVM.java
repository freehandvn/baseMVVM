package com.freehand.realmprovider;

import com.freehand.base_component.core.view_model.BaseViewModel;
import com.freehand.base_component.core.inteface.IViewModel;

import io.realm.Realm;

/**
 * Created by minhpham on 8/9/17.
 * Purpose:
 * Copyright © 2017 Pham Duy Minh. All rights reserved.
 */
public abstract class RealmVM extends BaseViewModel implements IRealmVM{
    private Realm mRealm;

    public RealmVM(IViewModel... models) {
        super(models);
    }

    public RealmVM() {
        super();
    }

    @Override
    public void destroy() {
        super.destroy();
//        if (mRealm != null) {
//            mRealm.close();
//        }
    }

    @Override
    public Realm getRealm() {
        if (mRealm == null) {
            mRealm = RealmUtility.realmProvider.getRealmOnMain();
            mRealm.setAutoRefresh(true);
        }
        return mRealm;
    }
}
