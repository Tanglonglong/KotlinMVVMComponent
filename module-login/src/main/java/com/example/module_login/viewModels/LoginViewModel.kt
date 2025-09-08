package com.example.module_login.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.library_data.model.User
import com.example.library_base.utils.LogUtil
import com.example.library_data.provider.UserServiceProvider
import com.example.library_network.error.ExceptionHandler
import com.example.module_login.model.LoginState
import com.example.module_login.model.RegisterState
import com.example.module_login.repositories.LoginAndRegisterRepository
import com.example.module_view.toast.TipsToast
import kotlinx.coroutines.launch

/**
 * @desc   登录viewModel
 */
class LoginViewModel : ViewModel() {
    private val _loginState = MutableLiveData<LoginState>()
    val loginState: LiveData<LoginState> = _loginState
    val loginAndRegisterRepository by lazy { LoginAndRegisterRepository() }

    var userPhone = MutableLiveData<String>()


    val userPwd = MutableLiveData<String>()

    /**
     * 登录
     * @param username  用户名
     * @param password  密码
     * @return
     */
    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                val user = loginAndRegisterRepository.login(username, password)
                //保存用户信息
                UserServiceProvider.saveUserInfo(user)
                UserServiceProvider.saveUserPhone(user?.username)
                LogUtil.v("登录saveUser${UserServiceProvider.getUserInfo()}")
                _loginState.value = LoginState.Success(user)
            } catch (e: Exception) {
                //http异常转换Api异常，统一api异常处理逻辑
                val exception = ExceptionHandler.handleException(e)
                LogUtil.v("exception.errCode:" + exception.errCode + " exception.errMsg:" + exception.errMsg)
                _loginState.value =
                    LoginState.Error(exception.errCode, e.message ?: "登录失败")
                LogUtil.e(e)
            }
        }
    }

}

