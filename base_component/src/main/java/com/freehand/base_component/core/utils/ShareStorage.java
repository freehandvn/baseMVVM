package com.freehand.base_component.core.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.freehand.base_component.core.ApplicationProvider.application;

/**
 * Created by minhpham on 12/12/18.
 * Purpose: wrap SharedPreferences
 * Copyright © 2018 Pham Duy Minh. All rights reserved.
 */
public class ShareStorage {

    private static Map<String, ShareStorage> share = new HashMap<>();
    private SharedPreferences sharedPreferences;

    private ShareStorage(String key, int mode) {
        sharedPreferences = application().getSharedPreferences(key, mode);
    }

    public static ShareStorage get(String key) {
        return get(key, Context.MODE_PRIVATE);
    }

    public static ShareStorage get(String key, int mode) {
        String storedKey = getStorageKey(key, mode);
        if (!share.containsKey(storedKey)) {
            share.put(storedKey, new ShareStorage(key, mode));
        }
        return share.get(storedKey);
    }

    private static String getStorageKey(String key, int mode) {
        return key + mode;
    }

    public static void release() {
        if (share.size() == 0) return;
        Set<String> keys = share.keySet();
        for (String key : keys) {
            share.get(key).destroy();
        }
        share.clear();
    }

    public static void clearAllStorage() {
        if (share.size() == 0) return;
        Set<String> keys = share.keySet();
        for (String key : keys) {
            share.get(key).clearAll();
        }
        share.clear();
    }

    /**
     * Save string.
     *
     * @param key   the key
     * @param value the value
     * @return true, if successful
     */
    public boolean saveString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    /**
     * Gets the string.
     *
     * @param key      the key
     * @param defValue the default value
     * @return the string
     */
    public String getString(String key, String defValue) {
        return sharedPreferences.getString(key, defValue);
    }

    /**
     * Save boolean.
     *
     * @param key   the key
     * @param value the value
     * @return true, if successful
     */
    public boolean saveBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    /**
     * Gets the boolean.
     *
     * @param key      the key
     * @param defValue the default value
     * @return the boolean
     */
    public boolean getBoolean(String key, boolean defValue) {
        return sharedPreferences.getBoolean(key, defValue);
    }

    public boolean saveInt(String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    public int getInt(String key, int defValue) {
        return sharedPreferences.getInt(key, defValue);
    }

    public boolean saveLong(String key, long value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        return editor.commit();
    }

    public long getLong(String key, long defValue) {
        return sharedPreferences.getLong(key, defValue);
    }

    /**
     * Clear.
     *
     * @param objKey the obj key
     */
    public void clear(String objKey) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(objKey);
        editor.apply();
    }

    public void destroy() {
        sharedPreferences = null;
    }

    public void clearAll() {
        sharedPreferences.edit().clear().apply();
        sharedPreferences = null;
    }
}
