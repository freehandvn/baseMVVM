package com.freehand.base_component.core.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.freehand.base_component.core.ApplicationProvider;
import com.freehand.base_component.core.activity.BaseActivity;
import com.freehand.base_component.core.inteface.IDialog;
import com.freehand.base_component.core.inteface.IDialogCallback;
import com.freehand.base_component.core.inteface.IDialogVM;
import com.freehand.base_component.core.utils.CodeUtils;
import com.freehand.base_component.core.utils.DeviceUtils;
import com.freehand.base_component.core.utils.KeyboardUtil;
import com.freehand.base_component.core.utils.ViewUtils;
import com.freehand.base_component.core.view_model.BaseViewModel;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

import static android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;

/**
 * Created by minhpham on 12/13/18.
 * Purpose: base of dialog
 * Copyright © 2018 Pham Duy Minh. All rights reserved.
 */
public abstract class BaseDialogFragment<T extends BaseViewModel & IDialogVM<O>, O> extends DialogFragment implements IDialogCallback, IDialog<O> {

    private static final String TAG = BaseDialogFragment.class.getSimpleName();
    protected T viewModel;
    protected ViewDataBinding dataBinding;
    protected KeyboardUtil keyboardUtil;
    protected PublishSubject<O> resultChannel = PublishSubject.create();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dataBinding = DataBindingUtil.inflate(inflater, defineLayout(), container, false);
        dataBinding.setVariable(defineVariableID(), viewModel);
        return dataBinding.getRoot();
    }

    protected abstract int defineVariableID();

    protected abstract int defineLayout();

//    @NonNull
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        super.onCreateDialog(savedInstanceState);
//        Dialog dialog = new Dialog(getActivity(), R.style.DialogFragmentStyle);
//        dataBinding = DataBindingUtil.bind(getActivity().getLayoutInflater().inflate(viewModel.getLayoutResId(), null));
//        dataBinding.setVariable(viewModel.getVariableId(), viewModel);
//        dialog.setContentView(dataBinding.getRoot());
//        return dialog;
//    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        configDialog();
        initViews(dataBinding.getRoot());
    }

    /**
     * Configure the attributes of dialog.
     */
    private void configDialog() {
        if (isFullScreenDialog()) {
            keyboardUtil = new KeyboardUtil(getActivity(), dataBinding.getRoot(), keyboardPercentMovement());
        }
        Window window = getDialog().getWindow();
        if (window == null) return;
        window.setGravity(Gravity.CENTER);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setSoftInputMode(SOFT_INPUT_ADJUST_RESIZE);
        onConfigurationChanged(getResources().getConfiguration());
    }

    @Override
    public void onDestroyView() {
        if (keyboardUtil != null) keyboardUtil.disable();
        super.onDestroyView();
    }

    /**
     * Sets size as width, height for dialog
     *
     * @param sizeInPixel the size in pixel.
     */
    public void setDialogSize(Point sizeInPixel) {
        Window window = getDialog().getWindow();
        if (window == null) return;
        window.setLayout(sizeInPixel.x, sizeInPixel.y);
    }

    /**
     * @return the percentage of width and height of dialog.
     */
    protected Point getSizePercentage() {
        return new Point(100, 100);
    }

    /**
     * Can initialize views in this method.
     *
     * @param rootView the root view.
     */
    protected abstract void initViews(View rootView);

    /**
     * @return the view model for view of dialog.
     */
    protected abstract T defineViewModel();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModel = defineViewModel();
        viewModel.setDismissCallback(this);
        viewModel.setOuputChannel(resultChannel);
        CodeUtils.hideKeyboard(CodeUtils.fromContext(context));
    }

    @Override
    public void onDetach() {
        resultChannel.onComplete();
        resultChannel = null;
        viewModel.destroy();
        ViewUtils.postDelay(() -> CodeUtils.hideKeyboard(getActivity()), 100);
        super.onDetach();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, "onConfigurationChanged() called with: newConfig = [" + newConfig.orientation + "]");
        if (keyboardUtil != null) keyboardUtil.setPercent(keyboardPercentMovement());
        boolean isPortrait = newConfig.orientation == Configuration.ORIENTATION_PORTRAIT;
        if (getSizePercentage() != null) {
            int percentage = getSizePercentage().x;
            if (percentage > 0) {
                int screenHeight = DeviceUtils.getHeightPercentage(percentage);
                int screenWidth = DeviceUtils.getWidthPercentage(percentage);
                int dialogHeight = isPortrait ? screenWidth : screenHeight;
                dialogHeight -= DeviceUtils.getNavigationBarHeight(getContext());
                int dialogWidth = isPortrait ? screenHeight : screenWidth;
                setDialogSize(new Point(dialogWidth, dialogHeight));
            }
        }
    }

    @Override
    public void onDismiss() {
        dismiss();
    }

    @Override
    public Observable<O> show() {
        Activity activity = ApplicationProvider.activity();
        if(activity instanceof BaseActivity){
            show(((BaseActivity)activity).getSupportFragmentManager(),defineDialogTag());
        }
        return resultChannel.doOnDispose(() -> dismiss());
    }

    protected abstract String defineDialogTag();

    public T getViewModel() {
        return viewModel;
    }

    public boolean isFullScreenDialog() {
        return true;
    }

    protected float keyboardPercentMovement() {
        return 0.5f;
    }
}
