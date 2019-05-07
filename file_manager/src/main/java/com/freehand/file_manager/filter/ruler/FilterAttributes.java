package com.freehand.file_manager.filter.ruler;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.text.TextUtils;

import com.freehand.file_manager.file_attrs.AttributeProvider;
import com.freehand.file_manager.filter.AFilter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by minhpham on 2/18/17.
 * Purpose: filter file based on their attributes
 */

public class FilterAttributes extends AFilter {

    private Pair<String, String> pair;

    public FilterAttributes() {
    }

    public FilterAttributes(Pair<String, String> pair) {
        super(pair);
        this.pair = pair;
    }

    @Override
    public boolean accept(File file) {
        if (pair == null || TextUtils.isEmpty(pair.first) || TextUtils.isEmpty(pair.second))
            return false;
        return pair.second.equals(AttributeProvider.getInstance().getAttribute(file, pair.first));
    }

    @Override
    public JSONObject exportJson() {
        JSONObject json = new JSONObject();
        JSONObject value = new JSONObject();
        try {
            value.putOpt("key", pair.first);
            value.putOpt("value", pair.second);
            json.putOpt(getClass().getName(), value);
            return json;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @NonNull
    @Override
    public void parseArgument(String content) {
        try {
            JSONObject json = new JSONObject(content);
            pair = new Pair<>(json.optString("key"), json.optString("value"));
        } catch (JSONException e) {
            pair = null;
        }
    }

}
