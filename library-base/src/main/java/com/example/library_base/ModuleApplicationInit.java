package com.example.library_base;

import android.app.Application;
import android.content.res.Configuration;

import androidx.annotation.NonNull;

public abstract class ModuleApplicationInit implements IModuleApplicationInit{

    @Override
    public void onCreate(Application application) {

    }

    @Override
    public void onConfigurationChanged(Application application, @NonNull Configuration newConfig) {

    }

    @Override
    public void onLowMemory(Application application) {

    }

    @Override
    public void onTerminate(Application application) {

    }

    @Override
    public void onTrimMemory(Application application, int level) {

    }
}
