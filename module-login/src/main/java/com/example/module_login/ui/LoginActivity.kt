package com.example.module_login.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.LinkMovementMethod
import android.text.method.PasswordTransformationMethod
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.example.library_base.ui.BaseMvvMActivity
import com.example.module_login.R
import com.example.module_login.databinding.LoginMActivityLoginBinding
import com.example.module_login.model.LoginState
import com.example.module_login.model.RegisterState
import com.example.module_login.utils.PrivacyPolicyUtil
import com.example.module_login.viewModels.LoginViewModel
import com.example.module_view.LoadingUtils
import com.example.module_view.toast.TipsToast
import com.example.module_view.view.textChangeFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class LoginActivity : BaseMvvMActivity<LoginMActivityLoginBinding, LoginViewModel>(
    LoginMActivityLoginBinding::inflate
) {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mDialogUtils by lazy {
        LoadingUtils(this)
    }

    private var isShowPassword = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding.vm = mViewModel
        initView()
        initListener()
        initObserve()
    }

    override fun getViewModelClass(): Class<LoginViewModel> {
        return LoginViewModel::class.java
    }

    fun initView() {
        val spanBuilder = PrivacyPolicyUtil.getPrivacyPolicy(this)
        mBinding.cbAgreement.movementMethod = LinkMovementMethod.getInstance()
        mBinding.cbAgreement.setText(spanBuilder, TextView.BufferType.SPANNABLE)
    }

    fun initObserve() {
        mViewModel.loginState.observe(this) { state ->
            when (state) {
                is LoginState.Loading -> mDialogUtils.showLoading()
                is LoginState.Error -> {
                    mDialogUtils.dismissLoading()
                    TipsToast.showTips("登录失败:${state.message}")
                }

                is LoginState.Success -> {
                    mDialogUtils.dismissLoading()
                    TipsToast.showTips("登录成功")
                }
            }
        }
    }

    fun initListener() {
        mBinding.ivPasswordToggle.setOnClickListener {
            setPasswordHide()
        }
        mBinding.tvForgetPassword.setOnClickListener {
            TipsToast.showTips(R.string.login_m_login_forget_password)
        }
        mBinding.tvLogin.setOnClickListener {
            toLogin()
        }
        mBinding.tvRegister.setOnClickListener {
            RegisterActivity.start(this)
        }
        mBinding.cbAgreement.setOnCheckedChangeListener { _, _ ->
            updateLoginState()
        }
        setEditTextChange(mBinding.etPhone)
        setEditTextChange(mBinding.etPassword)
        mBinding.tvLogin.setOnClickListener {
            toLogin()
        }
    }

    private fun toLogin() {
        val userName = mBinding.etPhone.text?.trim()?.toString()
        val password = mBinding.etPassword.text?.trim()?.toString()

        if (userName.isNullOrEmpty() || userName.length < 11) {
            TipsToast.showTips(R.string.login_m_error_phone_number)
            return
        }
        if (password.isNullOrEmpty()) {
            TipsToast.showTips(R.string.login_m_error_null_password)
            return
        }
        if (!mBinding.cbAgreement.isChecked) {
            TipsToast.showTips(R.string.login_m_tips_read_user_agreement)
            return
        }
        mViewModel.login(userName, password)
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

    private fun updateLoginState() {
        val phone = mViewModel.userPhone.value
        val phoneEnable = !phone.isNullOrEmpty()
        val password = mViewModel.userPwd.value
        val passwordEnable = !password.isNullOrEmpty()
        val agreementEnable = mBinding.cbAgreement.isChecked
        mBinding.tvRegister.isEnabled = phoneEnable && passwordEnable && agreementEnable
    }

    /**
     * 密码是否可见
     */
    private fun setPasswordHide() {
        isShowPassword = !isShowPassword
        if (isShowPassword) {
            mBinding.ivPasswordToggle.setImageResource(R.mipmap.login_m_ic_psw_invisible)
            mBinding.etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
        } else {
            mBinding.etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            mBinding.ivPasswordToggle.setImageResource(R.mipmap.login_m_ic_psw_visible)
        }
        mBinding.etPassword.setSelection(mBinding.etPassword.length())
    }


}