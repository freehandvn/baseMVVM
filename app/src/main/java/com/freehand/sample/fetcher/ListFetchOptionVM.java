package com.freehand.sample.fetcher;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.freehand.fetcher.Fetcher;
import com.freehand.fetcher.FetcherResult;
import com.freehand.realmprovider.RealmVM;
import com.freehand.sample.api.APIClient;
import com.freehand.sample.api.APIInterface;
import com.freehand.sample.db.Datum;
import com.freehand.sample.db.MultipleResource;
import com.freehand.sample.db.UserList;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by minhpham on 12/3/18.
 * Purpose: .
 * Copyright © 2018 Pham Duy Minh. All rights reserved.
 */
public class ListFetchOptionVM extends RealmVM {

    private Fetcher<MultipleResource<Datum>> fetcherMR;
    private Fetcher<UserList> fetcherUL;
    private APIInterface retrofit;

    @Override
    public void init() {
        super.init();
        fetcherMR = new Fetcher<>();
        fetcherUL = new Fetcher<>();
        retrofit = APIClient.getClient().create(APIInterface.class);
    }

    public void onFetchDoon(View v) {
        addDisposable(fetcherMR.getOutput().subscribe(new Consumer<FetcherResult<MultipleResource<Datum>>>() {
            @Override
            public void accept(FetcherResult<MultipleResource<Datum>> result) throws Exception {
                Log.d("minh", "accept: ");
            }
        }));
        Observable<MultipleResource<Datum>> temp = retrofit.doGetUserList("2");
        fetcherMR.fetch(retrofit.doGetUserList("2"));
    }

    public void onFetchDatum(View v) {
        addDisposable(fetcherUL.getOutput().subscribe(new Consumer<FetcherResult<UserList>>() {
            @Override
            public void accept(FetcherResult<UserList> userListFetcherResult) throws Exception {
                Log.d("minh", "accept: ");
            }
        }));
        fetcherUL.fetch(retrofit.doGetListResources());

    }

    public void onCreateUserForm(View v) {
        Toast.makeText(v.getContext(), "onFetcher", Toast.LENGTH_SHORT).show();

    }

    public void onCreateUserWithoutForm(View v) {
        Toast.makeText(v.getContext(), "onFetcher", Toast.LENGTH_SHORT).show();

    }

}
