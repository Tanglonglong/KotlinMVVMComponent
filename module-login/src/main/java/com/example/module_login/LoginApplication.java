package com.example.module_login;

import android.app.Application;

import com.example.library_base.BaseApplication;

public class LoginApplication extends BaseApplication {

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
