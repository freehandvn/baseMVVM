/*
 * Copyright 2015 Mike Penz All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.freehand.base_component.core.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by mikepenz on 14.03.15.
 * This class implements a hack to change the layout padding on bottom if the keyboard is shown
 * to allow long lists with editTextViews
 * Basic idea for this solution found here: http://stackoverflow.com/a/9108219/325479
 */
public class KeyboardUtil {
    private View decorView;
    private View contentView;
    private Integer keyboardHeight;
    private Integer padding = null;
    private int pre = -1;
    //a small helper to allow showing the editText focus
    ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {

        boolean isPadding = false;

        @Override
        public void onGlobalLayout() {
            Rect r = new Rect();
            //r will be populated with the coordinates of your view that area still visible.
            decorView.getWindowVisibleDisplayFrame(r);
            //application screen height and calculate the difference with the useable area from the r
            int height = decorView.getContext().getResources().getDisplayMetrics().heightPixels;
            int temp = height - r.bottom;
            if (pre == temp) return;
            pre = temp;
            Log.d("onGlobalLayout", "moveTo() called :" + " diff: " + temp);

            View focus = getFocusView(contentView);
            Rect rec = null;
            if (focus != null) {
                rec = new Rect();
                focus.getGlobalVisibleRect(rec);
            }
            //if it could be a keyboard add the padding to the view
            if (temp != 0 && !isPadding) {
                // if the use-able screen height differs from the total screen height we assume that it shows a keyboard now
                //check if the padding is 0 (if yes set the padding for the keyboard)
                keyboardHeight = temp;
                if(rec == null) {
                    padding = keyboardHeight/2;
                }else {
                    padding = Math.abs(height - rec.bottom - keyboardHeight) + 40;
                }


                if (rec != null && rec.top < padding && !isPadding)
                    return;
                isPadding = true;
                moveTo(contentView, padding);
            } else if (isPadding) {
                isPadding = false;
                //reset the padding of the contentView
//                    contentView.setPadding(0, 0, 0, 0);
                moveTo(contentView, -padding);
            }
        }
    };
    private float percent = 0.5f;

    public KeyboardUtil(Activity act, View contentView) {
        this.decorView = act.getWindow().getDecorView();
        this.contentView = contentView;

        //only required on newer android versions. it was working on API level 19
        if (Build.VERSION.SDK_INT >= 19) {
            decorView.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
        }
    }

    public KeyboardUtil(Activity act, View contentView, float percent) {
        this(act, contentView);
        this.percent = percent;
    }

    /**
     * Helper to hide the keyboard
     *
     * @param act
     */
    public static void hideKeyboard(Activity act) {
        if (act != null && act.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) act.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(act.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void enable() {
        if (Build.VERSION.SDK_INT >= 19) {
            decorView.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
        }
    }

    public void disable() {
        if (Build.VERSION.SDK_INT >= 19) {
            decorView.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener);
        }
    }

    private void moveTo(View contentView, int diff) {
        contentView.setY(contentView.getY() - diff);
    }

    private View getFocusView(View group) {
        if (group instanceof EditText && group.isFocused()) return group;
        if (!(group instanceof ViewGroup)) return null;
        if (group instanceof ViewGroup) {
            int n = ((ViewGroup) group).getChildCount();
            for (int i = 0; i < n; i++) {
                View focus = getFocusView(((ViewGroup) group).getChildAt(i));
                if (focus != null) return focus;
            }
        }
        return null;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }

    public static ViewTreeObserver.OnGlobalLayoutListener addKeyboardVisibilityListener(final View rootLayout, final OnKeyboardVisibilityListener keyboardVisibilityListener) {
        ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                rootLayout.getWindowVisibleDisplayFrame(r);
                int screenHeight = rootLayout.getRootView().getHeight();

                // r.bottom is the position above soft keypad or device button.
                // if keypad is shown, the r.bottom is smaller than that before.
                int keypadHeight = screenHeight - r.bottom;

                boolean isVisible = keypadHeight > screenHeight * 0.15; // 0.15 ratio is perhaps enough to determine keypad height.
                keyboardVisibilityListener.onVisibilityChange(isVisible);
            }
        };
        rootLayout.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);
        return globalLayoutListener;
    }

    public interface OnKeyboardVisibilityListener {
        void onVisibilityChange(boolean isVisible);
    }

}