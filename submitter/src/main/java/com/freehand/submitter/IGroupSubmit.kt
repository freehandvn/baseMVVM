package com.freehand.submitter

import io.reactivex.Completable

/**
 * Created by minhpham on 2/14/19.
 * Purpose: .
 * Copyright © 2019 Pham Duy Minh. All rights reserved.
 */
interface IGroupSubmit {
    /**
     * check object is ready for execute submit
     */
    fun isStable(): Boolean

    /**
     * return the execute chanel
     */
    fun getSubmitChannel():Completable

    /**
     * mark pause at current channel
     */
    fun pause()

    /**
     * reset object to init status and wait for re-submit
     */
    fun resetStatus()
}