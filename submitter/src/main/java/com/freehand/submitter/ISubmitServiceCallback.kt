package com.freehand.submitter

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
     * define number of thread for process
     */
    fun getThreadProgress(): Int

    /**
     * define the way to get stable group which not in-processing, not pause, not error
     */
    fun getStableGroup(): IGroupSubmit?

    /**
     * reset all submit has error, pause to stable group, ready for re-submit
     */
    fun resetAllGroup()
}