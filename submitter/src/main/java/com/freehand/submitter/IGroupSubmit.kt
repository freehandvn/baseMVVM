package com.freehand.submitter

import io.reactivex.Completable

/**
 * Created by minhpham on 2/14/19.
 * Purpose: .
 * Copyright © 2019 Pham Duy Minh. All rights reserved.
 */
interface IGroupSubmit {

    fun isStable(): Boolean

    fun getSubmitChannel():Completable
    fun pause()
}