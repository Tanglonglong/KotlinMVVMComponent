package com.example.module_login.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.module_login.repositories.LoginAndRegisterRepository
import com.example.library_base.model.User
import com.example.library_base.utils.LogUtil
import com.example.library_network.error.ExceptionHandler
import com.example.library_network.viewmodel.BaseViewModel
import com.example.module_view.toast.TipsToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterViewModel: BaseViewModel() {

    private val TAG = RegisterViewModel::class.simpleName

    val registerLiveData = MutableLiveData<User?>()
    val loginAndRegisterRepository by lazy { LoginAndRegisterRepository() }


    var userPhone = MutableLiveData<String>()


    val userPwd = MutableLiveData<String>()
    val userPwd2 = MutableLiveData<String>()

    fun register(username: String, password: String, repassword: String): LiveData<User?> {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                val data = loginAndRegisterRepository.register(username,password,repassword)
                registerLiveData.value = data
            } catch (e: Exception) {
                e.printStackTrace()
                LogUtil.e(e)
                //http异常转换Api异常，统一api异常处理逻辑
                val exception = ExceptionHandler.handleException(e)
                LogUtil.v("exception.errCode:" + exception.errCode + " exception.errMsg:" + exception.errMsg)
               //TODO 异常处理 这边可以抽象到父类  公共部分如下 register2
                TipsToast.showTips("注册异常")
                registerLiveData.value = null
            }
        }
        return registerLiveData
    }

    fun register2(username: String, password: String, repassword: String): LiveData<User?> {
        launchUI(responseBlock = {
            //请求逻辑+成功
            val data = loginAndRegisterRepository.register(username,password,repassword)
            registerLiveData.value = data
        }){code, error ->
            //异常处理
            LogUtil.v("error:$code error:$error")
            //TODO 异常处理 这边可以抽象到父类  公共部分
            TipsToast.showTips(error)
            registerLiveData.value = null
            TipsToast.showTips("注册异常")
            registerLiveData.value = null
        }
        return registerLiveData
    }





}