package com.freehand.sample.dialog;

import android.view.View;

import com.freehand.base_component.core.dialog.DialogVM;
import com.freehand.sample.BR;
import com.freehand.sample.R;

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

    @Override
    public int defineLayoutDefault() {
        return R.layout.dialog_sample;
    }

    @Override
    public int defineVariableID() {
        return BR.vm;
    }
}
