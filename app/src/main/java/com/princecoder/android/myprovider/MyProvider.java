package com.princecoder.android.myprovider;

import android.app.Application;

/**
 * Created by prinzlyngotoum on 12/2/14.
 * <p/>
 * <p/>
 * Application class.
 * <p/>
 * This class define a Singleton to make sure we have only one instance in all the app
 */


public class MyProvider extends Application {
    static private volatile MyProvider mInstance = null;

    private MyProvider() {
        super();
        mInstance = this;
    }

    static public MyProvider getInstance() {
        if (mInstance == null) {
            synchronized (MyProvider.class) {
                if (mInstance == null) {
                    mInstance = new MyProvider();
                }
            }
        }
        return mInstance;
    }
}
