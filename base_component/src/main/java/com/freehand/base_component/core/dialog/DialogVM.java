package com.freehand.base_component.core.dialog;

import com.freehand.base_component.core.inteface.IDialogCallback;
import com.freehand.base_component.core.inteface.IDialogVM;
import com.freehand.base_component.core.view_model.BaseViewModel;

import io.reactivex.subjects.Subject;

/**
 * Created by minhpham on 12/16/18.
 * Purpose: .
 * Copyright © 2018 Pham Duy Minh. All rights reserved.
 */
public abstract class DialogVM<O> extends BaseViewModel implements IDialogVM<O> {
    private IDialogCallback callback;
    private Subject<O> outputChannel;

    /**
     * call dismiss callback to dismiss dialog
     */
    @Override
    public void dismiss() {
        callback.onDismiss();
    }

    /**
     * call onNext to delivery result
     *
     * @param result
     */
    @Override
    public void putResult(O result) {
        outputChannel.onNext(result);
    }

    /**
     * setup output channel
     *
     * @param output
     */
    @Override
    public void setOuputChannel(Subject<O> output) {
        this.outputChannel = output;
    }

    /**
     * define dismiss callback
     *
     * @param callback
     */
    @Override
    public void setDismissCallback(IDialogCallback callback) {
        this.callback = callback;
    }

    @Override
    public void destroy() {
        super.destroy();
        callback = null;
        outputChannel = null;
    }
}
