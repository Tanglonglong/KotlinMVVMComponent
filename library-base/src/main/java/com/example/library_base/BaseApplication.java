package com.example.library_base;

import android.app.Application;
import android.content.res.Configuration;
import androidx.annotation.NonNull;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.library_base.utils.LogUtil;
import com.example.library_base.utils.SPUtil;
import com.tencent.mmkv.MMKV;

public class BaseApplication extends Application {

    public static BaseApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        if (BuildConfig.DEBUG)
        {
            LogUtil.i("开启ARouter日志");
            ARouter.openLog(); // 开启日志
            ARouter.openDebug(); // 使用InstantRun的时候，需要打开该开关，上线之后关闭，否则有安全风险
            ARouter.printStackTrace();
        }
        ARouter.init(this);
        LogUtil.init(true);
        SPUtil.init(this);
        String rootDir = MMKV.initialize(this); // 返回存储路径
        LogUtil.i("MMKV------:" + rootDir);
        LogUtil.i("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");


    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }
}
