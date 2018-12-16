package com.freehand.base_component.core.inteface;

import io.reactivex.Observable;

/**
 * Created by minhpham on 12/16/18.
 * Purpose: .
 * Copyright © 2018 Pham Duy Minh. All rights reserved.
 */
public interface IDialog<O>  extends IDialogCallback {
    Observable<O> show();
}
