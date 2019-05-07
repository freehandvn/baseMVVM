package com.freehand.file_manager.scheduler;


import java.io.File;
import java.util.List;

import com.freehand.file_manager.filter.AFilter;

/**
 * Created by minhpham on 2/27/17.
 * Purpose: define interface support filter file and automatic trigger on files has filtered
 */

public interface IScheduler {

    /**
     * define action on filter result
     * @param filterResult
     */
    void onScheduler(List<File> filterResult);

    /**
     *
     * @return filter instance
     */
    AFilter getFilter();

}
