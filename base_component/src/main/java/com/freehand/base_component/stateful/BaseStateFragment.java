package com.freehand.base_component.stateful;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.freehand.base_component.core.fragment.BaseFragmentBinding;
import com.freehand.base_component.core.view_model.BaseViewModel;

/**
 * Created by minhpham on 2/28/17.
 * Purpose: .
 */
public abstract class BaseStateFragment<T extends BaseStateVM> extends BaseFragmentBinding {

    protected T viewModel;
    protected IStatefulView statefulView;
    protected ViewDataBinding viewDataBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewDataBinding = DataBindingUtil.inflate(inflater, defineLayout(), container, false);
        viewDataBinding.setVariable(defineVariableID(), viewModel);
        View root = viewDataBinding.getRoot();
        statefulView = getStatefulView(root);
        statefulView.addStateVM(viewModel);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initStatefulView(statefulView);
        initView(getView());
    }

    protected abstract IStatefulView getStatefulView(View root);


    protected abstract void initStatefulView(IStatefulView statefulView);

    /**
     * support add state to stateful view
     *
     * @param vm
     * @param layout
     * @param variableID
     * @param state
     * @param needShow
     */
    protected void addState(BaseViewModel vm, int layout, int variableID, Integer parent, int state, boolean needShow) {
        viewModel.addChildViewModel(vm);
        statefulView.addState(vm, layout, variableID, parent, state, needShow);
    }

    /**
     * support add state to stateful view
     *
     * @param vm
     * @param layout
     * @param variableID
     */
    protected void addState(BaseViewModel vm, int layout, Integer parent, int variableID) {
        viewModel.addChildViewModel(vm);
        statefulView.addState(vm, layout, parent, variableID);
    }

    /**
     * Override method , don't need to care
     */

    protected void initView(View root) {

    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (statefulView != null) {
            statefulView.destroy();
            statefulView = null;
        }
    }



}
