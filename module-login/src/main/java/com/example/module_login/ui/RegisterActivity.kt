package com.example.module_login.ui

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.example.library_base.ui.BaseMvvMActivity
import com.example.library_base.utils.LogUtil
import com.example.module_login.LoginApplication
import com.example.module_login.R
import com.example.module_login.databinding.LoginMActivityRegisterBinding
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


class RegisterActivity : BaseMvvMActivity<LoginMActivityRegisterBinding, RegisterViewModel>(
    LoginMActivityRegisterBinding::inflate
) {


    private val mDialogUtils by lazy {
        LoadingUtils(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding.vm = mViewModel
        initView()
        initListener()

    }

    fun initView() {
        initPrivacyPolicy();

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
        mDialogUtils.showLoading()
        mViewModel.register2(userName, password, repassword).observe(this) { user ->
            mDialogUtils.dismissLoading()
            user?.let {
                TipsToast.showTips("注册成功")
//                //保存用户信息
//                UserServiceProvider.saveUserInfo(user)
//                UserServiceProvider.saveUserPhone(user.username)
//                LogUtil.e("user:$it")
//                MainServiceProvider.toMain(context = this)
//                finish()
            } ?: kotlin.run {

            }
        }

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

    override fun getViewModelClass(): Class<RegisterViewModel> {
        return RegisterViewModel::class.java
    }


    //设置隐私政策
    private fun initPrivacyPolicy() {
        val privacyStr = getString(R.string.login_m_login_agreement)
        mBinding.cbAgreement.movementMethod = LinkMovementMethod.getInstance()
        val spaBuilder = SpannableStringBuilder(privacyStr)
        val privacySpan = getString(R.string.login_m_login_privacy_agreement)
        val userSerSpan = getString(R.string.login_m_login_user_agreement)

        spaBuilder.setSpan(
            object : ClickableSpan() {
                override fun onClick(view: View) {
                    val textView = view as TextView
                    textView.highlightColor = getColor(R.color.login_m_transparent)
                    PrivacyPolicyActivity.launchStart(this@RegisterActivity)
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.color = getColor(R.color.login_m_color_0165b8)
                    ds.isUnderlineText = false
                    ds.clearShadowLayer()

                }
            },
            spaBuilder.indexOf(privacySpan),
            spaBuilder.indexOf(privacySpan) + privacySpan.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spaBuilder.setSpan(
            object : ClickableSpan() {
                override fun onClick(view: View) {
                    val textView = view as TextView
                    textView.highlightColor = getColor(R.color.login_m_transparent)
                    PrivacyPolicyActivity.launchStart(this@RegisterActivity)
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.color = getColor(R.color.login_m_color_0165b8)
                    ds.isUnderlineText = false
                    ds.clearShadowLayer()

                }

            }, spaBuilder.indexOf(userSerSpan),
            spaBuilder.indexOf(userSerSpan) + userSerSpan.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        mBinding.cbAgreement.setText(spaBuilder, TextView.BufferType.SPANNABLE)
    }


}