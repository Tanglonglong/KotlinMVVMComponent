package com.example.module_view.view

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 *
 * 给EditText增加一个扩展函数textChangeFlow，
 * 生产一个Flow流对应edittext的输入监听
 * 扩展函数默认持有被扩展类的属性，方法调用
 *
 * **/
fun EditText.textChangeFlow(): Flow<String> = callbackFlow {
    val watcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            s?.let { trySend(it.toString()).isSuccess }
        }
    }
    addTextChangedListener(watcher)
    awaitClose { removeTextChangedListener(watcher) } //协程作用域结束后remove掉监听
}