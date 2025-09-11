package com.example.module_home.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.activity.OnBackPressedCallback
import com.example.library_base.ui.BaseBindingActivity
import com.example.library_data.constant.KEY_TITLE
import com.example.library_data.constant.KEY_URL
import com.example.module_home.databinding.HomeMActivityTabDetailBinding
import com.example.module_view.LoadingUtils

class TabDetailActivity : BaseBindingActivity<HomeMActivityTabDetailBinding>(
    HomeMActivityTabDetailBinding::inflate
) {

    private var mTitle = ""

    private val mDialogUtils by lazy {
        LoadingUtils(this)
    }

    companion object {
        fun start(context: Context, url: String, title: String) {
            val intent = Intent(context, TabDetailActivity::class.java)
            intent.putExtra(KEY_URL, url)
            intent.putExtra(KEY_TITLE, title)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initWebView()
        initData()

    }

    private fun initWebView() {
        // WebView基础配置
        with(mBinding.webView.settings) {
            javaScriptEnabled = true
            domStorageEnabled = true
            loadWithOverviewMode = true
            useWideViewPort = true
            builtInZoomControls = true
            displayZoomControls = false
        }
        // 进度条处理
        mBinding.webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                mBinding.progressBar.progress = newProgress
                if (newProgress == 100) {
                    mBinding.progressBar.visibility = ProgressBar.GONE
                } else {
                    mBinding.progressBar.visibility = ProgressBar.VISIBLE
                }
            }
        }
        // 页面加载控制
        mBinding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                url: String,
            ): Boolean {
                view.loadUrl(url)
                return false
            }
        }
        // 注册返回键回调
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (mBinding.webView.canGoBack()) {
                    mBinding.webView.goBack()
                } else {
                    // 移除回调并执行默认返回操作
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }

    private fun initData() {
        val url = intent?.getStringExtra(KEY_URL)
        mTitle = intent?.getStringExtra(KEY_TITLE) ?: ""
        url?.let { mBinding.webView.loadUrl(it) }
        mBinding.titleBar.setMiddleText(mTitle)
    }


    override fun onDestroy() {
        mBinding.webView.destroy()
        super.onDestroy()
    }


}