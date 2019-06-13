package com.freehand.base_component.core.navigate;

import android.support.annotation.AnimRes;
import android.support.annotation.AnimatorRes;
import android.support.v4.app.FragmentTransaction;

import com.freehand.base_component.core.activity.BaseActivity;
import com.freehand.base_component.core.fragment.BaseFragment;
import com.freehand.base_component.core.inteface.OnDelegate;
import com.freehand.base_component.core.inteface.OnGener;

/**
 * Created by minhpham on 12/19/18.
 * Purpose: .
 * Copyright © 2018 Pham Duy Minh. All rights reserved.
 */
public class Navigator implements INavigator {
    private OnGener<BaseFragment> frgGenner;
    private Strategy strategy = Strategy.NONE;
    private boolean save = false;
    private OnDelegate<BaseActivity> onNavigate;
    private OnDelegate<FragmentTransaction> onTransaction;

    public static Navigator make(){
        return new Navigator();
    }

    @Override
    public BaseFragment getFragment() {
        if (frgGenner != null) {
            return frgGenner.generate();
        }
        return null;
    }

    public<T extends BaseFragment> Navigator fragment(Class<T> tClass) {
        this.frgGenner = new DefaultGenner(tClass);
        strategy = Strategy.ADD;
        return this;
    }

    public void execute(){
        NavigateManager.navigate(this);
    }

    public Navigator fragment(OnGener<BaseFragment> frgGenner) {
        this.frgGenner = frgGenner;
        strategy = Strategy.ADD;
        return this;
    }

    @Override
    public Strategy getStrategy() {
        return strategy;
    }

    public Navigator strategy(Strategy strategy) {
        this.strategy = strategy;
        return this;
    }

    @Override
    public boolean shouldAddToBackStack() {
        return save;
    }

    @Override
    public void setCustomTransaction(FragmentTransaction ft) {
        if (onTransaction != null) {
            onTransaction.onDelegate(ft);
        }
    }

    @Override
    public void onCustomNavigate(BaseActivity activity) {
        if (onNavigate != null) {
            onNavigate.onDelegate(activity);
        }
    }

    public Navigator enableBack() {
        save = true;
        return this;
    }

    public Navigator setCustomAnimations(@AnimatorRes @AnimRes int enter, @AnimatorRes @AnimRes int exit) {
        this.onTransaction = new AnimTransaction(enter, exit);
        return this;
    }

    public Navigator setCustomAnimations(@AnimatorRes @AnimRes int enter, @AnimatorRes @AnimRes int exit, @AnimatorRes @AnimRes int popEnter, @AnimatorRes @AnimRes int popExit) {
        this.onTransaction = new AnimTransaction(enter, exit, popEnter, popExit);
        return this;
    }

    public Navigator setCustomTransaction(OnDelegate<FragmentTransaction> handle) {
        this.onTransaction = handle;
        return this;
    }

    public Navigator onCustomNavigate(OnDelegate<BaseActivity> handle) {
        this.onNavigate = handle;
        return this;
    }

    static class AnimTransaction implements OnDelegate<FragmentTransaction> {
        private int[] anims;

        public AnimTransaction(@AnimatorRes @AnimRes int enter, @AnimatorRes @AnimRes int exit) {
            anims = new int[]{enter, exit};
        }

        public AnimTransaction(@AnimatorRes @AnimRes int enter, @AnimatorRes @AnimRes int exit, @AnimatorRes @AnimRes int popEnter, @AnimatorRes @AnimRes int popExit) {
            anims = new int[]{enter, exit, popEnter, popExit};
        }

        @Override
        public void onDelegate(FragmentTransaction data) {
            if (anims == null || anims.length < 2) return;
            if (anims.length == 2) {
                data.setCustomAnimations(anims[0], anims[1]);
            } else if (anims.length == 4) {
                data.setCustomAnimations(anims[0], anims[1], anims[2], anims[3]);
            }
        }
    }

    static class DefaultGenner<T extends BaseFragment> implements OnGener<T> {
        private final Class<T> clazz;

        public DefaultGenner(Class<T> clazz) {
            this.clazz = clazz;
        }

        @Override
        public T generate() {
            if (clazz == null) return null;
            try {
                return clazz.newInstance();
            } catch (Exception e) {
                return null;
            }
        }
    }
}
