package com.freehand.base_component.core.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * The class is used to save session token throughout communication
 *
 * @author Nam Hoai Nguyen
 */
public class SavedStore {

    private static final String PREFS_KEY = "account-session";

    /**
     * Save string.
     *
     * @param key   the key
     * @param value the value
     * @return true, if successful
     */
    public static boolean saveString(Context context, String key, String value) {
        Editor editor = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE).edit();
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
    public static String getString(Context context, String key, String defValue) {
        SharedPreferences savedSession = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
        return savedSession.getString(key, defValue);
    }

    /**
     * Save boolean.
     *
     * @param key   the key
     * @param value the value
     * @return true, if successful
     */
    public static boolean saveBoolean(Context context, String key, boolean value) {
        Editor editor = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE).edit();
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
    public static boolean getBoolean(Context context, String key, boolean defValue) {
        SharedPreferences savedSession = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
        return savedSession.getBoolean(key, defValue);
    }

    public static boolean saveInt(Context context, String key, int value) {
        Editor editor = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE).edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    public static int getInt(Context context, String key, int defValue) {
        SharedPreferences savedSession = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
        return savedSession.getInt(key, defValue);
    }

    public static boolean saveLong(Context context, String key, long value) {
        Editor editor = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE).edit();
        editor.putLong(key, value);
        return editor.commit();
    }

    public static long getLong(Context context, String key, long defValue) {
        SharedPreferences savedSession = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
        return savedSession.getLong(key, defValue);
    }

    /**
     * Clear.
     *
     * @param objKey the obj key
     */
    public static void clear(Context context, String objKey) {
        Editor editor = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE).edit();
        editor.remove(objKey);
        editor.apply();
    }
}
