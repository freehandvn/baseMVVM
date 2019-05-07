package com.freehand.file_manager.scheduler;

import java.io.File;
import java.util.List;

import io.reactivex.annotations.NonNull;
import com.freehand.file_manager.filter.AFilter;
import com.freehand.file_manager.filter.FilterCompoundAnd;

/**
 * Created by minhpham on 2/27/17.
 * Purpose: scheduler combine all child scheduler by operator and
 */

public class SchedulerCompoundAnd implements IScheduler {

    private final IScheduler[] schedulers;

    public SchedulerCompoundAnd(@NonNull IScheduler... schedulers) {
        this.schedulers = schedulers;
    }

    @Override
    public void onScheduler(List<File> filterResult) {
        if (this.schedulers == null || this.schedulers.length == 0) return;
        for (IScheduler scheduler : schedulers) {
            scheduler.onScheduler(filterResult);
        }
    }

    @Override
    public AFilter getFilter() {
        if (this.schedulers == null || this.schedulers.length == 0) return null;
        AFilter[] compound = new AFilter[this.schedulers.length];
        for (int i = 0; i < this.schedulers.length; i++) {
            compound[i] = schedulers[i].getFilter();
        }
        return new FilterCompoundAnd(compound);

    }


}
