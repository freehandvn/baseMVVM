package com.freehand.submitter

import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by minhpham on 2/14/19.
 * Purpose: control all submit action
 * Copyright © 2019 Pham Duy Minh. All rights reserved.
 */
class SubmitService(callback: ISubmitServiceCallback) {
    private val processQueue = ConcurrentHashMap<IGroupSubmit, Disposable>()
    private var isStop = false
    private val submitCallback = callback
    private val MAXPROCESS = callback.getThreadProgress()
    /**
     * call submit a object @ISubmitter
     */
    fun submit(submitter: ISubmitter) {
        val group = submitCallback.getGroup(submitter)
        // if running -> stop, reset status and run again
        processQueue[group]?.dispose()
        group.resetStatus()
        checkAndRun(group)
    }

    /**
     * start submit service, auto submit all object @ISubmitter in queue
     */
    fun start() {
        isStop = false
        submitCallback.resetAllGroup()
        checkAndRun(submitCallback.getStableGroup())
    }

    private fun checkAndRun(stableGroup: IGroupSubmit) {
        if (isStop) return
        val inProcessing = countProcessing()
        if (inProcessing >= MAXPROCESS) return
        processQueue[stableGroup] = executeSubmit(stableGroup)
    }

    private fun countProcessing(): Int {
        return processQueue.size
    }

    private fun executeSubmit(stableSubmit: IGroupSubmit): Disposable {
        return stableSubmit.getSubmitChannel()
                .doOnComplete {
                    afterProcess(stableSubmit)
                }
                .doOnError { afterProcess(stableSubmit) }
                .subscribeOn(Schedulers.io())
                .subscribe()
    }

    private fun afterProcess(group: IGroupSubmit) {
        // remove group from processing
        processQueue.remove(group)
        checkAndRun(submitCallback.getStableGroup())
    }


    /**
     * stop submit service, stop submit all object @ISubmitter in queue
     */
    fun stop() {
        processQueue.values.forEach { disposal -> disposal.dispose() }
        processQueue.clear()
        isStop = true
    }

    /**
     * stop submit an special object @submit
     */
    fun pause(submitter: ISubmitter) {
        val group = submitCallback.getGroup(submitter)
        processQueue[group]?.dispose()
        processQueue.remove(group)
        group.pause()
    }

    /**
     * destroy service, release all resource
     */
    fun destroy() {
        stop()
    }
}