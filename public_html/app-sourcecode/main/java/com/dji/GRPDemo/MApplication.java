//have used code on DJI official page , which is a tutorial about how to implements a app to control their aircraft
//https://developer.dji.com/mobile-sdk/documentation/android-tutorials/ActivationAndBinding.html
package com.dji.GRPDemo;

import android.app.Application;
import android.content.Context;

import com.secneo.sdk.Helper;

public class MApplication extends Application {

    private DemoApplication demoApplication;
    private FPVDemoApplication fpvDemoApplication;

    @Override
    protected void attachBaseContext(Context paramContext) {
        super.attachBaseContext(paramContext);
        Helper.install(MApplication.this); //loading sdk

        if (demoApplication == null) {
            demoApplication = new DemoApplication();
            demoApplication.setContext(this);
        }

        if (fpvDemoApplication == null) {
            fpvDemoApplication = new FPVDemoApplication();
            fpvDemoApplication.setContext(this);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        demoApplication.onCreate();
        fpvDemoApplication.onCreate();
    }

}
