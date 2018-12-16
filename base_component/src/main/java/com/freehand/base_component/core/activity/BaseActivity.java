package com.freehand.base_component.core.activity;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.freehand.base_component.core.fragment.BaseFragment;
import com.freehand.base_component.core.navigate.INavigator;
import com.freehand.base_component.core.navigate.Navigate;
import com.freehand.base_component.core.utils.CodeUtils;
import com.freehand.dynamicfunction.SafeObserver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import io.reactivex.functions.Action;

/**
 * Created by minhpham on 11/20/18.
 * Purpose: .
 * Copyright © 2018 Pham Duy Minh. All rights reserved.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private Map<Integer, Stack<Fragment>> stackMap = new HashMap<>();
    private int currentMenu = 0;
    private boolean isBackground;
    private SafeObserver<INavigator> navigateDispose;


    protected abstract int getFragmentContainerResId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(defineLayout());
        initView();
        listenerNavigate();
    }

    private void listenerNavigate() {
        navigateDispose = Navigate.getInstance().getChannel().subscribeWith(new SafeObserver<INavigator>() {
            @Override
            public boolean accept(INavigator navigator) {
                if (isFinishing()) return true;
                pushFragment(navigator);
                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        isBackground = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isBackground = true;
    }

    protected abstract void initView();

    protected abstract int defineLayout();

    public void dismissDialog(String tag) {
        DialogFragment dialogFragment = (DialogFragment) getSupportFragmentManager().findFragmentByTag(tag);
        if (dialogFragment != null) dialogFragment.dismissAllowingStateLoss();
    }

    @Override
    protected void onDestroy() {
        clearAll();
        if (navigateDispose != null) {
            navigateDispose.dispose();
            navigateDispose = null;
        }
        super.onDestroy();
    }

    protected void add2BackStack(Fragment fragment) {
        if (!stackMap.containsKey(currentMenu)) stackMap.put(currentMenu, new Stack<Fragment>());
        stackMap.get(currentMenu).push(fragment);
    }

    protected void replaceFromBackStack(Fragment fragment) {
        if (!stackMap.containsKey(currentMenu)) stackMap.put(currentMenu, new Stack<Fragment>());
        if (stackMap.get(currentMenu).size() >= 1) {
            stackMap.get(currentMenu).pop();
        }
        stackMap.get(currentMenu).push(fragment);
    }

    public void pushFragment(final Fragment fragment, final boolean shouldAdd) {
        CodeUtils.runOnMainThread(new Action() {
            @Override
            public void run() throws Exception {
                if (shouldAdd) {
                    add2BackStack(fragment);
                }
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction ft = manager.beginTransaction();
                Fragment currentFragment = getCurrentFragment();
                if (currentFragment != null) {
                    ft.hide(currentFragment);
                    if (currentFragment instanceof BaseFragment) {
                        ((BaseFragment) currentFragment).onBecomeInvisible();
                    }
                }
                ft.add(getFragmentContainerResId(), fragment, fragment.getClass().getSimpleName());
                ft.commitAllowingStateLoss();
//                manager.executePendingTransactions();
                if (fragment instanceof BaseFragment) {
                    ((BaseFragment) fragment).onBecomeVisible();
                }
            }
        });
    }

    public void replaceFragment(final Fragment fragment, final boolean shouldAdd) {
        CodeUtils.runOnMainThread(new Action() {
            @Override
            public void run() throws Exception {
                if (shouldAdd) {
                    replaceFromBackStack(fragment);
                }
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction ft = manager.beginTransaction();
                Fragment currentFragment = getCurrentFragment();
                if (currentFragment != null) {
                    ft.hide(currentFragment);
                    if (currentFragment instanceof BaseFragment) {
                        ((BaseFragment) currentFragment).onBecomeInvisible();
                    }
                }
                ft.replace(getFragmentContainerResId(), fragment, fragment.getClass().getSimpleName());
                ft.commitAllowingStateLoss();
//                manager.executePendingTransactions();
                if (fragment instanceof BaseFragment) {
                    ((BaseFragment) fragment).onBecomeVisible();
                }
            }
        });
    }

    public void pushFragmentWithoutHidingCurrent(final Fragment fragment, final boolean shouldAdd) {
        CodeUtils.runOnMainThread(new Action() {
            @Override
            public void run() throws Exception {
                if (shouldAdd) {
                    add2BackStack(fragment);
                }
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction ft = manager.beginTransaction();
                ft.add(getFragmentContainerResId(), fragment, fragment.getClass().getSimpleName());
                ft.commitAllowingStateLoss();
                manager.executePendingTransactions();
                if (fragment instanceof BaseFragment) {
                    ((BaseFragment) fragment).onBecomeVisible();
                }
            }
        });
    }

    public void pushFragment(INavigator navigator) {
        if (navigator.getFragment() == null || navigator.getStrategy() == Navigate.Strategy.POP)
            return;
        CodeUtils.runOnMainThread(() -> {
            BaseFragment fragment = navigator.getFragment();
            if (navigator.shouldAddToBackStack()) {
                if (navigator.getStrategy() == Navigate.Strategy.REPLACE) {
                    replaceFromBackStack(fragment);
                } else {
                    add2BackStack(fragment);
                }
            }

            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            navigator.setCustomAnimation(ft);

            if (navigator.getStrategy() != Navigate.Strategy.OVERLAP) {
                Fragment currentFragment = getCurrentFragment();
                if (currentFragment != null) {
                    ft.hide(currentFragment);
                    if (currentFragment instanceof BaseFragment) {
                        ((BaseFragment) currentFragment).onBecomeInvisible();
                    }
                }
            }

            if (navigator.getStrategy() == Navigate.Strategy.REPLACE) {
                ft.replace(getFragmentContainerResId(), fragment, fragment.getClass().getSimpleName());
            } else {
                ft.add(getFragmentContainerResId(), fragment, fragment.getClass().getSimpleName());
            }

            ft.commitAllowingStateLoss();
//                manager.executePendingTransactions();
            if (fragment instanceof BaseFragment) {
                fragment.onBecomeVisible();
            }
        });
    }

    public void pushFragmentNonReplace(Fragment fragment) {
        add2BackStack(fragment);
    }

    public void popFragment() {
        CodeUtils.runOnMainThread(new Action() {
            @Override
            public void run() throws Exception {
                final Stack<Fragment> stackFragment = stackMap.get(currentMenu);
                if (stackFragment != null && stackFragment.size() > 1) {
                    final Fragment fragment = stackFragment.elementAt(stackFragment.size() - 2);
                    stackFragment.pop();
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction ft = manager.beginTransaction();
                    Fragment currentFragment = getCurrentFragment();
                    if (currentFragment != null) {
                        if (currentFragment instanceof BaseFragment) {
                            ((BaseFragment) currentFragment).onBecomeInvisible();
                        }
                        ft.remove(currentFragment);
                    }
                    if (!fragment.isVisible()) {
                        ft.show(fragment);
                        if (fragment instanceof BaseFragment) {
                            ((BaseFragment) fragment).onBecomeVisible();
                        }
                    }
                    ft.commitAllowingStateLoss();
                    manager.executePendingTransactions();
                }
            }
        });
    }

    public void clearFragmentsInStack(int menuType) {
        final Stack<Fragment> fragmentStack = stackMap.get(menuType);
        if (fragmentStack != null) {
            while (!fragmentStack.isEmpty()) {
                Fragment fragment = fragmentStack.pop();
                if (fragment != null) {
                    getSupportFragmentManager().beginTransaction().remove(fragment).commitAllowingStateLoss();
                }
            }
            getSupportFragmentManager().executePendingTransactions();
            fragmentStack.clear();
        }
    }

    public void clearFragmentsInCurrentStack() {
        clearFragmentsInStack(currentMenu);
    }

    public void clearAll() {
        Fragment frg = getCurrentFragment();
        if (frg instanceof BaseFragment) {
            ((BaseFragment) frg).onBecomeInvisible();
        }
        for (Integer key : stackMap.keySet()) {
            clearFragmentsInStack(key);
        }
    }

    public void switchToOtherMenu(int newMenuType, Fragment newFragment) {
        Fragment frg = getCurrentFragment();
        if (frg instanceof BaseFragment) {
            ((BaseFragment) frg).onBecomeInvisible();
        }
        clearFragmentsInCurrentStack();
        setCurrentMenu(newMenuType);
        pushFragment(newFragment, true);
    }

    /**
     * @return
     */
    public void setCurrentMenu(int menuType) {
        if (currentMenu != menuType) {
            clearFragmentsInCurrentStack();
            currentMenu = menuType;
        }
    }

    public Integer getCurrentMenuType() {
        return currentMenu;
    }

    public Fragment getLastFragmentInStack() {
        Stack<Fragment> stack = stackMap.get(currentMenu);
        if (stack == null || stack.size() == 0) {
            return null;
        }
        return stack.lastElement();
    }

    public Stack<Fragment> getFragmentsInCurrentMenu() {
        return stackMap.get(currentMenu);
    }

    @Override
    public void onBackPressed() {
        Stack<Fragment> stack = stackMap.get(currentMenu);
        if (stack != null && stack.size() <= 1) {
            if (!handleExitActivity())
                super.onBackPressed(); // or call finish..
        } else {
            Fragment currentFragment = getCurrentFragment();
            if (currentFragment instanceof BaseFragment) {
                if (!((BaseFragment) currentFragment).handleBackPressed()) popFragment();
            } else {
                popFragment();
            }
        }
    }

    protected boolean handleExitActivity() {
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Fragment frg = getCurrentFragment();
        if (frg instanceof BaseFragment) {
            ((BaseFragment) frg).onBecomeInvisible();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Fragment frg = getCurrentFragment();
        if (frg instanceof BaseFragment) {
            ((BaseFragment) frg).onBecomeVisible();
        }
    }

    public Fragment getCurrentFragment() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(getFragmentContainerResId());
        if (currentFragment != null && currentFragment.isVisible()) {
            return currentFragment;
        }
        return null;
    }

    public Fragment getBeforeCurrentFragment() {
        Stack<Fragment> stack = stackMap.get(currentMenu);
        if (stack == null) return null;
        return stack.get(stack.size() - 2);
    }

    public boolean isScreenShown(Class<?> aClass) {
        List<Fragment> fragments = getFragmentsInCurrentMenu();
        if (fragments != null && fragments.size() > 0) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.getClass().getName().equals(aClass.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    public Fragment getFragmentInCurrentMenu(Class<?> aClass) {
        List<Fragment> fragments = getFragmentsInCurrentMenu();
        if (fragments != null && fragments.size() > 0) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.getClass().getName().equals(aClass.getName())) {
                    return fragment;
                }
            }
        }
        return null;
    }

    public boolean isFragmentShowing(Class<?> aClass) {
        Fragment fragment = getCurrentFragment();
        return fragment != null && fragment.getClass().getSimpleName().equals(aClass.getSimpleName());
    }

    public Fragment findFragmentByTag(String tag) {
        return getSupportFragmentManager().findFragmentByTag(tag);
    }

    /**
     * Removes a fragment by class name out of the current stack and UI as well.
     *
     * @param fragmentClass the class of fragment.
     */
    public void removeFragmentOutOfStack(Class fragmentClass) {
        Stack<Fragment> fragments = getFragmentsInCurrentMenu();
        if (fragments != null && !fragments.isEmpty()) {
            for (int i = fragments.size() - 1; i >= 0; i--) {
                Fragment fragment = fragments.elementAt(i);
                if (fragment != null && fragment.getClass().getName().equals(fragmentClass.getName())) {
                    getSupportFragmentManager().beginTransaction().remove(fragment).commitAllowingStateLoss();
                    fragments.removeElementAt(i);
                    return;
                }
            }
        }
    }

    public boolean isBackground() {
        return isBackground;
    }
}
