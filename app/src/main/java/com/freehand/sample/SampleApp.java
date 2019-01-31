package com.freehand.sample;

import com.freehand.dynamicfunction.DynamicFunctionService;
import com.freehand.fetcher.FetcherConfig;
import com.freehand.fetcher.FetcherErrorCallback;
import com.freehand.logger.LogcatLog;
import com.freehand.logger.Logger;
import com.freehand.realmprovider.RealmApplication;
import com.freehand.realmprovider.RealmProvider;
import com.freehand.realmprovider.RealmUtility;

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
        FetcherConfig.INSTANCE.addErrorHandle(new FetcherErrorCallback() {
            @Override
            public void onError(Throwable throwable) {
               Logger.log().e("minh1",throwable);
            }

            @Override
            public void onError(int errorCode, String message) {
               Logger.log().d("minh",message);
            }
        });
        DynamicFunctionService.enableLog(true);
        if(BuildConfig.DEBUG) {
            Logger.setDefaultLog(new LogcatLog());
        }
    }
}
