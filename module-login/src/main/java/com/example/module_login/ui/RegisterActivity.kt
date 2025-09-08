package com.example.module_login.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.library_base.ui.BaseMvvMActivity
import com.example.module_login.LoginApplication
import com.example.module_login.R
import com.example.module_login.databinding.LoginMActivityRegisterBinding
import com.example.module_login.model.RegisterState
import com.example.module_login.utils.PrivacyPolicyUtil
import com.example.module_login.viewModels.RegisterViewModel
import com.example.module_view.LoadingUtils
import com.example.module_view.toast.TipsToast
import com.example.module_view.view.textChangeFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Route(path = "/login/RegisterActivity", group = "login")
class RegisterActivity : BaseMvvMActivity<LoginMActivityRegisterBinding, RegisterViewModel>(
    LoginMActivityRegisterBinding::inflate
) {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, RegisterActivity::class.java)
            context.startActivity(intent)
        }
    }


    private val mDialogUtils by lazy {
        LoadingUtils(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding.vm = mViewModel
        initView()
        initListener()
        initObserve()

    }

    override fun getViewModelClass(): Class<RegisterViewModel> {
        return RegisterViewModel::class.java
    }

    fun initView() {
        val spanBuilder = PrivacyPolicyUtil.getPrivacyPolicy(this)
        mBinding.cbAgreement.movementMethod = LinkMovementMethod.getInstance()
        mBinding.cbAgreement.setText(spanBuilder, TextView.BufferType.SPANNABLE)

    }

    fun initListener() {
        mBinding.cbAgreement.setOnCheckedChangeListener { _, _ ->
            updateLoginState()
        }
        setEditTextChange(mBinding.etPhone)
        setEditTextChange(mBinding.etPassword)
        setEditTextChange(mBinding.etRepassword)
        mBinding.tvRegister.setOnClickListener { registerClick() }
    }

    fun initObserve() {
        mViewModel.registerState.observe(this) { state ->
            when (state) {
                is RegisterState.Loading -> mDialogUtils.showLoading()
                is RegisterState.Error -> {
                    mDialogUtils.dismissLoading()
                    TipsToast.showTips("注册失败:${state.message}")
                }
                is RegisterState.Success -> {
                    mDialogUtils.dismissLoading()
                    TipsToast.showTips("注册成功")
                }
            }
        }
    }

    /**
     * 监听EditText文本变化
     */
    @OptIn(FlowPreview::class)
    private fun setEditTextChange(editText: EditText) {
        editText.textChangeFlow()
            .debounce(300)
            .flowOn(Dispatchers.IO)
            .onEach {
                updateLoginState()
            }
            .launchIn(lifecycleScope)
    }

    fun registerClick() {
        Log.d(TAG, "registerClick")
        val userName = mBinding.etPhone.text?.trim()?.toString()
        val password = mBinding.etPassword.text?.trim()?.toString()
        val repassword = mBinding.etRepassword.text?.trim()?.toString()

        if (userName.isNullOrEmpty() || userName.length < 11) {
            TipsToast.showTips(LoginApplication.sApplication.getString(R.string.login_m_error_phone_number))
            return
        }
        if (password.isNullOrEmpty() || repassword.isNullOrEmpty() || password != repassword) {
            TipsToast.showTips(R.string.login_m_error_double_password)
            return
        }
        if (!mBinding.cbAgreement.isChecked) {
            TipsToast.showTips(R.string.login_m_tips_read_user_agreement)
            return
        }
        mViewModel.register(userName, password, repassword)
    }

    private fun updateLoginState() {
        val phone = mViewModel.userPhone.value
        val phoneEnable = !phone.isNullOrEmpty()
        val password = mViewModel.userPwd.value
        val repassword = mViewModel.userPwd2.value
        val passwordEnable = !password.isNullOrEmpty() && !repassword.isNullOrEmpty()
        val agreementEnable = mBinding.cbAgreement.isChecked
        mBinding.tvRegister.isEnabled = phoneEnable && passwordEnable && agreementEnable
    }

}