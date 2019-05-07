package com.freehand.file_manager.filter.ruler;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import com.freehand.file_manager.filter.AFilter;

/**
 * Created by minhpham on 2/27/17.
 * Purpose: filter file based on their last modified
 */

public class FilterLastModify extends AFilter {

    //time life of file count from last modify.
    private long duration;

    //1 day,2 day,1 month converst to milisecond
    public FilterLastModify(long duration) {
        super(duration);
        this.duration = duration;
    }

    public FilterLastModify() {
    }

    /**
     * Indicating whether a specific file should be included in a pathname list.
     *
     * @param pathname the abstract file to check.
     * @return {@code true} if the file should be included, {@code false}
     * otherwise.
     */
    @Override
    public boolean accept(File pathname) {
        if (pathname.exists()) {
            long duration = System.currentTimeMillis() - pathname.lastModified();
            if (this.duration <= duration) return true;
        }
        return false;
    }

    @Override
    public JSONObject exportJson() {
        JSONObject json = new JSONObject();
        try {
            json.putOpt(getClass().getName(),duration);
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
            duration = json.optLong(getClass().getName());
        }catch (JSONException e){
            e.printStackTrace();
            duration = Long.MAX_VALUE;
        }
    }
}
