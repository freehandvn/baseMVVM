package com.freehand.realmprovider;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.annotations.Nullable;
import io.reactivex.functions.Action;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by minhpham on 5/22/17.
 * Purpose: .
 * Copyright © 2017 Pham Duy Minh. All rights reserved.
 */

public class RealmUtility {

    public static IRealmProvider realmProvider;

    public static <T extends RealmObject> long countSync(Class<T> clazz, IRealmCondition<T> condition) {
        try (Realm realm = realmProvider.getRealm()) {
            if (condition != null) {
                return condition.condition(realm.where(clazz)).count();
            } else {
                return realm.where(clazz).count();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static <T extends RealmObject> long countBinding(Realm realm, Class<T> clazz, IRealmCondition<T> condition) {
        try {
            if (condition != null) {
                return condition.condition(realm.where(clazz)).count();
            } else {
                return realm.where(clazz).count();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static <T extends RealmObject> void updateProperty(final Class<T> clazz, final IRealmCondition<T> condition, final doUpdate<T> update) {
        try (Realm realm = realmProvider.getRealm()) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmQuery<T> query = realm.where(clazz);
                    if (condition != null) {
                        query = condition.condition(query);
                    }
                    List<T> entity = query.findAll();
                    if (entity != null && entity.size() > 0 && update != null) {
                        for (T temp : entity) {
                            update.update(temp);
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * update realm object from json
     *
     * @param clazz
     * @param json
     * @param <T>
     * @return
     */
    public static <T extends RealmObject> void updateSync(final Class<T> clazz, final String json) {
        if (TextUtils.isEmpty(json)) return;
        try (Realm realm = realmProvider.getRealm()) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.createOrUpdateObjectFromJson(clazz, json);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T extends RealmObject> Completable update(final Class<T> clazz, final String json) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                updateSync(clazz, json);
            }
        });
    }

    /**
     * @param condition
     * @return first row in table
     */
    public static <T extends RealmObject> T findFirstSync(Class<T> clazz, @Nullable IRealmCondition<T> condition) {
        try (Realm realm = realmProvider.getRealm()) {
            RealmQuery<T> query = realm.where(clazz);
            if (condition != null) {
                query = condition.condition(query);
            }
            T entity = query.findFirst();
            return entity == null ? null : realm.copyFromRealm(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * @param condition
     * @return first row in table
     */
    public static <T extends RealmObject> T findFirstBinding(Realm realm, Class<T> clazz, @Nullable IRealmCondition<T> condition) {

        try {
            RealmQuery<T> query = realm.where(clazz);
            if (condition != null) {
                query = condition.condition(query);
            }
            T entity = query.findFirst();
            return entity;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param condition
     * @return all rows in table
     */
    public static <T extends RealmObject> List<T> findAllSync(Class<T> clazz, @Nullable IRealmCondition<T> condition) {

        try (Realm realm = realmProvider.getRealm()) {
            RealmQuery<T> query = realm.where(clazz);
            if (condition != null) {
                query = condition.condition(query);
            }
            RealmResults<T> users = query.findAll();
            return realm.copyFromRealm(users);
        } catch (Exception e) {
            Log.v("findAllSync", e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * @param condition
     * @return all rows in table
     */
    public static <T extends RealmObject> RealmResults<T> findAllBinding(Realm realm, Class<T> clazz, @Nullable IRealmCondition<T> condition) {
        RealmQuery<T> query = realm.where(clazz);
        if (condition != null) {
            query = condition.condition(query);
        }
        RealmResults<T> users = query.findAll();

        return users;
    }

    /**
     * save instance to db
     *
     * @param entity
     * @return
     */
    public static <T extends RealmObject> T saveSync(final T entity) {
        if (!entity.isManaged()) {
            try (Realm realm = realmProvider.getRealm()) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.insertOrUpdate(entity);
                    }
                });
                return entity;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            try {
                Realm realm = entity.getRealm();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.insertOrUpdate(entity);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return entity;
        }
    }

    /**
     * save list instances to db
     *
     * @param entities
     * @return
     */
    public static <T extends RealmObject> List<T> saveSync(final Collection<T> entities) {
        try (Realm realm = realmProvider.getRealm()) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.insertOrUpdate(entities);
                }
            });
            return new ArrayList<>(entities);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * delete instance to db
     *
     * @param entity
     */
    public static <T extends RealmObject> void deleteSync(final T entity) {
        try (Realm realm = realmProvider.getRealm()) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealmOrUpdate(entity).deleteFromRealm();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * delete row has id is {@param id}
     *
     * @param condition
     */
    public static <T extends RealmObject> void deleteSync(final Class<T> clazz, final IRealmCondition<T> condition) {
        try (Realm realm = realmProvider.getRealm()) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmQuery<T> query = realm.where(clazz);
                    if (condition != null) {
                        query = condition.condition(query);
                    }
                    RealmResults<T> result = query.findAll();
                    if (result != null) {
                        result.deleteAllFromRealm();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * delete list instances in db
     *
     * @param entities
     */
    public static <T extends RealmObject> void deleteSync(final Iterable<T> entities) {
        try (Realm realm = realmProvider.getRealm()) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    for (T entity : entities) {
                        realm.copyToRealmOrUpdate(entity).deleteFromRealm();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * delete all row in db
     */
    public static <T extends RealmObject> void deleteAllSync(final Class<T> clazz) {
        try (Realm realm = realmProvider.getRealm()) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.delete(clazz);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * async method : application number of rows in table
     *
     * @return
     */
    public static <T extends RealmObject> Single<Long> count(final Class<T> clazz, final IRealmCondition<T> condition) {
        return Single.fromCallable(new Callable<Long>() {
            @Override
            public Long call() {
                return countSync(clazz, condition);
            }
        });
    }

    /**
     * async method : application first row match with your condition {@param query}
     *
     * @param query
     * @return
     */
    public static <T extends RealmObject> Single<T> findFirst(final Class<T> clazz, @Nullable final IRealmCondition<T> query) {
        return Single.fromCallable(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return findFirstSync(clazz, query);
            }
        });
    }

    /**
     * async method : application all row in table match with your condition {@param query}
     *
     * @param query
     * @return query null return all rows in table
     */
    public static <T extends RealmObject> Observable<List<T>> findAll(final Class<T> clazz, @Nullable final IRealmCondition<T> query) {
        return Observable.fromCallable(new Callable<List<T>>() {
            @Override
            public List<T> call() throws Exception {
                return findAllSync(clazz, query);
            }
        });
    }

    /**
     * async method : save instance to table
     *
     * @param entity
     * @return
     */
    public static <T extends RealmObject> Single<T> save(final T entity) {
        return Single.fromCallable(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return saveSync(entity);
            }
        });
    }


    /**
     * async method: save list instances to table
     *
     * @param entities
     * @return
     */
    public static <T extends RealmObject> Single<List<T>> save(final Collection<T> entities) {
        return Single.fromCallable(new Callable<List<T>>() {
            @Override
            public List<T> call() throws Exception {
                return saveSync(entities);
            }
        });
    }

    /**
     * async method : delete instance in table
     *
     * @param entity
     * @return
     */
    public static <T extends RealmObject> Completable delete(final T entity) {
        return Completable.fromCallable(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                deleteSync(entity);
                return null;
            }
        });
    }

    /**
     * async method : delete row match with your condition {@param query}
     *
     * @param query
     * @return
     */
    public static <T extends RealmObject> Completable delete(final Class<T> clazz, final IRealmCondition<T> query) {
        return Completable.fromCallable(new Callable<List<T>>() {
            @Override
            public List<T> call() throws Exception {
                deleteSync(clazz, query);
                return null;
            }
        });
    }


    /**
     * async method : delete list instances in table
     *
     * @param entities
     * @return
     */
    public static <T extends RealmObject> Completable delete(final Iterable<T> entities) {
        return Completable.fromCallable(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                deleteSync(entities);
                return null;
            }
        });
    }

    /**
     * delete all row in table
     *
     * @return
     */
    public static <T extends RealmObject> Completable deleteAll(final Class<T> clazz) {
        return Completable.fromCallable(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                deleteAllSync(clazz);
                return null;
            }
        });
    }

    /**
     * delete realm database
     */
    public static void deleteDB() {
        Realm realm = realmProvider.getRealm();
        RealmConfiguration config = realm.getConfiguration();
        realm.close();
        Realm.deleteRealm(config);
    }

    public static void clearAll() {
        Realm realm = realmProvider.getRealm();
        final RealmConfiguration realmConfiguration = realm.getConfiguration();
        realm.beginTransaction();

        realm.deleteAll();

        realm.commitTransaction();
    }

    public static <T extends RealmObject> void delete(T item, Realm... realms) {
        if (realms != null && realms.length > 0) {
            realms[0].copyToRealmOrUpdate(item).deleteFromRealm();
        } else {
            deleteSync(item);
        }
    }

    public interface doUpdate<T extends RealmObject> {
        void update(T value);
    }
}
