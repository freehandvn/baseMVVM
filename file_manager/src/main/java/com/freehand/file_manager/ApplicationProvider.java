package com.freehand.file_manager;

import android.app.Application;

/**
 * Created by minhpham on 2/27/17.
 * Purpose: provide application use in all file_manage package
 */
public class ApplicationProvider {

    private static ApplicationProvider ourInstance = new ApplicationProvider();

    private Application mApp;

    public static ApplicationProvider getInstance() {
        return ourInstance;
    }

    public void init(Application app){
        this.mApp = app;
    }

    public Application getApplication(){
        return mApp;
    }

    private ApplicationProvider() {
    }
}
