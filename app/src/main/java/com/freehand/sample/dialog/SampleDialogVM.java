package com.freehand.sample.dialog;

import android.view.View;

import com.freehand.base_component.core.dialog.DialogVM;

/**
 * Created by minhpham on 12/16/18.
 * Purpose: .
 * Copyright © 2018 Pham Duy Minh. All rights reserved.
 */
public class SampleDialogVM extends DialogVM<String> {
    public void close(View view) {
        putResult("im here");
        dismiss();
    }
}
