package com.freehand.base_component.core.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.view.View;

import java.util.List;


/**
 * Created by minhpham on 11/20/18.
 * Purpose: quick helper show loading or popup menu
 * Copyright © 2018 Pham Duy Minh. All rights reserved.
 */
public class QuickDialog {
    static private ProgressDialog progressDialog;

    /**
     * show loading dialog
     * @param context
     * @param title
     * @param message
     */
    static public void showLoadingDialog(final Context context, final String title, final String message) {
        hideLoadingDialog();
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    /**
     * hide loading dialog
     */
    static public void hideLoadingDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog = null;
    }

    /**
     * quick init popup menu
     * @param view
     * @param handle
     * @param src
     * @return
     */
    public static PopupMenu getPopupMenu(View view, PopupMenu.OnMenuItemClickListener handle, int src) {
        PopupMenu menu = new PopupMenu(view.getContext(), view);
        menu.setOnMenuItemClickListener(handle);
        menu.inflate(src);
        return menu;
    }

    /**
     * quick create popup menu from list
     * @param view
     * @param handle
     * @param items
     * @return
     */
    public static PopupMenu createPopupMenu(View view, PopupMenu.OnMenuItemClickListener handle, List<String> items) {
        PopupMenu menu = new PopupMenu(view.getContext(), view);
        menu.setOnMenuItemClickListener(handle);
        if (items != null && items.size() > 0) {
            for (String item : items) {
                if (item != null) {
                    menu.getMenu().add(1, items.size() - items.indexOf(item) - 1, items.size() - items.indexOf(item) - 1, item);
                }
            }
        }
        return menu;
    }


}
