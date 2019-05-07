package com.freehand.file_manager.filter;

import android.support.annotation.NonNull;

import org.json.JSONObject;

import java.io.FileFilter;

/**
 * Created by minhpham on 2/23/17.
 * Purpose: define the way accept file by implement FileFilter
 * and some function support save and load filter on file
 */

public abstract class AFilter implements FileFilter {
    /**
     * if true, filter only accept file, otherwise file and directory will accept
     */
    public boolean filterFileOnly = true;

    /**
     * if true, filter accept hidden file, otherwise hidden file will deny
     */
    public boolean showHiddenFile = false;
    public AFilter showHidden(boolean flag){
        showHiddenFile = flag;
        return this;
    }
    public AFilter(Object args) {

    }
    //this constructor use for gen instance by class name
    public AFilter() {

    }

    /**
     * parse json to filter argument, use when parse json to instance
     * @param json
     */
    public abstract void parseArgument(String json);

    /**
     * export instance to json, use in store filter to file
     * @return
     */
    public abstract JSONObject exportJson();

}
