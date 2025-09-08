package com.example.module_login.model

import com.example.library_data.model.User

sealed class RegisterState {
    object Loading : RegisterState()
    data class Success(val user: User?) : RegisterState()
    data class Error(val code: Int, val message: String) : RegisterState()
}