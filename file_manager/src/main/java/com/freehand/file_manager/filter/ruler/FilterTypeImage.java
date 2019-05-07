package com.freehand.file_manager.filter.ruler;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import com.freehand.file_manager.filter.AFilter;

/**
 * Created by minhpham on 2/17/17.
 * Purpose: filter file image
 */

public class FilterTypeImage extends AFilter {

    public FilterTypeImage(Object args) {
        super(args);
    }

    @Override
    public boolean accept(File file) {
        String name = file.getName();
        if (TextUtils.isEmpty(name) || !name.contains(".")) return false;
        String item_ext = name.substring(name.lastIndexOf("."), name.length());

        if (item_ext.equalsIgnoreCase(".jpeg") ||
                item_ext.equalsIgnoreCase(".jpg") ||
                item_ext.equalsIgnoreCase(".png") ||
                item_ext.equalsIgnoreCase(".gif") ||
                item_ext.equalsIgnoreCase(".tiff"))
            return true;
        return false;
    }

    public FilterTypeImage() {
    }

    @Override
    public JSONObject exportJson() {
        JSONObject json = new JSONObject();
        try {
            json.putOpt(getClass().getName(),"");
            return json;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @NonNull
    @Override
    public void parseArgument(String json) {
        return ;
    }

}
