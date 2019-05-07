package com.freehand.file_manager.filter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import io.reactivex.annotations.NonNull;
import com.freehand.file_manager.ParseConfig;

/**
 * Created by minhpham on 2/26/17.
 * Purpose: Filter combine all child filter by and operator or
 */

public class FilterCompoundOr extends AFilter {

    private AFilter[] args;

    public FilterCompoundOr(@NonNull AFilter... args) {
        super(args);
        this.args = args;
    }

    public FilterCompoundOr() {
    }

    @Override
    public boolean accept(File file) {
        if(args == null || args.length == 0) return false;

        for ( AFilter rule: args) {
            if(rule.accept(file)) return true;
        }
        return false;
    }


    @Override
    public JSONObject exportJson() {
        JSONObject json = new JSONObject();
        JSONArray arr = new JSONArray();
        try {
            for (AFilter filter : args) {
                arr.put(filter.exportJson());
            }
            json.putOpt(getClass().getName(), arr);
            return json;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @android.support.annotation.NonNull
    @Override
    public void parseArgument(String json) {
        try {
            JSONArray arr = new JSONArray(json);
            if(arr.length() == 0) return;
            args = new AFilter[arr.length()];
            for(int i =0;i<arr.length();i++){
                JSONObject temp = arr.optJSONObject(i);
                String key = temp.keys().next();
                AFilter filter = ParseConfig.genFilterByName(key);
                if (filter != null) {
                    filter.parseArgument(temp.optString(key));
                }
                args[i] = filter;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            args = null;
        }

    }
}
