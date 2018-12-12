package com.freehand.base_component.core.activity;

/**
 * Created by minhpham on 5/4/17.
 * Purpose: .
 * Copyright © 2017 Pham Duy Minh. All rights reserved.
 */

public abstract class SmartNavigateActivity extends BaseDrawerActivity {


    /**
     * show list fragment in backstack
     *
     * @param v
     */
//    public boolean showQuickNavigate(View v) {
//        List<Fragment> frgs = getSupportFragmentManager().getFragments();
//
//        //if size <=2 not show quick navigate
//        if (frgs == null || frgs.size() <= 2) return false;
//        List<String> reals = new ArrayList<>();
//        // when pop stack getSupportFragmentManager() return list with size and real items not match
//        // we need to create new list with size and real item matched
//        for (int i = 0; i < frgs.size(); i++) {
//            Fragment frg = frgs.application(i);
//            if (frg != null && frg instanceof Fragment) {
//                reals.add(frg.getTag());
//            }
//        }
//        // remove current fragment
//        reals.remove(reals.size() - 1);
//        //if size <=2 not show quick navigate
//        if (reals == null || reals.size() <= 2) return false;
//
//        PopupMenu menu = DialogUtils.createPopupMenu(v, new PopupMenu.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                //remove current fragment
//                getSupportFragmentManager().popBackStack();
//                //remove to selected item
//                int num = item.getItemId();
//                for (int i = 0; i < num; i++) {
//                    getSupportFragmentManager().popBackStack();
//                }
//                return false;
//            }
//        }, reals);
//        menu.show();
//        return true;
//    }
}
