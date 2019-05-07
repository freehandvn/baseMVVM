package com.freehand.file_manager.filter.ruler;


import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import com.freehand.file_manager.filter.AFilter;

/**
 * Created by minhpham on 2/17/17.
 * Purpose: filter file based on their name
 */

public class FilterByName extends AFilter {
    private String key;

    public FilterByName(Object args) {
        super(args);
        this.key = (String) args;
    }

    public FilterByName() {
    }

    public FilterByName changeKey(String key) {
        this.key = key;
        return this;
    }

    @Override
    public boolean accept(File path) {
        if (path == null || !path.exists()) return false;
        if(TextUtils.isEmpty(key)) return true;
        return path.getName().toLowerCase().contains(this.key.toLowerCase());
    }

    @Override
    public JSONObject exportJson() {
        JSONObject json = new JSONObject();
        try {
            json.putOpt(getClass().getName(), key);
            return json;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @NonNull
    @Override
    public void parseArgument(String content) {
        JSONObject json ;
        try {
            json = new JSONObject(content);
            key = json.optString(getClass().getName());
        } catch (JSONException e) {
            e.printStackTrace();
            key = null;
        }
    }

}
