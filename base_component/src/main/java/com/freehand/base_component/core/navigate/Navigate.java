package com.freehand.base_component.core.navigate;

import io.reactivex.Observable;

/**
 * Created by minhpham on 12/16/18.
 * Purpose: .
 * Copyright © 2018 Pham Duy Minh. All rights reserved.
 */
public class Navigate {
    private static final Navigate ourInstance = new Navigate();
    private NavigateFunc func;
    public enum Strategy{ADD,REPLACE,OVERLAP,POP}
    private Navigate() {
        func = new NavigateFunc();
    }

    public static Navigate getInstance() {
        return ourInstance;
    }

    public void show(INavigator navigator) {
        func.execute(navigator);
    }

    public Observable<INavigator> getChannel(){
        return func.getOutput();
    }

}
