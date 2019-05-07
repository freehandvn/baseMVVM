package com.freehand.file_manager.filter.ruler;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import com.freehand.file_manager.filter.AFilter;

/**
 * Created by minhpham on 2/17/17.
 * Purpose: filter file video
 */

public class FilterTypeVideo extends AFilter {

    public FilterTypeVideo(Object args) {
        super(args);
    }

    public FilterTypeVideo() {
    }

    @Override
    public boolean accept(File file) {
        String name = file.getName();
        if (TextUtils.isEmpty(name) || !name.contains(".")) return false;
        String item_ext = name.substring(name.lastIndexOf("."), name.length());
        if (item_ext.equalsIgnoreCase(".m4v") ||
                item_ext.equalsIgnoreCase(".3gp") ||
                item_ext.equalsIgnoreCase(".wmv") ||
                item_ext.equalsIgnoreCase(".mp4") ||
                item_ext.equalsIgnoreCase(".ogg") ||
                item_ext.equalsIgnoreCase(".wav"))
            return true;
        return false;
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
