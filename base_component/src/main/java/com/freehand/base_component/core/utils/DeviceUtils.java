/**
 * Copyright (c) 2014 CoderKiss
 * <p/>
 * CoderKiss[AT]gmail.com
 */

package com.freehand.base_component.core.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import static com.freehand.base_component.core.ApplicationProvider.application;

//hardware related functions
public final class DeviceUtils {

    public static final String TAG = DeviceUtils.class.getSimpleName();

    public static int SCREEN_WIDTH, SCREEN_HEIGHT;

    public static void initialize() {
        Point point = getScreenSize();
        SCREEN_WIDTH = Math.max(point.x, point.y);
        SCREEN_HEIGHT = Math.min(point.x, point.y) - getNavigationBarHeight(application());
    }

    public static Point getRealScreenSize() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) application().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        return new Point(width, height);
    }

    public static final Point getScreenSize() {
        WindowManager wm = (WindowManager) application().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        // since SDK_INT = 1;
        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;
        // includes window decorations (status bar bar/menu bar)
        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17) {
            try {
                widthPixels = (Integer) Display.class.getMethod("getRawWidth")
                        .invoke(display);
                heightPixels = (Integer) Display.class
                        .getMethod("getRawHeight").invoke(display);
            } catch (Exception ignored) {
            }
        }
        // includes window decorations (status bar bar/menu bar)
        else if (Build.VERSION.SDK_INT >= 17) {
            try {
                Point realSize = new Point();
                Display.class.getMethod("getRealSize", Point.class).invoke(
                        display, realSize);
                widthPixels = realSize.x;
                heightPixels = realSize.y;
            } catch (Exception ignored) {
            }
        }
        return new Point(widthPixels, heightPixels);
    }

    public static int getWidthPercentage(float percent) {
        return (int) (SCREEN_WIDTH * percent / 100);
    }

    public static int getHeightPercentage(float percent) {
        return (int) (SCREEN_HEIGHT * percent / 100);
    }

    public static final float getScreenDensity() {
        Resources resources = application().getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.density;
    }

    public static int getScreenDensityDpi() {
        Resources resources = application().getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.densityDpi;
    }

    public static int dp2px(int dip) {
        Resources resources = application().getResources();
        return Math
                .round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        dip, resources.getDisplayMetrics()));
    }

    public static int px2dp(int px) {
        DisplayMetrics displayMetrics = application().getResources()
                .getDisplayMetrics();
        return Math.round(px
                / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static int sp2px(float sp) {
        final float scale = application().getResources().getDisplayMetrics().scaledDensity;
        return Math.round(sp * scale);
    }

    /**
     * application height of navigation bar
     *
     * @param context
     * @return
     */
    public static int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static boolean isDevicePortrait() {
        return application().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }
    
    public static String getDeviceID() {
        return Settings.Secure.getString(application().getContentResolver(), Settings.Secure.ANDROID_ID);
    }


}
