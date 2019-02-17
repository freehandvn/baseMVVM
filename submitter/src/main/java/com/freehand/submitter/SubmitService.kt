package com.freehand.submitter

/**
 * Created by minhpham on 2/14/19.
 * Purpose: control all submit action
 * Copyright © 2019 Pham Duy Minh. All rights reserved.
 */
abstract class SubmitService {
    /**
     * call submit a object @ISubmitter
     */
    fun submit(submitter: ISubmitter) {
        //
    }

    /**
     * start submit service, auto submit all object @ISubmitter in queue
     */
    fun start(){
        //TODO : mount all group submit
    }

    /**
     * stop submit service, stop submit all object @ISubmitter in queue
     */
    fun stop(){}

    /**
     * stop submit an special object @submit
     */
    fun pause(submitter: ISubmitter){

    }
    /**
     * destroy service, release all resource
     */
    fun destroy(){

    }

    /**
     * fetch all submit group by priority
     */
    protected abstract fun getPriorityGroup():List<IGroupSubmit>
}