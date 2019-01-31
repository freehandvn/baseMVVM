package com.freehand.fetcher

import java.util.HashSet

/**
 * Created by minhpham on 12/9/18.
 * Purpose: control multiple fetcher
 * Copyright © 2018 Pham Duy Minh. All rights reserved.
 */
class FetchBag {

    private var bag: MutableSet<IFetcher<*, *>>? = null

    /**
     * add fetcher
     *
     * @param fetcher
     */
    fun addFetcher(fetcher: IFetcher<*, *>?) {
        if (fetcher == null) return
        if (bag == null) {
            bag = HashSet()
        }
        bag?.add(fetcher)
    }

    /**
     * cancel all fetcher
     */
    fun cancel() {
        if (bag == null) return
        for (fetcher in bag!!) {
            fetcher.cancel()
        }
    }

    /**
     * destroy all fetcher
     */
    fun destroy() {
        if (bag == null) return
        for (fetcher in bag!!) {
            fetcher.destroy()
        }
        bag?.clear()
        bag = null
    }
}
