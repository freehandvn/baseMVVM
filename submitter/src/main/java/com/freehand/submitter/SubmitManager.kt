package com.freehand.submitter

/**
 * Created by minhpham on 3/20/19.
 * Purpose: .
 * Copyright © 2019 Pham Duy Minh. All rights reserved.
 */
object SubmitManager {

    private lateinit var submitService: SubmitService
    private var initted = false

    fun init(callback: ISubmitServiceCallback) {
        submitService = SubmitService(callback)
        initted = true
    }

    fun getService(): SubmitService {
        if (!initted) throw Exception("You must call init first, recommend call it in application")
        return submitService
    }
}