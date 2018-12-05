package com.freehand.base_component.core.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static android.support.v4.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
import static android.support.v4.widget.DrawerLayout.LOCK_MODE_UNLOCKED;

/**
 * Created by minhpham on 11/20/18.
 * Purpose: .
 * Copyright © 2018 Pham Duy Minh. All rights reserved.
 */

public abstract class BaseDrawerActivity extends BaseActivity {

    protected DrawerLayout drawerLayout;
    private boolean isStillAlive, isBackground;

    /**
     * add quick navigate when press back button on action bar
     * the back button has customize
     * should not show title on action bar, the title will locate on start, left of back button
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(null);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    protected abstract DrawerLayout getDrawer();

    @Override
    protected void onStart() {
        super.onStart();
        isBackground = false;
    }

    @Override
    protected void onStop() {
        isBackground = true;
        super.onStop();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        isStillAlive = false;
    }

    /**
     * init view when create activity
     */
    protected void initView() {
        isStillAlive = true;
        drawerLayout = getDrawer();
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
    }

    public void openAndCloseLeftMenu() {
        if (drawerLayout.isDrawerOpen(Gravity.START)) {
            drawerLayout.closeDrawer(Gravity.START);
        } else {
            drawerLayout.openDrawer(Gravity.START);
        }
    }

    public Observable<Boolean> closeLeftMenuDelayed() {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                closeLeftMenu();
                return true;
            }
        }).subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread()).debounce(2000, TimeUnit.MILLISECONDS);
    }

    private void replaceFragment(int containerResId, Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(containerResId, fragment, fragment.getClass().getSimpleName()).commitAllowingStateLoss();
    }

    protected void replaceLeftMenu(Fragment fragment) {
        replaceFragment(defineLeftMenuContainId(), fragment);
    }

    protected abstract int defineLeftMenuContainId();

    public void closeLeftMenu() {
        if (drawerLayout != null) {
            drawerLayout.closeDrawer(Gravity.START);
        }
    }

    public void disableDrawer() {
        drawerLayout.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED);
    }

    public void enableDrawer() {
        drawerLayout.setDrawerLockMode(LOCK_MODE_UNLOCKED);
    }
}
