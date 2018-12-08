package com.freehand.fetcher;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by minhpham on 12/9/18.
 * Purpose: control multiple fetcher
 * Copyright © 2018 Pham Duy Minh. All rights reserved.
 */
public class FetchBag {

    private Set<IFetcher> bag;

    /**
     * add fetcher
     *
     * @param fetcher
     */
    public void addFetcher(IFetcher fetcher) {
        if(fetcher == null) return;
        if (bag == null) {
            bag = new HashSet<>();
        }
        bag.add(fetcher);
    }

    /**
     * cancel all fetcher
     */
    public void cancel() {
        if (bag == null) return;
        for (IFetcher fetcher : bag) {
            fetcher.cancel();
        }
    }

    /**
     * destroy all fetcher
     */
    public void destroy() {
        if (bag == null) return;
        for (IFetcher fetcher : bag) {
            fetcher.destroy();
        }
        bag.clear();
        bag = null;
    }
}
