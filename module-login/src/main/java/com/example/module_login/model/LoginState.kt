package com.example.module_login.model

import com.example.library_data.model.User

sealed class LoginState {

    object Loading : LoginState()
    data class Success(val user: User?) : LoginState()
    data class Error(val code: Int, val message: String) : LoginState()


}