package com.freehand.realmprovider;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.exceptions.RealmMigrationNeededException;

/**
 * Created by minhpham on 5/26/17. Purpose: . Copyright © 2017 Pham Duy Minh. All rights reserved.
 */

public class RealmProvider implements IRealmProvider {
    private static final String DIR_NAME = "realm_data";
    private final RealmConfiguration config;
    private Realm realmOnMain = null;

    public RealmProvider(Context context , String name) {
        config = new RealmConfiguration.Builder()
                .directory(context.getDir(DIR_NAME, Context.MODE_PRIVATE))
                .name(name)
                .schemaVersion(getRealmVersion())
                .build();
        try {
//            Realm.migrateRealm(config, new Migration());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected long getRealmVersion(){
        return 0;
    }

    public Realm getRealmOnMain() {
        if (realmOnMain == null || realmOnMain.isClosed()) {
            try {
                realmOnMain = Realm.getInstance(config);
            } catch (Exception e) {
                e.printStackTrace();
                Realm.deleteRealm(config);
            }
        }
        return realmOnMain;
    }

    @Override
    public void release() {
        if (realmOnMain != null && !realmOnMain.isClosed()) {
            try {
                realmOnMain.close();
                realmOnMain = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Realm getRealm() {
        try {
            return Realm.getInstance(config);
        } catch (RealmMigrationNeededException exception) {
            exception.printStackTrace();
//            Realm.deleteRealm(config);
            return null;
        }
    }
}
