package com.freehand.base_component.core.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.freehand.base_component.core.activity.BaseDrawerActivity;
import com.freehand.base_component.core.utils.CodeUtils;

/**
 * Created by minhpham on 2/28/17.
 * Purpose: .
 */

public abstract class BaseFragment extends Fragment {

    private static final String TAG = BaseFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(defineLayout(), container, false);
        initView(root);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        CodeUtils.hideKeyboard(getActivity());
    }

    @Override
    public void onDestroyView() {
        CodeUtils.hideKeyboard(getActivity());
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        System.gc();
        super.onDestroy();
    }

    protected abstract void initView(View root);

    protected abstract int defineLayout();

    public boolean handleBackPressed() {
        return false;
    }

    public String getName() {
        return getClass().getName();
    }

    public BaseDrawerActivity getBaseActivity() {
        FragmentActivity context = getActivity();
        if (context == null || !(context instanceof BaseDrawerActivity)) return null;
        return (BaseDrawerActivity) context;
    }

    public void onBecomeVisible() {

    }

    public void onBecomeInvisible() {

    }
}
