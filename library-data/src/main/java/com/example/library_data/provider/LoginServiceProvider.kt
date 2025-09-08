package com.example.library_data.provider

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.launcher.ARouter
import com.example.library_data.constant.LOGIN_SERVICE_LOGIN
import com.example.library_data.service.ILoginService

/**
 * @author mingyan.su
 * @date   2023/3/26 07:30
 * @desc   UserService提供类，对外提供相关能力
 * 任意模块就能通过LoginServiceProvider使用对外暴露的能力
 */
object LoginServiceProvider {
    //val loginService = ARouter.getInstance().build(LOGIN_SERVICE_LOGIN).navigation() as? ILoginService
    // [The inject fields CAN NOT BE 'private'!!! please check field [userService] in class [LoginServiceProvider]]
    // 依赖注入，这边其实就是动态注入，也就是可以先不实现ILoginService接口，编译时有注解了
    //@Route(path = USER_SERVICE_USER)的实现了ILoginService接口的类注入
    //所以我UserService这个实现类都不用定义这个模块里，但是为了其他模块能独立运行就写在这边了
    @Autowired(name = LOGIN_SERVICE_LOGIN)
    lateinit var loginService: ILoginService

    init {
        ARouter.getInstance().inject(this)
    }

    /**
     * 是否登录
     * @return Boolean
     */
    fun isLogin(): Boolean {
        return loginService.isLogin()
    }

    /**
     * 跳转登录页
     * @param context
     */
    fun login(context: Context) {
        loginService.login(context)
    }

    /**
     * 跳转隐私协议
     * @param context
     */
    fun readPolicy(context: Context) {
        loginService.readPolicy(context)
    }

    /**
     * 登出
     * @param context
     * @param lifecycleOwner
     * @param observer
     */
    fun logout(
        context: Context,
        lifecycleOwner: LifecycleOwner?,
        observer: Observer<Boolean>
    ) {
        loginService.logout(context, lifecycleOwner, observer)
    }
}