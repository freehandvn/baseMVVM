package com.freehand.file_manager;

import android.util.Log;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import com.freehand.file_manager.filter.AFilter;
import com.freehand.file_manager.filter.FilterCompoundAnd;

/**
 * Created by minhpham on 3/2/17.
 * Purpose: helper support all function about store and load config file
 */

public class ParseConfig {

    /**
     * get instance of Filter class by name
     * @param name
     * @return
     */
    public static AFilter genFilterByName(String name) {
        try {
            Class<?> clazz = Class.forName(name);
            AFilter instance = (AFilter) clazz.newInstance();
            return instance;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * store filter to file
     * @param des
     * @param path
     * @param filter
     */
    public static void saveFilter(String des, File path, AFilter filter) {
        if (filter == null) return;
        try {
            JSONObject json = new JSONObject();
            json.putOpt("root_name", FileManage.getInstance().getHomePath());
            json.putOpt("description", des);
            JSONObject jsonFilter = filter.exportJson();
            if (jsonFilter == null) return;
            json.putOpt("filter", jsonFilter);
            FileWriter file = new FileWriter(path);
            file.write(json.toString());
            file.flush();
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG", "Error in Writing: " + e.getLocalizedMessage());
        }
    }

    /**
     * parse filter from file
     * @param file
     * @return
     */
    public static Observable<ConfigInformation> getFilter(final File file) {
        return Observable.create(new ObservableOnSubscribe<ConfigInformation>() {
            @Override
            public void subscribe(ObservableEmitter<ConfigInformation> e) throws Exception {
                //check whether file exists
                FileInputStream is = new FileInputStream(file);
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                String content = new String(buffer, "UTF-8");
                e.onNext(parseFilter(new JSONObject(content)));
                e.onComplete();

            }
        });

    }

    private static ConfigInformation parseFilter(JSONObject json) {
        if (json == null) return null;
        String rootName = json.optString("root_name");
        String des = json.optString("description");
        JSONObject filter = json.optJSONObject("filter");
        ArrayList<AFilter> list = new ArrayList<>();
        Iterator<String> inter = filter.keys();
        while (inter.hasNext()) {
            String key = inter.next();
            AFilter temp = genFilterByName(key);
            if (temp != null) {
                temp.parseArgument(filter.optString(key));
                list.add(temp);
            }
        }
        AFilter lastFilter;
        if (list.size() > 1) {
            lastFilter = new FilterCompoundAnd(list.toArray(new AFilter[list.size()]));
        } else if (list.size() == 1) {
            lastFilter = list.get(0);
        } else {
            return null;
        }
        return new ConfigInformation(rootName, des, lastFilter);
    }

    /**
     * hold on file config information
     */
    public static class ConfigInformation {
        public final String rootName;
        public final String description;
        public final AFilter filter;

        public ConfigInformation(String rootName, String description, AFilter filter) {
            this.rootName = rootName;
            this.description = description;
            this.filter = filter;
        }
    }
}
