package com.freehand.file_manager.scheduler;

import android.support.v4.util.Pair;

import com.freehand.file_manager.file_model.FileUtils;
import com.freehand.file_manager.filter.AFilter;
import com.freehand.file_manager.filter.ruler.FilterAttributes;

import java.io.File;
import java.util.List;

/**
 * Created by minhpham on 3/2/17.
 * Purpose: delete all files based on their attributes match with input
 */

public class DeleteByAttrScheduler implements IScheduler {

    private final Pair<String, String> pair;

    public DeleteByAttrScheduler(String key, String value) {
        this.pair = new Pair<String,String>(key,value);
    }

    @Override
    public void onScheduler(List<File> filterResult) {
        for (File file: filterResult) {
            try {
                FileUtils.buildFile(file).delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public AFilter getFilter() {
        return new FilterAttributes(pair);
    }
}
