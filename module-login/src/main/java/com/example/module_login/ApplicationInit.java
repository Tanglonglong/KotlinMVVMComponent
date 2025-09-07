package com.example.module_login;

import android.app.Application;

import com.example.library_base.ModuleApplicationInit;
import com.example.module_view.toast.TipsToast;


public class ApplicationInit extends ModuleApplicationInit {



    @Override
    public void onCreate(Application application) {
        super.onCreate(application);
        TipsToast.INSTANCE.init(application);

    }
    //TODO 可以根据需要，实现application不同的生命周期代码


}
