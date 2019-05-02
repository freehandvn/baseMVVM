package com.freehand.sample.dialog;

import android.graphics.Point;
import android.view.View;

import com.freehand.base_component.core.dialog.BaseDialogFragment;
import com.freehand.sample.BR;
import com.freehand.sample.R;

/**
 * Created by minhpham on 12/16/18.
 * Purpose: .
 * Copyright © 2018 Pham Duy Minh. All rights reserved.
 */
public class SampleDialog extends BaseDialogFragment<SampleDialogVM,String> {
    @Override
    protected Point getSizePercentage() {
        return new Point(50,50);
    }

    /**
     * Can initialize views in this method.
     *
     * @param rootView the root view.
     */
    @Override
    protected void initViews(View rootView) {

    }

    /**
     * @return the view model for view of dialog.
     */
    @Override
    protected SampleDialogVM defineViewModel() {
        return new SampleDialogVM();
    }

    @Override
    protected String defineDialogTag() {
        return getClass().getName();
    }
}
