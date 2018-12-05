package com.freehand.base_component.core.recycle;

import android.view.View;

/**
 * Created by minhpham on 11/26/18.
 * Purpose: .
 * Copyright © 2018 Pham Duy Minh. All rights reserved.
 */
public interface OnItemListener<T> {
    void onSingleConfirm(View v , T item, int position);
}
