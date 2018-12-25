package com.freehand.realmprovider;

import com.freehand.base_component.core.view_model.BaseViewModel;
import com.freehand.base_component.core.inteface.IViewModel;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.realm.ObjectChangeSet;
import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.RealmObjectChangeListener;
import io.realm.RealmResults;

/**
 * Created by minhpham on 8/9/17.
 * Purpose:
 * Copyright © 2017 Pham Duy Minh. All rights reserved.
 */
public abstract class RealmVM extends BaseViewModel implements IRealmVM{
    private Realm mRealm;
    private Map<Object,IRealmListener> listenerMap;


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
        if(listenerMap!=null && listenerMap.size() >0){
            Collection<IRealmListener> listeners = listenerMap.values();
            for(IRealmListener listener : listeners){
                listener.destroy();
            }
            listenerMap.clear();
            listenerMap = null;
        }
    }

    @Override
    public Realm getRealm() {
        if (mRealm == null) {
            mRealm = RealmUtility.realmProvider.getRealmOnMain();
            mRealm.setAutoRefresh(true);
        }
        return mRealm;
    }

    public <T extends RealmObject> Observable<ObjectChangeSet> getChangeListener(T realmObject){
        IRealmListener<ObjectChangeSet> listener = getListener(realmObject);
        if (listener == null) return Observable.empty();
        return listener.getOuput();
    }

    public <T extends RealmObject> Observable<OrderedCollectionChangeSet> getChangeListener(RealmResults<T> realmResult){
        IRealmListener listener = getListener(realmResult);
        return listener.getOuput();
    }

    public <T extends RealmObject> void removeListener(T realmObject){
        if(listenerMap == null || !listenerMap.containsKey(realmObject)) return;
        listenerMap.get(realmObject).destroy();
        listenerMap.remove(realmObject);
    }

    public <T extends RealmObject> void removeListener(RealmResults<T> realmResult){
        if(listenerMap == null || !listenerMap.containsKey(realmResult)) return;
        listenerMap.get(realmResult).destroy();
        listenerMap.remove(realmResult);
    }

    private IRealmListener getListener(Object object) {
        if(listenerMap == null){
            listenerMap = new HashMap<>();
        }
        if(listenerMap.containsKey(object)) return listenerMap.get(object);
        IRealmListener listener = null;
        if(object instanceof RealmObject){
            listener = new RealmObjectListener((RealmObject) object);
        }else if (listener instanceof RealmResults){
            listener = new RealmResultListener((RealmResults) object);
        }else {
            return null;
        }
        listenerMap.put(object,listener);
        return listener;
    }


}
