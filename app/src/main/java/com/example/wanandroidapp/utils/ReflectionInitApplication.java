package com.example.wanandroidapp.utils;

import android.app.Application;
import android.content.res.Configuration;

import androidx.annotation.NonNull;

import com.example.library_base.IModuleApplicationInit;

import java.util.Hashtable;

public class ReflectionInitApplication {

    private final Application mApplication;

    private static volatile ReflectionInitApplication sInstance;

    public static Hashtable<String, IModuleApplicationInit> classMap = new Hashtable<>();//Hashtable value不能为空


    private ReflectionInitApplication(Application application) {
        this.mApplication = application;
    }

    public static ReflectionInitApplication getInstance(Application application) {
        if (sInstance == null) {
            synchronized (ReflectionInitApplication.class) {
                if (sInstance == null) {
                    sInstance = new ReflectionInitApplication(application);
                }
            }
        }
        return sInstance;
    }


    public static String[] sInitApplicationPackage = {
            };


    private IModuleApplicationInit getInitClass(String item) {
        Class<?> clazz = null;
        try {
            clazz = Class.forName(item);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        IModuleApplicationInit init = null;
        try {
            init = (IModuleApplicationInit) clazz.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }
        return init;
    }

    public void initOnCreate() {
        for (String item : sInitApplicationPackage) {
            if (classMap.containsKey(item)) classMap.get(item).onCreate(mApplication);
            else {
                IModuleApplicationInit init = getInitClass(item);
                if (!classMap.containsKey(item)) {
                    classMap.put(item, init);
                }
                init.onCreate(mApplication);
            }
        }
    }

    public void initOnConfigurationChanged(@NonNull Configuration newConfig) {
        for (String item : sInitApplicationPackage) {
            if (classMap.containsKey(item))
                classMap.get(item).onConfigurationChanged(mApplication, newConfig);
            else {
                IModuleApplicationInit init = getInitClass(item);
                if (!classMap.containsKey(item)) {
                    classMap.put(item, init);
                }
                init.onConfigurationChanged(mApplication, newConfig);
            }
        }
    }


}
