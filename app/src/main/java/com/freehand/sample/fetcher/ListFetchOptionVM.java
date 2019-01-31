package com.freehand.sample.fetcher;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.freehand.fetcher.Fetcher;
import com.freehand.logger.Logger;
import com.freehand.realmprovider.RealmVM;
import com.freehand.sample.api.APIClient;
import com.freehand.sample.api.APIInterface;
import com.freehand.sample.db.Datum;
import com.freehand.sample.db.MultipleResource;
import com.freehand.sample.db.User;
import com.freehand.sample.db.UserList;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by minhpham on 12/3/18.
 * Purpose: .
 * Copyright © 2018 Pham Duy Minh. All rights reserved.
 */
public class ListFetchOptionVM extends RealmVM {

    private Fetcher<Integer, MultipleResource<Datum>> fetcherMR;
    private Fetcher<String, UserList> fetcherUL;
    private Fetcher<String, User> fetcherError;
    private APIInterface retrofit;

    @Override
    public void init() {
        super.init();
        fetcherMR = new Fetcher<Integer, MultipleResource<Datum>>() {
            @Override
            protected Observable<MultipleResource<Datum>> createApiObservable(Integer input) {
                return retrofit.doGetUserList(input);
            }

            @NotNull
            @Override
            protected Observable<MultipleResource<Datum>> processAfterFetch(MultipleResource<Datum> output, Integer input) {
                Logger.log().d("minh","out: "+output+" in: "+input);
                return super.processAfterFetch(output, input);
            }
        };
        fetcherUL = new Fetcher<String, UserList>() {
            @Override
            protected Observable<UserList> createApiObservable(String input) {
                return retrofit.doGetListResources();
            }
        };
        fetcherError = new Fetcher<String, User>() {
            @Nullable
            @Override
            protected Observable<User> createApiObservable(String input) {
                return retrofit.ghostUser();
            }

            @NotNull
            @Override
            protected Observable<String> preProcessFetcher(String input) {
                return super.preProcessFetcher(input)
                        .observeOn(Schedulers.io())
                        .doOnNext(it -> {
                            for (int i = 0; i < 1000000; i++) {
//                                Logger.log().d("minh", "" + i);
                            }
                            Logger.log().d("minh",Thread.currentThread().getName());
                        });
            }
        };
        retrofit = APIClient.getClient().create(APIInterface.class);
        addDisposable(fetcherMR.getOutput().subscribe(result -> Log.d("minh", "accept: ")));
        addDisposable(fetcherUL.getOutput().subscribe(userListFetcherResult -> Log.d("minh", "accept: ")));
        addDisposable(fetcherError.getOutput().subscribe(t -> Logger.log().d("minh", " has error: "
                + t.isSuccess() + " error: " + t.toString() + " thread: " + Thread.currentThread().getName())));
    }

    public void onFetchDoon(View v) {
        fetcherMR.fetch(2);
    }

    public void onFetchDatum(View v) {
        fetcherUL.fetch("");

    }

    public void onErrorApi(View v) {
        for(int i=0;i<30;i++) {
            fetcherError.fetch("come here ok?");
        }

    }

    public void onCreateUserForm(View v) {
        Toast.makeText(v.getContext(), "onFetcher", Toast.LENGTH_SHORT).show();

    }

    public void onCreateUserWithoutForm(View v) {
        Toast.makeText(v.getContext(), "onFetcher", Toast.LENGTH_SHORT).show();

    }

}
