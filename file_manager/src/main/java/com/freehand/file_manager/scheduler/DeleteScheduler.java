package com.freehand.file_manager.scheduler;

import java.io.File;
import java.util.List;

import com.freehand.file_manager.file_model.FileUtils;
import com.freehand.file_manager.filter.AFilter;
import com.freehand.file_manager.filter.ruler.FilterLastModify;

/**
 * Created by minhpham on 2/27/17.
 * Purpose: delete all file over duration
 */

public class DeleteScheduler implements IScheduler {
    private static final long A_WEEKS = 1*7*24*60*60*1000;
    private final long duration;

    public DeleteScheduler(long duration) {
        this.duration = duration;
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
        return new FilterLastModify(duration);
    }

}
