package com.freehand.base_component.core.navigate;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by minhpham on 12/16/18.
 * Purpose: Use navigate to pop/add/replace fragment
 * Copyright © 2018 Pham Duy Minh. All rights reserved.
 */
public class NavigateManager {
    private static final NavigateManager ourInstance = new NavigateManager();
    private PublishSubject<INavigator> channel;

    private NavigateManager() {
        channel = PublishSubject.create();
    }

    public static void navigate(INavigator navigator) {
        ourInstance.channel.onNext(navigator);
    }

    public static Observable<INavigator> getChannel() {
        return ourInstance.channel;
    }

    public static void destroy() {
        ourInstance.channel.onComplete();
    }

}
