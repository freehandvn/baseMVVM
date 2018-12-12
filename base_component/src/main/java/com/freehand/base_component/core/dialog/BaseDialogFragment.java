package com.freehand.base_component.core.dialog;

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

import com.freehand.base_component.core.utils.CodeUtils;
import com.freehand.base_component.core.utils.DeviceUtils;
import com.freehand.base_component.core.utils.KeyboardUtil;
import com.freehand.base_component.core.utils.ViewUtils;
import com.freehand.base_component.core.view_model.BaseViewModel;

import static android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;

/**
 * Created by namhoainguyen on 8/16/17.
 */

public abstract class BaseDialogFragment<T extends BaseViewModel> extends DialogFragment {

    private static final String TAG = BaseDialogFragment.class.getSimpleName();
    protected T viewModel;
    protected ViewDataBinding dataBinding;
    private KeyboardUtil keyboardUtil;

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
        CodeUtils.hideKeyboard(CodeUtils.fromContext(context));
    }

    @Override
    public void onDetach() {
        viewModel.destroy();
        ViewUtils.postDelay(new Runnable() {
            @Override
            public void run() {
                CodeUtils.hideKeyboard(getActivity());
            }
        }, 100);
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