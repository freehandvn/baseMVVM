package com.freehand.submitter

import io.reactivex.disposables.Disposable
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by minhpham on 3/20/19.
 * Purpose: .
 * Copyright © 2019 Pham Duy Minh. All rights reserved.
 */
interface ISubmitServiceCallback {

    /**
     * define the way to get group by submitter
     */
    fun getGroup(submitter: ISubmitter): IGroupSubmit

    /**
     * fetch all submit group by priority
     */
    fun getPriorityGroup(): ConcurrentHashMap<IGroupSubmit, Disposable?>

    /**
     * define number of thread for process
     */
    fun getThreadProgress(): Int
}