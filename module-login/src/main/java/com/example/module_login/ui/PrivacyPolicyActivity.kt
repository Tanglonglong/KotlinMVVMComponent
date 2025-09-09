package com.example.module_login.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.library_base.ui.BaseBindingActivity
import com.example.library_data.constant.Login_ACTIVITY_POLICY
import com.example.module_login.databinding.LoginMActivityPrivacyPolicyBinding
@Route(path = Login_ACTIVITY_POLICY)
class PrivacyPolicyActivity : BaseBindingActivity<LoginMActivityPrivacyPolicyBinding>(
    LoginMActivityPrivacyPolicyBinding::inflate
) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    companion object {
        fun launchStart(context: Context) {
            val intent = Intent(context, PrivacyPolicyActivity::class.java)
            context.startActivity(intent)
        }
    }
}