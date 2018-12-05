package com.freehand.base_component.core.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.freehand.base_component.core.view_model.BaseViewModel;


/**
 * Created by minhpham on 2/28/17.
 * Purpose: .
 */
public abstract class BaseFragmentBinding<T extends BaseViewModel> extends BaseFragment {

    protected T viewModel;
    protected ViewDataBinding dataBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dataBinding = DataBindingUtil.inflate(inflater, defineLayout(), container, false);
        dataBinding.setVariable(defineVariableID(), viewModel);
        View root = dataBinding.getRoot();
        initView(root);
        return root;
    }

    protected abstract int defineVariableID();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModel = onCreateViewModel();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (viewModel != null) viewModel.onConfigurationChanged(newConfig);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        viewModel.onActivityResult(requestCode,resultCode,data);
    }

    @NonNull
    protected abstract T onCreateViewModel();


    protected void initView(View root) {
        viewModel.init();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        viewModel.destroy();
        viewModel = null;
        Log.e(getName(), "onDetach");
    }

    public String getName() {
        return getClass().getName();
    }

    public T getViewModel() {
        return viewModel;
    }
}
