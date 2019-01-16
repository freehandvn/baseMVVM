package com.freehand.sample;

import com.freehand.dynamicfunction.DynamicFunctionService;
import com.freehand.fetcher.FetcherConfig;
import com.freehand.logger.LogcatLog;
import com.freehand.logger.Logger;
import com.freehand.realmprovider.RealmApplication;
import com.freehand.realmprovider.RealmProvider;
import com.freehand.realmprovider.RealmUtility;
import com.freehand.sample.api.StoreInterceptor;

/**
 * Created by minhpham on 12/3/18.
 * Purpose: .
 * Copyright © 2018 Pham Duy Minh. All rights reserved.
 */
public class SampleApp extends RealmApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        RealmUtility.realmProvider = new RealmProvider(this,"SampleDB");
        FetcherConfig.getInstance().addDefaultReponseInterceptor(new StoreInterceptor());
        DynamicFunctionService.enableLog(true);
        if(BuildConfig.DEBUG) {
            Logger.setDefaultLog(new LogcatLog());
        }
    }
}
