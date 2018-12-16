package com.freehand.base_component.core.inteface;

import io.reactivex.subjects.Subject;

/**
 * Created by minhpham on 12/16/18.
 * Purpose: .
 * Copyright © 2018 Pham Duy Minh. All rights reserved.
 */
public interface IDialogVM<O> {
    /**
     * call dismiss callback to dismiss dialog
     */
    void dismiss();

    /**
     * call onNext to delivery result
     * @param result
     */
    void putResult(O result);

    /**
     * setup output channel
     * @param output
     */
    void setOuputChannel(Subject<O> output);

    /**
     * define dismiss callback
     * @param callback
     */
    void setDismissCallback(IDialogCallback callback);
}
