package com.example.module_home;

import android.app.Application;
import com.example.library_base.BaseApplication;

public class HomeApplication extends BaseApplication {

    public static Application sApplication;


    public ApplicationInit mApplicationInit;

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
        mApplicationInit = new ApplicationInit();
        mApplicationInit.onCreate(this);
    }

}
