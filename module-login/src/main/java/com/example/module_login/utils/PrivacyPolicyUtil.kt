package com.example.module_login.utils

import android.app.Activity
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import com.example.module_login.R
import com.example.module_login.ui.PrivacyPolicyActivity

object PrivacyPolicyUtil {

    //设置隐私政策
    fun getPrivacyPolicy(activity: Activity): SpannableStringBuilder {
        val privacyStr = activity.getString(R.string.login_m_login_agreement)
        val spaBuilder = SpannableStringBuilder(privacyStr)
        val privacySpan = activity.getString(R.string.login_m_login_privacy_agreement)
        val userSerSpan = activity.getString(R.string.login_m_login_user_agreement)

        spaBuilder.setSpan(
            object : ClickableSpan() {
                override fun onClick(view: View) {
                    val textView = view as TextView
                    textView.highlightColor = activity.getColor(com.example.library_common.R.color.transparent)
                    PrivacyPolicyActivity.launchStart(activity)
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.color = activity.getColor(com.example.library_common.R.color.color_0165b8)
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
                    textView.highlightColor = activity.getColor(com.example.library_common.R.color.transparent)
                    PrivacyPolicyActivity.launchStart(activity)
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.color = activity.getColor(com.example.library_common.R.color.color_0165b8)
                    ds.isUnderlineText = false
                    ds.clearShadowLayer()

                }

            }, spaBuilder.indexOf(userSerSpan),
            spaBuilder.indexOf(userSerSpan) + userSerSpan.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spaBuilder
    }


}