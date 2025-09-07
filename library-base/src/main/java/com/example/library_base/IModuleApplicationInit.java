package com.example.library_base;

import android.app.Application;
import android.content.res.Configuration;

import androidx.annotation.NonNull;

public interface  IModuleApplicationInit {

    void onCreate(Application application);
    void onConfigurationChanged(Application application, @NonNull Configuration newConfig);
    void onLowMemory(Application application);

    void onTerminate(Application application);
    void onTrimMemory(Application application,int level);
}
