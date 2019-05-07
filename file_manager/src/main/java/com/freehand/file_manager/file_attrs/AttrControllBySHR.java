package com.freehand.file_manager.file_attrs;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;


import com.freehand.file_manager.ApplicationProvider;

import java.io.File;


/**
 * Created by minhpham on 2/17/17.
 * Purpose: Implement IAttributeControl store file attr on ShareReference
 */
public class AttrControllBySHR implements IAttributeControl {
    private static final String FILE_ATTR_STORAGE = "com.nexes.manager.file_manage.file_attrs.attr_stored";
    private static AttrControllBySHR ourInstance = new AttrControllBySHR();
    private static SharedPreferences share;

    //should add application context
    public static AttrControllBySHR getInstance() {
        share = PreferenceManager.getDefaultSharedPreferences(ApplicationProvider.getInstance().getApplication());
        return ourInstance;
    }

    private AttrControllBySHR() {
    }

    @Override
    public void addAttribute(File file, String attr, String value) {
        SharedPreferences.Editor editor = share.edit();
        int key = (file.getPath()+attr).hashCode();
        editor.putString(key+"",value);
        editor.commit();
    }

    @Override
    public String getAttribute(File file, String attr) {
        int key = (file.getPath()+attr).hashCode();
        return share.getString(key+"",null);
    }

    @Override
    public void removeAttribute(File file, String attr) {
        int key = (file.getPath()+attr).hashCode();
        SharedPreferences.Editor edit = share.edit();
        edit.remove(key+"");
        edit.commit();
    }
}
