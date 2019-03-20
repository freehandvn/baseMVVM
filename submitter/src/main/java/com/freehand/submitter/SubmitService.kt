package com.freehand.submitter

import io.reactivex.disposables.Disposable
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by minhpham on 2/14/19.
 * Purpose: control all submit action
 * Copyright © 2019 Pham Duy Minh. All rights reserved.
 */
class SubmitService(callback: ISubmitServiceCallback) {
    private var processQueue: MutableMap<IGroupSubmit, Disposable?>? = null
    private var isStop = false
    private val submitCallback = callback
    private val MAXPROCESS = callback.getThreadProgress()
    /**
     * call submit a object @ISubmitter
     */
    fun submit(submitter: ISubmitter) {
        val group = submitCallback.getGroup(submitter)

        processQueue?.let {
            if (it.containsKey(group)) {
                it[group]?.dispose()
                it[group] = null
            }
        } ?: run {
            processQueue = ConcurrentHashMap()
            processQueue?.put(group, null)
        }

        checkAndRun()
    }

    /**
     * start submit service, auto submit all object @ISubmitter in queue
     */
    fun start() {
        isStop = false
        processQueue = submitCallback.getPriorityGroup()
        checkAndRun()
    }

    private fun checkAndRun() {
        if (isStop) return
        val inProcessing = countProcessing()
        if (inProcessing >= MAXPROCESS) return
        getStableSubmit()?.let {
            processQueue?.put(it, executeSubmit(it))
        }
    }

    private fun countProcessing(): Int {
        return processQueue?.let { it.values.count { disposable -> disposable != null } } ?: 0
    }

    private fun executeSubmit(stableSubmit: IGroupSubmit): Disposable {
        return stableSubmit.getSubmitChannel()
                .doOnComplete {
                    afterProcess(stableSubmit)
                }
                .doOnError { afterProcess(stableSubmit) }
                .subscribe()
    }

    private fun afterProcess(group: IGroupSubmit) {
        // remove group from processing
        processQueue?.remove(group)
        checkAndRun()
    }

    /**
     * define the way to get stable group which not in-processing, not pause, not error
     */
    fun getStableSubmit(): IGroupSubmit?{
        return processQueue?.filter { entry -> entry.value == null }?.filter { it.key.isStable() }?.keys?.first()
    }

    /**
     * stop submit service, stop submit all object @ISubmitter in queue
     */
    fun stop() {
        processQueue?.let { it.values.forEach { disposal -> disposal?.dispose() };it.clear() }
        processQueue = null
        isStop = true
    }

    /**
     * stop submit an special object @submit
     */
    fun pause(submitter: ISubmitter) {
        val group = submitCallback.getGroup(submitter)
        processQueue?.let {
            if (it.containsKey(group)) {
                it[group]?.dispose()
                it[group] = null
            }
        } ?: run {
            processQueue = ConcurrentHashMap()
            processQueue?.put(group, null)
        }
        group.pause()
    }

    /**
     * destroy service, release all resource
     */
    fun destroy() {
        stop()
    }
}