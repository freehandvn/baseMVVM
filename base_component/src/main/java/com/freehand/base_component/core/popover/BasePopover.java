package com.freehand.base_component.core.popover;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.freehand.base_component.core.view_model.BaseViewModel;

/**
 * Created by minhpham on 1/3/19.
 * Purpose: .
 * Copyright © 2019 Pham Duy Minh. All rights reserved.
 */
public abstract class BasePopover<V extends BaseViewModel> {

    private final PopupWindow popupWindow;

    public BasePopover(Context context) {
        V viewModel = createViewModel(context);
        ViewDataBinding viewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(context), defineLayout(), null, false);
        viewDataBinding.setVariable(defineVariableID(), viewModel);
        popupWindow = new PopupWindow(viewDataBinding.getRoot(), defineWidth(), defineHeight(), true);
        popupWindow.setOutsideTouchable(dimissOutside());
    }

    public void show(View anchorView){
        popupWindow.showAsDropDown(anchorView);
    }

    public void dismiss(){
        popupWindow.dismiss();
    }

    protected int defineWidth() {
        return ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    protected int defineHeight() {
        return ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    protected boolean dimissOutside() {
        return true;
    }

    protected abstract V createViewModel(Context context);

    protected abstract int defineVariableID();

    protected abstract int defineLayout();
}
