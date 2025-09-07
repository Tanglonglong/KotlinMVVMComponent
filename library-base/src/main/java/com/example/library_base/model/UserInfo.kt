package com.example.library_base.model


data class User(
    val id: Int? = 0,
    val username: String?,
    var nickname: String?,
    val token: String?,
    var icon: String? = "",
    val email: String? = "",
    var password: String?,
    var signature: String?,
    var sex: String?,
    var birthday: String? = ""
)