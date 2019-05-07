package com.freehand.file_manager;

import android.app.Application;

import java.io.File;
import java.util.Comparator;

import io.reactivex.annotations.NonNull;

/**
 * Created by minhpham on 2/24/17.
 * Purpose: config file manage, use in init file manage
 */
public class FileConfig {
    /**
     * define Application, it use init ApplicationProvider
     */
    public final Application mApp;
    /**
     * define the root path
     */
    public String homePath;
    /**
     * define show or hide all file hidden when travel file in file browser
     */
    public boolean showHiddenFile = false;
    /**
     * define default sort when travel file in browser
     */
    private Comparator<File> defaultSort = new Comparator<File>() {
        @Override
        public int compare(File l, File r) {
            if (l.isFile() && r.isFile()) return l.getName().compareTo(r.getName());
            if (l.isDirectory() && r.isDirectory()) return l.getName().compareTo(r.getName());
            if (l.isDirectory()) return 1;
            return -1;
        }
    };
    // home path default by /sdcard
    public FileConfig(@NonNull String homePath, @NonNull Application app) {
        this.homePath = homePath;
        this.mApp = app;
    }

    public void setHomePath(String homePath) {
        this.homePath = homePath;
    }

    public FileConfig addDefaultSort(Comparator<File> sort) {
        this.defaultSort = sort;
        return this;
    }

    public Comparator<File> getDefaultSort() {
        return defaultSort;
    }

}
