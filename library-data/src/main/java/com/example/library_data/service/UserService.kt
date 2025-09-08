package com.example.library_data.service

import android.content.Context
import androidx.lifecycle.LiveData
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.library_data.constant.USER_SERVICE_USER
import com.example.library_data.manager.UserManager
import com.example.library_data.model.User

/**
 * @desc   提供对IUserService接口的具体实现
 * 依赖注入，这边其实就是动态注入，也就是可以先不实现ILoginService接口，编译时有注解了
 * @Route(path = USER_SERVICE_USER)的实现了ILoginService接口的类注入
 *  所以我UserService这个实现类都不用定义这个模块里，但是为了其他模块能独立运行就写在这边了
 */
@Route(path = USER_SERVICE_USER)
class UserService : IUserService {
    /**
     * 是否登录
     * @return Boolean
     */
    override fun isLogin(): Boolean {
        return UserManager.isLogin()
    }

    /**
     * 获取用户信息
     * @return User or null
     */
    override fun getUserInfo(): User? {
        return UserManager.getUserInfo()
    }

    /**
     * 保存用户信息
     * @param user
     */
    override fun saveUserInfo(user: User?) {
        UserManager.saveUserInfo(user)
    }

    /**
     * 清除用户信息
     */
    override fun clearUserInfo() {
        UserManager.clearUserInfo()
    }

    /**
     * 获取User信息LiveData
     */
    override fun getUserLiveData(): LiveData<User?> {
        return UserManager.getUserLiveData()
    }

    /**
     * 保存用户手机号码
     * @param phone
     */
    override fun saveUserPhone(phone: String?) {
        UserManager.saveUserPhone(phone)
    }

    /**
     * 保存用户手机号码
     * @return phone
     */
    override fun getUserPhone(): String? {
        return UserManager.getUserPhone()
    }

    override fun init(context: Context?) {

    }
}