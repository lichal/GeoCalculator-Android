package com.codemasters.ryancheng.cis357;

import android.app.Application;
import net.danlew.android.joda.JodaTimeAndroid;


/**
 * Created by d on 11/6/17.
 */


public class GeoCalculatorApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
    }
}